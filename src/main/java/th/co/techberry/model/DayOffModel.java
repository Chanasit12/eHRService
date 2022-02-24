package th.co.techberry.model;
import java.util.Map;
public class DayOffModel {
	private int DayOff_id;
	private String Detail;
	private int hour;
	private int Emp_id;
    
	public void setDayOff_id(int id) {
		DayOff_id = id;
	}
	
	public void setHour(int time) {
		hour = time;
	}
	
	public void setDetail(String detail) {
		if(detail!="") {
			Detail = detail;
		}
	}

	public void setEmp_id(int id) {
		Emp_id = id;
	}

/// Get
	public int getDayOffId() {
		return DayOff_id;
	}
	
	public String getDetail() {
		return Detail;
	}

	public int getEmpId() {
		return Emp_id;
	}

	public int getHour() {
		return hour;
	}


	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setDayOff_id((Integer) data.get("Day_off_id"));
			setHour((Integer) data.get("Hours"));
			setEmp_id((Integer) data.get("Emp_id"));
			setDetail((String) data.get("Detail"));
		}
		else {
			return;
		}
	}
}
