package Model;

import java.sql.Date;
import java.util.Map;

public class HolidayModel {
	private int ID;
	private String Holiday_Name;
	private Date begin_date;
	private Date end_date;
    
	public void setid(int id) {
		ID = id;
	}
	
	public void setName(String name) {
		if(name!="") {
			Holiday_Name = name;
		}
	}

	public void setStart(Date time) {
		if(begin_date != time) {
			begin_date = time;
		}
	}

	public void setEnd(Date time) {
		if(end_date != time) {
			end_date = time;
		}
	}

/// Get
	public int getId() {
		return ID;
	}
	
	public String getName() {
		return Holiday_Name;
	}

	public Date getStart() {
		return begin_date;
	}

	public Date getEnd() {
		return end_date;
	}


	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setid((Integer) data.get("ID"));
			setName((String) data.get("Holiday_Name"));
			setStart((Date)data.get("begin_date"));
			setEnd((Date)data.get("end_date"));
		}
		else {
			return;
		}
	}
}
