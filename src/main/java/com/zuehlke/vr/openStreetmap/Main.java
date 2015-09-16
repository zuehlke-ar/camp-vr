package com.zuehlke.vr.openStreetmap;

import com.zuehlke.vr.domain.GpsPoint;
import com.zuehlke.vr.domain.Point;
import com.zuehlke.vr.domain.Run;
import com.zuehlke.vr.domain.TrackData;
import com.zuehlke.vr.googleMaps.Klm;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ExtendedOsm osm = new ExtendedOsm(new File("data/correlate/winti_big_track_only.osm"))
                .extractSBBTracks();
        TrackData trackData = osm.toDomainObject();

        Run run = new Run()
                .name("Winterthur - Hardbruecke")
                .timestamp(0);
        run.setGpsPoints(Klm.getDataPointsFromKlmFile(new File("data/correlate/Winterthur - Hardbruecke.kml")));
        trackData.setRun(run);

        calculateRun(trackData);

        new ExtendedOsm(trackData)
                .write(new File("data/correlate/winti_big_track_only_test.osm"));
        trackData.toFile(new File("data/correlate/winti_big_track_only_test.json"));

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
//        for (GpsPoint gpsPoint : trackData.getRun().getGpsPoints()) {
//            Point point = new Point();
//
//            trackData.getRun().getPoints().add(point);
//        }
    }

    public static void main1(String[] args) throws IOException {
        List<GpsPoint> GpsPoints = Klm.getDataPointsFromKlmFile(new File("data/Winterthur - Hardbruecke.kml"));

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
            GpsPoint d = GpsPoints.get(i);
//            osm.addNode(i, d.getLatitude(), d.getLongitude());
            double dist = 0.0;//haversine(lastLatitude, lastLongitude, d.getLatitude(), d.getLongitude());

            if (dist > 1) {
                System.out.println("index: " + i + " " + dist);
//                ExtendedOsm localOsm = new ExtendedOsm(d.getLongitude(), d.getLatitude(), 0.0015);
//                osm.append(localOsm);
//                localOsm.write(new File("data\\temp" + i + ".osm"));
                lastLongitude = d.getLongitude();
                lastLatitude = d.getLatitude();
            }
        }

//        osm.removeUnreferencedNodes();
        osm.write(osmFile2);

//        System.out.println(osm.getNodeById("886973221").get().getLat());
    }

}
