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
		List<Map<String, Object>> Check_data = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		String Emp_id = Integer.toString(id);
		try {
			Check_data = dbutil.selectArray(connection,"check_in/check_out","Emp_id",Emp_id);
			if(Check_data != null) {
				for(Map<String,Object> temp : Check_data) {
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
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		WorkingTimeModel working_time_model = new WorkingTimeModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		LocalDateTime now = LocalDateTime.now();
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
        	working_time_model.setModel(working_time_data);
        	try {
        		java.sql.Time timeValue = new java.sql.Time(timeFormat.parse(current_time).getTime());
				boolean start_before = timeValue.after(working_time_model.getStart());
				String str_time = current_day+" "+timeValue;
				System.out.println("str_time " + str_time);
				Timestamp timestamp = Timestamp.valueOf(str_time);
				System.out.println("start_before " + start_before);
				System.out.println("timestamp " + timestamp);
				if(start_before) {
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
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		WorkingTimeModel working_time_model = new WorkingTimeModel();
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
        	Last_Checkin = dbutil.selectLastCheckIn(connection, current_day,Emp_id);
        	working_time_model.setModel(working_time_data);
        	model.setModel(Last_Checkin);
        	try {
        		java.sql.Time timeValue = new java.sql.Time(timeFormat.parse(current_time).getTime());
				boolean start_before = timeValue.after(working_time_model.getEnd());
				String str_time = current_day+" "+timeValue;
				System.out.println("str_time " + str_time);
				Timestamp timestamp = Timestamp.valueOf(str_time);
				System.out.println("start_before " + start_before);
				System.out.println("timestamp " + timestamp);
				if(start_before) {
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
}
