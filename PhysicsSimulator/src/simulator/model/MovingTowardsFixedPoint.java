package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {

	private static final double G = 9.81;
	private double g;
	private Vector2D c = new Vector2D(0, 0);
	
	public MovingTowardsFixedPoint(double g, Vector2D c) {
		this.g = g;
		this.c = new Vector2D(c.getX(), c.getY());
	}
	
	public MovingTowardsFixedPoint() {
		this.g = G; 
	}
	
	public void apply(List<Body> bs) {
		
		for (Body b: bs) {
			b.addForce(c.minus(b.getPosition()).direction().scale(g*b.getMass()));
		}
	}
	
	public String toString() {
		return "Moving towards: " + c + " with constant acceleration: " + g;
	}

}
