package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLosingBody;

public class MassLosingBodyBuilder extends Builder<Body> {

	public MassLosingBodyBuilder() {
		super("mlb", "mass losing body builder");
	}
	@Override
	protected Body createTheInstance(JSONObject jo) {
		
		JSONArray pos = jo.getJSONArray("p");
		Vector2D p = new Vector2D(pos.getDouble(0), pos.getDouble(1));
		
		JSONArray vel = jo.getJSONArray("v");
		Vector2D v = new Vector2D(vel.getDouble(0), vel.getDouble(1));
		
		String id = jo.getString("id");
		double m = jo.getDouble("m");
		double freq = jo.getDouble("freq");
		double factor = jo.getDouble("factor");
		
		return new MassLosingBody(id, m, v, p, factor, freq);
	}
	
	protected JSONObject createData() {
		JSONObject j = new JSONObject();
		
		j.put("id", "the identifier");
		j.put("p", "the position");
		j.put("v", "the velocity");
		j.put("m", "the mass");
		
		return j;
	}

}
