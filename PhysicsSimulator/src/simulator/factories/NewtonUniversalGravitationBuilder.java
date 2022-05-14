package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws>{

	public NewtonUniversalGravitationBuilder() {
		super("nlug", "Newton's law of universal gravitation");
	}

	@Override
	protected ForceLaws createTheInstance(JSONObject jo) {
		double g = jo.has("G") ? jo.getDouble("G") : 6.67E-11;
		return new NewtonUniversalGravitation(g);
	}
	
	protected JSONObject createData()
	{
		JSONObject data = new JSONObject();
		data.put("G", "the gravitational constant (a number)");
		return data;
		
	}
}
