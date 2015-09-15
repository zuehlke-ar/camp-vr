using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

public class Node
{
	public long id;
	public double lat;
	public double lon;
	public double ele;

	public Node(){

	}

	public Node(JSONObject json){
		id = TrackGenerator.getByKey (json, "id").i;
		lat = TrackGenerator.getByKey (json, "lat").n;
		lon = TrackGenerator.getByKey (json, "lon").n;
		ele = TrackGenerator.getByKey (json, "ele").n;
	}
}

public class Track
{
	public Node from;
	public Node to;

	public Track(JSONObject json, Dictionary<long, Node> nodes){
		from = nodes[TrackGenerator.getByKey (json, "fromRef").i];
		to = nodes[TrackGenerator.getByKey (json, "toRef").i];
	}
}

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

	void OnDrawGizmos ()
	{
		JSONObject json = new JSONObject (asset.text);

		Node origin = new Node ();
		origin.lat = 0.0; // max
		origin.lon = 1000.0; // min
		origin.ele = 9000.0; // min

		var nodesJson = getByKey (json, "nodes").list;
		var nodeMap = new Dictionary<long, Node> ();
		foreach (JSONObject n in nodesJson) {
			Node node = new Node(n);
			nodeMap [node.id] = node;
			origin.lat = Math.Max (origin.lat, node.lat);
			origin.lon = Math.Min (origin.lon, node.lon);
			origin.ele = Math.Min (origin.ele, node.ele);
		}

		var tracksJson = getByKey (json, "tracks").list;
		var trackList = new List<Track> ();
		foreach (JSONObject t in tracksJson) {
			trackList.Add (new Track(t, nodeMap));
		}

		foreach (Track t in trackList) {
//			Mesh mesh = new Mesh();
//			GetComponent<MeshFilter>().mesh = mesh;
//			mesh.vertices;
//			mesh.uv = newUV;
//			mesh.triangles = newTriangles;
			Gizmos.color = Color.red;
			Gizmos.DrawLine (convertCoords(origin, t.from),
			                 convertCoords(origin, t.to));
		}
	}

	private Vector3 convertCoords(Node origin, Node node){
		double distLat = approxDistance (origin.lat, origin.lon, node.lat, origin.lon);
		double distLon = approxDistance (origin.lat, origin.lon, origin.lat, node.lon);
		Vector3 vec = Quaternion.Euler (0, 90, 0) * (new Vector3 ((float)distLat, (float)(node.ele - origin.ele), (float)distLon));
		vec += new Vector3 (-1376, 25, 723);
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

	public static JSONObject getByKey (JSONObject json, string key)
	{
		for (int i = 0; i < json.list.Count; i++) {
			string otherKey = (string)json.keys [i];
			if (string.Equals (key, otherKey)) {
				return (JSONObject)json.list [i];
			}
		}
		return null;
	}
	
	void Update ()
	{
		Mesh mesh = GetComponent<MeshFilter> ().mesh;
		Vector3[] vertices = mesh.vertices;
		Vector3[] normals = mesh.normals;
		int i = 0;
		while (i < vertices.Length) {
			vertices [i] += normals [i] * Mathf.Sin (Time.time * 5.0f) * 0.004f;
			i++;
		}
		mesh.vertices = vertices;
	}
}
