using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Node {
	public long id;
	public double lat;
	public double lon;

	public static Node fromJSON(JSONObject json){
		Node node = new Node();
		node.id = TrackGenerator.getByKey (json, "id").i;
		node.lat = TrackGenerator.getByKey (json, "lat").n;
		node.lon = TrackGenerator.getByKey (json, "lon").n;
		return node;
	}
}

public class Track {
	public long fromRef;
	public long toRef;
	public Node from;
	public Node to;
	
	public static Track fromJSON(JSONObject json){
		Track track = new Track();
		track.fromRef = TrackGenerator.getByKey (json, "fromRef").i;
		track.toRef = TrackGenerator.getByKey (json, "toRef").i;
		return track;
	}
}

public class TrackGenerator : MonoBehaviour {

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

	void OnDrawGizmos() {
		JSONObject json = new JSONObject (asset.text);

		var nodesJson = getByKey (json, "nodes").list;
		var nodeMap = new Dictionary<long, Node>();
		foreach(JSONObject n in nodesJson){
			Node node = Node.fromJSON(n);
			nodeMap[node.id] = node;
		}

		var tracksJson = getByKey (json, "tracks").list;
		var trackList = new List<Track>();
		foreach(JSONObject t in tracksJson){
			Track track = Track.fromJSON(t);
			track.from = nodeMap[track.fromRef];
			track.to = nodeMap[track.toRef];
			trackList.Add(track);
		}

		foreach (Track t in trackList) {
//			Mesh mesh = new Mesh();
//			GetComponent<MeshFilter>().mesh = mesh;
//			mesh.vertices;
//			mesh.uv = newUV;
//			mesh.triangles = newTriangles;
			Gizmos.color = Color.red;
			Gizmos.DrawLine(new Vector3(((float)t.from.lat-47.48950f)*10000f, -1f, ((float)t.from.lon-8.70850f)*10000f),
			                new Vector3(((float)t.to.lat-47.48950f)*10000f, -1f, ((float)t.to.lon-8.70850f)*10000f));
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
	}

	//Display without having to press Play
//	void OnDrawGizmos() {
//		Gizmos.color = Color.white;
//		
//		//Draw a sphere at each control point
//		for (int i = 0; i < 10; i++) {
//			Gizmos.DrawWireSphere(new Vector3(i,i,i), 0.3f);
//		}
//	}

	public static JSONObject getByKey(JSONObject json, string key) {
		for(int i = 0; i < json.list.Count; i++){
			string otherKey = (string)json.keys[i];
			if (string.Equals(key, otherKey)){
				return (JSONObject)json.list[i];
			}
		}
		return null;
	}
	
	void Update() {
		Mesh mesh = GetComponent<MeshFilter>().mesh;
		Vector3[] vertices = mesh.vertices;
		Vector3[] normals = mesh.normals;
		int i = 0;
		while (i < vertices.Length) {
			vertices[i] += normals[i] * Mathf.Sin(Time.time * 5.0f) * 0.004f;
			i++;
		}
		mesh.vertices = vertices;
	}
}
