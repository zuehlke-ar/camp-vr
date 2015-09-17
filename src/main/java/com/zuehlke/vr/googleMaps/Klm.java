package com.zuehlke.vr.googleMaps;

import com.zuehlke.vr.domain.GpsPoint2;
import net.opengis.kml.DocumentType;
import net.opengis.kml.KmlType;
import net.opengis.kml.PlacemarkType;
import net.opengis.kml.ex.MultiTrackType;
import net.opengis.kml.ex.SimpleArrayDataType;
import net.opengis.kml.ex.TrackType;

import javax.xml.bind.JAXB;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Klm {

    public static List<GpsPoint2> getDataPointsFromKlmFile(File file) {
        KmlType kml = JAXB.unmarshal(file, KmlType.class);

        DocumentType doc = (DocumentType) kml.getAbstractFeatureGroup().getValue();
        PlacemarkType placemark = (PlacemarkType) doc.getAbstractFeatureGroup().get(1).getValue();
        MultiTrackType multiTrack = (MultiTrackType) placemark.getAbstractGeometryGroup().getValue();

        List<GpsPoint2> gpsPoints = new ArrayList<>();
        for (TrackType track : multiTrack.getTrack()) {
            addDataPoints(track, gpsPoints);
        }

        return gpsPoints;
    }

    private static void addDataPoints(TrackType track, List<GpsPoint2> gpsPoints) {
        Iterator<String> when = track.getWhen().iterator();
        Iterator<String> coord = track.getCoord().iterator();
        Iterator<String> accuracy = ((SimpleArrayDataType) track.getExtendedData().getSchemaData().get(0).getSchemaDataExtension().get(2).getValue()).getValue().iterator();

        while (when.hasNext()) {
            gpsPoints.add(parseDataPoint(when.next(), coord.next(), accuracy.next()));
        }
    }

    private static GpsPoint2 parseDataPoint(String when, String coord, String accuracy) {
        String[] coordArray = coord.split(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        long time = 0;
        try {
            time = sdf.parse(when).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new GpsPoint2(time, Double.parseDouble(coordArray[1]), Double.parseDouble(coordArray[0]), Double.parseDouble(coordArray[2]), Double.parseDouble(accuracy));
    }
}
