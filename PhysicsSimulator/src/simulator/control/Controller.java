package simulator.control;

import java.io.*;
import java.util.List;

import org.json.*;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	
	PhysicsSimulator simulator;
	Factory<Body> bodiesFactory;
	Factory<ForceLaws> fLawsFactory;

	public Controller(PhysicsSimulator simulator, Factory<Body> bodiesFactory, Factory<ForceLaws> fLawsFactory) {
		this.simulator = simulator;
		this.bodiesFactory = bodiesFactory;
		this.fLawsFactory = fLawsFactory;
	}
	
	public void loadBodies(InputStream in){
		JSONObject jsonImput = new JSONObject(new JSONTokener(in));
		JSONArray bodies = jsonImput.getJSONArray("bodies"); 
		
		for(int i = 0; i < bodies.length(); i++) {
			simulator.addBody(bodiesFactory.createInstance(bodies.getJSONObject(i)));
		}
	}
	
	public void run(double steps, OutputStream out, InputStream expOut, StateComparator cmp) {
		OutputStream outputStream = new OutputStream() {
			public void write(int b) throws IOException{};
		};
		
		JSONObject expOutJO = null;
		
		if(expOut != null) {
			expOutJO = new JSONObject(new JSONTokener(expOut));
		}
		
		if (out == null) {
			out = new OutputStream() {
				@Override
				public void write(int b) throws IOException {}
			};
		}
		
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("\"states\": [");
		
		JSONObject currState = null;
		JSONObject expState = null;
		
		currState = simulator.getState();
		p.println(currState);
		if (expOutJO != null) {
			expState = expOutJO.getJSONArray("states").getJSONObject(0);
			if(!cmp.equal(expState, currState)) throw new NotEqualStatesException(expState, currState, 0); // OJO a esto !!
		}
		
		for(int i = 1; i <= steps; i++) {
			simulator.advance();
			currState = simulator.getState();
			
			if (expOutJO != null) {
				expState = expOutJO.getJSONArray("states").getJSONObject(i);
				if (!cmp.equal(expState, currState)) throw new NotEqualStatesException(expState, currState, i);
			}
			
			p.print(",");
			p.println(currState);
		}
		
		p.println("]");
		p.println("}");
	}
	
	public List<JSONObject> getForceLawsInfo(){
		return fLawsFactory.getInfo();
	}
	
	public List<Body> getBodies(){
		return simulator.getBodies();
	}
	
	public void setForceLaws(JSONObject info) {
		ForceLaws fl = fLawsFactory.createInstance(info);
		simulator.setForceLawsLaws(fl);
	}
	
	
	public void reset() {
		simulator.reset();
	}
	
	public void setDeltaTime(double dt) {
		simulator.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		simulator.addObserver(o);
	}
}

