using System;

public class Node
{
	public long id;
	public double lat;
	public double lon;
	public double ele;
	
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
}