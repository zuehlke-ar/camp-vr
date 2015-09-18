using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

public class TrackGenerator : MonoBehaviour
{	

	public TextAsset asset;
	public GameObject trackProfile;

	private static List<Track> tracks;
	private static List<GpsPoint> gpsPoints;
	private static Node origin;
	private double time = 0.0;

	void Start ()
	{
		LoadData ();
		time = 0.0;

		GpsPoint last = null;
		foreach (GpsPoint g in gpsPoints) {
			if (last != null) { 
				float width = 1.5f / 2.0f;
				float h = 1.0f;
				
				Vector3 centerLast = last.project ();
				Vector3 leftLast = centerLast + (last.rotation * new Vector3 (-width, -h, 0));
				Vector3 rightLast = centerLast + (last.rotation * new Vector3 (width, -h, 0));
				
				Vector3 center = g.project ();
				Vector3 left = center + (g.rotation * new Vector3 (-width, -h, 0));
				Vector3 right = center + (g.rotation * new Vector3 (width, -h, 0));

				GameObject obj = (GameObject) Instantiate (trackProfile, centerLast + new Vector3 (0, -1.5f, 0),  Quaternion.Euler (0, 90, 0)*last.rotation);

				Vector3 scale = new Vector3(obj.transform.localScale.x,obj.transform.localScale.y,obj.transform.localScale.z);
				scale.x *= Vector3.Distance(leftLast,left);
				obj.transform.localScale = scale;
			}
			last = g;
		}
	}

	void Update ()
	{
		double speedFactor = 1.0;
		if (Input.GetKey ("space")) {
			speedFactor = 10.0;
		}
		time += Time.deltaTime * speedFactor;

		Transform camera = GameObject.Find ("Camera").transform;

		GpsPoint last = gpsPoints [0];
		foreach (GpsPoint g in gpsPoints) {
			if (g.absoluteTime > time) {
				camera.position = last.interpolatePos (g, time);
				camera.rotation = last.interpolateRot (g, time);
				break;
			}
			last = g;
		}
	}

	void OnDrawGizmos ()
	{
		LoadData ();

		foreach (Track t in tracks) {
			Gizmos.color = Color.red;
			Gizmos.DrawLine (t.from.project (), t.to.project ());
		}

		GpsPoint last = null;
		foreach (GpsPoint g in gpsPoints) {
			if (last != null) {
				Gizmos.color = Color.green;
				Gizmos.DrawLine (last.project (), g.project ());
			}
			last = g;
		} 
	}

	private void LoadData ()
	{
		if (tracks != null) {
			return;
		}

		JSONObject json = new JSONObject (asset.text);
		
		var nodesJson = JSONHelper.getByKey (json, "nodes").list;
		var nodeMap = new Dictionary<long, Node> ();
		foreach (JSONObject n in nodesJson) {
			Node node = new Node (n);
			nodeMap [node.id] = node;
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
		Node.origin = origin;
		Debug.Log ("origin: " + origin.lat + " " + origin.lon + " " + origin.ele);

		GpsPoint last = null;
		foreach (GpsPoint g in gpsPoints) {
			if (last != null) {
				last.updateRotation (g);
			}
			last = g;
		}

		List<Vector3> vectors = new List<Vector3> ();
		List<int> triangles = new List<int> ();

		int count = 0;
		last = null;
		foreach (GpsPoint g in gpsPoints) {
			if (last != null) { 
				float width = 1.5f / 2.0f;
				float h = 2.0f;

				Vector3 centerLast = last.project ();
				Vector3 leftLast = centerLast + (last.rotation * new Vector3 (-width, -h, 0));
				Vector3 rightLast = centerLast + (last.rotation * new Vector3 (width, -h, 0));

				Vector3 center = g.project ();
				Vector3 left = center + (g.rotation * new Vector3 (-width, -h, 0));
				Vector3 right = center + (g.rotation * new Vector3 (width, -h, 0));

				vectors.Add (right);
				vectors.Add (rightLast);
				vectors.Add (leftLast);

				vectors.Add (right);
				vectors.Add (leftLast);
				vectors.Add (left);
				
				triangles.Add (count++);
				triangles.Add (count++);
				triangles.Add (count++);
				triangles.Add (count++);
				triangles.Add (count++);
				triangles.Add (count++);

				//Instantiate (trackProfile, Vector3.zero, Quaternion.identity);
			}
			last = g;
		}
			
//		Mesh mesh = new Mesh ();
//		GetComponent<MeshFilter> ().mesh = mesh;
//		mesh.vertices = vectors.ToArray ();
//		mesh.uv = new Vector2[]{};
//		mesh.triangles = triangles.ToArray ();
	}
}
