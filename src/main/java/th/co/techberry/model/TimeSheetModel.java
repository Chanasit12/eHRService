package th.co.techberry.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

public class TimeSheetModel {
	
	private int Emp_id;
	private int Sheet_id;
	private int Location_id;
	private int Charge_code_id;
	private Date Date;
	private Time Start_at;
	private Time End_at;
	private String Detail;
	private String Remark;
	
	public void setEmpId(int id) {
		Emp_id = id;
	}
	
	public void setSheetId(int id) {
		Sheet_id = id;
	}
	
	public void setLocationId(int id) {
		Location_id = id;
	}
	
	public void setChargeCodeId(int id) {
		Charge_code_id = id;
	}
	
	public void setDetail(String detail) {
		Detail = detail;
	}
	
	public void setRemark(String detail) {
		Remark = detail;
	}
	
	public void setDate(Date day) {
			Date = day;
	}
	
	public void setStart(Time time) {
		Start_at = time;
	}
	
	public void setEnd(Time time) {
		End_at = time;
	}

/// Get
	public int getEmpId() {
		return Emp_id;
	}
	
	public int getSheetId() {
		return Sheet_id;
	}
	
	public int getLocationId() {
		return Location_id;
	}
	
	public int getChargeCodeId() {
		return Charge_code_id;
	}
	
	public String getDetail() {
		return Detail;
	}
	
	public String getRemark() {
		return Remark;
	}

	public Time getStart() {
		return Start_at;
	}

	public Time getEnd() {
		return End_at;
	}
	
	public Date getDate() {
		return Date;
	}
	

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setEmpId((Integer) data.get("Emp_id"));
			setDetail((String) data.get("Detail"));
			setRemark((String) data.get("Remark"));
			setSheetId((Integer) data.get("Sheet_id"));
			setLocationId((Integer) data.get("Location_id"));
			setChargeCodeId((Integer) data.get("Charge_code_id"));
			setDate((Date)data.get("Date"));
			setStart((Time)data.get("Start_at"));
			setEnd((Time)data.get("End_at"));
		}
		else {
			return;
		}
	}
}

