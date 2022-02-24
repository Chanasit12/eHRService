package th.co.techberry.model;

//import java.sql.Timestamp;
import java.sql.*;
import java.util.Map;
public class MeetingModel {
	
	private int Meet_id;
	private String Subject;
	private Timestamp Create_at;
	private Timestamp Cancel_at;
	private int Creator;
	private int Room_Id;
	private Date Date;
	private Time Start_at;
	private Time End_at;
	private boolean Status;
	private String description;
	
	public void setMeetId(int id) {
		Meet_id = id;
	}
	
	public void setSubject(String detail) {
		Subject = detail;
	}
	
	public void setDescription(String detail) {
		description = detail;
	}
	
	public void setCreate(Timestamp time) {
		Create_at = time;
	}

	public void setCancel(Timestamp time) {
		Cancel_at = time;
	}

	public void setRoomId(int id) {
		Room_Id = id;
	}

	public void setDate(Date day) {
			Date = day;
	}
	
	public void setCreator(int id) {
			Creator = id;
	}
	
	public void setStart(Time time) {
		Start_at = time;
	}
	
	public void setEnd(Time time) {
		End_at = time;
	}
	
	public void setStatus(boolean status) {
		Status = status;
	}

/// Get
	public int getMeetId() {
		return Meet_id;
	}
	
	public int getRoomId() {
		return Room_Id;
	}
	
	public String getSubject() {
		return Subject;
	}
	
	public String getDescription() {
		return description;
	}

	public Timestamp getCreate() {
		return Create_at;
	}

	public Timestamp getCancel() {
		return Cancel_at;
	}

	public Time getStart() {
		return Start_at;
	}

	public Time getEnd() {
		return End_at;
	}

	public int getCreator() {
		return Creator;
	}
	
	public Date getDate() {
		return Date;
	}
	
	public boolean getStatus() {
		return Status;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setMeetId((Integer) data.get("Meet_id"));
			setSubject((String) data.get("Subject"));
			setCreate((Timestamp) data.get("Create_at"));
			setCancel((Timestamp) data.get("Cancel_at"));
			setCreator((Integer) data.get("Creator"));
			setRoomId((Integer) data.get("Room_Id"));
			setDate((Date)data.get("Date"));
			setStart((Time)data.get("Start_at"));
			setEnd((Time)data.get("End_at"));
			setStatus((Boolean)data.get("Status"));
			setDescription((String) data.get("description"));
		}
		else {
			return;
		}
	}
}
