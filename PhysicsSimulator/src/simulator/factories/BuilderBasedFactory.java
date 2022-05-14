package simulator.factories;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T>{

	List<Builder<T>> builders;
	List<JSONObject> factoryElements;
	
	public BuilderBasedFactory(List<Builder<T>> builders) {
		this.builders = new ArrayList<>(builders);
		factoryElements = new ArrayList<>();
		
		for (Builder<T> b: builders) {
			factoryElements.add(b.getBuilderInfo());
		}
	}

	@Override
	public T createInstance(JSONObject info) {
		if (info == null) throw new IllegalArgumentException("Invalid value for createInstance : NULL");
		else {
			for(Builder<T> b: builders) {
				T obj = b.createInstance(info);
				if (obj != null) {
					return obj; 
				}
			}
			throw new IllegalArgumentException("Invalid value for createInstance : NULL");
		}
	}

	@Override
	public List<JSONObject> getInfo() {
		return factoryElements;
	}
	
	
}
