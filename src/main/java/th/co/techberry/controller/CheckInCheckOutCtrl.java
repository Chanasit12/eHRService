package th.co.techberry.controller;

//import java.sql.Date;
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
import java.util.*;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;

public class CheckInCheckOutCtrl {

	public Map<String, Object> CheckInCheckOut_Data(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> res = new ArrayList<>();
		List<Map<String, Object>> data ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		String Emp_id = Integer.toString(id);
		try {
			data = dbutil.selectArray(connection,"checkin_checkout","Emp_id",Emp_id);
			if(data != null) {
				for(Map<String,Object> temp : data) {
					Map<String, Object> ans = new HashMap<>();
					model.setModel(temp);
					ans.put("Check_id", model.getCheckId());
					ans.put("Checkin_at", model.getCheckin());
					ans.put("Checkout_at", model.getCheckout());
					ans.put("Emp_id", model.getEmpId());
					ans.put("Status_CheckIn", model.getStatusCheckIn());
					ans.put("Status_CheckOut", model.getStatusCheckOut());
					res.add(ans);
				}
				responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		responseBodyStr.put("data",res);
		return responseBodyStr;
	}

	public int findDifferenceHour(String start_date, String end_date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		int hour = 0;
		try {
			Date d1 = sdf.parse(start_date);
			Date d2 = sdf.parse(end_date);
			long difference_In_Time = d2.getTime() - d1.getTime();
			Long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
			hour = difference_In_Hours.intValue();
			System.out.println("Long Hour "+difference_In_Hours);
			System.out.println("Int Hour "+hour);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return hour;
	}

	public Map<String, Object> CheckIn(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> working_time_data ;
		Map<String, Object> Checkin ;
		Map<String, Object> Leave_req ;
		Map<String, Object>  Log_detail ;
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		LeaveModel leave_model = new LeaveModel();
		WorkingTimeModel working_time_model = new WorkingTimeModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DateFormat inFormat2 = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Emp_id = Integer.toString(id);
		String[] current_date_time = dtf.format(now).split(" ");
		String[] current_date_raw = current_date_time[0].split("-");
		String current_time = current_date_time[1]+":00";
		String current_day = current_date_time[0];
        LocalDate localDate = LocalDate.of(Integer.parseInt(current_date_raw[0]),
        		Integer.parseInt(current_date_raw[1]),Integer.parseInt(current_date_raw[2]));
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        String DayName = dayOfWeek.toString();
		DateTimeFormatter Log_dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String Log_Time = Log_dtf.format(now);
        try {
        	working_time_data = dbutil.select(connection,"working_time","Day_Name",DayName);
        	Checkin = dbutil.selectLastCheckIn(connection,current_day,Emp_id);
			working_time_model.setModel(working_time_data);
			Leave_req = dbutil.Select_Leave_req(connection,Emp_id,current_date_time[0]);
//			java.util.Date Start_time = inFormat2.parse(current_day+" "+current_time);
			SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date Start_time =formatter1.parse(current_day+" "+current_time);
			java.util.Date Start_working = inFormat2.parse(current_day+" "+working_time_model.getStart());
			boolean start_status = Start_working.after(Start_time)
					|| working_time_model.getStart().toString().equals(current_time);
			System.out.println("Time "+current_day+" "+current_time);
			System.out.println("start_status "+start_status);
			System.out.println("Start_working "+Start_working);
			System.out.println("Start_time "+Start_time);
			System.out.println("Start_working.after(Start_time) "+Start_working.after(Start_time));
			if(Checkin != null){
				model.setModel(Checkin);
				if(model.getStatusCheckIn().equals("-")){
					if(Leave_req != null){
						leave_model.setModel(Leave_req);
						String Begin = leave_model.getBegin().toString().split(" ")[0];
						String End = leave_model.getEnd().toString().split(" ")[0];
						if(!(leave_model.getBegin().getHours() == 9 && leave_model.getEnd().getHours() == 18)){
							if(current_day.equals(Begin)){
								if(leave_model.getBegin().getHours() > 9){
									if(start_status){
										model.setStatusCheckIn("In time");
										responseBodyStr.put("Message","Check in complete");
									}
									else{
										model.setStatusCheckIn("Late");
										responseBodyStr.put("Message","Check in complete");
									}
								}
							}
							else if(current_day.equals(End)){
								String leave_end_time = leave_model.getEnd().toString().split(" ")[1];
								start_status = leave_model.getEnd().after(Start_time) || leave_end_time.equals(current_time+".0");
								if(leave_model.getEnd().getHours() < 18){
									if(start_status){
										model.setStatusCheckIn("In time");
										responseBodyStr.put("Message","Check in complete");
									}
									else{
										model.setStatusCheckIn("Late");
										responseBodyStr.put("Message","Check in complete");
									}
								}
							}
						}
					}
					else if(start_status) {
						model.setStatusCheckIn("In time");
					}
					else {
						model.setStatusCheckIn("Late");
					}
					model.setEmpId(id);
					model.setCheckInStr(dtf.format(now)+":00");
					model.setCheckoutStr(model.getCheckout().toString());
					dbutil.Update_Checkin_Checkout(connection,model,current_day);
					dbutil.Update_Log_Status(connection,"checkin_checkout_log","Check_id",Integer.toString(model.getCheckId()));
					dbutil.Add_Checkin_Checkout_Detail_log(connection,model.getCheckId(),Log_Time);
					Log_detail = dbutil.select2con(connection,"checkin_checkout_detail_log",
							"Check_id","Time",Integer.toString(model.getCheckId()),Log_Time);
					dbutil.Addlog(connection,"checkin_checkout_log","Check_id",
							Integer.toString(model.getCheckId()),Log_Time,Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
					responseBodyStr.put("Message","Check in complete");
				}
				else{
					responseBodyStr.put("Message","You have already Check in");
				}
			}
			else{
				Map<String, Object> emp_data;
				EmployeeModel emp_model = new EmployeeModel();
				emp_data = dbutil.select(connection,"employee","Emp_id",Integer.toString(id));
				emp_model.setModel(emp_data);
				if(start_status) {
					model.setStatusCheckIn("In time");
				}
				else {
					model.setStatusCheckIn("Late");
				}
				model.setEmpId(id);
				model.setDetail("-");
				model.setStatusCheckOut("-");
				model.setCheckInStr(dtf.format(now)+":00");
				model.setCheckoutStr(current_day+" 00:00:00");
				dbutil.AddCheckIn(connection,model);
				Checkin = dbutil.select2con(connection,"checkin_checkout","Emp_id","Checkin_at",
						Integer.toString(model.getEmpId()),model.getCheckInStr());
				model.setModel(Checkin);
				dbutil.Add_Checkin_Checkout_Detail_log(connection,model.getCheckId(),Log_Time);
				Log_detail = dbutil.select2con(connection,"checkin_checkout_detail_log",
						"Check_id","Time",Integer.toString(model.getCheckId()),Log_Time);
				dbutil.Addlog(connection,"checkin_checkout_log","Check_id",
						Integer.toString(model.getCheckId()),Log_Time,"0","1","Add",(Integer)Log_detail.get("Log_id"));
			}
        	} catch(SQLException e) {
        	e.printStackTrace();
			} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}
	
	public Map<String, Object> CheckOut(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> working_time_data ;
		Map<String, Object> Last_Checkin ;
		Map<String, Object> Leave_req ;
		Map<String, Object> DayOff ;
		Map<String, Object> Leave_count ;
		Map<String, Object> Log_detail ;
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		LeaveModel leave_model = new LeaveModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		LeaveCountModel count_model = new LeaveCountModel();
		WorkingTimeModel working_time_model = new WorkingTimeModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DateFormat inFormat2 = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Emp_id = Integer.toString(id);
		String Checkin_id = (String) data.get("Checkin_id");
        try {
			Last_Checkin = dbutil.select(connection,"checkin_checkout","Check_id",Checkin_id);
			model.setModel(Last_Checkin);
			String[] current_date_time = dtf.format(now).split(" ");
			String current_time = current_date_time[1]+":00";
			String current_day = current_date_time[0];
			String[] Checkout_DateTime = model.getCheckout().toString().split(" ");
			String[] Checkout_Date = Checkout_DateTime[0].split("-");
			String[] Checkin_DateTime = model.getCheckin().toString().split(" ");
			LocalDate localDate = LocalDate.of(Integer.parseInt(Checkout_Date[0]),
					Integer.parseInt(Checkout_Date[1]),Integer.parseInt(Checkout_Date[2]));
			java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
			String DayName = dayOfWeek.toString();
        	working_time_data = dbutil.select(connection,"working_time","Day_Name",DayName);
			Leave_req = dbutil.Select_Leave_req(connection,Emp_id,current_date_time[0]);
        	working_time_model.setModel(working_time_data);
			java.util.Date End_time = inFormat2.parse(current_day+" "+current_time);
			java.util.Date Workingtime_End_time = inFormat2.parse(current_day+" "+working_time_model.getEnd());
			java.util.Date Ot_time = inFormat2.parse(Checkout_DateTime[0]+" "+working_time_model.getOvertime());
			boolean End_after = End_time.after(Workingtime_End_time)
						|| current_time.equals(working_time_model.getEnd().toString());
			String str_time = current_day+" "+current_time;
			if(model.getStatusCheckOut().equals("-")) {
				if(Leave_req != null){
					leave_model.setModel(Leave_req);
					String Begin = leave_model.getBegin().toString().split(" ")[0];
					String End = leave_model.getEnd().toString().split(" ")[0];
					if(!(leave_model.getBegin().getHours() == 9 && leave_model.getEnd().getHours() == 18) ){
						if(current_day.equals(Begin)){
							End_after = End_time.after(leave_model.getBegin()) || leave_model.getBegin().equals(current_time+".0");
							if(End_after){
									model.setStatusCheckOut("In time");
									responseBodyStr.put("Message","Check Out Complete");
							}
							else{
								model.setStatusCheckOut("Check Out Early");
								responseBodyStr.put("Message","Check Out Complete");
							}
						}
						else if(current_day.equals(End)){
								boolean	Ot_status = End_time.after(Ot_time) ||
										End_time.equals(Ot_time);
								if(End_after){
									if(Ot_status){
										String working_time = Checkout_DateTime[0] +" " +working_time_model.getEnd().toString();
										int hour = findDifferenceHour(working_time,dtf.format(now)+":00");
										model.setStatusCheckOut("Over Time");
										DayOff = dbutil.select(connection,"leave_type","Type_name",ConfigConstants.DAY_OFF_NAME);
										type_model.setModel(DayOff);
										Leave_count = dbutil.select2con(connection,"leave_day_count",
												"Emp_id","Type_ID",Emp_id,Integer.toString(type_model.getId()));
										count_model.setModel(Leave_count);
										count_model.setLeaved(count_model.getLeaved()+hour);
										dbutil.AddDayOff(connection,count_model);
										leave_model.setAmount(hour);
										leave_model.setReqId(0);
										dbutil.Leave_count_log(connection,count_model,leave_model,str_time,"Add",id);
										responseBodyStr.put("Message","Check Out Complete");
									}
									else{
										model.setStatusCheckOut("In time");
										responseBodyStr.put("Message","Check Out Complete");
									}
								}
								else {
									model.setStatusCheckOut("Check Out Early");
									responseBodyStr.put("Message","Check Out Early");
								}
						}
					}
				}
				else if(End_after) {
					boolean Ot_status = End_time.after(Ot_time) ||
							End_time.equals(Ot_time);
					if(Ot_status){
						String working_time = Checkout_DateTime[0] +" " +working_time_model.getEnd().toString();
						int hour = findDifferenceHour(working_time,dtf.format(now)+":00");
						model.setStatusCheckOut("Over Time");
						DayOff = dbutil.select(connection,"leave_type","Type_name",ConfigConstants.DAY_OFF_NAME);
						type_model.setModel(DayOff);
						Leave_count = dbutil.select2con(connection,"leave_day_count",
								"Emp_id","Type_ID",Emp_id,Integer.toString(type_model.getId()));
						count_model.setModel(Leave_count);
						count_model.setLeaved(count_model.getLeaved()+hour);
						dbutil.AddDayOff(connection,count_model);
						leave_model.setAmount(hour);
						leave_model.setReqId(0);
						leave_model.setDepend(id);
						dbutil.Leave_count_log(connection,count_model,leave_model,str_time,"Add",id);
						responseBodyStr.put("Message","Check Out Complete");
					}
					else{
						model.setStatusCheckOut("In time");
						responseBodyStr.put("Message","Check Out Complete");
					}
				}
				else {
					model.setStatusCheckOut("Check Out Early");
					responseBodyStr.put("Message","Check Out Early");
				}
				model.setEmpId(id);
				model.setCheckoutStr(str_time);
				model.setCheckInStr(model.getCheckin().toString());
				dbutil.Update_Checkin_Checkout(connection,model,Checkin_DateTime[0]);
				dbutil.Update_Log_Status(connection,"checkin_checkout_log","Check_id",Integer.toString(model.getCheckId()));
				dbutil.Update_Checkin_Checkout_Detail_log(connection,model,current_time);
				Log_detail = dbutil.select2con(connection,"checkin_checkout_detail_log",
						"Check_id","Time",Integer.toString(model.getCheckId()),current_time);
				dbutil.Addlog(connection,"checkin_checkout_log","Check_id",
						Integer.toString(model.getCheckId()),current_time,Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
				responseBodyStr.put("status",200);
			}
			else{
				responseBodyStr.put("status",400);
				responseBodyStr.put("Message","You have already Check Out");
			}
        } catch(SQLException e) {
        	e.printStackTrace();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Get_CheckInCheckOut_By_Emp_Id(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> res = new ArrayList<>();
		List<Map<String, Object>> Check_data = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		String Emp_id = (String) data.get("Emp_id");
		String Duration = (String) data.get("Type");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm");
		LocalDateTime now = LocalDateTime.now();
		String[] current_date_raw = ((String) data.get("Date")).split("-");
		String current_time = dtf.format(now).split(" ")[1];
//		String current_time = current_date_time[1]+":00";
		try{
			if(Duration.equals("Day")){
				Check_data = dbutil.selectCheckin(connection,"%-"+current_date_raw[2]+"%",Emp_id);
			}
			else if(Duration.equals("Month")){
				Check_data = dbutil.selectCheckin(connection,"%-"+current_date_raw[1]+"%",Emp_id);
			}
			else if(Duration.equals("Year")) {
				Check_data = dbutil.selectCheckin(connection, current_date_raw[0] + "%", Emp_id);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		if(Check_data != null){
			for(Map<String, Object> temp : Check_data){
				Map<String, Object> ans = new HashMap<String, Object>();
				model.setModel(temp);
				ans.put("CheckId",model.getCheckId());
				ans.put("Check_in",model.getCheckin());
				ans.put("Check_out",model.getCheckout());
				ans.put("Emp_id",model.getEmpId());
				ans.put("Check_in_status",model.getStatusCheckIn());
				ans.put("Check_out_status",model.getStatusCheckOut());
				ans.put("Detail",model.getDetail());
				res.add(ans);
			}
		}
		responseBodyStr.put("data",res);
		responseBodyStr.put("status",200);
		responseBodyStr.put("message","success");
		return responseBodyStr;
	}

	public Map<String, Object> Get_ALL_CheckInCheckOut(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> res = new ArrayList<>();
		List<Map<String, Object>> Check_data = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		CheckInCheckOutModel model = new CheckInCheckOutModel();
		String Duration = (String) data.get("Type");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm");
		LocalDateTime now = LocalDateTime.now();
		String[] current_date_raw = ((String) data.get("Date")).split("-");
		String current_time = dtf.format(now).split(" ")[1];
		System.out.println("Time now " + current_time);
//		String current_time = current_date_time[1]+":00";
		try{
			if(Duration.equals("Day")){
				Check_data = dbutil.selectCheckinAll(connection,"%-"+current_date_raw[2]+"%");
			}
			else if(Duration.equals("Month")){
				Check_data = dbutil.selectCheckinAll(connection,"%-"+current_date_raw[1]+"%");
			}
			else if(Duration.equals("Year")){
				Check_data = dbutil.selectCheckinAll(connection,current_date_raw[0]+"%");
			}
			if(Check_data != null){
				for(Map<String, Object> temp : Check_data){
					Map<String, Object> ans = new HashMap<>();
					model.setModel(temp);
					ans.put("CheckId",model.getCheckId());
					ans.put("Check_in",model.getCheckin());
					ans.put("Check_out",model.getCheckout());
					ans.put("Emp_id",model.getEmpId());
					ans.put("Check_in_status",model.getStatusCheckIn());
					ans.put("Check_out_status",model.getStatusCheckOut());
					ans.put("Detail",model.getDetail());
					res.add(ans);
				}
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

}
