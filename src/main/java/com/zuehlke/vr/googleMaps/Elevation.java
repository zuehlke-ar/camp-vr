package com.zuehlke.vr.googleMaps;

import com.google.common.collect.Lists;
import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.zuehlke.vr.openStreetmap.json.Node;

import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Elevation {

    private GeoApiContext context;

    public Elevation() {
        context = new GeoApiContext().setApiKey("AIzaSyBSs8oOMH-u57MunNlOOO7vUAnlfU1IE4I");
    }

    public void extendNodesWithElevation(List<Node> allNodes) {
        Lists.partition(allNodes, 32).forEach(this::extendNodeSublistWithElevation);
    }

    private void extendNodeSublistWithElevation(List<Node> sublist)  {
        List<LatLng> latLng = sublist.stream()
                .map(n -> new LatLng(n.getLat(), n.getLon()))
                .collect(toList());

        ElevationResult[] result = new ElevationResult[0];
        try {
            result = ElevationApi.getByPoints(context, new EncodedPolyline(latLng)).await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<Node> iterator = sublist.iterator();
        for (ElevationResult elevationResult : result) {
            iterator.next().setEle(elevationResult.elevation);
        }
    }
}
