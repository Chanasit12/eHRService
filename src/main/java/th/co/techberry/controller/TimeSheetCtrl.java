package th.co.techberry.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;

import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;

public class TimeSheetCtrl {
	
	public Map<String, Object> Show_Time_Sheet(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Time_sheet ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		TimeSheetModel Timesheet_model = new TimeSheetModel();
		String emp_id = Integer.toString(id);
		try {
			Time_sheet = dbutil.selectArray(connection,"timesheet","Emp_id",emp_id);
			if(Time_sheet != null) {
				for(Map<String,Object> temp : Time_sheet) {
					Map<String, Object> ans = new HashMap<>();
					Timesheet_model.setModel(temp);
					ans.put("Sheet_id", Timesheet_model.getSheetId());
					ans.put("Start_at", Timesheet_model.getStart());
					ans.put("End_at", Timesheet_model.getEnd());
					ans.put("Date", Timesheet_model.getDate());
					if(Timesheet_model.getDetail() == null) {
						ans.put("Detail","");
					}
					else {
						ans.put("Detail", Timesheet_model.getDetail());
					}
					if(Timesheet_model.getRemark() == null) {
						ans.put("Remark","");
					}
					else {
						ans.put("Remark", Timesheet_model.getRemark());
					}
					ans.put("Location_id", Timesheet_model.getLocationId());
					ans.put("Charge_code_id", Timesheet_model.getChargeCodeId());
					res.add(ans);
				}
				responseBodyStr.put("status",200);
			}
			else{
				responseBodyStr.put("status",200);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",400);
		}
		responseBodyStr.put("data",res);
		responseBodyStr.put("Message","success");
		return responseBodyStr;
	}
	
	public Map<String, Object> Show_Time_Sheet_By_id(Map<String, Object> data) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Time_sheet ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		TimeSheetModel Timesheet_model = new TimeSheetModel();
		String emp_id = (String) data.get("Emp_id");
		String Date = (String) data.get("Date");
		try {
			Time_sheet = dbutil.select2conArray(connection,"timesheet","Emp_id","Date",emp_id,Date);
			if(Time_sheet != null) {
				for(Map<String,Object> temp : Time_sheet) {
					Map<String, Object> ans = new HashMap<>();
					Timesheet_model.setModel(temp);
					ans.put("Sheet_id", Timesheet_model.getSheetId());
					ans.put("Start_at", Timesheet_model.getStart());
					ans.put("End_at", Timesheet_model.getEnd());
					ans.put("Date", Timesheet_model.getDate());
					if(Timesheet_model.getDetail() == null) {
						ans.put("Detail","");

					}
					else {
						ans.put("Detail", Timesheet_model.getDetail());
					}
					if(Timesheet_model.getRemark() == null) {
						ans.put("Remark","");
					}
					else {
						ans.put("Remark", Timesheet_model.getRemark());
					}
					ans.put("Location_id", Timesheet_model.getLocationId());
					ans.put("Charge_code_id", Timesheet_model.getChargeCodeId());
					res.add(ans);
				}
				responseBodyStr.put("status",200);
			}
			else{
				responseBodyStr.put("status",200);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",400);
		}
		responseBodyStr.put("data",res);
		responseBodyStr.put("Message","success");
		return responseBodyStr;
	}
	
	public Map<String, Object> Show_Time_Sheet_By_Date(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Time_sheet ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		TimeSheetModel Timesheet_model = new TimeSheetModel();
		String emp_id = Integer.toString(id);
		String Date = (String) data.get("Date");
		try {
			Time_sheet = dbutil.select2conArray(connection,"timesheet","Emp_id","Date",emp_id,Date);
			if(Time_sheet != null) {
				for(Map<String,Object> temp : Time_sheet) {
					Map<String, Object> ans = new HashMap<>();
					Timesheet_model.setModel(temp);
					ans.put("Sheet_id", Timesheet_model.getSheetId());
					ans.put("Start_at", Timesheet_model.getStart());
					ans.put("End_at", Timesheet_model.getEnd());
					ans.put("Date", Timesheet_model.getDate());
					if(Timesheet_model.getDetail() == null) {
						ans.put("Detail","");
					}else {
						ans.put("Detail", Timesheet_model.getDetail());
					}
					if(Timesheet_model.getRemark() == null) {
						ans.put("Remark", "");
						
					}else {
						ans.put("Remark", Timesheet_model.getRemark());
					}
					ans.put("Location_id", Timesheet_model.getLocationId());
					ans.put("Charge_code_id", Timesheet_model.getChargeCodeId());
					res.add(ans);
				}
				responseBodyStr.put("status",200);
			}
			else{
				responseBodyStr.put("status",200);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",400);
		}
		responseBodyStr.put("data",res);
		responseBodyStr.put("Message","success");
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_Time_Sheet(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Log_detail ;
		Map<String, Object> TimeSheet ;
		TimeSheetModel Timesheet_model = new TimeSheetModel();
		String emp_id = Integer.toString(id);
		String Date = (String) data.get("Date");
		String Start = (String) data.get("Start_at");
		String End = (String) data.get("End_at");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try {
			Timesheet_model.setEmpId(Integer.valueOf(emp_id));
			if (!data.get("Location_id").equals("")) {
				Timesheet_model.setLocationId(Integer.valueOf((String)data.get("Location_id")));
			}
			if (!data.get("Charge_Code_Id").equals("")) {
				Timesheet_model.setChargeCodeId(Integer.valueOf((String)data.get("Charge_Code_Id")));
			}
			if (!data.get("Detail").equals("")) {
				String Detail = (String) data.get("Detail");
				Detail = Detail.replace("\"","\\\"");
				Detail = Detail.replace("\'","\\\'");
				Detail = Detail.replace("\\","\\\\");
				Timesheet_model.setDetail(Detail);
			}
			if (!data.get("Remark").equals("")) {
				String Remark = (String) data.get("Remark");
				Remark = Remark.replace("\"","\\\"");
				Remark = Remark.replace("\'","\\\'");
				Remark = Remark.replace("\\","\\\\");
				Timesheet_model.setRemark(Remark);
			}
			dbutil.AddTimeSheet(connection,Date,Start,End,Timesheet_model);
			TimeSheet = dbutil.select3con(connection,"timesheet","Emp_id","Date","Start_at",
					emp_id,Date,Start);
			Timesheet_model.setModel(TimeSheet);
			dbutil.TimeSheet_Detail_log(connection,Timesheet_model.getSheetId(),Time);
			Log_detail = dbutil.select2con(connection,"timesheet_detail_log",
					"Sheet_id","Time",Integer.toString(Timesheet_model.getSheetId()),Time);
			dbutil.Addlog(connection,"timesheet_log","Sheet_id",Integer.toString(Timesheet_model.getSheetId()),
					Time,Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message", ConfigConstants.RESPONSE_KEY_SUCCESS);
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",400);
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Updated_Time_Sheet(Map<String, Object> data,int id)
			throws SQLException, ClassNotFoundException, ParseException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Log_detail ;
		Map<String, Object> Sheet_detail ;
		TimeSheetModel Timesheet_model = new TimeSheetModel();
		String Location_id = (String) data.get("Location_id");
		String Charge_code_id = (String) data.get("Charge_Code_Id");
		String Raw_start = (String) data.get("Start_at");
		String Raw_End = (String) data.get("End_at");;
		String Sheet_id = (String) data.get("Sheet_id");
		SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try {
			Sheet_detail = dbutil.select(connection, "timesheet", "Sheet_id", Sheet_id);
			Timesheet_model.setModel(Sheet_detail);
			if (!data.get("Detail").equals("")) {
				String Detail = (String) data.get("Detail");
				Detail = Detail.replace("\"","\\\"");
				Detail = Detail.replace("\'","\\\'");
				Detail = Detail.replace("\\","\\\\");
				Timesheet_model.setDetail(Detail);
			}
			if (!data.get("Remark").equals("")) {
				String Remark = (String) data.get("Remark");
				Remark = Remark.replace("\"","\\\"");
				Remark = Remark.replace("\'","\\\'");
				Remark = Remark.replace("\\","\\\\");
				Timesheet_model.setRemark(Remark);
			}
			if (!Location_id.equals("")) {
				int location = Integer.valueOf(Location_id);
				Timesheet_model.setLocationId(location);
			}
			if (!Charge_code_id.equals("")) {
				int Charge_code = Integer.valueOf(Charge_code_id);
				Timesheet_model.setChargeCodeId(Charge_code);
			}
			if (!Raw_start.equals("")) {
				String[] start = Raw_start.split("[:]");
				String Start = start[0] + ":" + start[1] + ":00";
				Date time = formatter1.parse(Start);
				java.sql.Time timeValue = new java.sql.Time(time.getTime());
				Timesheet_model.setStart(timeValue);
			}
			if (!Raw_End.equals("")) {
				String[] end = Raw_End.split("[:]");
				String End = end[0] + ":" + end[1] + ":00";
				Date time = formatter1.parse(End);
				java.sql.Time timeValue = new java.sql.Time(time.getTime());
				Timesheet_model.setEnd(timeValue);
			}
			dbutil.UpdateTimeSheet(connection, Timesheet_model);
			dbutil.Update_Log_Status(connection,"timesheet_log","Sheet_id",Integer.toString(Timesheet_model.getSheetId()));
			dbutil.TimeSheet_Detail_log(connection,Timesheet_model.getSheetId(),Time);
			Log_detail = dbutil.select2con(connection,"timesheet_detail_log",
					"Sheet_id","Time",Integer.toString(Timesheet_model.getSheetId()),Time);
			dbutil.Addlog(connection,"timesheet_log","Sheet_id",Integer.toString(Timesheet_model.getSheetId()),
					Time,Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",200);

		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Delete_Time_Sheet(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		String Sheet_id = (String) data.get("Sheet_id");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try {
			dbutil.Delete(connection,"timesheet","Sheet_id",Sheet_id);
			dbutil.Update_Log_Status(connection,"timesheet_log","Sheet_id",Sheet_id);
			dbutil.Addlog(connection,"timesheet_log","Sheet_id",Sheet_id,
					Time,Integer.toString(id),"1","Delete",0);
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message","Delete complete");
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",400);
		}
		return responseBodyStr;
	}
	
	
	
	
}
