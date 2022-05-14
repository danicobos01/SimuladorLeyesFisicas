package simulator.factories;

import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStateBuilder extends Builder<StateComparator>{

	
	public EpsilonEqualStateBuilder() {
		super("epseq", "epsilon - state comparator");
	}
	
	@Override
	protected StateComparator createTheInstance(JSONObject jo) {
		double eps = jo.has("eps") ? jo.getDouble("eps") : 0.0;
		return new EpsilonEqualStates(eps);
	}
	
	protected JSONObject createData() {
		JSONObject jo = new JSONObject();
		jo.put("eps", "the allowed error eps");
		return jo;
	}

}
