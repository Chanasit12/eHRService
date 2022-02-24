package th.co.techberry.model;

import java.util.Map;

public class LocationModel {
	
	private int Location_id;
	private String Location_Name;
	private String description;
    
	public void setLocationId(int id) {
		Location_id = id;
	}
	
	public void setLocationName(String name) {
		Location_Name = name;
	}
	
	public void setDescription(String data) {
		description = data;
	}

/// Get
	public int getLocationId() {
		return Location_id;
	}
	
	public String getLocationName() {
		return Location_Name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setLocationId((Integer) data.get("Location_id"));
			setLocationName((String) data.get("Location_name"));
			setDescription((String) data.get("description"));
		}
		else {
			return;
		}
	}
	
}
