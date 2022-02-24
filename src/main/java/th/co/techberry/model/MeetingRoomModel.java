package th.co.techberry.model;

import java.util.Map;

public class MeetingRoomModel {
	
	private int Room_Id;
	private String Room_Name;
	private String description;
    
	public void setRoom_Id(int id) {
		Room_Id = id;
	}
	
	public void setRoom_Name(String name) {
			Room_Name = name;
	}
	
	public void setDescription(String detail) {
		description = detail;
	}

/// Get
	public int getRoomId() {
		return Room_Id;
	}
	
	public String getRoomName() {
		return Room_Name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setRoom_Id((Integer) data.get("Room_Id"));
			setRoom_Name((String) data.get("Room_Name"));
			setDescription((String) data.get("description"));
		}
		else {
			return;
		}
	}
}
