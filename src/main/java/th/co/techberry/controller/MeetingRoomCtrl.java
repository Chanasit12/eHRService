package th.co.techberry.controller;

import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import com.mysql.jdbc.Connection;

import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import th.co.techberry.util.MailUtil2;

import java.util.*;
public class MeetingRoomCtrl {

	public Map<String, Object> Show_Meeting_Room() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Room = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		MeetingRoomModel room_model = new MeetingRoomModel();
		try {
			Room = dbutil.selectAll(connection,"meeting_room");
			if(Room != null) {
				for(Map<String,Object> temp : Room) {
					Map<String, Object> ans = new HashMap<String, Object>();
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
		List<Map<String, Object>> Meeting = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> meeting = new HashMap<String, Object>();
		MeetingModel meeting_model = new MeetingModel();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		String Date = java.time.LocalDate.now().toString();
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				meeting = new HashMap<String, Object>();
				try {
					Meeting = dbutil.select2conArray(connection,"meeting_room_booking","Room_id","Status",(String)Target.get(size),"1");
					if(Meeting != null) {
						for(Map<String,Object> temp : Meeting) {
							List<Map<String, Object>> Emp = new ArrayList<Map<String, Object>>();
							Map<String, Object> ans = new HashMap<String, Object>();
							ArrayList<Integer> member = new ArrayList<Integer>();
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
		List<Map<String, Object>> Meeting = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		MeetingModel meeting_model = new MeetingModel();
		String Emp_Id = (String) data.get("Emp_Id");
		String Date = java.time.LocalDate.now().toString();
		try {
			Meeting = dbutil.selectMeetingByEmpId(connection,Emp_Id,Date);
			if(Meeting != null) {
				for(Map<String,Object> temp : Meeting) {
					List<Map<String, Object>> Emp = new ArrayList<Map<String, Object>>();
					Map<String, Object> ans = new HashMap<String, Object>();
					ArrayList<Integer> member = new ArrayList<Integer>();
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
		List<Map<String, Object>> Meeting = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Emp = new HashMap<String, Object>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		MeetingModel meeting_model = new MeetingModel();
		EmployeeModel employee_model = new EmployeeModel();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
//		String Date = (String) data.get("Date");
		String Date = java.time.LocalDate.now().toString();
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					List<Map<String, Object>> Emp_Meeting = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> member = new ArrayList<Map<String, Object>>();
					Meeting = dbutil.selectEmpMeetingFromDate(connection,"Emp_id",(String)Target.get(size),Date);
					if(Meeting != null) {
						for(Map<String,Object> temp : Meeting) {
							Map<String, Object> meeting_info = new HashMap<String, Object>();
							ArrayList<Integer> member_in_meeting = new ArrayList<Integer>();
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
						}
					}
					Emp.put((String)Target.get(size),Emp_Meeting);
				}catch (SQLException e) {
					e.printStackTrace();
					responseBodyStr.put("status",400);
					return responseBodyStr;
				}
				size++;
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
		List<Map<String, Object>> Meeting = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Meet = new HashMap<String, Object>();
		MeetingModel meeting_model = new MeetingModel();
		Meeting = dbutil.selectEmpMeetingFromCreate(connection,"Creator",Integer.toString(id));
		if(Meeting != null) {
			for(Map<String,Object> temp : Meeting) {
				List<Map<String, Object>> Emp_Meeting = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> member = new ArrayList<Map<String, Object>>();
				Map<String, Object> meeting_info = new HashMap<String, Object>();
				ArrayList<Integer> member_in_meeting = new ArrayList<Integer>();
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
		List<Map<String, Object>> Meeting = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
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
					Map<String, Object> ans = new HashMap<String, Object>();
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
		List<Map<String, Object>> Meeting = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		MeetingModel meeting_model = new MeetingModel();
		EmployeeModel employee_model = new EmployeeModel();
		List Target = new ArrayList();
		Target = (ArrayList)data.get("Value");
		String Date = (String) data.get("Date");
		String[] Raw_start = ((String) data.get("Start_at")).split("[:]");
		String[] Raw_End = ((String) data.get("End_at")).split("[:]");
		String Start = Raw_start[0]+":"+Raw_start[1]+":00";
		String End = Raw_End[0]+":"+Raw_End[1]+":00";
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					List<Map<String, Object>> Emp_Meeting = new ArrayList<Map<String, Object>>();
					Meeting = dbutil.CheckEmpMeetingDuration(connection,(String)Target.get(size),Date,Start,End);
					Emp_info = dbutil.select(connection, "employee", "Emp_id",(String)Target.get(size));
					employee_model.setModel(Emp_info);
					if(Meeting != null) {
						for(Map<String,Object> temp : Meeting) {
							Map<String, Object> meeting_info = new HashMap<String, Object>();
							meeting_model.setModel(temp);	
							meeting_info.put("Meet_Id", meeting_model.getMeetId());
							meeting_info.put("Start_at", meeting_model.getStart());
							meeting_info.put("End_at", meeting_model.getEnd());
							meeting_info.put("Name",employee_model.getFirstname()+" "+employee_model.getLastname());
							Emp_Meeting.add(meeting_info);
						}
						System.out.println("Emp_id" + employee_model.getEmpid());
						result.put(Integer.toString(employee_model.getEmpid()),Emp_Meeting);
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
				size++;
			}
			if(result.size() != 0) {
				responseBodyStr.put("data",result);
				responseBodyStr.put("status",false);
				return responseBodyStr;
			}
			if(Target.size() == size) {
				responseBodyStr.put("status",true);
			}
		}
		else {
			responseBodyStr.put("status",true);
		}
		return responseBodyStr;
	}
	
	public void Add_Employee_To_Meeting(List Target,String id,Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> mailmap = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		Map<String, Object> Room_info = new HashMap<String, Object>();
	    List<String> member = new ArrayList<String>();
		EmployeeModel employee_model = new EmployeeModel();
		MeetingRoomModel room_model = new MeetingRoomModel();
		MeetingModel meeting_model = new MeetingModel();
		int size = 0;
		while(Target.size() > size) {
			    Employee_info = dbutil.select(connection,"employee","Emp_id",(String)Target.get(size));
			    employee_model.setModel(Employee_info);
			    member.add(size+1+". "+employee_model.getFirstname()+" "+employee_model.getLastname());
			    size++;
		}
		if(!Target.isEmpty()) {
			size = 0;
			while(Target.size() > size) {
				try {
						dbutil.AddMeeting(connection,id,(String)Target.get(size));
						Employee_info = dbutil.select(connection,"employee","Emp_id",(String)Target.get(size));
						Room_info = dbutil.select(connection,"meeting_room","Room_Id",(String)Target.get(size));
						employee_model.setModel(Employee_info);
						room_model.setModel(Room_info);
						meeting_model.setModel(data);
						MailUtil2 mail = new MailUtil2();
			            mailmap.put("to", employee_model.getFirstname()+" "+employee_model.getLastname());
			            mailmap.put("Link",room_model.getDescription());
			            mailmap.put("Date",meeting_model.getDate());
			            mailmap.put("Start_at", meeting_model.getStart());
			            mailmap.put("End_at", meeting_model.getEnd());
			            mailmap.put("Detail", meeting_model.getDescription());
			            mailmap.put("Members",member);
			             try {
								mail.sendMail(employee_model.getEmail(),meeting_model.getSubject(),mailmap,ConfigConstants.MAIL_TEMPLATE_ADD_MEETING);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}catch (SQLException e) {
						e.printStackTrace();
					}
					size++;
				}
			}
		
	}
	
	public Map<String, Object> Check_Meeting(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Check_Room = new HashMap<String, Object>();
		Map<String, Object> Check_Emp = new HashMap<String, Object>();
		Map<String, Object> ans = new HashMap<String, Object>();
		Check_Emp = Check_Employee(data);
		Check_Room = Check_Meeting_Room(data);
		System.out.println("Check Emp " + (Boolean)Check_Emp.get("status"));
		System.out.println("Check_Room " + (Boolean)Check_Room.get("status"));
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
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Check_Room = new HashMap<String, Object>();
		Map<String, Object> Check_Emp = new HashMap<String, Object>();
		Map<String, Object> ans = new HashMap<String, Object>();
		String Subject = (String) data.get("Subject");
		String Room_Id = (String) data.get("Room_Id");
		String Date = (String) data.get("Date");
		String[] Raw_start = ((String) data.get("Start_at")).split("[:]");
		String[] Raw_End = ((String) data.get("End_at")).split("[:]");
		String Start = Raw_start[0]+":"+Raw_start[1]+":00";
		String End = Raw_End[0]+":"+Raw_End[1]+":00";
		System.out.println("Start " + Start);
		System.out.println("End " + End);
		String Description = (String) data.get("Description");
		String Time = java.time.LocalDateTime.now().toString();
		String Create = Time.split("[.]")[0];
		String Creator = Integer.toString(id);
		Check_Emp = Check_Employee(data);
		Check_Room = Check_Meeting_Room(data);
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		if((Boolean)Check_Emp.get("status") && (Boolean)Check_Room.get("status")) {
			try {
				ans = dbutil.AddMeeting(connection,Subject,Create,Creator,Room_Id,Date,Start,End,Description);
				String meet_id = Integer.toString((Integer)ans.get("Meet_id"));
				if(Target.size() != 0) {
					Add_Employee_To_Meeting(Target,meet_id,ans);
				}
				responseBodyStr.put("status",200);
				responseBodyStr.put("message","Add Complete");
				responseBodyStr.put("Emp_message","");
				responseBodyStr.put("Room_message","");
				responseBodyStr.put("Emp",Check_Emp);
				responseBodyStr.put("Room",Check_Room);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			responseBodyStr.put("status",400);
			responseBodyStr.put("Emp",Check_Emp);
			responseBodyStr.put("Room",Check_Room);
			if(!(Boolean)Check_Emp.get("status")) {
				responseBodyStr.put("Emp_message","There are some employees who are busy during this time.");
			}
			if(!(Boolean)Check_Room.get("status")) {
				responseBodyStr.put("Room_message","This room is busy during this time.");
			}
				
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Confirm_Add_Meeting(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Check_Room = new HashMap<String, Object>();
		Map<String, Object> dummy = new HashMap<String, Object>();
		Map<String, Object> ans = new HashMap<String, Object>();
		String Subject = (String) data.get("Subject");
		String Room_Id = (String) data.get("Room_Id");
		String Date = (String) data.get("Date");
		String Start = (String) data.get("Start_at");
		String End = (String) data.get("End_at");
		String Description = (String) data.get("Description");
		String Time = java.time.LocalDateTime.now().toString();
		String Create = Time.split("[.]")[0];
		String Creator = Integer.toString(id);
		Check_Room = Check_Meeting_Room(data);
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		if((Boolean)Check_Room.get("status")) {
			try {
				ans = dbutil.AddMeeting(connection,Subject,Create,Creator,Room_Id,Date,Start,End,Description);
				String meet_id = Integer.toString((Integer)ans.get("Meet_id"));
				if(Target.size() != 0) {
					Add_Employee_To_Meeting(Target,meet_id,ans);
				}
				responseBodyStr.put("status",200);
				responseBodyStr.put("message","Add Complete");
				responseBodyStr.put("Room",Check_Room);
				responseBodyStr.put("Room_message","This room is busy during this time.");
				responseBodyStr.put("Emp",dummy);
				responseBodyStr.put("Emp_message","");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
			else {
				responseBodyStr.put("status",400);
				responseBodyStr.put("Room",Check_Room);
				responseBodyStr.put("Room_message","This room is busy during this time.");
				responseBodyStr.put("message","");
				responseBodyStr.put("Emp",dummy);
				responseBodyStr.put("Emp_message","");
			}
		return responseBodyStr;
	}
	
	public Map<String, Object> Delete_Meeting(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		String Meeting_Id = (String) data.get("Meeting_id");
		String emp_id = Integer.toString(id);
		String Time = java.time.LocalDateTime.now().toString();
		try {
				dbutil.DeleteMeeting(connection,Time,emp_id,Meeting_Id);
				dbutil.Delete(connection,"Meeting","Meet_id",Meeting_Id);
			} catch(SQLException e) {
				e.printStackTrace();
				responseBodyStr.put("status",400);
				responseBodyStr.put("message","Delete Fail");
				return responseBodyStr;
			}
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","Delete Complete");
		return responseBodyStr;
	}
	
	public Map<String, Object> Update_Meeting(Map<String, Object> data) throws SQLException, ClassNotFoundException, ParseException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> member = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Meeting_detail = new HashMap<String, Object>();
		Map<String, Object> Check_Room = new HashMap<String, Object>();
		Map<String, Object> Check_Emp = new HashMap<String, Object>();
		List<String> member_id = new ArrayList<String>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		MeetingModel Meeting_model = new MeetingModel(); 
		String Meeting_id = (String) data.get("Meeting_id") ;
		String description = (String) data.get("Description");
		String Subject = (String) data.get("Subject");
		String Room_id = (String) data.get("Room_Id");
		String Time = java.time.LocalDateTime.now().toString();
//		String Start = (String) data.get("Start_at");
//		String End = (String) data.get("End_at");
//		String Date = (String) data.get("Date");
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		try {
			Meeting_detail = dbutil.select(connection,"meeting_room_booking","Meet_id",Meeting_id);
			member = dbutil.selectArray(connection,"meeting","Meet_id",Meeting_id);
			Meeting_model.setModel(Meeting_detail);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		for(Map<String,Object> temp : member) {
			member_id.add(Integer.toString((Integer) temp.get("Emp_id")));
			System.out.println("member_id " + member_id);
		}
		if(Integer.toString(Meeting_model.getRoomId()).equals(Room_id)) {
			try {
				dbutil.Delete(connection,"Meeting","Meet_id",Meeting_id);
			} catch(SQLException e) {
				e.printStackTrace();
			}
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
//			if(!Start.equals("")) {
//				Date start = formatter1.parse(Start);
//				java.sql.Time timeValue = new java.sql.Time(start.getTime());
//				Meeting_model.setStart(timeValue);
//				System.out.println("time " + Meeting_model.getStart());
//	        }
//			if(!End.equals("")) {
//				Date end = formatter1.parse(End);
//				java.sql.Time timeValue = new java.sql.Time(end.getTime());
//				Meeting_model.setEnd(timeValue);
//				System.out.println("time " + Meeting_model.getEnd());
//	        }
//			if(!Date.equals("")) {
//				Date date = formatter2.parse(Date);
//				java.sql.Date sqlPackageDate = new java.sql.Date(date.getTime());
//				Meeting_model.setDate(sqlPackageDate);
//	        }
			if((Boolean)Check_Emp.get("status")) {
				try {
					dbutil.UpdateMeeting(connection,Meeting_model,Meeting_id);
					if(Target.size() != 0) {
						Add_Employee_To_Meeting(Target,Meeting_id,Meeting_detail);
						responseBodyStr.put("status",200);
						responseBodyStr.put("message","Update Complete");
					}	
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			else {
				Add_Employee_To_Meeting(member_id,Meeting_id,Meeting_detail);
				responseBodyStr.put("status",400);
				responseBodyStr.put("message","");
				responseBodyStr.put("Emp",Check_Emp);
				responseBodyStr.put("Emp_message","");
				responseBodyStr.put("Room",Check_Room);
				responseBodyStr.put("Room_message","");
				if(!(Boolean)Check_Emp.get("status")) {
					responseBodyStr.put("Emp",Check_Emp);
					responseBodyStr.put("Emp_message","There are some employees who are busy during this time.");
				}
//				if(!(Boolean)Check_Room.get("status")) {
//					responseBodyStr.put("Room",Check_Room);
//					responseBodyStr.put("Room_message","This room is busy during this time.");
//				}
			}
		}
		else {
			Check_Room = Check_Meeting_Room(data);
			if((Boolean)Check_Room.get("status")) {
				try {
					dbutil.Delete(connection,"Meeting","Meet_id",Meeting_id);
				} catch(SQLException e) {
					e.printStackTrace();
				}
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
//				if(!Start.equals("")) {
//					Date start = formatter1.parse(Start);
//					java.sql.Time timeValue = new java.sql.Time(start.getTime());
//					Meeting_model.setStart(timeValue);
//					System.out.println("time " + Meeting_model.getStart());
//		        }
//				if(!End.equals("")) {
//					Date end = formatter1.parse(End);
//					java.sql.Time timeValue = new java.sql.Time(end.getTime());
//					Meeting_model.setEnd(timeValue);
//					System.out.println("time " + Meeting_model.getEnd());
//		        }
//				if(!Date.equals("")) {
//					Date date = formatter2.parse(Date);
//					java.sql.Date sqlPackageDate = new java.sql.Date(date.getTime());
//					Meeting_model.setDate(sqlPackageDate);
//		        }
				if((Boolean)Check_Emp.get("status")) {
					try {
						dbutil.UpdateMeeting(connection,Meeting_model,Meeting_id);
						if(Target.size() != 0) {
							Add_Employee_To_Meeting(Target,Meeting_id,Meeting_detail);
							responseBodyStr.put("status",200);
							responseBodyStr.put("message","Update Complete");
						}	
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
				else {
					responseBodyStr.put("status",400);
					responseBodyStr.put("message","");
					responseBodyStr.put("Emp",Check_Emp);
					responseBodyStr.put("Emp_message","");
					responseBodyStr.put("Room",Check_Room);
					responseBodyStr.put("Room_message","");
					if(!(Boolean)Check_Emp.get("status")) {
						responseBodyStr.put("Emp",Check_Emp);
						responseBodyStr.put("Emp_message","There are some employees who are busy during this time.");
					}
					if(!(Boolean)Check_Room.get("status")) {
						responseBodyStr.put("Room",Check_Room);
						responseBodyStr.put("Room_message","This room is busy during this time.");
					}
				}
			}
			else {
				responseBodyStr.put("status",400);
				responseBodyStr.put("message","");
				responseBodyStr.put("Emp",Check_Emp);
				responseBodyStr.put("Emp_message","");
				responseBodyStr.put("Room",Check_Room);
				responseBodyStr.put("Room_message","");
				if(!(Boolean)Check_Room.get("status")) {
					responseBodyStr.put("Room",Check_Room);
					responseBodyStr.put("Room_message","This room is busy during this time.");
				}
			}
		}
		return responseBodyStr;
	}
	
	
	
	
}
