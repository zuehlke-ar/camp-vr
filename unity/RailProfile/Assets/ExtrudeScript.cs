using UnityEngine;
using System.Collections;

public class ExtrudeScript : MonoBehaviour {

	// Use this for initialization
	void Start () {
		Mesh mesh = GetComponent<MeshFilter>().mesh;


		foreach (Vector3 vertex in mesh.vertices) {
			Debug.Log (vertex.x+" "+vertex.y+" "+vertex.z);
		}
	}
	
	// Update is called once per frame
	void Update () {
	
	}
}
