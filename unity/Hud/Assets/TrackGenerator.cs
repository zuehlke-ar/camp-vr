using UnityEngine;
using System.Collections;

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
