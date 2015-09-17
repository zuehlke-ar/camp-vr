using System.Collections;
using System.Collections.Generic;
using System;

public class Track
{
	public Node from;
	public Node to;
	
	public Track (JSONObject json, Dictionary<long, Node> nodes)
	{
		JSONObject fromObject = JSONHelper.getByKey (json, "from");
		JSONObject toObject = JSONHelper.getByKey (json, "to");
		
		long fromRef = JSONHelper.getByKey (fromObject, "@ref").i;
		long toRef = JSONHelper.getByKey (toObject, "@ref").i;
		
		from = nodes [fromRef];
		to = nodes [toRef];
	}
}