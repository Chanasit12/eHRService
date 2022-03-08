package th.co.techberry.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;
public class LeaveModel {

    private int Req_id;
    private int Type_ID;
    private int Emp_id;
	private int Depend;
    private Timestamp Begin;
	private Timestamp End;
    private String Detail;
    private boolean Emergency;
	private String Status;
	private String Comment;

	public void setReqId(int id) {
		Req_id = id;
	}

	public void setTypeId(int id) {
		Type_ID = id;
	}

	public void setEmpId(int id) {
		Emp_id = id;
	}

	public void setDepend(int id) {
		Depend = id;
	}

	public void setBegin(Timestamp time) {
		Begin = time;
	}

	public void setEnd(Timestamp time) {
		End = time;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public void setEmergency(boolean status) {
		Emergency = status;
	}

	/// Get

	public int getDepend() {
		return Depend;
	}

	public int getReqId() {
		return Req_id;
	}

	public int getTypeId() {
		return Type_ID;
	}

	public int getEmpId() {
		return Emp_id;
	}

	public Timestamp getBegin() {
		return Begin;
	}

	public Timestamp getEnd() {
		return End;
	}

	public String getDetail() {
		return Detail;
	}

	public String getStatus() {
		return Status;
	}

	public String getComment() {
		return Comment;
	}

	public Boolean getEmergency() {
		return Emergency;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setReqId((Integer) data.get("Req_id"));
			setTypeId((Integer) data.get("Type_ID"));
			setEmpId((Integer) data.get("Emp_id"));
			setTypeId((Integer) data.get("Depend"));
			setBegin((Timestamp) data.get("Begin"));
			setEnd((Timestamp) data.get("End"));
			setDetail((String) data.get("Detail"));
			setStatus((String) data.get("Status"));
			setComment((String) data.get("Comment"));
			setEmergency((boolean) data.get("Emergency"));

		}
		else {
			return;
		}
	}
	public void setModelByReq(Map<String, Object> data) {
		if(data != null) {
			int Depend,Type_ID,Emp_id;
			Timestamp Begin,End;
			String Detail;
			String[] Raw_Begin,Raw_End;
			Boolean Emergency;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			if(!data.get("Type_ID").equals("")){
				Type_ID = Integer.parseInt((String) data.get("Type_ID"));
				setTypeId(Type_ID);
			}
			if(!data.get("Emp_id").equals("")){
				Emp_id = Integer.parseInt((String) data.get("Emp_id"));
				setEmpId(Emp_id);
			}
			if(!data.get("Depend").equals("")){
				Depend = Integer.parseInt((String) data.get("Depend"));
				setDepend(Depend);
			}
			if(!data.get("Begin").equals("")){
				String[] temp = ((String) data.get("Begin")).split(" ");
				Raw_Begin = temp[1].split(":");
				Begin = Timestamp.valueOf(temp[0]+" "+Raw_Begin[0]+":"+Raw_Begin[1]+":00");
				setBegin(Begin);
			}
			if(!data.get("End").equals("")){
				String[] temp = ((String) data.get("End")).split(" ");
				Raw_End = temp[1].split(":");
				End = Timestamp.valueOf(temp[0]+" "+Raw_End[0]+":"+Raw_End[1]+":00");
				setEnd(End);
			}
			if(!data.get("Detail").equals("")){
				Detail = (String) data.get("Detail");
				setDetail(Detail);
			}
			if(!data.get("Emergency").equals("")){
				Emergency = Boolean.parseBoolean((String) data.get("Emergency"));
				setEmergency(Emergency);
			}
		}
		else {
			return;
		}
	}
}
