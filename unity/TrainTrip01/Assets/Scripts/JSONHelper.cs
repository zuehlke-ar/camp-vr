using System;

public class JSONHelper
{
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
}
