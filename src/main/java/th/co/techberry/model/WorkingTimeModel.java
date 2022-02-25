package th.co.techberry.model;

import java.sql.Time;
import java.util.Map;

public class WorkingTimeModel {
	private String WorkingTime_Name;
	private Time start_work;
	private Time off_work;
	private Time over_time;
    
	public void setName(String name) {
		if(name!="") {
			WorkingTime_Name = name;
		}
	}

	public void setStart(Time time) {
			start_work = time;
	}

	public void setEnd(Time time) {
			off_work = time;
	}
	
	public void setOvertime(Time time) {
		over_time = time;
}

/// Get
	
	public String getName() {
		return WorkingTime_Name;
	}

	public Time getStart() {
		return start_work;
	}

	public Time getEnd() {
		return off_work;
	}

	public Time getOvertime() {
		return over_time;
	}
	
	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setName((String) data.get("Day_Name"));
			setStart((Time)data.get("start_work"));
			setEnd((Time)data.get("off_work"));
			setOvertime((Time)data.get("OverTime"));
		}
		else {
			return;
		}
	}
}
