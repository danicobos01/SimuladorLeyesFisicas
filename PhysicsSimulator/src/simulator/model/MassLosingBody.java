package simulator.model;

import simulator.misc.Vector2D;

public class MassLosingBody extends Body {

	double lossFactor;
	double lossFrecuency;
	double cTime;
	
	public MassLosingBody(String id, double m, Vector2D v, Vector2D p, double lossFactor, double lossFrecuency) {
		super(id, m, v, p);
		this.lossFactor = lossFactor;
		this.lossFrecuency = lossFrecuency;
	}
	
	void move(double t)
	{
		super.move(t);
		cTime += t;
		if (cTime >= lossFrecuency){
			cTime = 0;
			m = m * (1 - lossFactor);
		}
	}

}
