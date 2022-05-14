package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws{

	
	private double G = 6.67E-11;
	
	public NewtonUniversalGravitation(double g) {
		this.G = g;
	}
	
	public NewtonUniversalGravitation() {
		new NewtonUniversalGravitation(G);
	}
	
	@Override
	public void apply(List<Body> bs) {
		
		for (Body b : bs) {
			
			for (Body b2 : bs) {
				
				if (!b.equals(b2)) {
					b.addForce(force(b, b2));
				}
			}
		}
		
	}
	
	public Vector2D force(Body a, Body b) {
		Vector2D delta = b.getPosition().minus(a.getPosition());
		double dist = delta.magnitude();
		double magnitude = dist>0 ? (G * a.getMass() * b.getMass()) / (dist * dist) : 0.0;
		return delta.direction().scale(magnitude);
	}
	
	public String toString() {
		return "Newton Universal Gravitation with G: " + G;
	}
	

}
