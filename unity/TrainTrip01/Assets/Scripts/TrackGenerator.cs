using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

public class TrackGenerator : MonoBehaviour
{
	
	//	public Vector3[] newVertices;
	//	public Vector2[] newUV;
	//	public int[] newTriangles;
	//	void Start() {
	//		Mesh mesh = new Mesh();
	//		GetComponent<MeshFilter>().mesh = mesh;
	//		mesh.vertices = newVertices;
	//		mesh.uv = newUV;
	//		mesh.triangles = newTriangles;
	//	}
	
	public TextAsset asset;
	private static List<Track> tracks;
	private static List<GpsPoint> gpsPoints;
	private static Node origin;

	void Start ()
	{
		LoadData ();
	}

	void Update () 
	{
		long startTime = gpsPoints [0].timeStamp;
		double t = Time.time * 10.0;

		GpsPoint last = gpsPoints [0];
		foreach (GpsPoint g in gpsPoints) {
			if (g.absoluteTime > t) {
				Debug.Log (g.lat);
				GameObject.Find ("Camera").transform.position = interpolate (t, last, g);
				break;
			}
			last = g;
		}

	
//		foreach (Track t in gpsPoints) {
//			if (g.absoluteTime > t) {
//				Debug.Log (g.lat);
//				GameObject.Find ("Camera").transform.position = interpolate (t, last, g);
//				break;
//			}
//			last = g;
//		}
	}

	private Vector3 interpolate (double time, GpsPoint first, GpsPoint second)
	{
		double deltaTime = second.absoluteTime - first.absoluteTime;
		double deltaFirst = time - first.absoluteTime;
		double fract = deltaFirst / deltaTime;
		return  Vector3.Lerp (convertCoords (origin, first), convertCoords (origin, second), (float)fract);
	}
	
	void OnDrawGizmos ()
	{
		if (tracks == null) {
			LoadData ();
		}

		foreach (Track t in tracks) {
			//			Mesh mesh = new Mesh();
			//			GetComponent<MeshFilter>().mesh = mesh;
			//			mesh.vertices;
			//			mesh.uv = newUV;
			//			mesh.triangles = newTriangles;
			Gizmos.color = Color.red;
			Gizmos.DrawLine (convertCoords (origin, t.from),
			                 convertCoords (origin, t.to));
		}

		GpsPoint last = null;
		foreach (GpsPoint g in gpsPoints) {
			//			Mesh mesh = new Mesh();
			//			GetComponent<MeshFilter>().mesh = mesh;
			//			mesh.vertices;
			//			mesh.uv = newUV;
			//			mesh.triangles = newTriangles;
			if (last != null) {
				Gizmos.color = Color.green;
				Gizmos.DrawLine (convertCoords (origin, last),
			                 convertCoords (origin, g));
			}
			last = g;
		}
	}

	private void LoadData ()
	{
		JSONObject json = new JSONObject (asset.text);
		
		origin = new Node ();
		origin.lat = 0.0; // max
		origin.lon = 1000.0; // min
		origin.ele = 0.0;
		
		var nodesJson = JSONHelper.getByKey (json, "nodes").list;
		var nodeMap = new Dictionary<long, Node> ();
		foreach (JSONObject n in nodesJson) {
			Node node = new Node (n);
			nodeMap [node.id] = node;
			origin.lat = Math.Max (origin.lat, node.lat);
			origin.lon = Math.Min (origin.lon, node.lon);
		}

		var tracksJson = JSONHelper.getByKey (json, "tracks").list;
		tracks = new List<Track> ();
		foreach (JSONObject t in tracksJson) {
			tracks.Add (new Track (t, nodeMap));
		}

		var runJson = JSONHelper.getByKey (json, "run");
		var gpsPointsJson = JSONHelper.getByKey (runJson, "gpsPoints").list;
		gpsPoints = new List<GpsPoint> ();
		foreach (JSONObject g in gpsPointsJson) {
			GpsPoint gpsPoint = new GpsPoint (g);
			gpsPoints.Add (gpsPoint);
			gpsPoint.absoluteTime = (gpsPoint.timeStamp - gpsPoints [0].timeStamp) / 1000.0;
		}

		origin = gpsPoints [0];
		Debug.Log ("origin: " + origin.lat + " " + origin.lon + " " + origin.ele);
	}
	
	private Vector3 convertCoords (Node origin, Node node)
	{
		double distLat = approxDistance (origin.lat, origin.lon, node.lat, origin.lon);
		double distLon = approxDistance (origin.lat, origin.lon, origin.lat, node.lon);
		var vec = new Vector3 ((float)distLon, (float)(node.ele - origin.ele), (float)distLat);
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
	
	//		for(float i = 0; i <= 1.0f;)  
	//		{  
	//			Interpol
	//			Vector2 p = CatmullRom.calculatePoint(dataSet, i);  
	//			Vector2 deriv = CatmullRom.calculateDerivative(dataSet, i);  
	//			float len = deriv.Length();  
	//			i += step / len;  
	//			deriv.divide(len);  
	//			deriv.scale(thickness);  
	//			deriv.set(-deriv.y, deriv.x);  
	//			Vector2 v1 = new Vector2();  
	//			v1.set(p).add(deriv);  
	//			vertices.add(v1);  
	//			Vector2 v2 = new Vector2();  
	//			v2.set(p).sub(deriv);  
	//			vertices.add(v2);  
	//			
	//			if(i > 1.0f) i = 1.0f;  
	//		}
	
//	void Update ()
//	{
//		Mesh mesh = GetComponent<MeshFilter> ().mesh;
//		Vector3[] vertices = mesh.vertices;
//		Vector3[] normals = mesh.normals;
//		int i = 0;
//		while (i < vertices.Length) {
//			vertices [i] += normals [i] * Mathf.Sin (Time.time * 5.0f) * 0.004f;
//			i++;
//		}
//		mesh.vertices = vertices;
//	}
}
