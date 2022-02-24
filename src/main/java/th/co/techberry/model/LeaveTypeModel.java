package th.co.techberry.model;

import java.util.Map;

public class LeaveTypeModel {
	
	private int Type_ID;
	private String Type_name;
	private int Num_per_year;
	private int Num_can_add;
	
	public void setType_id(int id) {
		Type_ID = id;
	}
	
	public void setNum_per_year(int day) {
		Num_per_year = day;
	}
	
	public void setName(String name) {
		if(name!="") {
			Type_name = name;
		}
	}

	public void setNum_can_add(int day) {
		Num_can_add = day;
	}

/// Get
	public int getId() {
		return Type_ID;
	}
	
	public String getName() {
		return Type_name;
	}

	public int getNum_per_year() {
		return Num_per_year;
	}

	public int getNum_can_add() {
		return Num_can_add;
	}


	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setType_id((Integer) data.get("Type_ID"));
			setNum_can_add((Integer) data.get("Num_can_add"));
			setNum_per_year((Integer) data.get("Num_per_year"));
			setName((String) data.get("Type_name"));
		}
		else {
			return;
		}
	}
}
