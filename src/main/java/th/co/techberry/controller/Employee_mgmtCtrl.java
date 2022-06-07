package th.co.techberry.controller;

import java.sql.SQLException;
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
import th.co.techberry.util.Encryption;
import th.co.techberry.util.*;
import th.co.techberry.util.RandomUtil;

public class Employee_mgmtCtrl {

	public Map<String, Object> Employee_mgmt() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Employee ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Role_info ;
		Map<String, Object> Position_info ;
		Map<String, Object> Company_info ;
		Map<String, Object> Login_info ;
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		try {
			Employee = dbutil.selectAll(connection,"employee");
			if(Employee != null) {
				for(Map<String, Object> temp : Employee){
					employee_model.setModel(temp);
					Login_info = dbutil.select(connection, "login", "Id", Integer.toString(employee_model.getId()));
					login_model.setModel(Login_info);
					Map<String, Object> ans = new HashMap<>();
					String roleid = Integer.toString(employee_model.getRold_id());
					String positionid = Integer.toString(employee_model.getPositionid());
					String companyid = Integer.toString(employee_model.getCompanyid());
					Role_info = dbutil.select(connection,"user_role","Role_ID",roleid);
					Company_info = (dbutil.select(connection,"company","Comp_ID",companyid));
					Position_info = (dbutil.select(connection,"position","Position_ID",positionid));
					employee_model.setRole((String) Role_info.get("Role_Name"));
					employee_model.setCompany((String) Company_info.get("Company_Name"));
					employee_model.setPosition((String) Position_info.get("Position_Name"));
					String Img = new String(employee_model.getImg_Path());
					ans.put("Emp_id", employee_model.getEmpid());
					ans.put("Title", employee_model.getTitle());
					ans.put("Firstname", employee_model.getFirstname());
					ans.put("Lastname", employee_model.getLastname());
					ans.put("BirthDate", employee_model.getBirthdate());
					ans.put("Gender", employee_model.getGender());
					ans.put("Phone", employee_model.getPhone());
					ans.put("Id", employee_model.getId());
					ans.put("Email", employee_model.getEmail());
					ans.put("Address", employee_model.getAddress());
					ans.put("Role", employee_model.getRole());
					ans.put("Img", Img);
					ans.put("Position", employee_model.getPosition());
					ans.put("Company", employee_model.getCompany());
					ans.put("Active_Status", login_model.getActivestatus());
					res.add(ans);
				}
				responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
			}
			else {
				responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
			responseBodyStr.put("data",res);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}
	
	public Map<String, Object> Employee_mgmt_detail(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Employee ;
		Map<String, Object> Login_info ;
		Map<String, Object> Role_info ;
		Map<String, Object> Position_info ;
		Map<String, Object> Company_info ;
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		try {
			Employee = dbutil.select(connection, "employee", "Emp_id", (String) data.get("Emp_id"));
			int id = (Integer) Employee.get("Id");
			Login_info = dbutil.select(connection, "login", "Id", Integer.toString(id));
			employee_model.setModel(Employee);
			login_model.setModel(Login_info);
			String roleid = Integer.toString(employee_model.getRold_id());
			String positionid = Integer.toString(employee_model.getPositionid());
			String companyid = Integer.toString(employee_model.getCompanyid());
			Role_info = dbutil.select(connection,"user_role","Role_ID",roleid);
			Company_info = (dbutil.select(connection,"company","Comp_ID",companyid));
			Position_info = (dbutil.select(connection,"position","Position_ID",positionid));
			employee_model.setRole((String) Role_info.get("Role_Name"));
			employee_model.setCompany((String) Company_info.get("Company_Name"));
			employee_model.setPosition((String) Position_info.get("Position_Name"));
			String Img = new String(employee_model.getImg_Path());
			responseBodyStr.put("Emp_id", employee_model.getEmpid());
			responseBodyStr.put("Title", employee_model.getTitle());
			responseBodyStr.put("Firstname", employee_model.getFirstname());
			responseBodyStr.put("Lastname", employee_model.getLastname());
			responseBodyStr.put("BirthDate", employee_model.getBirthdate());
			responseBodyStr.put("Gender", employee_model.getGender());
			responseBodyStr.put("Phone", employee_model.getPhone());
			responseBodyStr.put("Email", employee_model.getEmail());
			responseBodyStr.put("Address", employee_model.getAddress());
			responseBodyStr.put("Role", employee_model.getRole());
			responseBodyStr.put("Img", Img);
			responseBodyStr.put("Position", employee_model.getPosition());
			responseBodyStr.put("Company", employee_model.getCompany());
			responseBodyStr.put("Username", login_model.getUsername());
			responseBodyStr.put("Active_Status", login_model.getActivestatus());
			responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
			}
		catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
		}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_Employee(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_Day ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> mailmap = new HashMap<>();
		Map<String, Object> Login_info ;
		Map<String, Object> Employee_info ;
		Map<String, Object> Email ;
		Map<String, Object> Log_detail ;
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		LeaveTypeModel leave_model = new LeaveTypeModel();
		LeaveModel req_model = new LeaveModel();
		LeaveCountModel count_model = new LeaveCountModel();
		String Title = (String) data.get("Title");
		String Firstname = (String) data.get("Firstname");
		String Lastname = (String) data.get("Lastname");
		String BirthDate = (String) data.get("BirthDate");
		String Gender = (String) data.get("Gender");
		String Phone = (String) data.get("Phone");
		String Role_ID = (String) data.get("Role");
		String Address = (String) data.get("Address");
		String Img = (String) data.get("Img");
		String Position_ID = (String) data.get("Position_ID");
		String Comp_ID = (String) data.get("Comp_ID");
		String Username = (String) data.get("Username");
		char[] password = RandomUtil.generatePassword(15);
        String Password = String.valueOf(password);
		String encryptedPassword = Encryption.encryptPassword(Username,Password);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try {
			Email = dbutil.select(connection,"login","Username",Username);
			if(Email != null){
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"This Email Has Already Used");
				responseBodyStr.put("status",400);
			}
			else{
				dbutil.AddLogin(connection,Username,encryptedPassword);
				Login_info = dbutil.select(connection,"login","Username",Username);
				login_model.setModel(Login_info);
				dbutil.Login_Detail_log(connection,login_model.getId(),Time);
				Log_detail = dbutil.select2con(connection,"login_detail_log",
						"Login_id","Time",Integer.toString(login_model.getId()),Time);
				dbutil.Addlog(connection,"login_log","Login_id",Integer.toString(login_model.getId()),
						Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				dbutil.AddEmployee(connection,Title,Firstname,Lastname,BirthDate,Gender,Phone,login_model.getId(),Username,
						Address,Role_ID,Img,Position_ID,Comp_ID);
				String login_id = Integer.toString(login_model.getId());
				Employee_info = dbutil.select(connection,"employee","Id",login_id);
				employee_model.setModel(Employee_info);
				dbutil.Employee_Detail_log(connection,employee_model.getEmpid(),Time);
				Log_detail = dbutil.select2con(connection,"employee_detail_log",
						"Emp_id","Time",Integer.toString(employee_model.getEmpid()),Time);
				dbutil.Addlog(connection,"employee_log","Emp_id",Integer.toString(employee_model.getEmpid()),
						Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				if(Employee_info != null) {
					Leave_Day = dbutil.selectAll(connection,"leave_type");
					if(Leave_Day != null) {
						for(Map<String, Object> temp : Leave_Day){
							leave_model.setModel(temp);
							String Emp_id = String.valueOf(employee_model.getEmpid());
							dbutil.AddLeave_Day(connection,Emp_id,leave_model);
							count_model.setEmpid(employee_model.getEmpid());
							count_model.setTypeId(leave_model.getId());
							req_model.setAmount(leave_model.getNum_per_year());
							dbutil.Leave_count_log(connection,count_model,req_model,Time,"Add",id);
						}
					}
					MailUtil2 mail = new MailUtil2();
					mailmap.put("to", employee_model.getFirstname()+" "+employee_model.getLastname());
					mailmap.put("username", Username);
					mailmap.put("password", Password);
					mail.sendMail(employee_model.getEmail(),ConfigConstants.MAIL_SUBJECT_ADD_EMPLOYEE,mailmap,ConfigConstants.MAIL_TEMPLATE_ADD_EMPLOYEE);
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
					responseBodyStr.put("status",200);
				}
				else {
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
					responseBodyStr.put("status",400);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Update_Employee(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Login_info ;
		Map<String, Object> Employee_info ;
		Map<String, Object> Log_detail ;
		Map<String, Object> Check_username ;
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try {
			Employee_info = dbutil.select(connection,"employee","Emp_id",(String)data.get("Emp_id"));
			employee_model.setModel(Employee_info);
			Login_info = dbutil.select(connection,"login","Id",Integer.toString(employee_model.getId()));
			login_model.setModel(Login_info);
	        if(!data.get("Title").equals("")) {
	        	employee_model.setTitle((String)data.get("Title"));
	        }
	        if(!data.get("Firstname").equals("")) {
	        	employee_model.setFirstname((String)data.get("Firstname"));
	        }
	        if(!data.get("Lastname").equals("")) {
	        	employee_model.setLastname((String)data.get("Lastname"));
	        }
	        if(!data.get("BirthDate").equals("")) {
		        employee_model.setBirth((String)data.get("BirthDate"));
	        }
	        if(!data.get("Gender").equals("")) {
		        employee_model.setGender((String)data.get("Gender"));
	        }
	        if(!data.get("Phone").equals("")) {
		        employee_model.setPhone((String)data.get("Phone"));
	        }
	        if(!data.get("Role").equals("")) {
		        employee_model.setRole(String.valueOf(data.get("Role")));
				dbutil.UpdateRole(connection,employee_model);
	        }
	        if(!data.get("Address").equals("")) {
		        employee_model.setAddress((String)data.get("Address"));
	        }
	        if(!data.get("Img").equals("")) {
		        employee_model.setStrImg((String)data.get("Img"));
	        }
	        if(!data.get("Position_ID").equals("")) {
	        	String Position = (String) data.get("Position_ID");
		        employee_model.setPositionid(Integer.valueOf(Position));
	        }
	        if(!data.get("Comp_ID").equals("")) {
	        	String Company = (String) data.get("Comp_ID");
		        employee_model.setCompanyid(Integer.valueOf(Company));
	        }
			dbutil.UpdateProfile(connection,employee_model);
	        if(!data.get("Username").equals("")) {
				Check_username = dbutil.select(connection,"login","Username",(String)data.get("Username"));
				if(Check_username == null){
					String login_id = Integer.toString(employee_model.getId());
					login_model.setUsername((String)data.get("Username"));
					dbutil.UpdateUsername(connection,login_model.getUsername(),login_id);
					dbutil.Login_Detail_log(connection,login_model.getId(),Time);
					dbutil.Update_Log_Status(connection,"login_log","Login_id",Integer.toString(login_model.getId()));
					Log_detail = dbutil.select2con(connection,"login_detail_log",
							"Login_id","Time",Integer.toString(login_model.getId()),Time);
					dbutil.Addlog(connection,"login_log","Login_id",Integer.toString(login_model.getId()),
							Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
				}
				else{
					responseBodyStr.put("status",400);
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"This Username Has Already Used");
					return responseBodyStr;
				}
	        }
			dbutil.Update_Log_Status(connection,"employee_log","Emp_id",Integer.toString(employee_model.getEmpid()));
			dbutil.Employee_Detail_log(connection,employee_model.getEmpid(),Time);
			Log_detail = dbutil.select2con(connection,"employee_detail_log",
					"Emp_id","Time",Integer.toString(employee_model.getEmpid()),Time);
			dbutil.Addlog(connection,"employee_log","Emp_id",Integer.toString(employee_model.getEmpid()),
					Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}
	
	public Map<String, Object> Delete_Employee(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> Login_info ;
		Map<String, Object> Log_detail ;
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		ArrayList<String> Target = (ArrayList)data.get("Value");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(!Target.isEmpty()) {
				for(String temp : Target){
					try {
						Employee_info = dbutil.select(connection,"employee","Emp_id",temp);
						employee_model.setModel(Employee_info);
						String login_id = Integer.toString(employee_model.getId());
						dbutil.DeleteEmployee(connection,login_id);
						Login_info = dbutil.select(connection,"login","Id",login_id);
						login_model.setModel(Login_info);
						dbutil.Login_Detail_log(connection,login_model.getId(),Time);
						Log_detail = dbutil.select2con(connection,"login_detail_log",
								"Login_id","Time",Integer.toString(login_model.getId()),Time);
						dbutil.Addlog(connection,"login_log","Login_id",Integer.toString(login_model.getId()),
								Time, Integer.toString(id),"1","Delete",(Integer)Log_detail.get("Log_id"));
					}catch (SQLException e) {
						e.printStackTrace();
						responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
					}
				}
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		}
		else {
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}
	
	public Map<String, Object> Active_Employee(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> Login_info ;
		Map<String, Object> Log_detail ;
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		ArrayList<String> Target = (ArrayList)data.get("Value");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(!Target.isEmpty()) {
			for(String temp : Target){
				try {
					Employee_info = dbutil.select(connection,"employee","Emp_id",temp);
					employee_model.setModel(Employee_info);
					String login_id = Integer.toString(employee_model.getId());
					dbutil.ActiveEmployee(connection,login_id);
					Login_info = dbutil.select(connection,"login","Id",login_id);
					login_model.setModel(Login_info);
					dbutil.Login_Detail_log(connection,login_model.getId(),Time);
					Log_detail = dbutil.select2con(connection,"login_detail_log",
							"Login_id","Time",Integer.toString(login_model.getId()),Time);
					dbutil.Addlog(connection,"login_log","Login_id",Integer.toString(login_model.getId()),
							Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				}catch (SQLException e) {
					e.printStackTrace();
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
				}
			}
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		}
		else {
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}

}
