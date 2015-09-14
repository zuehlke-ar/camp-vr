using UnityEngine;
using System.Collections;

public class LookAtPlayer : MonoBehaviour {
	
	public Transform target;
	void Update() {
		transform.LookAt(target);
	}
}