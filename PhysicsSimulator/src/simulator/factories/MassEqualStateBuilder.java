package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;

public class MassEqualStateBuilder extends Builder<StateComparator> {

	public MassEqualStateBuilder() {
		super("masseq", "mass equal state builder");
	}
	
	@Override
	protected StateComparator createTheInstance(JSONObject jo) {
		return new MassEqualStates();
	}

}
