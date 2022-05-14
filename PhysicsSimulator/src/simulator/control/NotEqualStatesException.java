package simulator.control;

import org.json.JSONObject;

public class NotEqualStatesException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private JSONObject actual;
	private JSONObject expected;

	
	NotEqualStatesException(JSONObject exp, JSONObject act, int step){
		super("States are different at step: " + step + System.lineSeparator() +
				"Actual: " + act + System.lineSeparator() + 
				"Expected: " + exp + System.lineSeparator()
				);
		actual = act;
		expected = exp;
	}
	
	public JSONObject getActual() {
		return actual;
	}
	
	public JSONObject getExpected() {
		return expected;
	}
}
