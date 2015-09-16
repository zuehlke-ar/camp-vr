package com.zuehlke.vr.openStreetmap.util;

public class GeoUtil {

    public static double approxDistance(double lat1, double lon1, double lat2, double lon2) {
        double r = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180.0;
        double dLon = (lon2 - lon1) * Math.PI / 180.0;
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) +
                Math.cos(lat1 * Math.PI / 180.0) * Math.cos(lat2 * Math.PI / 180.0) *
                        Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return r * c * 1000.0;
    }

    /**
     * Returns closest point on segment to point
     *
     * @param sx1 segment x coord 1
     * @param sy1 segment y coord 1
     * @param sx2 segment x coord 2
     * @param sy2 segment y coord 2
     * @param px  point x coord
     * @param py  point y coord
     * @return closets point on segment to point
     */
    public static double[] getClosestPointOnSegment(double sx1, double sy1, double sx2, double sy2, double px, double py) {
        double xDelta = sx2 - sx1;
        double yDelta = sy2 - sy1;

//        if ((xDelta == 0) && (yDelta == 0)) {
//            throw new IllegalArgumentException("Segment start equals segment end");
//        }

        double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

        double[] closestPoint = new double[3];
        if (u < 0) {
            closestPoint[0] = sx1;
            closestPoint[1] = sy1;
        } else if (u > 1) {
            closestPoint[0] = sx2;
            closestPoint[1] = sy2;
        } else {
            closestPoint[0] = sx1 + u * xDelta;
            closestPoint[1] = sy1 + u * yDelta;
        }

        closestPoint[2] = u;

        return closestPoint;
    }
}
