package simulator.control;

import org.json.JSONObject;

public class MassEqualStates implements StateComparator {

	public MassEqualStates() {
	
	}
	
	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		
		if (!(s1.getDouble("Time") == s2.getDouble("Time"))) return false;
		if (s1.length() != s2.length()) return false; // No se si el length sería lo correcto (creo que no)
		else {
			for (int i = 0; i < s1.getJSONArray("Bodies").length(); i++)
			{
				if(!(s1.getJSONArray("Bodies").getJSONObject(i).getString("id").equals(s2.getJSONArray("Bodies").getJSONObject(i).getString("id")))) {
					return false;
				}
				if (!(s1.getJSONArray("Bodies").getJSONObject(i).getDouble("m") == s2.getJSONArray("Bodies").getJSONObject(i).getDouble("m"))) {
					return false;
				}
			}
			return true;
		}
	}

}
