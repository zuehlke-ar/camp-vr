using UnityEngine;
using System;

public class Node
{
	public long id;
	public double lat;
	public double lon;
	public double ele;

	public static Node origin;
	
	public Node ()
	{
		
	}
	
	public Node (JSONObject json)
	{
		JSONObject idJson = JSONHelper.getByKey (json, "@id");
		if (idJson != null) {
			id = idJson.i;
		}
		lat = JSONHelper.getByKey (json, "lat").n;
		lon = JSONHelper.getByKey (json, "lon").n;
		ele = JSONHelper.getByKey (json, "ele").n;
	}

	public Quaternion getRotation (Node second)
	{
		Vector3 relativePos = second.project () - project ();
		Quaternion rotation = Quaternion.LookRotation (relativePos);
		rotation.x = 0;
		rotation.z = 0;
		return rotation;
	}

	public Vector3 project() {
		double distLat = approxDistance (origin.lat, origin.lon, lat, origin.lon);
		double distLon = approxDistance (origin.lat, origin.lon, origin.lat, lon);
		var vec = new Vector3 ((float)distLon, (float)(ele - origin.ele), (float)distLat);
		vec = Quaternion.Euler (0, 180, 0) * vec;
		return vec;
	}

	private double approxDistance (double lat1, double lon1, double lat2, double lon2)
	{
		var r = 6378.137; // Radius of earth in KM
		var dLat = (lat2 - lat1) * Math.PI / 180.0;
		var dLon = (lon2 - lon1) * Math.PI / 180.0;
		var a = Math.Sin (dLat / 2.0) * Math.Sin (dLat / 2.0) +
			Math.Cos (lat1 * Math.PI / 180.0) * Math.Cos (lat2 * Math.PI / 180.0) *
				Math.Sin (dLon / 2.0) * Math.Sin (dLon / 2.0);
		var c = 2.0 * Math.Atan2 (Math.Sqrt (a), Math.Sqrt (1.0 - a));
		return r * c * 1000.0;
	}
}