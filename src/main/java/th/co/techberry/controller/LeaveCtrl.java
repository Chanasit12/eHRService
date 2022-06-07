package th.co.techberry.controller;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import java.util.*;
import java.time.temporal.ChronoUnit;
public class LeaveCtrl {

	public Map<String, Object> Leave_info_Profile(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_info ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		LeaveTypeModel type_model = new LeaveTypeModel();
		LeaveCountModel count = new LeaveCountModel();
		String Id = String.valueOf(id);
		try{
			Employee_info = dbutil.select(connection,"employee","Emp_id",Id);
			if(Employee_info != null) {
				String emp_id = String.valueOf(Employee_info.get("Emp_id"));
				Leave_info = dbutil.selectArray(connection,"leave_day_count","Emp_id",emp_id);
				if(Leave_info != null){
					for(Map<String, Object> temp : Leave_info){
						Map<String, Object> leave_type = new HashMap<String, Object>();
						Map<String, Object> ans = new HashMap<String, Object>();
						String leave_id = String.valueOf((Integer) temp.get("Type_ID"));
						leave_type = dbutil.select(connection,"leave_type","Type_ID",leave_id);
						type_model.setModel(leave_type);
						count.setModel(temp);
						ans.put("Type_name",type_model.getName());
						ans.put("Leaved",count.getLeaved());
						res.add(ans);
					}
					responseBodyStr.put("data",res);
				}
				else{
					responseBodyStr.put("data",res);
				}
			}
			else {
				responseBodyStr.put("data",res);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Leave_request(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_request ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		LeaveModel Leave_model = new LeaveModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		String Id = String.valueOf(id);
		try{
			Employee_info = dbutil.select(connection,"employee","Emp_id",Id);
			if(Employee_info != null) {
				String emp_id = String.valueOf(Employee_info.get("Emp_id"));
				Leave_request = dbutil.selectArray(connection,"leave_request","Emp_id",emp_id);
				if(Leave_request != null){
					for(Map<String, Object> temp : Leave_request){
						Map<String, Object> leave_type ;
						Map<String, Object> ans = new HashMap<>();
						Leave_model.setModel(temp);
						leave_type = dbutil.select(connection,"leave_type","Type_ID",String.valueOf(Leave_model.getTypeId()));
						type_model.setModel(leave_type);
						ans.put("id",Leave_model.getReqId());
						ans.put("Type_name",type_model.getName());
						ans.put("Begin",Leave_model.getBegin());
						ans.put("End",Leave_model.getEnd());
						ans.put("Leave_status",Leave_model.getStatus());
						ans.put("Emergency",Leave_model.getEmergency());
						ans.put("Comment",Leave_model.getComment());
						ans.put("Detail",Leave_model.getDetail());
						ans.put("Amount",Leave_model.getAmount());
						res.add(ans);
					}
					System.out.println("res "+res);
					responseBodyStr.put("data",res);
				}
				else{
					responseBodyStr.put("data",res);

				}
			}
			else {
				responseBodyStr.put("data",res);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Profile_leave(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> leave_info = new HashMap<>();
		Map<String, Object> leave_request = new HashMap<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		DatabaseUtil dbutil = new DatabaseUtil();
		leave_info.putAll(Leave_info_Profile(id));
		leave_request.putAll(Leave_request(id));
		System.out.println("leave_request "+leave_request);
		System.out.println("leave_info "+leave_info);
		if(leave_info != null && leave_request != null) {
			responseBodyStr.put("status", 200);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
			responseBodyStr.put("Leave_infomation",leave_info);
			responseBodyStr.put("Leave_request",leave_request);
		}
		else {
			responseBodyStr.put("status", 404);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put("message",ConfigConstants.ID_NOT_FOUND);
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Leave_type_mgmt() throws ClassNotFoundException, SQLException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> Leave_type = new ArrayList<Map<String, Object>>();
		LeaveTypeModel model = new LeaveTypeModel();
		try {
			Leave_type = dbutil.selectAll(connection,"leave_type");
			if(Leave_type != null) {
				for(Map<String, Object> temp : Leave_type){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("id",model.getId());
					ans.put("Type_name",model.getName());
					ans.put("Num_per_year",model.getNum_per_year());
					ans.put("Num_can_add",model.getNum_can_add());
					res.add(ans);
				}
				System.out.println("res "+res);
				responseBodyStr.put("status",200);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		responseBodyStr.put("data",res);
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_Leave_type(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Employee ;
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Leave_type ;
		LeaveTypeModel model = new LeaveTypeModel();
		if(data.get("Type_name").equals("") || data.get("Num_per_year").equals("") || data.get("Num_can_add").equals("")) {
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		else {
			String Type_name = (String) data.get("Type_name");
			String Num_per_year = (String) data.get("Num_per_year");
			String Num_can_add = (String) data.get("Num_can_add");
			try {
				dbutil.AddLeave_type(connection,Type_name,Num_per_year,Num_can_add);
				Leave_type = dbutil.select(connection,"leave_type","Type_name",Type_name);
				model.setModel(Leave_type);
				Employee = dbutil.selectAll(connection,"employee");
				if(Employee != null) {
					for(Map<String, Object> temp : Employee){
							try {
								String Emp_id = String.valueOf(temp.get("Emp_id"));
								dbutil.AddLeave_Day(connection,Emp_id,model);
							}catch (SQLException e) {
								e.printStackTrace();
								result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
							}
					}
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
				}
				else {
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
				}
		}	catch (SQLException e) {
				e.printStackTrace();
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
		}
		result.put("status",200);
		return result;
	}
	
	public Map<String, Object> Delete_Leave_Type(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					dbutil.Delete(connection,"leave_type","Type_ID",(String)Target.get(size));
					dbutil.Delete(connection,"leave_day_count","Type_ID",(String)Target.get(size));
				}catch (SQLException e) {
					e.printStackTrace();
					result.put("status",400);
					result.put("message","Delete fail");
					break;
				}
				size++;
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
	
	public Map<String, Object> Update_Leave_Type(Map<String, Object> data) throws SQLException, ClassNotFoundException ,NumberFormatException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Employee_Leave ;
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Leave_info ;
		LeaveTypeModel model = new LeaveTypeModel();
		LeaveCountModel count_model = new LeaveCountModel();
		Leave_info = (dbutil.select(connection,"leave_type","Type_ID",(String)data.get("Type_ID")));
		model.setModel(Leave_info);
		try{
			if(!data.get("Type_name").equals("")) {
				model.setName((String)data.get("Type_name"));
			}
			if(!data.get("Num_per_year").equals("")) {
				model.setNum_per_year(Integer.valueOf((String)data.get("Num_per_year")));
			}
			if(!data.get("Num_can_add").equals("")) {
				model.setNum_can_add(Integer.valueOf((String)data.get("Num_can_add")));
			}
			dbutil.UpdateLeaveType(connection,model);
			Employee_Leave = dbutil.selectArray(connection,"leave_day_count","Type_ID",(String)data.get("Type_ID"));
			if(!Employee_Leave.isEmpty()) {
				for(Map<String, Object> temp : Employee_Leave ){
					count_model.setModel(temp);
					if(count_model.getLeaved() > model.getNum_per_year()) {
						count_model.setLeaved(model.getNum_per_year());
						dbutil.UpdateLeaveCount(connection,count_model);
					}
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		result.put("status",200);
		result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		return result;
	}

	public Map<String, Object> Send_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException,ParseException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Leave_req ;
		Map<String, Object> Log_detail ;
		LeaveModel leave_model = new LeaveModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		leave_model.setModelByReq(data);
		int Emergency;
		if(leave_model.getEmergency()){
			Emergency = 1;
		} else{
			Emergency = 0;
		}
		try{
			leave_model.setStatus(ConfigConstants.REQUESTED);
			leave_model.setAmount(Calculate_hour(leave_model));
			dbutil.AddLeave_Req(connection,leave_model,Emergency);
			Leave_req = dbutil.select2con(connection,"leave_request","Emp_id","Begin"
					,Integer.toString(leave_model.getEmpId()),leave_model.getBegin().toString());
			leave_model.setModel(Leave_req);
			dbutil.Leave_Request_Detail_log(connection,leave_model,Time);
			Log_detail = dbutil.select2con(connection,"leave_request_detail_log","Emp_id","Time",
					Integer.toString(leave_model.getEmpId()),Time);
			int log_id = (Integer) Log_detail.get("Log_id");
			dbutil.Addlog(connection,"leave_request_log","Req_id",Integer.toString((leave_model.getReqId())),Time,Integer.toString(id),
					"1","Add",log_id);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message",leave_model.getStatus());
		} catch(SQLException e){
			e.printStackTrace();
		} catch(ParseException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Get_Request(int id) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_request ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Emp_detail ;
		LeaveModel leave_model = new LeaveModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		EmployeeModel emp_model = new EmployeeModel();
		try{
			Leave_request = dbutil.selectArray(connection,"leave_request","Depend",Integer.toString(id));
			if(Leave_request != null){
				for(Map<String, Object> temp : Leave_request){
					leave_model.setModel(temp);
					Map<String, Object> leave_type ;
					Map<String, Object> ans = new HashMap<>();
					leave_model.setModel(temp);
					leave_type = dbutil.select(connection,"leave_type","Type_ID",String.valueOf(leave_model.getTypeId()));
					Emp_detail = dbutil.select(connection,"employee","Emp_id",Integer.toString(leave_model.getEmpId()));
					emp_model.setModel(Emp_detail);
					type_model.setModel(leave_type);
					ans.put("Request_id",leave_model.getReqId());
					ans.put("Sender_Name",emp_model.getFirstname()+" "+emp_model.getLastname());
					ans.put("Type_name",type_model.getName());
					ans.put("Begin",leave_model.getBegin());
					ans.put("End",leave_model.getEnd());
					ans.put("Detail",leave_model.getDetail());
					ans.put("Leave_status",leave_model.getStatus());
					ans.put("Emergency",leave_model.getEmergency());
					ans.put("Comment",leave_model.getComment());
					ans.put("Amount",leave_model.getAmount());
					res.add(ans);
				}
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public boolean Check_Holliday(String Date) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Holiday ;
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		HolidayModel model = new HolidayModel();
		String[] date = Date.split("-");
		String DayAndMonth = date[1]+"-%";
		boolean result = true;
		try{
			Holiday = dbutil.selectHoliday(connection,DayAndMonth);
			Date d1 = sdformat.parse(Date);
			if(Holiday != null){
				for(Map<String, Object> temp : Holiday){
					model.setModel(temp);
					d1.compareTo(model.getStart());
					if(d1.compareTo(model.getStart()) >= 0 && d1.compareTo(model.getEnd()) <= 0) {
						result = false;
					}
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		} catch (ParseException e){
			e.printStackTrace();
		}
		return result;
	}

	public boolean Check_Leaved_Count(LeaveModel model,int id) throws SQLException, ClassNotFoundException ,ParseException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> Leave_count_info;
		LeaveCountModel LeaveCount_model = new LeaveCountModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		boolean result = false;
		try{
			Leave_count_info = dbutil.select2con(connection,"leave_day_count","Emp_id","Type_ID",
					Integer.toString(model.getEmpId()),Integer.toString(model.getTypeId()));
			LeaveCount_model.setModel(Leave_count_info);
				if(LeaveCount_model.getLeaved() >= model.getAmount()){
					LeaveCount_model.setLeaved(LeaveCount_model.getLeaved()-model.getAmount());
					dbutil.UpdateLeaveCount(connection,LeaveCount_model);
					Leave_count_info = dbutil.select2con(connection,"leave_day_count","Emp_id","Type_ID",
							Integer.toString(model.getEmpId()),Integer.toString(model.getTypeId()));
					LeaveCount_model.setModel(Leave_count_info);
					dbutil.Leave_count_log(connection,LeaveCount_model,model,Time,"Use",id);
					result = true;
				}
				else{
					result = false;
				}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}

	public int Calculate_hour(LeaveModel model) throws SQLException, ClassNotFoundException ,ParseException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> working_time_data = new HashMap<String, Object>();
		WorkingTimeModel WorkingTime = new WorkingTimeModel();
		String[] Begin = model.getBegin().toString().split(" ");
		String[] End = model.getEnd().toString().split(" ");
		LocalDate dateBefore = LocalDate.parse(Begin[0]);
		LocalDate dateAfter = LocalDate.parse(End[0]);
		long day = ChronoUnit.DAYS.between(dateBefore, dateAfter)+1;
		long ONE_DAY_MILLI_SECONDS = 24 * 60 * 60 * 1000;
		int hour = 0;
		DateFormat dsf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			if(Begin[0].equals(End[0])){
				if((model.getEnd().getHours() < 12 && model.getBegin().getHours() < 12) || (model.getEnd().getHours() > 12 && model.getBegin().getHours() > 12)){
					hour = model.getEnd().getHours()-model.getBegin().getHours();
				}
				else{
					hour = model.getEnd().getHours()-model.getBegin().getHours()-1;
				}
			}
			else{
				String date = Begin[0];
				while(day>0){
					String[] start = date.toString().split("-");
					LocalDate localDate = LocalDate.of(Integer.parseInt(start[0]),
							Integer.parseInt(start[1]),Integer.parseInt(start[2]));
					java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
					String DayName = dayOfWeek.toString();
					if(!DayName.equals("SATURDAY") && !DayName.equals("SUNDAY") && Check_Holliday(date)){
						working_time_data = dbutil.select(connection,"working_time","Day_Name",DayName);
						WorkingTime.setModel(working_time_data);
						if(date.equals(Begin[0])){
							if(model.getBegin().getHours() > 12){
								hour += WorkingTime.getEnd().getHours() - model.getBegin().getHours();
							}
							else if(model.getBegin().getHours() < 12){
								hour += WorkingTime.getEnd().getHours() - model.getBegin().getHours() -1 ;
							}
						}
						else if(date.equals(End[0])){
							if(model.getEnd().getHours() > 12){
								hour += model.getEnd().getHours() - WorkingTime.getStart().getHours() - 1 ;
							}
							else if(model.getEnd().getHours() < 12){
								hour += model.getEnd().getHours() - WorkingTime.getStart().getHours();
							}
						}
						else{
							hour += WorkingTime.getEnd().getHours() - WorkingTime.getStart().getHours() - 1;
						}
					}
					Date Nextdate = dsf.parse(date);
					long nextDayMilliSeconds = Nextdate.getTime() + ONE_DAY_MILLI_SECONDS;
					Date nextDate = new Date(nextDayMilliSeconds);
					date = dsf.format(nextDate);
					day--;
				}
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return hour;
	}

	public int Add_Leave_Checkin_Checkout(LeaveModel model,int id) throws SQLException, ClassNotFoundException ,ParseException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> working_time_data ;
		Map<String, Object> Checkin ;
		Map<String, Object> Log_detail ;
		WorkingTimeModel WorkingTime = new WorkingTimeModel();
		CheckInCheckOutModel check_model ;
		String[] Begin = model.getBegin().toString().split(" ");
		String[] End = model.getEnd().toString().split(" ");
		LocalDate dateBefore = LocalDate.parse(Begin[0]);
		LocalDate dateAfter = LocalDate.parse(End[0]);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String current_time = dtf.format(now);
		long day = ChronoUnit.DAYS.between(dateBefore, dateAfter)+1;
		long ONE_DAY_MILLI_SECONDS = 24 * 60 * 60 * 1000;
		int hour = 0;
		DateFormat dsf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			if(Begin[0].equals(End[0])){
				String[] current_date_time = model.getBegin().toString().split(" ");
				String Time = current_date_time[0]+" "+"00:00:00";
				check_model = new CheckInCheckOutModel();
				check_model.setEmpId(model.getEmpId());
				check_model.setStatusCheckIn("-");
				check_model.setStatusCheckOut("-");
				check_model.setDetail("Leaved");
				check_model.setCheckInStr(Time);
				check_model.setCheckoutStr(Time);
				dbutil.AddCheckIn(connection,check_model);
				Checkin = dbutil.select2con(connection,"checkin_checkout","Emp_id","Checkin_at",
						Integer.toString(check_model.getEmpId()),check_model.getCheckInStr());
				check_model.setModel(Checkin);
				dbutil.Add_Checkin_Checkout_Detail_log(connection,check_model.getCheckId(),current_time);
				Log_detail = dbutil.select2con(connection,"checkin_checkout_detail_log",
						"Check_id","Time",Integer.toString(check_model.getCheckId()),current_time);
				dbutil.Addlog(connection,"checkin_checkout_log","Check_id",
						Integer.toString(check_model.getCheckId()),current_time,Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
			}
			else{
				String date = Begin[0];
				while(day>0){
					check_model = new CheckInCheckOutModel();
					String[] start = date.split("-");
					String Time = date+" "+"00:00:00";
					LocalDate localDate = LocalDate.of(Integer.parseInt(start[0]),
							Integer.parseInt(start[1]),Integer.parseInt(start[2]));
					java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
					String DayName = dayOfWeek.toString();
					if(!DayName.equals("SATURDAY") && !DayName.equals("SUNDAY") && Check_Holliday(date)){
						working_time_data = dbutil.select(connection,"working_time","Day_Name",DayName);
						WorkingTime.setModel(working_time_data);
						check_model.setDetail("Leaved");
						check_model.setEmpId(model.getEmpId());
						check_model.setStatusCheckIn("-");
						check_model.setStatusCheckOut("-");
						check_model.setCheckInStr(Time);
						check_model.setCheckoutStr(Time);
						dbutil.AddCheckIn(connection,check_model);
						Checkin = dbutil.select2con(connection,"checkin_checkout","Emp_id","Checkin_at",
								Integer.toString(check_model.getEmpId()),check_model.getCheckInStr());
						check_model.setModel(Checkin);
						System.out.println("current_time "+current_time);
						dbutil.Add_Checkin_Checkout_Detail_log(connection,check_model.getCheckId(),current_time);
						Log_detail = dbutil.select2con(connection,"checkin_checkout_detail_log",
								"Check_id","Time",Integer.toString(check_model.getCheckId()),current_time);
						dbutil.Addlog(connection,"checkin_checkout_log","Check_id",
								Integer.toString(check_model.getCheckId()),current_time,Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
					}
					Date Nextdate = dsf.parse(date);
					long nextDayMilliSeconds = Nextdate.getTime() + ONE_DAY_MILLI_SECONDS;
					Date nextDate = new Date(nextDayMilliSeconds);
					date = dsf.format(nextDate);
					day--;
				}
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return hour;
	}

	public Map<String, Object> Response_Leave_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Approver_detail;
		Map<String, Object> Role_detail;
		Map<String, Object> Leave_request;
		Map<String, Object> leave_type_detail;
		Map<String, Object> Log_detail;
		LeaveModel leave_model = new LeaveModel();
		EmployeeModel Approver_model = new EmployeeModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		String Req_id = (String) data.get("Req_id");
		String Comment = (String) data.get("Comment");
		String Leave_type = (String) data.get("Leave_type");
		String Depend = (String) data.get("Depend");
		Boolean Status = Boolean.parseBoolean((String) data.get("Status"));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
 		try{
			Approver_detail = dbutil.select(connection,"employee","Emp_id",Integer.toString(id));
			Approver_model.setModel(Approver_detail);
			Role_detail = dbutil.select(connection,"user_role","Role_ID",Integer.toString(Approver_model.getRold_id()));
			Leave_request = dbutil.select(connection,"leave_request","Req_id",Req_id);
			leave_type_detail = dbutil.select(connection,"leave_type","Type_ID",Leave_type);
			type_model.setModel(leave_type_detail);
			leave_model.setModel(Leave_request);
			if(!Comment.equals("")){
				leave_model.setComment(Comment);
			}
			if(!Leave_type.equals("")){
				leave_model.setTypeId(Integer.parseInt(Leave_type));
			}
				if( Role_detail.get("Role_Name").equals("Approver")){
					leave_model.setDepend(leave_model.getEmpId());
						if(Status){
							if(Check_Leaved_Count(leave_model,id)){
								leave_model.setStatus(ConfigConstants.APPROVED_BY_APPROVER);
								Add_Leave_Checkin_Checkout(leave_model,id);
							}
							else{
								leave_model.setStatus(ConfigConstants.DECLINED_BY_APPROVER);
								leave_model.setComment("Leaved days you have is less than leaved day you want");
							}
						}
						else{
							leave_model.setStatus(ConfigConstants.DECLINED_BY_APPROVER);
						}
				}
				else{
					int Depend_id;
					if(Status){
						Depend_id = Integer.parseInt(Depend);
						leave_model.setDepend(Depend_id);
						leave_model.setStatus(ConfigConstants.LEAVE_REQUEST_APPROVED_BY_CHIEF);
					}
					else{
						leave_model.setDepend(leave_model.getEmpId());
						leave_model.setStatus(ConfigConstants.LEAVE_REQUEST_DECLINED_BY_CHIEF);
					}

				}
			responseBodyStr.put("message",leave_model.getStatus());
			responseBodyStr.put("comment",leave_model.getComment());
			dbutil.UpdateLeaveReq(connection,leave_model);
			dbutil.Leave_Request_Detail_log(connection,leave_model,Time);
			dbutil.Update_Log_Status(connection,"leave_request_log","Req_id",Integer.toString(leave_model.getReqId()));
			Log_detail = dbutil.select2con(connection,"leave_request_detail_log","Emp_id","Time",
					Integer.toString(leave_model.getEmpId()),Time);
			int log_id = (Integer) Log_detail.get("Log_id");
			dbutil.Addlog(connection,"leave_request_log","Req_id",Integer.toString(leave_model.getReqId()),Time,Integer.toString(id),
					"1","Update",log_id);
			responseBodyStr.put("status",200);
		} catch(SQLException e){
			e.printStackTrace();
		} catch (ParseException e){
			 e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Send_Cancellation(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Emp_detail ;
		Map<String, Object> Leave_req ;
		Map<String, Object> Log_detail ;
		LeaveModel leave_model = new LeaveModel();
		EmployeeModel emp_model = new EmployeeModel();
		String Req_id = (String) data.get("Req_id");
		String Depend = (String) data.get("Depend");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try{
			Emp_detail = dbutil.select(connection,"employee","Emp_id",Depend);
			emp_model.setModel(Emp_detail);
			Leave_req = dbutil.select(connection,"leave_request","Req_id",Req_id);
			leave_model.setModel(Leave_req);
			if(leave_model.getStatus().equals(ConfigConstants.REQUESTED)){
				leave_model.setDepend(leave_model.getEmpId());
				leave_model.setStatus(ConfigConstants.CANCELLATION);
			}
			else{
				leave_model.setDepend(Integer.parseInt(Depend));
				leave_model.setStatus(ConfigConstants.CANCELLATION_REQUEST);
			}
			dbutil.UpdateLeaveReq(connection,leave_model);
			dbutil.Leave_Request_Detail_log(connection,leave_model,Time);
			dbutil.Update_Log_Status(connection,"leave_request_log","Req_id",Integer.toString(leave_model.getReqId()));
			Log_detail = dbutil.select2con(connection,"leave_request_detail_log","Emp_id","Time",
					Integer.toString(leave_model.getEmpId()),Time);
			int log_id = (Integer) Log_detail.get("Log_id");
			dbutil.Addlog(connection,"leave_request_log","Req_id",Integer.toString(leave_model.getReqId()),Time,Integer.toString(id),
					"1","Update",log_id);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message",leave_model.getStatus());
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Response_Cancelled_Leave_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Leave_req ;
		Map<String, Object> Leave_count ;
		Map<String, Object> Log_detail ;
		LeaveModel leave_model = new LeaveModel();
		LeaveCountModel count_model = new LeaveCountModel();
		String Req_id = (String) data.get("Req_id");
		String Comment = (String) data.get("Comment");
		Boolean Status = Boolean.parseBoolean((String) data.get("Status"));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try{
			Leave_req = dbutil.select(connection,"leave_request","Req_id",Req_id);
			leave_model.setModel(Leave_req);
			if(Status){
				leave_model.setStatus(ConfigConstants.APPROVED_CANCELLATION);
				leave_model.setComment(Comment);
				Leave_count = dbutil.select2con(connection,"leave_day_count","Emp_id","Type_ID",
						Integer.toString(leave_model.getEmpId()),Integer.toString(leave_model.getTypeId()));
				count_model.setModel(Leave_count);
				count_model.setLeaved(count_model.getLeaved()+leave_model.getAmount());
				dbutil.UpdateLeaveCount(connection,count_model);
				dbutil.Leave_count_log(connection,count_model,leave_model,Time,"Add",id);
			}
			else{
				leave_model.setComment(Comment);
				leave_model.setStatus(ConfigConstants.DECLINED_CANCELLATION);
			}
			dbutil.UpdateLeaveReq(connection,leave_model);
			dbutil.Leave_Request_Detail_log(connection,leave_model,Time);
			dbutil.Update_Log_Status(connection,"leave_request_log","Req_id",Integer.toString(leave_model.getReqId()));
			Log_detail = dbutil.select2con(connection,"leave_request_detail_log","Emp_id","Time",
					Integer.toString(leave_model.getEmpId()),Time);
			int log_id = (Integer) Log_detail.get("Log_id");
			dbutil.Addlog(connection,"leave_request_log","Req_id",Integer.toString(leave_model.getReqId()),Time,Integer.toString(id),
					"1","Update",log_id);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message",leave_model.getStatus());
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Leave_request_By_Emp_id(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_request ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		LeaveModel Leave_model = new LeaveModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		String Id = Integer.toString(id);
		try{
			Employee_info = dbutil.select(connection,"employee","Emp_id",Id);
			if(Employee_info != null) {
				String emp_id = String.valueOf(Employee_info.get("Emp_id"));
				Leave_request = dbutil.selectArray(connection,"leave_request","Emp_id",emp_id);
				for(Map<String, Object> temp : Leave_request){
					Map<String, Object> leave_type ;
					Map<String, Object> ans = new HashMap<>();
					Leave_model.setModel(temp);
					leave_type = dbutil.select(connection,"leave_type","Type_ID",String.valueOf(Leave_model.getTypeId()));
					type_model.setModel(leave_type);
					ans.put("id",Leave_model.getReqId());
					ans.put("Type_name",type_model.getName());
					ans.put("Begin",Leave_model.getBegin());
					ans.put("End",Leave_model.getEnd());
					ans.put("Leave_status",Leave_model.getStatus());
					ans.put("Emergency",Leave_model.getEmergency());
					ans.put("Comment",Leave_model.getComment());
					ans.put("Detail",Leave_model.getDetail());
					ans.put("Amount",Leave_model.getAmount());
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
			}
			else {
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Emp_leave_information_By_id(Map<String, Object> data) throws ClassNotFoundException, SQLException {
		Map<String, Object> leave_info = new HashMap<>();
		Map<String, Object> leave_request = new HashMap<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		int id = Integer.parseInt((String) data.get("Emp_id"));
		leave_info.putAll(Leave_info_Profile(id));
		leave_request.putAll(Leave_request(id));
		if(leave_info != null && leave_request != null) {
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
			responseBodyStr.put("Leave_infomation",leave_info);
			responseBodyStr.put("Leave_request",leave_request);
		}
		else {
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.ID_NOT_FOUND);
		}
		responseBodyStr.put("status", 200);
		return responseBodyStr;
	}

	public Map<String, Object> All_Leave_Request() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_request ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		LeaveModel Leave_model = new LeaveModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		try{
			Leave_request = dbutil.selectAll(connection,"leave_request");
			if(Leave_request != null) {
					for(Map<String, Object> temp : Leave_request){
						Map<String, Object> leave_type ;
						Map<String, Object> ans = new HashMap<>();
						Leave_model.setModel(temp);
						leave_type = dbutil.select(connection,"leave_type","Type_ID",String.valueOf(Leave_model.getTypeId()));
						type_model.setModel(leave_type);
						ans.put("id",Leave_model.getReqId());
						ans.put("Emp_id",Leave_model.getEmpId());
						ans.put("Type_name",type_model.getName());
						ans.put("Begin",Leave_model.getBegin());
						ans.put("End",Leave_model.getEnd());
						ans.put("Leave_status",Leave_model.getStatus());
						ans.put("Emergency",Leave_model.getEmergency());
						ans.put("Comment",Leave_model.getComment());
						ans.put("Detail",Leave_model.getDetail());
						ans.put("Amount",Leave_model.getAmount());
						res.add(ans);
					}
				}
			responseBodyStr.put("data",res);
		} catch (SQLException e){
			e.printStackTrace();
		}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}

	public Map<String, Object> All_Leave_Count() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_count ;
		List<Map<String, Object>> Employee ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> Login_info ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		LeaveCountModel count_model = new LeaveCountModel();
		EmployeeModel emp_model = new EmployeeModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		LoginModel login_model = new LoginModel();
		try{
			Employee = dbutil.selectAll(connection,"employee");
			if(Employee != null) {
				for (Map<String, Object> temp : Employee) {
					Map<String, Object> emp_leave_count = new HashMap<>();
					List<Map<String, Object>> leave_count = new ArrayList<>();
					emp_model.setModel(temp);
					Login_info = dbutil.select(connection, "login", "Id", Integer.toString(emp_model.getId()));
					login_model.setModel(Login_info);
					if (login_model.getActivestatus()) {
						Leave_count = dbutil.selectArray(connection, "leave_day_count", "Emp_id", Integer.toString(emp_model.getEmpid()));
						if (Leave_count != null) {
							for (Map<String, Object> temp1 : Leave_count) {
								Map<String, Object> ans = new HashMap<>();
								Map<String, Object> leave_type ;
								String leave_id = String.valueOf((Integer) temp1.get("Type_ID"));
								leave_type = dbutil.select(connection, "leave_type", "Type_ID", leave_id);
								type_model.setModel(leave_type);
								count_model.setModel(temp1);
								ans.put("Type_name", type_model.getName());
								ans.put("Leaved", count_model.getLeaved());
								leave_count.add(ans);
							}
						}
					}
					emp_leave_count.put(Integer.toString(emp_model.getEmpid()),leave_count);
					res.add(emp_leave_count);
				}
			}
			responseBodyStr.put("data",res);
		} catch (SQLException e){
			e.printStackTrace();
		}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}
}
