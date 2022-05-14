package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class Body {
	
	protected String id;
	protected Double m;
	protected Vector2D v;
	protected Vector2D f;
	protected Vector2D p;
	
	public Body(String id, double m, Vector2D v, Vector2D p)
	{
		this.id = id;
		this.m = m;
		this.v = v;
		this.p = p;
		this.f = new Vector2D();
	}
	
	public String getId()
	{
		return id;
	}
	
	public Vector2D getForce()
	{
		return new Vector2D(f.getX(), f.getY());
	}
	
	public Vector2D getVelocity()
	{
		return new Vector2D(v.getX(), v.getY());
	}
	
	public Vector2D getPosition()
	{
		return new Vector2D(p.getX(), p.getY());
	}
	
	public double getMass()
	{
		return m;
	}
	
	void addForce(Vector2D f)
	{
		this.f = this.f.plus(f);
	}
	
	void resetForce()
	{
		f = new Vector2D();
	}
	
	void move (double t)
	{
		Vector2D acc;		
		acc = f.scale(1.0 / m);
		p = p.plus(v.scale(t).plus(acc.scale(0.5 * t * t)));
		v = v.plus(acc.scale(t));
	}
	
	public boolean equals (Object obj) {
		return this.id == ((Body) obj).getId(); 
	}
	
	public JSONObject getState()
	{
		JSONObject jo = new JSONObject();
		jo.put("id" , getId());
		jo.put("m", getMass());
		jo.put("p", getPosition().asJSONArray());
		jo.put("v", getVelocity().asJSONArray());
		jo.put("f", getForce().asJSONArray());		
		return jo;
	}
	
	public String toString()
	{
		return getState().toString();
	}
}
