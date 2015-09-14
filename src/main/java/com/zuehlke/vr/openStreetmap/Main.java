package com.zuehlke.vr.openStreetmap;

import com.zuehlke.vr.openStreetmap.json.TrackData;
import net.opengis.kml.DocumentType;
import net.opengis.kml.KmlType;
import net.opengis.kml.PlacemarkType;
import net.opengis.kml.ex.MultiTrackType;
import net.opengis.kml.ex.SimpleArrayDataType;
import net.opengis.kml.ex.TrackType;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        ExtendedOsm osm = new ExtendedOsm(new File("data/winti_big2.osm"));
        osm.removeNonSBBRails();
        osm.write(new File("data/winti_big2_rails_only.osm"));
        osm.writeToJson(new File("data/winti_big2_rails_only.json"));
    }

    public static void main1(String[] args) throws IOException {

        System.out.println(new File(".").getAbsolutePath());

        KmlType kml = JAXB.unmarshal(new File("data/Winterthur - Hardbruecke.kml"), KmlType.class);

        DocumentType doc = (DocumentType) kml.getAbstractFeatureGroup().getValue();
        PlacemarkType placemark = (PlacemarkType) doc.getAbstractFeatureGroup().get(1).getValue();
        MultiTrackType multiTrack = (MultiTrackType) placemark.getAbstractGeometryGroup().getValue();

        List<DataPoint> dataPoints = new ArrayList<>();
        for (TrackType track : multiTrack.getTrack()) {
            addDataPoints(track, dataPoints);
        }

        File osmFile = new File("data/winti_hb_original.osm");
//        ExtendedOsm osm = new ExtendedOsm(8.707025, 47.4664648879, 0.001);
//        osm.clean();
//        osm.write(osmFile);

        ExtendedOsm osm = new ExtendedOsm(osmFile);

        File osmFile2 = new File("data/winti_big2.osm");

        System.out.println(dataPoints.size());

        double lastLongitude = 0;
        double lastLatitude = 0;

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint d = dataPoints.get(i);
//            osm.addNode(i, d.getLatitude(), d.getLongitude());
            double dist = haversine(lastLatitude, lastLongitude, d.getLatitude(), d.getLongitude());

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

    public static double haversine(double lat1, double lng1, double lat2, double lng2) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c;
    }

    private static void addDataPoints(TrackType track, List<DataPoint> dataPoints) {
        Iterator<String> when = track.getWhen().iterator();
        Iterator<String> coord = track.getCoord().iterator();
        Iterator<String> accuracy = ((SimpleArrayDataType) track.getExtendedData().getSchemaData().get(0).getSchemaDataExtension().get(2).getValue()).getValue().iterator();

        while (when.hasNext()) {
            dataPoints.add(parseDataPoint(when.next(), coord.next(), accuracy.next()));
        }
    }

    private static DataPoint parseDataPoint(String when, String coord, String accuracy) {
        String[] coordArray = coord.split(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        long time = 0;
        try {
            time = sdf.parse(when).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new DataPoint(time, Double.parseDouble(coordArray[1]), Double.parseDouble(coordArray[0]), Double.parseDouble(coordArray[2]), Double.parseDouble(accuracy));
    }

}
