package com.zuehlke.vr.openStreetmap;

import com.zuehlke.vr.domain.*;
import com.zuehlke.vr.googleMaps.Klm;
import com.zuehlke.vr.openStreetmap.util.GeoUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ExtendedOsm osm = new ExtendedOsm(new File("data/correlate/winti_big.osm"))
                .extractSBBTracks();
        TrackData trackData = osm.toDomainObject();

        Run run = new Run()
                .name("Winterthur - Hardbruecke")
                .timestamp(0);
        run.setGpsPoints(Klm.getDataPointsFromKlmFile(new File("data/correlate/Winterthur - Hardbruecke.kml")));
        trackData.setRun(run);

        calculateRun(trackData);

        new ExtendedOsm(trackData)
                .write(new File("data/correlate/winti_big_clean.osm"));
        trackData.toFile(new File("data/correlate/winti_big_clean.json"));

//        Map<GpsPoint, double[]> nearestNode = new HashMap<>();
//
//        for (GpsPoint gpsPoint : run.getGpsPoints()) {
//            double minDist = Double.MAX_VALUE;
//
//            for (Track track : trackData.getTracks()) {
//                com.zuehlke.vr.domain.Node from = track.getFrom();
//                com.zuehlke.vr.domain.Node to = track.getTo();
//                double[] closestPoint = GeoUtil.getClosestPointOnSegment(from.getLon(), from.getLat(), to.getLon(), to.getLat(), gpsPoint.getLongitude(), gpsPoint.getLatitude());
//
//                double dist = GeoUtil.approxDistance(d.getLatitude(),d.getLongitude(),closestPoint[1], closestPoint[0]);
//                if (dist < minDist) {
//                    nearestNode.put(d, closestPoint);
//                    minDist = dist;
//                }
//            }
//        }
//
//        List<Node> nodes = Lists.newArrayList();
//        for (GpsPoint d : dataPoints) {
//            Node node = osm.addNode(d.getLatitude(), d.getLongitude());
//            nodes.add(node);
//
//            Node referenceNode = osm.addNode(nearestNode.get(d)[1], nearestNode.get(d)[0]);
//            osm.addWay(Arrays.asList(node, referenceNode), "highway", "footway");
//        }
//
//        osm.addWay(nodes, "highway", "footway");
//
//        trackData = osm.toDomainObject();
//        trackData.toFile(new File("data/correlate/winti_big_track_only_temp.json"));
//
//        osm.write(new File("data/correlate/winti_big_track_only_temp.osm"));
//        osm.writeToJson();
    }

    public static void calculateRun(TrackData trackData) {
        for (GpsPoint2 gpsPoint : trackData.getRun().getGpsPoints()) {
            double minDist = Double.MAX_VALUE;
            Track nearestTrack = null;
            double[] nearestPoint = null;
            double nearestEle = 0.0;
            for (Track track : trackData.getTracks()) {
                com.zuehlke.vr.domain.Node from = track.getFrom();
                com.zuehlke.vr.domain.Node to = track.getTo();

                double[] point = GeoUtil.getClosestPointOnSegment(from.getLon(), from.getLat(), to.getLon(), to.getLat(), gpsPoint.getLon(), gpsPoint.getLat());
                double dist = GeoUtil.approxDistance(gpsPoint.getLat(), gpsPoint.getLon(), point[1], point[0]);

                if (dist < minDist) {
                    nearestTrack = track;
                    minDist = dist;
                    nearestPoint = point;
                    nearestEle = (from.getEle() + to.getEle()) / 2.0;
                }
            }
            if (nearestTrack != null) {
                gpsPoint.setLat(nearestPoint[1]);
                gpsPoint.setLon(nearestPoint[0]);
                gpsPoint.setEle(nearestEle);
//                Point point = new Point();
//                point.debug = "" + gpsPoint.getLat() + " " + gpsPoint.getLon();
//                point.setTimestamp(gpsPoint.getTimeStamp());
//                point.setTrack(nearestTrack);
//
//                double whole = GeoUtil.approxDistance(nearestTrack.getFrom().getLat(), nearestTrack.getFrom().getLon(), nearestTrack.getTo().getLat(), nearestTrack.getTo().getLon());
//                double fract = GeoUtil.approxDistance(nearestTrack.getFrom().getLat(), nearestTrack.getFrom().getLon(), nearestPoint[1], nearestPoint[0]);
//                point.setPosition(fract / whole);
//
////                trackData.getNodes().add(gpsPoint);
//                trackData.getRun().getPoints().add(point);
            }
        }
    }

    public static void main1(String[] args) throws IOException {
        List<GpsPoint2> GpsPoints = Klm.getDataPointsFromKlmFile(new File("data/Winterthur - Hardbruecke.kml"));

        File osmFile = new File("data/winti_hb_original.osm");
//        ExtendedOsm osm = new ExtendedOsm(8.707025, 47.4664648879, 0.001);
//        osm.clean();
//        osm.write(osmFile);

        ExtendedOsm osm = new ExtendedOsm(osmFile);

        File osmFile2 = new File("data/winti_big2.osm");

        System.out.println(GpsPoints.size());

        double lastLongitude = 0;
        double lastLatitude = 0;

        for (int i = 0; i < GpsPoints.size(); i++) {
            GpsPoint2 d = GpsPoints.get(i);
//            osm.addNode(i, d.getLatitude(), d.getLongitude());
            double dist = 0.0;//haversine(lastLatitude, lastLongitude, d.getLatitude(), d.getLongitude());

            if (dist > 1) {
                System.out.println("index: " + i + " " + dist);
//                ExtendedOsm localOsm = new ExtendedOsm(d.getLongitude(), d.getLatitude(), 0.0015);
//                osm.append(localOsm);
//                localOsm.write(new File("data\\temp" + i + ".osm"));
                lastLongitude = d.getLon();
                lastLatitude = d.getLat();
            }
        }

//        osm.removeUnreferencedNodes();
        osm.write(osmFile2);

//        System.out.println(osm.getNodeById("886973221").get().getLat());
    }

}
