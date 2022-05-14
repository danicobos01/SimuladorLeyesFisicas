package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {

	public BasicBodyBuilder() {
		super("basic", "Basic Body");
	}
	
	@Override
	protected Body createTheInstance(JSONObject jo) {
		JSONArray pos = jo.getJSONArray("p");
		Vector2D p = new Vector2D(pos.getDouble(0), pos.getDouble(1));
		JSONArray vel = jo.getJSONArray("v");
		Vector2D v = new Vector2D(vel.getDouble(0), vel.getDouble(1));
		String id = jo.getString("id");
		double m = jo.getDouble("m");
		
		return new Body(id, m, v, p);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("id", "the identifier");
		data.put("m", "the mass");
		data.put("v", "the velocity");
		data.put("p", "the position");
		return data;
	}

}
