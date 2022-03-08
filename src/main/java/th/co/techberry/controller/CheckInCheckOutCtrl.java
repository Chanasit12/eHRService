package th.co.techberry.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;
import java.time.*;
import java.time.format.DateTimeFormatter;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;

public class CheckInCheckOutCtrl {

	public Map<String, Object> CheckInCheckOut_Data(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		String Emp_id = Integer.toString(id);
		try {
			data = dbutil.selectArray(connection,"checkin_checkout","Emp_id",Emp_id);
			if(data != null) {
				for(Map<String,Object> temp : data) {
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("Check_id", model.getCheckId());
					ans.put("Checkin_at", model.getCheckin());
					ans.put("Checkout_at", model.getCheckout());
					ans.put("Emp_id", model.getEmpId());
					ans.put("Status_CheckIn", model.getStatusCheckIn());
					ans.put("Status_CheckOut", model.getStatusCheckOut());
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
				responseBodyStr.put("Message","success");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",400);
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> CheckIn(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> working_time_data = new HashMap<String, Object>();
		Map<String, Object> Last_Checkin = new HashMap<String, Object>();
		Map<String, Object> Leave_req = new HashMap<String, Object>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		WorkingTimeModel working_time_model = new WorkingTimeModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Emp_id = Integer.toString(id);
		String[] current_date_time = dtf.format(now).split(" ");
		String[] current_date_raw = current_date_time[0].split("/");
		String current_time = current_date_time[1]+":00";
		String current_day = current_date_raw[0]+"-"+current_date_raw[1]+"-"+current_date_raw[2];

        LocalDate localDate = LocalDate.of(Integer.parseInt(current_date_raw[0]),
        		Integer.parseInt(current_date_raw[1]),Integer.parseInt(current_date_raw[2]));
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        String DayName = dayOfWeek.toString();
        try {
        	working_time_data = dbutil.select(connection,"working_time","Day_Name",DayName);
        	Last_Checkin = dbutil.selectLastCheckIn(connection,current_day,Emp_id);
        	working_time_model.setModel(working_time_data);
        	try {
        		java.sql.Time Start_time = new java.sql.Time(timeFormat.parse(current_time).getTime());
				boolean start_status = working_time_model.getStart().after(Start_time) 
						|| working_time_model.getStart().equals(Start_time);
				String str_time = current_day+" "+Start_time;
				System.out.println("str_time " + str_time);
				Timestamp timestamp = Timestamp.valueOf(str_time);
				System.out.println("start_before " + start_status);
				System.out.println("timestamp " + timestamp);
				if(start_status && Last_Checkin == null) {
					model.setStatusCheckIn("Intime");
					model.setEmpId(id);
					model.setCheckin(timestamp);
					System.out.println("model.gettime " + model.getCheckin());
					try {
						dbutil.AddCheckIn(connection,model);
						responseBodyStr.put("status",200);
						responseBodyStr.put("Message","Check in complete");
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
				else if(Last_Checkin != null) {
					responseBodyStr.put("status",400);
					responseBodyStr.put("Message","You are already Check in");
				}
				else {
					model.setStatusCheckIn("Late");
					model.setEmpId(id);
					model.setCheckin(timestamp);
					System.out.println("model.gettime " + model.getCheckin());
					try {
						dbutil.AddCheckIn(connection,model);
						responseBodyStr.put("status",200);
						responseBodyStr.put("Message","Check in complete");
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch(SQLException e) {
        	e.printStackTrace();
        }
		return responseBodyStr;
	}
	
	public Map<String, Object> CheckOut(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> working_time_data = new HashMap<String, Object>();
		Map<String, Object> Last_Checkin = new HashMap<String, Object>();
		Map<String, Object> Day_off = new HashMap<String, Object>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		WorkingTimeModel working_time_model = new WorkingTimeModel();
		DayOffModel dayoff_model = new DayOffModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Emp_id = Integer.toString(id);
		String[] current_date_time = dtf.format(now).split(" ");
		String[] current_date_raw = current_date_time[0].split("-");
		String current_time = current_date_time[1]+":00";
		String current_day = current_date_raw[0]+"-"+current_date_raw[1]+"-"+current_date_raw[2];
        LocalDate localDate = LocalDate.of(Integer.parseInt(current_date_raw[0]),
        		Integer.parseInt(current_date_raw[1]),Integer.parseInt(current_date_raw[2]));
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        String DayName = dayOfWeek.toString();
        try {
        	working_time_data = dbutil.select(connection,"working_time","Day_Name",DayName);
        	Last_Checkin = dbutil.selectLastCheckIn(connection,current_day,Emp_id);
        	working_time_model.setModel(working_time_data);
        	model.setModel(Last_Checkin);
        	try {
        		java.sql.Time End_time = new java.sql.Time(timeFormat.parse(current_time).getTime());
				boolean End_after = End_time.after(working_time_model.getEnd())
						|| End_time.equals(working_time_model.getEnd());
				String str_time = current_day+" "+End_time;
				Timestamp timestamp = Timestamp.valueOf(str_time);
				if(End_after && model.getCheckout() == null) {
					java.sql.Time ot_time = new java.sql.Time(timeFormat.parse(working_time_model.getOvertime().toString()).getTime());
					boolean ot_status = End_time.after(ot_time) || End_time.equals(ot_time);
					System.out.println("ot_status " + ot_status);
					if(ot_status) {
						model.setStatusCheckOut("Over Time");
						model.setEmpId(id);
						model.setCheckout(timestamp);
						System.out.println("working_time_model.getEnd().getHours() " + working_time_model.getEnd().getHours());
						System.out.println("End_time.getHours() " + End_time.getHours());
						int ot_hour = End_time.getHours() - working_time_model.getEnd().getHours();
						System.out.println("result " + ot_hour);
						try {
							dbutil.AddCheckOut(connection,model);
							Day_off = dbutil.select(connection,"day_off_list","Emp_id",Emp_id);
							if(Day_off != null) {
								dayoff_model.setModel(Day_off);
								int day = dayoff_model.getHour() + ot_hour;
								dayoff_model.setHour(day);
								dbutil.UpdateDayOff(connection,dayoff_model);
							}
							else {
								dbutil.AddDayOff(connection, Emp_id,"Over Time",Integer.toString(ot_hour));
							}
						} catch(SQLException e) {
							e.printStackTrace();
						}
						responseBodyStr.put("status",200);
						responseBodyStr.put("Message","Check Out Complete");
					}
					else {
						model.setStatusCheckOut("Intime");
						model.setEmpId(id);
						model.setCheckout(timestamp);
						try {
							dbutil.AddCheckOut(connection,model);
						} catch(SQLException e) {
							e.printStackTrace();
						}
						responseBodyStr.put("status",200);
						responseBodyStr.put("Message","Check Out Complete");
					}
//					System.out.println("model.gettime " + model.getCheckin());
//					try {
//						dbutil.AddCheckIn(connection,model);
//						responseBodyStr.put("status",200);
//						responseBodyStr.put("Message","Check in complete");
//					} catch(SQLException e) {
//						e.printStackTrace();
//					}
				}
				else if(model.getCheckout() != null) {
					responseBodyStr.put("status",400);
					responseBodyStr.put("Message","You have already Check Out");
				}
				else {
//					try {
//						dbutil.AddCheckIn(connection,model);
						responseBodyStr.put("status",400);
						responseBodyStr.put("Message","Cannot Check out");
//					} catch(SQLException e) {
//						e.printStackTrace();
//					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch(SQLException e) {
        	e.printStackTrace();
        }
		return responseBodyStr;
	}

	public Map<String, Object> Get_CheckInCheckOut_By_Emp_Id(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> Check_data = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		String Emp_id = (String) data.get("Emp_id");
		String Duration = (String) data.get("Type");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm");
		LocalDateTime now = LocalDateTime.now();
		String[] current_date_raw = ((String) data.get("Date")).split("-");
		String current_time = dtf.format(now).split(" ")[1];
		System.out.println("Time now " + current_time);
//		String current_time = current_date_time[1]+":00";

		if(Duration.equals("Day")){
			try{
				Check_data = dbutil.selectCheckin(connection,"%-"+current_date_raw[2]+"%",Emp_id);
				for(Map<String, Object> temp : Check_data){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("CheckId",model.getCheckId());
					ans.put("Check_in",model.getCheckin());
					ans.put("Check_out",model.getCheckout());
					ans.put("Emp_id",model.getEmpId());
					ans.put("Check_in_status",model.getStatusCheckIn());
					ans.put("Check_out_status",model.getStatusCheckOut());
					res.add(ans);
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		else if(Duration.equals("Month")){
			try{
				Check_data = dbutil.selectCheckin(connection,"%-"+current_date_raw[1]+"%",Emp_id);
				for(Map<String, Object> temp : Check_data){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("CheckId",model.getCheckId());
					ans.put("Check_in",model.getCheckin());
					ans.put("Check_out",model.getCheckout());
					ans.put("Emp_id",model.getEmpId());
					ans.put("Check_in_status",model.getStatusCheckIn());
					ans.put("Check_out_status",model.getStatusCheckOut());
					res.add(ans);
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		else if(Duration.equals("Year")){
			try{
				Check_data = dbutil.selectCheckin(connection,current_date_raw[0]+"%",Emp_id);
				for(Map<String, Object> temp : Check_data){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("CheckId",model.getCheckId());
					ans.put("Check_in",model.getCheckin());
					ans.put("Check_out",model.getCheckout());
					ans.put("Emp_id",model.getEmpId());
					ans.put("Check_in_status",model.getStatusCheckIn());
					ans.put("Check_out_status",model.getStatusCheckOut());
					res.add(ans);
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		return responseBodyStr;
	}

	public Map<String, Object> Get_ALL_CheckInCheckOut(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> Check_data = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		String Emp_id = (String) data.get("Emp_id");
		String Duration = (String) data.get("Type");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm");
		LocalDateTime now = LocalDateTime.now();
		String[] current_date_raw = ((String) data.get("Date")).split("-");
		String current_time = dtf.format(now).split(" ")[1];
		System.out.println("Time now " + current_time);
//		String current_time = current_date_time[1]+":00";

		if(Duration.equals("Day")){
			try{
				Check_data = dbutil.selectCheckin(connection,"%-"+current_date_raw[2]+"%",Emp_id);
				for(Map<String, Object> temp : Check_data){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("CheckId",model.getCheckId());
					ans.put("Check_in",model.getCheckin());
					ans.put("Check_out",model.getCheckout());
					ans.put("Emp_id",model.getEmpId());
					ans.put("Check_in_status",model.getStatusCheckIn());
					ans.put("Check_out_status",model.getStatusCheckOut());
					res.add(ans);
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		else if(Duration.equals("Month")){
			try{
				Check_data = dbutil.selectCheckin(connection,"%-"+current_date_raw[1]+"%",Emp_id);
				for(Map<String, Object> temp : Check_data){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("CheckId",model.getCheckId());
					ans.put("Check_in",model.getCheckin());
					ans.put("Check_out",model.getCheckout());
					ans.put("Emp_id",model.getEmpId());
					ans.put("Check_in_status",model.getStatusCheckIn());
					ans.put("Check_out_status",model.getStatusCheckOut());
					res.add(ans);
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		else if(Duration.equals("Year")){
			try{
				Check_data = dbutil.selectCheckin(connection,current_date_raw[0]+"%",Emp_id);
				for(Map<String, Object> temp : Check_data){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("CheckId",model.getCheckId());
					ans.put("Check_in",model.getCheckin());
					ans.put("Check_out",model.getCheckout());
					ans.put("Emp_id",model.getEmpId());
					ans.put("Check_in_status",model.getStatusCheckIn());
					ans.put("Check_out_status",model.getStatusCheckOut());
					res.add(ans);
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		return responseBodyStr;
	}


}
