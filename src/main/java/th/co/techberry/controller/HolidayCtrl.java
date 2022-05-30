package th.co.techberry.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;  
import java.text.ParseException;
public class HolidayCtrl {
	
	static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
	
	public Map<String, Object> Holiday() throws SQLException, ClassNotFoundException, ParseException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Holiday;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		try {
			Holiday = dbutil.selectAll(connection,"holiday_list");
			if(Holiday != null) {
				for(Map<String,Object> temp : Holiday) {
					Map<String, Object> ans = new HashMap<>();
					String Holiday_id = String.valueOf(temp.get("ID"));
					String Holiday_Name = (String) temp.get("Holiday_Name");
					Date Start = ((Date) temp.get("begin_date"));
					Date End = ((Date) temp.get("end_date"));
					ans.put("ID",Holiday_id);
					ans.put("Holiday_Name",Holiday_Name);
					ans.put("Start",Start);
					ans.put("End",End);
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
				responseBodyStr.put("Message","success");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Add＿Holiday(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Holiday_detail ;
		Map<String, Object> Log_detail ;
		HolidayModel model = new HolidayModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(data.get("Holiday_Name").equals("") || data.get("begin_date").equals("") || data.get("end_date").equals("")) {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		else {
			String Name = (String) data.get("Holiday_Name");
			String begin_date = (String) data.get("begin_date");
			String end_date = (String) data.get("end_date");
			try {
				int check = dbutil.AddHoliday(connection,Name,begin_date,end_date);
				Holiday_detail = dbutil.select2con(connection,"holiday_list","Holiday_Name","begin_date",Name,begin_date);
				model.setModel(Holiday_detail);
				dbutil.Holiday_Detail_log(connection,model.getId(),Time);
				Log_detail = dbutil.select2con(connection,"holiday_list_detail_log",
						"Holiday_ID","Time",Integer.toString(model.getId()),Time);
				dbutil.Addlog(connection,"holiday_list_log","Holiday_ID",Integer.toString(model.getId()),
						Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				if(check == 1) {
					result.put("status",200);
					result.put("message","Add success");
				}
			}catch (SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put("message","Add fail");
			}
		}
		return result;
	}
	
	public Map<String, Object> Update_Holiday(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Holiday_info ;
		Map<String, Object> Log_detail ;
		HolidayModel model = new HolidayModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
			Holiday_info = (dbutil.select(connection,"holiday_list","ID",(String)data.get("ID")));
			model.setModel(Holiday_info);
            Date date = formatter.parse((String)data.get("begin_date"));
            Date date1 = formatter.parse((String)data.get("end_date"));
            java.sql.Date sqlPackageDate = new java.sql.Date(date.getTime());
            java.sql.Date sqlPackageDate1 = new java.sql.Date(date1.getTime());
    		model.setName((String)data.get("Holiday_Name"));
    		model.setStart(sqlPackageDate);
    		model.setEnd(sqlPackageDate1);
    		dbutil.UpdateHoliday(connection,model);
			Holiday_info = (dbutil.select(connection,"holiday_list","ID",(String)data.get("ID")));
			model.setModel(Holiday_info);
			dbutil.Update_Log_Status(connection,"holiday_list_log","Holiday_ID",Integer.toString(model.getId()));
			dbutil.Holiday_Detail_log(connection,model.getId(),Time);
			Log_detail = dbutil.select2con(connection,"holiday_list_detail_log",
					"Holiday_ID","Time",Integer.toString(model.getId()),Time);
			dbutil.Addlog(connection,"holiday_list_log","Holiday_ID",Integer.toString(model.getId()),
					Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (SQLException e) {
			e.printStackTrace();
			result.put("status",400);
			result.put("message","Update fail");
		}
		result.put("status",200);
		result.put("message","Update success");
		return result;
	}
	
	public Map<String, Object> Delete_Holiday(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Log_detail ;
		ArrayList<String> Target = (ArrayList)data.get("Value");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(!Target.isEmpty()) {
			try{
				for(String temp : Target){
					dbutil.Delete(connection,"holiday_list","ID",temp);
					dbutil.Update_Log_Status(connection,"holiday_list_log","Holiday_ID",temp);
					dbutil.Addlog(connection,"holiday_list_log","Holiday_ID",temp,
							Time, Integer.toString(id),"1","Delete",0);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			result.put("status",200);
			result.put("message","Delete Complete");
		}
		else {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		return result;
	}
}
