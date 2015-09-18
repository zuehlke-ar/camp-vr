using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

public class GpsPoint : Node
{
	public long timeStamp;
	public double absoluteTime;
	public double accuracy;
	public Quaternion rotation;

	public GpsPoint (JSONObject json) : base(json)
	{
		timeStamp = JSONHelper.getByKey (json, "timeStamp").i;
		accuracy = JSONHelper.getByKey (json, "accuracy").n;
	}

	public void updateRotation(GpsPoint second){
		rotation = getRotation (second);
	}

	public Vector3 interpolatePos (GpsPoint second, double time)
	{
		return Vector3.Lerp (project (), second.project (), (float) getFract(second, time));
	}

	public Quaternion interpolateRot ( GpsPoint second, double time)
	{
		return Quaternion.Lerp (rotation, second.rotation, (float) getFract(second, time));
	}

	private double getFract (GpsPoint second, double time)
	{
		double deltaTime = second.absoluteTime - absoluteTime;
		double deltaFirst = time - absoluteTime;
		return deltaFirst / deltaTime;
	}
}