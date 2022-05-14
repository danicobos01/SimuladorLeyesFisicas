package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "Moving towards a fixed point");
	}
	@Override
	protected ForceLaws createTheInstance(JSONObject jo) {
		try {
			double g = jo.has("g") ? jo.getDouble("g") : 9.81;
			Vector2D c = new Vector2D();
			if(jo.has("c")) {
				JSONArray pos = jo.getJSONArray("c");
				c = new Vector2D(pos.getDouble(0), pos.getDouble(1));
			}
			return new MovingTowardsFixedPoint(g, c);
		}catch(JSONException e) {
			throw new IllegalArgumentException("MovingTowardsFixedPoint");
		}
	}
	
	protected JSONObject createData() {
		JSONObject j = new JSONObject();
		j.put("c", "the point towards which bodies move (a json list of 2 numbers, e.g., [100.0, 50.0]");
		j.put("g", "the length of the acceleration vector (a number)");
		return j;
	}

}
