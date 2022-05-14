package simulator.control;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator{

	double eps;
	
	public EpsilonEqualStates(double eps)
	{
		this.eps = eps;
	}
	
	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		if (!(s1.getDouble("time") == s2.getDouble("time"))) return false;
        else {
            for (int i = 0; i < s1.getJSONArray("bodies").length(); i++) {
                if (!(equalEpsilon(s1.getJSONArray("bodies").getJSONObject(i).getDouble("m"), s2.getJSONArray("bodies").getJSONObject(i).getDouble("m"))))
                	return false;
                
                if(!s1.getJSONArray("bodies").getJSONObject(i).get("id").equals(s1.getJSONArray("bodies").getJSONObject(i).get("id"))) {
                	return false;
                }

                Vector2D p1 = new Vector2D(s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("p").getDouble(0), s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("p").getDouble(1));
                Vector2D p2 = new Vector2D(s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("p").getDouble(0), s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("p").getDouble(1));
                if (!(equalEpsilon(p1, p2))) {
                    return false;
                }
                
                Vector2D v1 = new Vector2D(s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("v").getDouble(0), s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("v").getDouble(1));
                Vector2D v2 = new Vector2D(s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("v").getDouble(0), s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("v").getDouble(1));
                if (!(equalEpsilon(v1, v2))) {
                    return false;
                }
                
                Vector2D f1 = new Vector2D(s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("f").getDouble(0), s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("f").getDouble(1));
                Vector2D f2 = new Vector2D(s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("f").getDouble(0), s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("f").getDouble(1));
                if (!(equalEpsilon(f1, f2))) {
                    return false;
                }
                
            }
        }
        
        return true;
	}
	
	public boolean equalEpsilon(double a, double b) {
		return Math.abs(a - b) <= eps;
	}
	
	public boolean equalEpsilon(Vector2D v1, Vector2D v2) {
		return v1.distanceTo(v2) <= eps;
	}

}

