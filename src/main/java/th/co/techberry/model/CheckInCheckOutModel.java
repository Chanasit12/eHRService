package th.co.techberry.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

public class CheckInCheckOutModel {
	private int Check_Id;
	private Timestamp Checkin_at;
	private Timestamp Checkout_at;
	private int Emp_id;
	private String Status_CheckIn;
	private String Status_CheckOut;
	
	public void setCheckId(int id) {
		Check_Id = id;
	}
	
	public void setEmpId(int id) {
		Emp_id = id;
	}
	
	public void setCheckin(Timestamp time) {
		Checkin_at = time;
	}
	
	public void setCheckout(Timestamp time) {
		Checkout_at = time;
	}

	
	public void setStatusCheckIn(String status) {
		Status_CheckIn = status;
	}
	
	public void setStatusCheckOut(String status) {
		Status_CheckOut = status;
	}
	
/// Get
	public int getCheckId() {
		return Check_Id;
	}
		
	public int getEmpId() {
		return Emp_id;
	}
	
	public Timestamp getCheckin() {
		return Checkin_at;
	}

	public Timestamp getCheckout() {
		return Checkout_at;
	}
		
	public String getStatusCheckIn() {
		return Status_CheckIn;
	}
	
	public String getStatusCheckOut() {
		return Status_CheckOut;
	}
	
	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setCheckId((Integer) data.get("Check_id"));
			setEmpId((Integer) data.get("Emp_id"));
			setCheckin((Timestamp) data.get("Checkin_at"));
			setCheckout((Timestamp) data.get("Checkout_at"));
			setStatusCheckIn((String) data.get("Status_CheckIn"));
			setStatusCheckOut((String) data.get("Status_CheckOut"));
		}
		else {
			return;
		}
	}
}
