package th.co.techberry.model;

import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

public class CheckInCheckOutModel {
	private int Check_Id;
	private Date Checkin_at;
	private Date Checkout_at;
	private String CheckIn_Str;
	private String Checkout_Str;
	private int Emp_id;
	private String Status_CheckIn;
	private String Status_CheckOut;
	private String Detail;
	
	public void setCheckId(int id) {
		Check_Id = id;
	}
	
	public void setEmpId(int id) {
		Emp_id = id;
	}
	
	public void setCheckin(Date time) {
		Checkin_at = time;
	}
	
	public void setCheckout(Date time) {
		Checkout_at = time;
	}

	public void setCheckInStr(String detail) {
		CheckIn_Str = detail;
	}

	public void setCheckoutStr(String detail) {
		Checkout_Str = detail;
	}

	public void setStatusCheckIn(String status) {
		Status_CheckIn = status;
	}
	
	public void setStatusCheckOut(String status) {
		Status_CheckOut = status;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

/// Get
	public int getCheckId() {
		return Check_Id;
	}
		
	public int getEmpId() {
		return Emp_id;
	}
	
	public Date getCheckin() {
		return Checkin_at;
	}

	public Date getCheckout() {
		return Checkout_at;
	}

	public String getCheckInStr() {
		return CheckIn_Str;
	}

	public String getCheckoutStr() {
		return Checkout_Str;
	}

	public String getStatusCheckIn() {
		return Status_CheckIn;
	}
	
	public String getStatusCheckOut() {
		return Status_CheckOut;
	}

	public String getDetail() {
		return Detail;
	}
	
	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setCheckId((Integer) data.get("Check_id"));
			setEmpId((Integer) data.get("Emp_id"));
			setCheckin((Date) data.get("Checkin_at"));
			setCheckout((Date) data.get("Checkout_at"));
			setStatusCheckIn((String) data.get("Status_CheckIn"));
			setStatusCheckOut((String) data.get("Status_CheckOut"));
			setDetail((String) data.get("Detail"));
			setCheckInStr(getCheckin().toString());
			setCheckoutStr(getCheckout().toString());
		}
		else {
			return;
		}
	}
}
