package th.co.techberry.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.mysql.jdbc.Connection;

import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import th.co.techberry.util.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class MeetingRoomCtrl {

	public Map<String, Object> Show_Meeting_Room() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Room ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		MeetingRoomModel room_model = new MeetingRoomModel();
		try {
			Room = dbutil.selectAll(connection,"meeting_room");
			if(Room != null) {
				for(Map<String,Object> temp : Room) {
					Map<String, Object> ans = new HashMap<>();
					room_model.setModel(temp);
					ans.put("Room_Name", room_model.getRoomName());
					ans.put("Room_Id", room_model.getRoomId());
					ans.put("Description", room_model.getDescription());
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
				responseBodyStr.put("Message","success");
			}
			else {
				responseBodyStr.put("status",404);
				responseBodyStr.put("Message","Not found");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",400);
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Show_Meeting(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Meeting ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> meeting = new HashMap<>();
		MeetingModel meeting_model = new MeetingModel();
		List Target ;
		Target = ((ArrayList)data.get("Value"));
		String Date = java.time.LocalDate.now().toString();
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				meeting = new HashMap<>();
				try {
					Meeting = dbutil.select2conArray(connection,"meeting_room_booking","Room_id","Status",(String)Target.get(size),"1");
					if(Meeting != null) {
						for(Map<String,Object> temp : Meeting) {
							List<Map<String, Object>> Emp;
							Map<String, Object> ans = new HashMap<>();
							ArrayList<Integer> member = new ArrayList<>();
							meeting_model.setModel(temp);
							try {
								Emp = dbutil.selectArray(connection,"meeting","Meet_id",Integer.toString(meeting_model.getMeetId()));
								if(Emp != null) {
									for(Map<String,Object> temp2 : Emp) {
										member.add((Integer)temp2.get("Emp_id"));
									}
								}
							} catch(SQLException e) {
								e.printStackTrace();
							}
							ans.put("Meet_Id", meeting_model.getMeetId());
							ans.put("Subject", meeting_model.getSubject());
							ans.put("Create_at", meeting_model.getCreate());
							ans.put("Creator", meeting_model.getCreator());
							ans.put("Date", meeting_model.getDate());
							ans.put("Start_at", meeting_model.getStart());
							ans.put("End_at", meeting_model.getEnd());
							ans.put("Room_Id", meeting_model.getRoomId());
							ans.put("Status", meeting_model.getStatus());
							ans.put("Description", meeting_model.getDescription());
							ans.put("Members",member);
							res.add(ans);
						}
						meeting.put("meeting",res);
					}
					}catch(SQLException e) {
						e.printStackTrace();
						responseBodyStr.put("status",404);
						responseBodyStr.put("Message","Not found");
					}
				size++;
			}
			responseBodyStr.put("data",meeting);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
			
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Show_Employee_Meeting_By_ID(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Meeting ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		MeetingModel meeting_model = new MeetingModel();
		String Emp_Id = (String) data.get("Emp_Id");
		String Date = java.time.LocalDate.now().toString();
		try {
			Meeting = dbutil.selectMeetingByEmpId(connection,Emp_Id,Date);
			if(Meeting != null) {
				for(Map<String,Object> temp : Meeting) {
					List<Map<String, Object>> Emp ;
					Map<String, Object> ans = new HashMap<>();
					ArrayList<Integer> member = new ArrayList<>();
					meeting_model.setModel(temp);	
					try {
						Emp = dbutil.selectArray(connection,"meeting","Meet_id",Integer.toString(meeting_model.getMeetId()));
						for(Map<String,Object> temp2 : Emp) {
							member.add((Integer)temp2.get("Emp_id"));
						}
					} catch(SQLException e) {
						e.printStackTrace();
					}
					ans.put("Meet_Id", meeting_model.getMeetId());
					ans.put("Subject", meeting_model.getSubject());
					ans.put("Create_at", meeting_model.getCreate());
					ans.put("Creator", meeting_model.getCreator());
					ans.put("Date", meeting_model.getDate());
					ans.put("Start_at", meeting_model.getStart());
					ans.put("End_at", meeting_model.getEnd());
					ans.put("Room_Id", meeting_model.getRoomId());
					ans.put("Status", meeting_model.getStatus());
					ans.put("Description", meeting_model.getDescription());
					ans.put("Members",member);
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
				responseBodyStr.put("Message","success");
			}
			else {
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
				responseBodyStr.put("Message","success");
			}
		} catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",404);
			responseBodyStr.put("Message","Not found");
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Show_Selected_Employee_Meeting_Detail(Map<String, Object> data) 
			throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Meeting ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Emp = new HashMap<>();
		MeetingModel meeting_model = new MeetingModel();
		ArrayList<String> Target = (ArrayList)data.get("Value");
		String Date = java.time.LocalDate.now().toString();
		if(!Target.isEmpty()) {
			for(String temp1 : Target){
				try {
					List<Map<String, Object>> Emp_Meeting = new ArrayList<>();
					List<Map<String, Object>> member ;
//					Meeting = dbutil.selectEmpMeetingFromDate(connection,"Emp_id",temp1,Date);
					Meeting = dbutil.selectEmpMeetingFromDate(connection,"Emp_id",temp1);
					if(Meeting != null) {
						for(Map<String,Object> temp : Meeting) {
							Map<String, Object> meeting_info = new HashMap<>();
							ArrayList<Integer> member_in_meeting = new ArrayList<>();
							meeting_model.setModel(temp);
							member = dbutil.selectArray(connection,"meeting","Meet_id",Integer.toString(meeting_model.getMeetId()));
							for(Map<String,Object> temp2 : member) {
								member_in_meeting.add((Integer)temp2.get("Emp_id"));
							}
							meeting_info.put("Meet_Id", meeting_model.getMeetId());
							meeting_info.put("Subject", meeting_model.getSubject());
							meeting_info.put("Create_at", meeting_model.getCreate());
							meeting_info.put("Creator", meeting_model.getCreator());
							meeting_info.put("Date", meeting_model.getDate());
							meeting_info.put("Start_at", meeting_model.getStart());
							meeting_info.put("End_at", meeting_model.getEnd());
							meeting_info.put("Room_Id", meeting_model.getRoomId());
							meeting_info.put("Status", meeting_model.getStatus());
							meeting_info.put("Description", meeting_model.getDescription());
							meeting_info.put("Members",member_in_meeting);
							Emp_Meeting.add(meeting_info);
						}
					}
					Emp.put(temp1,Emp_Meeting);
				}catch (SQLException e) {
					e.printStackTrace();
					responseBodyStr.put("status",400);
					return responseBodyStr;
				}
			}
			responseBodyStr.put("data",Emp);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		System.out.println("responseBodyStr " + responseBodyStr);
		return responseBodyStr;
	}
	
	public Map<String, Object> Show_Create_Meeting(int id) 
			throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Meeting ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Meet = new HashMap<>();
		MeetingModel meeting_model = new MeetingModel();
		Meeting = dbutil.selectEmpMeetingFromCreate(connection,"Creator",Integer.toString(id));
		if(Meeting != null) {
			for(Map<String,Object> temp : Meeting) {
				List<Map<String, Object>> Emp_Meeting = new ArrayList<>();
				List<Map<String, Object>> member  ;
				Map<String, Object> meeting_info = new HashMap<>();
				ArrayList<Integer> member_in_meeting = new ArrayList<>();
				meeting_model.setModel(temp);
				try {
					member = dbutil.selectArray(connection,"meeting","Meet_id",Integer.toString(meeting_model.getMeetId()));
					for(Map<String,Object> temp2 : member) {
						member_in_meeting.add((Integer)temp2.get("Emp_id"));
					}
				} catch(SQLException e) {
					e.printStackTrace();
				}
				meeting_info.put("Meet_Id", meeting_model.getMeetId());
				meeting_info.put("Subject", meeting_model.getSubject());
				meeting_info.put("Create_at", meeting_model.getCreate());
				meeting_info.put("Creator", meeting_model.getCreator());
				meeting_info.put("Date", meeting_model.getDate());
				meeting_info.put("Start_at", meeting_model.getStart());
				meeting_info.put("End_at", meeting_model.getEnd());
				meeting_info.put("Room_Id", meeting_model.getRoomId());
				meeting_info.put("Status", meeting_model.getStatus());
				meeting_info.put("Description", meeting_model.getDescription());
				meeting_info.put("Members",member_in_meeting);
				Emp_Meeting.add(meeting_info);
				Meet.put(Integer.toString(meeting_model.getMeetId()),Emp_Meeting);
			}
			responseBodyStr.put("data",Meet);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		else {
			responseBodyStr.put("data",Meet);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","success");
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Check_Meeting_Room(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Meeting ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		MeetingModel meeting_model = new MeetingModel();
		String Room_Id = (String) data.get("Room_Id");
		String Date = (String) data.get("Date");
		String[] Raw_start = ((String) data.get("Start_at")).split("[:]");
		String[] Raw_End = ((String) data.get("End_at")).split("[:]");
		String Start = Raw_start[0]+":"+Raw_start[1]+":00";
		String End = Raw_End[0]+":"+Raw_End[1]+":00";
		try {
			Meeting = dbutil.CheckRoomMeeting(connection,Room_Id,Date,Start,End);
			if(Meeting != null) {
				for(Map<String,Object> temp : Meeting) {
					Map<String, Object> ans = new HashMap<>();
					meeting_model.setModel(temp);
					ans.put("Meet_Id", meeting_model.getMeetId());
					ans.put("Subject", meeting_model.getSubject());
					ans.put("Create_at", meeting_model.getCreate());
					ans.put("Creator", meeting_model.getCreator());
					ans.put("Date", meeting_model.getDate());
					ans.put("Start_at", meeting_model.getStart());
					ans.put("End_at", meeting_model.getEnd());
					ans.put("Room_Id", meeting_model.getRoomId());
					ans.put("Status", meeting_model.getStatus());
					ans.put("Description", meeting_model.getDescription());
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",false);
			}
			else {
				responseBodyStr.put("status",true);
			}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		return responseBodyStr;
	}
	
	public Map<String, Object> Check_Employee(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Meeting ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Emp_info ;
		MeetingModel meeting_model = new MeetingModel();
		EmployeeModel employee_model = new EmployeeModel();
		ArrayList<String> Target = (ArrayList)data.get("Value");
		String Date = (String) data.get("Date");
		String[] Raw_start = ((String) data.get("Start_at")).split("[:]");
		String[] Raw_End = ((String) data.get("End_at")).split("[:]");
		String Start = Raw_start[0]+":"+Raw_start[1]+":00";
		String End = Raw_End[0]+":"+Raw_End[1]+":00";
		if(!Target.isEmpty()) {
			for(String temp1 : Target){
				try {
					List<Map<String, Object>> Emp_Meeting = new ArrayList<>();
					Meeting = dbutil.CheckEmpMeetingDuration(connection,temp1,Date,Start,End);
					Emp_info = dbutil.select(connection, "employee", "Emp_id",temp1);
					employee_model.setModel(Emp_info);
					if(Meeting != null) {
						for(Map<String,Object> temp : Meeting) {
							Map<String, Object> meeting_info = new HashMap<>();
							meeting_model.setModel(temp);
							meeting_info.put("Meet_Id", meeting_model.getMeetId());
							meeting_info.put("Start_at", meeting_model.getStart());
							meeting_info.put("End_at", meeting_model.getEnd());
							meeting_info.put("Name",employee_model.getFirstname()+" "+employee_model.getLastname());
							Emp_Meeting.add(meeting_info);
						}
						result.put(Integer.toString(employee_model.getEmpid()),Emp_Meeting);
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(result.size() != 0) {
				responseBodyStr.put("data",result);
				responseBodyStr.put("status",false);
				return responseBodyStr;
			}
			else{
				responseBodyStr.put("status",true);
			}
		}
		else {
			responseBodyStr.put("status",true);
		}
		return responseBodyStr;
	}
	
	public void Add_Employee_To_Meeting(List<String> Target,int operator_id,MeetingModel meeting_model,String Option) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> mailmap = new HashMap<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> Room_info ;
		Map<String, Object> Log_detail ;
	    List<String> member = new ArrayList<>();
		EmployeeModel employee_model = new EmployeeModel();
		MeetingRoomModel room_model = new MeetingRoomModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		int index = 1;
		try {
			dbutil.Update_Log_Status(connection,"meeting_log","Meet_id",Integer.toString(meeting_model.getMeetId()));
			dbutil.Add_Meeting_log(connection,meeting_model.getMeetId(),Time,Option,operator_id,"1");
			Log_detail = dbutil.select2con(connection,"meeting_log",
					"Meet_id","Time",Integer.toString(meeting_model.getMeetId()),Time);
			for(String temp : Target){
				Employee_info = dbutil.select(connection,"employee","Emp_id",temp);
				employee_model.setModel(Employee_info);
				member.add(index+". "+employee_model.getFirstname()+" "+employee_model.getLastname());
				index++;
			}
			if(!Target.isEmpty()) {
				for(String temp : Target){
					dbutil.AddMeeting(connection,Integer.toString(meeting_model.getMeetId()),temp);
					dbutil.Add_Meeting_detail_log(connection,employee_model.getEmpid(),meeting_model.getMeetId(),Time,(Integer) Log_detail.get("Log_id"));
					Employee_info = dbutil.select(connection,"employee","Emp_id",temp);
					Room_info = dbutil.select(connection,"meeting_room","Room_Id",Integer.toString(meeting_model.getRoomId()));
					employee_model.setModel(Employee_info);
					room_model.setModel(Room_info);
					MailUtil2 mail = new MailUtil2();
					mailmap.put("to", employee_model.getFirstname()+" "+employee_model.getLastname());
					mailmap.put("Link",room_model.getDescription());
					mailmap.put("Date",meeting_model.getDate());
					mailmap.put("Start_at", meeting_model.getStart());
					mailmap.put("End_at", meeting_model.getEnd());
					mailmap.put("Detail", meeting_model.getDescription());
					mailmap.put("Members",member);
					mail.sendMail(employee_model.getEmail(),meeting_model.getSubject(),mailmap,ConfigConstants.MAIL_TEMPLATE_ADD_MEETING);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, Object> Check_Meeting(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Check_Room ;
		Map<String, Object> Check_Emp ;
		Check_Emp = Check_Employee(data);
		Check_Room = Check_Meeting_Room(data);
		if((Boolean)Check_Emp.get("status") && (Boolean)Check_Room.get("status")) {
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","All employees are free at this time.");
			responseBodyStr.put("Emp",Check_Emp);
			responseBodyStr.put("Room",Check_Room);
		}
		else {
			responseBodyStr.put("status",200);
			responseBodyStr.put("Emp",Check_Emp);
			responseBodyStr.put("Room",Check_Room);
			if(!Check_Emp.isEmpty()) {
				responseBodyStr.put("message","There are some employees who are busy during this time.");
			}
			if(!Check_Room.isEmpty()) {
				responseBodyStr.put("message","This room is busy during this time.");
			}
			
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_Meeting(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Check_Room ;
		Map<String, Object> Check_Emp ;
		Map<String, Object> ans ;
		Map<String, Object> Log_detail ;
		MeetingModel meeting_model = new MeetingModel();
		String Subject = (String) data.get("Subject");
		String Room_Id = (String) data.get("Room_Id");
		String Date = (String) data.get("Date");
		String[] Raw_start = ((String) data.get("Start_at")).split("[:]");
		String[] Raw_End = ((String) data.get("End_at")).split("[:]");
		String Start = Raw_start[0]+":"+Raw_start[1]+":00";
		String End = Raw_End[0]+":"+Raw_End[1]+":00";
		String Description = (String) data.get("Description");
		String Creator = Integer.toString(id);
		Check_Emp = Check_Employee(data);
		Check_Room = Check_Meeting_Room(data);
		ArrayList Target = (ArrayList)data.get("Value");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if((Boolean)Check_Emp.get("status") && (Boolean)Check_Room.get("status")) {
			try {
				ans = dbutil.AddMeeting(connection,Subject,Time,Creator,Room_Id,Date,Start,End,Description);
				meeting_model.setModel(ans);
				dbutil.Add_Meeting_Booking_Detail_log(connection,meeting_model.getMeetId(),Time);
				Log_detail = dbutil.select2con(connection,"meeting_room_booking_detail_log",
						"Meet_id","Time",Integer.toString(meeting_model.getMeetId()),Time);
				dbutil.Addlog(connection,"meeting_room_booking_log","Meet_id",Integer.toString(meeting_model.getMeetId()),
						Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				if(Target.size() != 0) {
					Add_Employee_To_Meeting(Target,id,meeting_model,"Add");
				}
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
				responseBodyStr.put("Emp_message","");
				responseBodyStr.put("Room_message","");
				responseBodyStr.put("status",200);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			if(!(Boolean)Check_Emp.get("status")) {
				responseBodyStr.put("Emp_message","There are some employees who are busy during this time.");
			}
			if(!(Boolean)Check_Room.get("status")) {
				responseBodyStr.put("Room_message","This room is busy during this time.");
			}
			responseBodyStr.put("status",400);
		}
		responseBodyStr.put("Emp",Check_Emp);
		responseBodyStr.put("Room",Check_Room);
		return responseBodyStr;
	}
	
	public Map<String, Object> Confirm_Add_Meeting(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Check_Room ;
		Map<String, Object> dummy = new HashMap<>();
		Map<String, Object> ans ;
		Map<String, Object> Log_detail ;
		MeetingModel meeting_model = new MeetingModel();
		String Subject = (String) data.get("Subject");
		String Room_Id = (String) data.get("Room_Id");
		String Date = (String) data.get("Date");
		String Start = (String) data.get("Start_at");
		String End = (String) data.get("End_at");
		String Description = (String) data.get("Description");
		String Creator = Integer.toString(id);
		Check_Room = Check_Meeting_Room(data);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		ArrayList Target = (ArrayList)data.get("Value");
		if((Boolean)Check_Room.get("status")) {
			try {
				ans = dbutil.AddMeeting(connection,Subject,Time,Creator,Room_Id,Date,Start,End,Description);
				meeting_model.setModel(ans);
				dbutil.Add_Meeting_Booking_Detail_log(connection,meeting_model.getMeetId(),Time);
				Log_detail = dbutil.select2con(connection,"meeting_room_booking_detail_log",
						"Meet_id","Time",Integer.toString(meeting_model.getMeetId()),Time);
				dbutil.Addlog(connection,"meeting_room_booking_log","Meet_id",Integer.toString(meeting_model.getMeetId()),
						Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				if(Target.size() != 0) {
					Add_Employee_To_Meeting(Target,id,meeting_model,"Add");
				}
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
				responseBodyStr.put("Room_message","This room is busy during this time.");
				responseBodyStr.put("Emp_message","");
				responseBodyStr.put("status",200);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
			else {
				responseBodyStr.put("Room_message","This room is busy during this time.");
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
				responseBodyStr.put("Emp_message","");
			responseBodyStr.put("status",400);
			}
		responseBodyStr.put("Room",Check_Room);
		responseBodyStr.put("Emp",dummy);
		return responseBodyStr;
	}
	
	public Map<String, Object> Delete_Meeting(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Log_detail ;
		String Meeting_Id = (String) data.get("Meeting_id");
		String emp_id = Integer.toString(id);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try {
			dbutil.Update_Log_Status(connection,"meeting_room_booking_log","Meet_id",Meeting_Id);
			dbutil.Update_Log_Status(connection,"meeting_log","Meet_id",Meeting_Id);
			dbutil.Add_Meeting_log(connection,Integer.valueOf(Meeting_Id),Time,"Delete",id,"1");
			dbutil.Addlog(connection,"meeting_room_booking_log","Meet_id",Meeting_Id,
					Time, Integer.toString(id),"1","Delete",0);
				dbutil.DeleteMeeting(connection,Time,emp_id,Meeting_Id);
				dbutil.Delete(connection,"Meeting","Meet_id",Meeting_Id);
			} catch(SQLException e) {
				e.printStackTrace();
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
			responseBodyStr.put("status",200);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		return responseBodyStr;
	}

	public Map<String, Object> Update_Meeting(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException, ParseException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Meeting_detail ;
		Map<String, Object> Check_Room = new HashMap<>();
		Map<String, Object> Check_Emp = new HashMap<>();
		Map<String, Object> Log_detail ;
		MeetingModel Meeting_model = new MeetingModel();
		String Meeting_id = (String) data.get("Meeting_id") ;
		String description = (String) data.get("Description");
		String Subject = (String) data.get("Subject");
		String Room_id = (String) data.get("Room_Id");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		ArrayList Target = (ArrayList)data.get("Value");
		try {
			Meeting_detail = dbutil.select(connection,"meeting_room_booking","Meet_id",Meeting_id);
			Meeting_model.setModel(Meeting_detail);
			if(Integer.toString(Meeting_model.getRoomId()).equals(Room_id)) {
				dbutil.Delete(connection,"Meeting","Meet_id",Meeting_id);
				Check_Emp = Check_Employee(data);
				if(!description.equals("")) {
					Meeting_model.setDescription(description);
				}
				if(!Subject.equals("")) {
					Meeting_model.setSubject(Subject);
				}
				if((Boolean)Check_Emp.get("status")) {
					dbutil.UpdateMeeting(connection,Meeting_model);
					if(Target.size() != 0) {
						Add_Employee_To_Meeting(Target,id,Meeting_model,"Update");
						responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
						responseBodyStr.put("status",200);
					}
				}
				else {
//					Add_Employee_To_Meeting(Target,id,Meeting_model,"Update");
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"");
					responseBodyStr.put("Emp_message","");
					responseBodyStr.put("Room_message","");
					if(!(Boolean)Check_Emp.get("status")) {
						responseBodyStr.put("Emp_message","There are some employees who are busy during this time.");
						responseBodyStr.put("status",400);
					}
				}
			}
			else {
				Check_Room = Check_Meeting_Room(data);
				if((Boolean)Check_Room.get("status")) {
					dbutil.Delete(connection,"Meeting","Meet_id",Meeting_id);
					Check_Emp = Check_Employee(data);
					if(!description.equals("")) {
						Meeting_model.setDescription(description);
					}
					if(!Subject.equals("")) {
						Meeting_model.setSubject(Subject);
					}
					if(!Room_id.equals("")) {
						Meeting_model.setRoomId(Integer.valueOf(Room_id));
					}
					if((Boolean)Check_Emp.get("status")) {
						dbutil.UpdateMeeting(connection,Meeting_model);
						if(Target.size() != 0) {
							Add_Employee_To_Meeting(Target,id,Meeting_model,"Update");
							responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
							responseBodyStr.put("status",200);
						}
					}
					else {
						responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"");
						responseBodyStr.put("Emp_message","");
						responseBodyStr.put("Room_message","");
						if(!(Boolean)Check_Emp.get("status")) {
							responseBodyStr.put("Emp_message","There are some employees who are busy during this time.");
						}
						if(!(Boolean)Check_Room.get("status")) {
							responseBodyStr.put("Room_message","This room is busy during this time.");
						}
						responseBodyStr.put("status",400);
					}
				}
				else {
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"");
					responseBodyStr.put("Emp_message","");
					responseBodyStr.put("Room_message","");
					if(!(Boolean)Check_Room.get("status")) {
						responseBodyStr.put("Room_message","This room is busy during this time.");
					}
					responseBodyStr.put("status",400);
				}
			}
			dbutil.Update_Log_Status(connection,"meeting_room_booking_log","Meet_id",Integer.toString(Meeting_model.getMeetId()));
			dbutil.Add_Meeting_Booking_Detail_log(connection,Meeting_model.getMeetId(),Time);
			Log_detail = dbutil.select2con(connection,"meeting_room_booking_detail_log",
					"Meet_id","Time",Integer.toString(Meeting_model.getMeetId()),Time);
			dbutil.Addlog(connection,"meeting_room_booking_log","Meet_id",Integer.toString(Meeting_model.getMeetId()),
					Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
		} catch(SQLException e) {
			e.printStackTrace();
		}

		responseBodyStr.put("Emp",Check_Emp);
		responseBodyStr.put("Room",Check_Room);
		return responseBodyStr;
	}

	public Map<String, Object> Confirm_Update_Meeting(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException, ParseException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Meeting_detail ;
		Map<String, Object> Check_Room = new HashMap<>();
		Map<String, Object> Log_detail ;
		MeetingModel Meeting_model = new MeetingModel();
		String Meeting_id = (String) data.get("Meeting_id") ;
		String description = (String) data.get("Description");
		String Subject = (String) data.get("Subject");
		String Room_id = (String) data.get("Room_Id");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		ArrayList Target = (ArrayList)data.get("Value");
		try {
			Meeting_detail = dbutil.select(connection,"meeting_room_booking","Meet_id",Meeting_id);
			Meeting_model.setModel(Meeting_detail);
			if(Integer.toString(Meeting_model.getRoomId()).equals(Room_id)) {
				dbutil.Delete(connection,"Meeting","Meet_id",Meeting_id);
				if(!description.equals("")) {
					Meeting_model.setDescription(description);
				}
				if(!Subject.equals("")) {
					Meeting_model.setSubject(Subject);
				}
				dbutil.UpdateMeeting(connection,Meeting_model);
				if(Target.size() != 0) {
					Add_Employee_To_Meeting(Target,id,Meeting_model,"Update");
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
				}
			}
			else {
				Check_Room = Check_Meeting_Room(data);
				if((Boolean)Check_Room.get("status")) {
					dbutil.Delete(connection,"Meeting","Meet_id",Meeting_id);
					if(!description.equals("")) {
						Meeting_model.setDescription(description);
					}
					if(!Subject.equals("")) {
						Meeting_model.setSubject(Subject);
					}
					if(!Room_id.equals("")) {
						Meeting_model.setRoomId(Integer.valueOf(Room_id));
					}
					dbutil.UpdateMeeting(connection,Meeting_model);
					if(Target.size() != 0) {
						Add_Employee_To_Meeting(Target,id,Meeting_model,"Update");
						responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
					}
				}
				else {
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"");
					responseBodyStr.put("Emp_message","");
					responseBodyStr.put("Room_message","This room is busy during this time.");
				}
			}
			dbutil.Update_Log_Status(connection,"meeting_room_booking_log","Meet_id",Integer.toString(Meeting_model.getMeetId()));
			dbutil.Add_Meeting_Booking_Detail_log(connection,Meeting_model.getMeetId(),Time);
			Log_detail = dbutil.select2con(connection,"meeting_room_booking_detail_log",
					"Meet_id","Time",Integer.toString(Meeting_model.getMeetId()),Time);
			dbutil.Addlog(connection,"meeting_room_booking_log","Meet_id",Integer.toString(Meeting_model.getMeetId()),
					Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
		} catch(SQLException e) {
			e.printStackTrace();
		}
		responseBodyStr.put("status",200);
		responseBodyStr.put("Room",Check_Room);
		return responseBodyStr;
	}


}
