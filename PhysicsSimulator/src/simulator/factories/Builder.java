package simulator.factories;


import org.json.JSONObject;

public abstract class Builder<T> {

	String typeTag;
	String descr;
	
	public Builder(String typeTag, String descr){
		this.typeTag = typeTag;
		this.descr = descr;
	}
	
	public T createInstance(JSONObject info) {
		T type = null;
		if (typeTag != null && typeTag.equals(info.getString("type"))) {
			type = createTheInstance(info.getJSONObject("data"));
		}
		return type;
	}
	
	protected abstract T createTheInstance(JSONObject jo);
	
	
	public JSONObject getBuilderInfo() {
		JSONObject jo = new JSONObject();
		jo.put("type", typeTag);
		jo.put("data", createData());
		jo.put("desc", descr);		
		return jo;
	}
	
	protected JSONObject createData() {
		return new JSONObject();
	}
	
}
