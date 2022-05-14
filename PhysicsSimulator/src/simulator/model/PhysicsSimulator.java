package simulator.model;

import java.util.ArrayList;
import java.util.List;


import org.json.*;

public class PhysicsSimulator  {

	double tRealStep;
	double tActual = 0.0;
	ForceLaws fLaws;
	List<Body> bs;
	List<SimulatorObserver> simulatorObservers;
	
	public PhysicsSimulator(double tRealStep, ForceLaws fLaws) throws IllegalArgumentException
	{
		if (tRealStep <= 0) {
			throw new IllegalArgumentException("unvalid tRealStep");
		}
		else this.tRealStep = tRealStep;
		
		if(fLaws == null) {
			throw new IllegalArgumentException("unvalid forceLaws");
		}
		else this.fLaws = fLaws;
		bs = new ArrayList<Body>();
		simulatorObservers = new ArrayList<SimulatorObserver>();
	}
	
	public void advance()
	{
		
		for (Body b: bs) {
			b.resetForce();
		}
		
		fLaws.apply(bs);
		
		for (Body b: bs) {
			b.move(tRealStep);
		}
		tActual += tRealStep;
		
		for (SimulatorObserver o: simulatorObservers) {
			o.onAdvance(bs, tActual);
		}
		
	}
	
	public void addBody (Body b) throws IllegalArgumentException
	{
		if (bs.contains(b.id)){
			throw new IllegalArgumentException("Body already included");
		}else {
			bs.add(b);
		}
		
		for (SimulatorObserver o: simulatorObservers) {
			o.onBodyAdded(bs, b);
		}
	}
	
	public void reset() {
		tActual = 0.0;
		bs.clear();
		
		for(SimulatorObserver o: simulatorObservers) {
			o.onReset(bs, tActual, tRealStep, fLaws.toString());
		}
	}
	
	public void setDeltaTime(double dt) throws IllegalArgumentException {
		if (dt < 0.0) {
			throw new IllegalArgumentException("Invalid delta time");
		}else {
			tRealStep = dt;
		}
		
		for (SimulatorObserver o: simulatorObservers) {
			o.onDeltaTimeChanged(tRealStep);
		}
	}
	
	public void setForceLawsLaws(ForceLaws forceLaws) throws IllegalArgumentException {
		if (forceLaws == null) {
			throw new IllegalArgumentException("forceLaws cannot be null");
		}
		else {
			fLaws = forceLaws;
		}
		
		for (SimulatorObserver o: simulatorObservers) {
			o.onForceLawsChanged(fLaws.toString());
		}
	}
	
	public void addObserver(SimulatorObserver o) {
		if(!simulatorObservers.contains(o)) {
			simulatorObservers.add(o);
			o.onRegister(bs, tActual, tRealStep, fLaws.toString());
		}
	}
	
	public JSONObject getState() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		
		for(Body b: bs) {
			ja.put(b.getState());
		}
		
		jo.put("bodies", ja);
		jo.put("time", tActual);
		
		return jo;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	public List<Body> getBodies(){
		return bs;
	}
	
}
