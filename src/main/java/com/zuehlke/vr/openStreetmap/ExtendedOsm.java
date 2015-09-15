package com.zuehlke.vr.openStreetmap;

import com.zuehlke.vr.googleMaps.Elevation;
import com.zuehlke.vr.openStreetmap.json.*;
import generated.osm.*;
import generated.osm.Node;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ExtendedOsm {

    private static final String OPENSTREETMAP_API_06_URL = "http://api.openstreetmap.org/api/0.6/";
    private static final List<Class> SORT_ORDER = Arrays.asList(Node.class, Way.class, Relation.class);

    private Osm osm;
    private List<Node> nodes = new ArrayList<>();
    private List<Way> ways = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();

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
        relations.clear();
        for (Object o : osm.getBoundOrUserOrPreferences()) {
            if (o instanceof Node) {
                nodes.add((Node) o);
            } else if (o instanceof Way) {
                ways.add((Way) o);
            } else if (o instanceof Relation) {
                relations.add((Relation) o);
            }
        }
    }

//    public void clean() {
//        List<Object> filtered = osm.getBoundOrUserOrPreferences().stream()
//                .filter(o -> o instanceof Node || isRailRelated(o))
//                .collect(toList());
//
//        final List<BigInteger> referencedIds = filtered.stream()
//                .filter(o -> o instanceof Way)
//                .flatMap(o -> ((Way) o).getRest().stream())
//                .filter(o -> o instanceof Nd)
//                .map(o -> ((Nd) o).getRef())
//                .distinct()
//                .collect(toList());
//
//        filtered = filtered.stream()
//                .filter(o -> o instanceof Way || isReferenced(o, referencedIds))
//                .collect(toList());
//
//        osm.getBoundOrUserOrPreferences().clear();
//        osm.getBoundOrUserOrPreferences().addAll(filtered);
//    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static <T> void filter(List<T> list, Predicate<T> predicate) {
        List<T> filtered = list.stream().filter(predicate).collect(toList());
        list.clear();
        list.addAll(filtered);
    }

    private static boolean isRailRelated(Way way) {
        return way.getRest().stream()
                .flatMap(instancesOf(Tag.class))
                .filter(t -> "railway".equals(t.getK()))
                .findAny()
                .isPresent();
    }

    private static boolean hasTracks(Way way) {
        return way.getRest().stream()
                .flatMap(instancesOf(Tag.class))
                .filter(t -> "tracks".equals(t.getK()))
                .findAny()
                .isPresent();
    }

    private static boolean hasRail(Way way) {
        return way.getRest().stream()
                .flatMap(instancesOf(Tag.class))
                .filter(t -> "railway".equals(t.getK()) && "rail".equals(t.getV()))
                .findAny()
                .isPresent();
    }

    private static boolean isOperatedBySBB(Way way) {
        return way.getRest().stream()
                .flatMap(instancesOf(Tag.class))
                .filter(t -> "operator".equals(t.getK()) && "SBB".equals(t.getV()))
                .findAny()
                .isPresent();
    }

    private boolean isReferenced(Object o, List<BigInteger> ids) {
        return o instanceof Node && ids.contains(((Node) o).getId());
    }

    public void removeNonSBBRails() {
        filter(ways, ExtendedOsm::hasTracks);
        filter(ways, ExtendedOsm::hasRail);
        filter(ways, ExtendedOsm::isOperatedBySBB);
        removeUnreferencedNodes();
        relations.clear();
    }

    public void removeUnreferencedNodes() {
        Set<BigInteger> referencedIds =
                ways.stream()
                        .flatMap(w -> w.getRest().stream())
                        .flatMap(instancesOf(Nd.class))
                        .map(Nd::getRef)
                        .collect(toSet());
        filter(nodes, n -> referencedIds.contains(n.getId()));
    }

    private void removeDuplicates() {
        filter(nodes, distinctByKey(OsmBasicType::getId));
        filter(ways, distinctByKey(OsmBasicType::getId));
        filter(relations, distinctByKey(OsmBasicType::getId));
    }

    public void write(File file) {
        removeDuplicates();
        osm.getBoundOrUserOrPreferences().clear();
        osm.getBoundOrUserOrPreferences().addAll(nodes);
        osm.getBoundOrUserOrPreferences().addAll(ways);
        osm.getBoundOrUserOrPreferences().addAll(relations);
        JAXB.marshal(osm, file);
    }

    public void writeToJson(File file) throws IOException {
        removeDuplicates();
        TrackData json = new TrackData();

        for (Node node : nodes) {
            json.getNodes().add(new com.zuehlke.vr.openStreetmap.json.Node(node.getId().intValue(), node.getLat(), node.getLon()));
        }

        new Elevation().extendNodesWithElevation(json.getNodes());

        for (Way way : ways) {
            BigInteger last = null;
            for (BigInteger id : getRefs(way)) {
                if (last != null) {
                    json.getTracks().add(new Track(last.intValue(), id.intValue()));
                }
                last = id;
            }
        }

        json.toFile(file);
    }


    public Optional<Node> getNodeById(String id) {
        return getNodeById(new BigInteger(id));
    }

    public List<BigInteger> getRefs(Way way) {
        return way.getRest().stream()
                .flatMap(instancesOf(Nd.class))
                .map(Nd::getRef)
                .collect(toList());
    }

    public void addNode(long id, double lat, double lon) {
        Node node = new Node();
        node.setId(BigInteger.valueOf(id + 1));
        node.setLat((float) lat);
        node.setLon((float) lon);
        node.setVersion(1);
        osm.getBoundOrUserOrPreferences().add(0, node);
    }

    public void addNode(Node node) {
        osm.getBoundOrUserOrPreferences().add(0, node);
    }

    public void addWay(Way way) {
        osm.getBoundOrUserOrPreferences().add(way);
    }

    public Optional<Node> getNodeById(BigInteger id) {
        Optional<Node> node = osm.getBoundOrUserOrPreferences().stream()
                .filter(o -> o instanceof Node)
                .map(o -> (Node) o)
                .filter(n -> n.getId().equals(id))
                .findFirst();
        return node;
    }

    public static <E> Function<Object, Stream<E>> instancesOf(Class<E> cls) {
        return o -> cls.isInstance(o)
                ? Stream.of(cls.cast(o))
                : Stream.empty();
    }
}
