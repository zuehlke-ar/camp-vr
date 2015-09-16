package com.zuehlke.vr.openStreetmap;

import com.zuehlke.vr.domain.*;
import com.zuehlke.vr.openStreetmap.util.CollectionUtils;
import generated.osm.*;
import generated.osm.Node;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ExtendedOsm {

    private static final String OPENSTREETMAP_API_06_URL = "http://api.openstreetmap.org/api/0.6/";

    private Osm osm;
    private List<Node> nodes = new ArrayList<>();
    private List<Way> ways = new ArrayList<>();

    public ExtendedOsm(File file) {
        setOsm(JAXB.unmarshal(file, Osm.class));
    }

    public ExtendedOsm(double lat, double lon, double vicinityRange) {
        DecimalFormat format = new DecimalFormat("##0.0000000");
        String left = format.format(lat - vicinityRange);
        String bottom = format.format(lon - vicinityRange);
        String right = format.format(lat + vicinityRange);
        String top = format.format(lon + vicinityRange);

        String url = OPENSTREETMAP_API_06_URL + "map?bbox=" + left + "," + bottom + "," + right + "," + top;
        System.out.println("getting map: " + url);
        try {
            InputStream inputStream = new URL(url).openStream();
            setOsm(JAXB.unmarshal(inputStream, Osm.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ExtendedOsm(TrackData trackData) {
        osm = new Osm();
        osm.setVersion(BigDecimal.valueOf(0.6));
        int idCounter = 1;

        Map<com.zuehlke.vr.domain.Node, BigInteger> nodeMap = new HashMap<>();
        for (com.zuehlke.vr.domain.Node domainNode : trackData.getNodes()) {
            Node node = new Node();
            BigInteger id = BigInteger.valueOf(idCounter++);
            node.setId(id);
            node.setLat((float) domainNode.getLat());
            node.setLon((float) domainNode.getLon());
            node.setVersion(1);
            nodes.add(node);
            nodeMap.put(domainNode, id);
        }

        for (Track track : trackData.getTracks()) {
            Way way = new Way();
            way.setId(BigInteger.valueOf(idCounter++));
            way.setVersion(1);
            way.setVisible(true);

            Nd ndFrom = new Nd();
            ndFrom.setRef(nodeMap.get(track.getFrom()));
            way.getRest().add(ndFrom);

            Nd ndTo = new Nd();
            ndTo.setRef(nodeMap.get(track.getTo()));
            way.getRest().add(ndTo);

            Tag tag0 = new Tag();
            tag0.setV("railway");
            tag0.setK("rail");
            way.getRest().add(tag0);

            Tag tag1 = new Tag();
            tag1.setV("operator");
            tag1.setK("SBB");
            way.getRest().add(tag1);

            ways.add(way);
        }

        for (GpsPoint gpsPoint : trackData.getRun().getGpsPoints()) {
            Node node = new Node();
            BigInteger id = BigInteger.valueOf(idCounter++);
            node.setId(id);
            node.setLat((float) gpsPoint.getLatitude());
            node.setLon((float) gpsPoint.getLongitude());
            node.setVersion(1);
            nodes.add(node);
        }
    }

    public List<Object> getObjects() {
        return osm.getBoundOrUserOrPreferences();
    }

    public void append(ExtendedOsm other) {
        osm.getBoundOrUserOrPreferences().addAll(other.getObjects());
        extractLists();
    }

    private void setOsm(Osm osm) {
        this.osm = osm;
        extractLists();
    }

    private void extractLists() {
        nodes.clear();
        ways.clear();
        for (Object o : osm.getBoundOrUserOrPreferences()) {
            if (o instanceof Node) {
                nodes.add((Node) o);
            } else if (o instanceof Way) {
                ways.add((Way) o);
            }
        }
    }

    public ExtendedOsm extractSBBTracks() {
        CollectionUtils.filter(ways, ExtendedOsm::hasTracks);
        CollectionUtils.filter(ways, ExtendedOsm::hasRail);
        CollectionUtils.filter(ways, ExtendedOsm::isOperatedBySBB);
        clean();
        return this;
    }

    public ExtendedOsm clean() {
        removeDuplicates();
        removeUnreferencedNodes();
        return this;
    }

    private static boolean hasTracks(Way way) {
        return way.getRest().stream()
                .flatMap(CollectionUtils.instancesOf(Tag.class))
                .filter(t -> "tracks".equals(t.getK()))
                .findAny()
                .isPresent();
    }

    private static boolean hasRail(Way way) {
        return way.getRest().stream()
                .flatMap(CollectionUtils.instancesOf(Tag.class))
                .filter(t -> "railway".equals(t.getK()) && "rail".equals(t.getV()))
                .findAny()
                .isPresent();
    }

    private static boolean isOperatedBySBB(Way way) {
        return way.getRest().stream()
                .flatMap(CollectionUtils.instancesOf(Tag.class))
                .filter(t -> "operator".equals(t.getK()) && "SBB".equals(t.getV()))
                .findAny()
                .isPresent();
    }

    public ExtendedOsm normalizeIds() {
        int idCounter = 1; // 0 is not allowed in OSM

        Map<BigInteger, BigInteger> ids = new TreeMap<>();
        for (Node node : nodes) {
            BigInteger newId = BigInteger.valueOf(idCounter++);
            ids.put(node.getId(), newId);
            node.setId(newId);
        }
        for (Way way : ways) {
            BigInteger newId = BigInteger.valueOf(idCounter++);
            ids.put(way.getId(), newId);
            way.setId(newId);
        }

        ways.stream()
                .flatMap(w -> w.getRest().stream())
                .flatMap(CollectionUtils.instancesOf(Nd.class))
                .forEach(nd -> nd.setRef(ids.get(nd.getRef())));
        return this;
    }

    public void removeUnreferencedNodes() {
        Set<BigInteger> referencedIds =
                ways.stream()
                        .flatMap(w -> w.getRest().stream())
                        .flatMap(CollectionUtils.instancesOf(Nd.class))
                        .map(Nd::getRef)
                        .collect(toSet());
        CollectionUtils.filter(nodes, n -> referencedIds.contains(n.getId()));
    }

    private void removeDuplicates() {
        CollectionUtils.filter(nodes, CollectionUtils.distinctByKey(OsmBasicType::getId));
        CollectionUtils.filter(ways, CollectionUtils.distinctByKey(OsmBasicType::getId));
    }

    public void write(File file) {
//        clean();
        normalizeIds();
        osm.getBoundOrUserOrPreferences().clear();
        osm.getBoundOrUserOrPreferences().addAll(nodes);
        osm.getBoundOrUserOrPreferences().addAll(ways);
        JAXB.marshal(osm, file);
    }

    public TrackData toDomainObject() throws IOException {
        clean();

        TrackData trackData = new TrackData();

        Map<BigInteger, com.zuehlke.vr.domain.Node> nodeMap = new HashMap<>();
        for (Node node : nodes) {
            com.zuehlke.vr.domain.Node domainNode = new com.zuehlke.vr.domain.Node(node.getLat(), node.getLon());
            nodeMap.put(node.getId(), domainNode);
            trackData.getNodes().add(domainNode);
        }

//        new Elevation().extendNodesWithElevation(json.getNodes());

        for (Way way : ways) {
            com.zuehlke.vr.domain.Node last = null;
            for (BigInteger id : getRefs(way)) {
                com.zuehlke.vr.domain.Node node = nodeMap.get(id);
                if (last != null) {
                    trackData.getTracks().add(new Track(last, node));
                }
                last = node;
            }
        }
        return trackData;
    }

    private List<BigInteger> getRefs(Way way) {
        return way.getRest().stream()
                .flatMap(CollectionUtils.instancesOf(Nd.class))
                .map(Nd::getRef)
                .collect(toList());
    }
}
