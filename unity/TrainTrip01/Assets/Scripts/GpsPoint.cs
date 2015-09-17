using System.Collections;
using System.Collections.Generic;
using System;

public class GpsPoint : Node
{
	public long timeStamp;
	public double absoluteTime;
	public double accuracy;

	public GpsPoint (JSONObject json) : base(json)
	{
		timeStamp = JSONHelper.getByKey (json, "timeStamp").i;
		accuracy = JSONHelper.getByKey (json, "accuracy").n;
	}
}