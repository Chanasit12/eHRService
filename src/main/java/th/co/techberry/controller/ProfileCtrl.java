package th.co.techberry.controller;

import java.sql.SQLException;
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


public class ProfileCtrl {
	
	public Map<String, Object> Profile(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		EmployeeModel employee_model = new EmployeeModel();
		TeamModel team_model = new TeamModel();
		EmployeeModel host_model = new EmployeeModel();
		List<Map<String, Object>> Team_info ;
		List<Map<String, Object>> team_datail = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> Role_info ;
		Map<String, Object> Position_info ;
		Map<String, Object> Company_info ;
		String Id = String.valueOf(id);
		try{
			Employee_info = (dbutil.select(connection,"employee","Emp_id",Id));
			if (Employee_info == null) {
				responseBodyStr.put("status", 404);
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
				responseBodyStr.put("message",ConfigConstants.ID_NOT_FOUND);
				return responseBodyStr;
			}
			else {
				employee_model.setModel(Employee_info);
				String roleid = Integer.toString(employee_model.getRold_id());
				String positionid = Integer.toString(employee_model.getPositionid());
				String companyid = Integer.toString(employee_model.getCompanyid());
				String data = new String(employee_model.getImg_Path());
				Role_info = (dbutil.select(connection,"user_role","Role_ID",roleid));
				Company_info = (dbutil.select(connection,"company","Comp_ID",companyid));
				Position_info = (dbutil.select(connection,"position","Position_ID",positionid));
				employee_model.setRole((String) Role_info.get("Role_Name"));
				employee_model.setCompany((String) Company_info.get("Company_Name"));
				employee_model.setPosition((String) Position_info.get("Position_Name"));
				employee_model.setModel(Employee_info);
				responseBodyStr.put("status", 200);
				responseBodyStr.put("Emp_id", employee_model.getEmpid());
				responseBodyStr.put("Title", employee_model.getTitle());
				responseBodyStr.put("Firstname", employee_model.getFirstname());
				responseBodyStr.put("Lastname", employee_model.getLastname());
				responseBodyStr.put("BirthDate", employee_model.getBirthdate());
				responseBodyStr.put("Gender", employee_model.getGender());
				responseBodyStr.put("Phone", employee_model.getPhone());
				responseBodyStr.put("Id", employee_model.getId());
				responseBodyStr.put("Email", employee_model.getEmail());
				responseBodyStr.put("Address", employee_model.getAddress());
				responseBodyStr.put("Role", employee_model.getRole());
				responseBodyStr.put("Img", data);
				responseBodyStr.put("Position", employee_model.getPosition());
				responseBodyStr.put("Company", employee_model.getCompany());
				Team_info = dbutil.selectArray(connection,"emp_team","Emp_id",String.valueOf(employee_model.getEmpid()));
				if(Team_info != null) {
					int index = 1;
					for(Map<String, Object> Team_temp : Team_info){
						Map<String, Object> team_ans = new HashMap<>();
						Map<String, Object> Team_data ;
						Map<String, Object> Host_info ;
						Team_data = dbutil.select(connection,"team","Team_id",String.valueOf((Integer) Team_temp.get("Team_id")));
						team_model.setModel(Team_data);
						Host_info = dbutil.select(connection,"employee","Emp_id",Integer.toString(team_model.getHost()));
						host_model.setModel(Host_info);
						team_ans.put("TeamName", team_model.getTeamName());
						team_ans.put("HostName", host_model.getFirstname()+" "+host_model.getLastname());
						team_ans.put("HostImg",host_model.getImg_Path());
						team_ans.put("index", index);
						team_datail.add(team_ans);
						index++;
					}
					responseBodyStr.put("Team_Info", team_datail);
				}
				else {
					responseBodyStr.put("Team_Info", team_datail);
				}
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> UpdateProfile(Map<String, Object> data,int id)
			throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		EmployeeModel employee_model = new EmployeeModel();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> Log_detail ;
		String Id = String.valueOf(id);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try{
			Employee_info = (dbutil.select(connection,"employee","Emp_id",Id));
			employee_model.setModel(Employee_info);
			if(!data.get("Email").equals("")) {
				employee_model.setEmail((String) data.get("Email"));
			}
			if(!data.get("Address").equals("")) {
				String Address = (String) data.get("Address");
				Address = Address.replace("\"","\\\"");
				Address = Address.replace("\'","\\\'");
				Address = Address.replace("\\","\\\\");
				employee_model.setAddress(Address);
			}
			if(!data.get("Lastname").equals("")) {
				employee_model.setLastname((String) data.get("Lastname"));
			}
			if(!data.get("Title").equals("")) {
				employee_model.setTitle((String) data.get("Title"));
			}
			if(!data.get("Gender").equals("")) {
				employee_model.setGender((String) data.get("Gender"));
			}
			if(!data.get("Firstname").equals("")) {
				employee_model.setFirstname((String) data.get("Firstname"));
			}
			if(!data.get("Phone").equals("")) {
				employee_model.setPhone((String) data.get("Phone"));
			}
			if(!data.get("BirthDate").equals("")) {
				employee_model.setBirth((String) data.get("BirthDate"));
			}
			if(!data.get("Img").equals("")) {
				System.out.println("Img "+data.get("Img"));
				employee_model.setStrImg((String) data.get("Img"));
			}
			dbutil.UpdateProfile(connection,employee_model);
			dbutil.Update_Log_Status(connection,"employee_log","Emp_id",Integer.toString(employee_model.getEmpid()));
			dbutil.Employee_Detail_log(connection,employee_model.getEmpid(),Time);
			Log_detail = dbutil.select2con(connection,"employee_detail_log",
					"Emp_id","Time",Integer.toString(employee_model.getEmpid()),Time);
			dbutil.Addlog(connection,"employee_log","Emp_id",Integer.toString(employee_model.getEmpid()),
					Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
			responseBodyStr.put("status", 200);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "Change Profile Success");
		} catch(SQLException e){
			e.printStackTrace();
			responseBodyStr.put("status", 400);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "Change Profile Fail");
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> UpdateProfileById(Map<String, Object> data,int id)
			throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> Log_detail ;
		EmployeeModel employee_model = new EmployeeModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		String Emp_Id = (String) data.get("Id");
		try{
			Employee_info = (dbutil.select(connection,"employee","Emp_id",Emp_Id));
			employee_model.setModel(Employee_info);
			if(!data.get("Email").equals("")) {
				employee_model.setEmail((String) data.get("Email"));
			}
			if(!data.get("Address").equals("")) {
				String Address = (String) data.get("Address");
				Address = Address.replace("\"","\\\"");
				Address = Address.replace("\'","\\\'");
				Address = Address.replace("\\","\\\\");
				employee_model.setAddress(Address);
			}
			if(!data.get("Lastname").equals("")) {
				employee_model.setLastname((String) data.get("Lastname"));
			}
			if(!data.get("Title").equals("")) {
				employee_model.setTitle((String) data.get("Title"));
			}
			if(!data.get("Gender").equals("")) {
				employee_model.setGender((String) data.get("Gender"));
			}
			if(!data.get("Firstname").equals("")) {
				employee_model.setFirstname((String) data.get("Firstname"));
			}
			if(!data.get("Phone").equals("")) {
				employee_model.setPhone((String) data.get("Phone"));
			}
			if(!data.get("BirthDate").equals("")) {
				employee_model.setBirth((String) data.get("BirthDate"));
			}
			if(!data.get("Img").equals("")) {
				employee_model.setStrImg((String) data.get("Img"));
			}
			if(!data.get("Company").equals("")) {
				employee_model.setCompanyid(Integer.valueOf((String) data.get("Company")));
			}
			if(!data.get("Position").equals("")) {
				employee_model.setPositionid(Integer.valueOf((String) data.get("Position")));
			}
			dbutil.UpdateProfile(connection,employee_model);
			dbutil.Update_Log_Status(connection,"employee_log","Emp_id",Integer.toString(employee_model.getEmpid()));
			dbutil.Employee_Detail_log(connection,employee_model.getEmpid(),Time);
			Log_detail = dbutil.select2con(connection,"employee_detail_log",
					"Emp_id","Time",Integer.toString(employee_model.getEmpid()),Time);
			dbutil.Addlog(connection,"employee_log","Emp_id",Integer.toString(employee_model.getEmpid()),
					Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "Change Profile Success");
		} catch (SQLException e){
			e.printStackTrace();
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "Change Profile Fail");
		}
		responseBodyStr.put("status", 200);
		return responseBodyStr;
	}
	
	public Map<String, Object> GetProfileById(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		TeamModel team_model = new TeamModel();
		EmployeeModel host_model = new EmployeeModel();
		List<Map<String, Object>> Team_info ;
		List<Map<String, Object>> team_datail = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Employee_info ;
		Map<String, Object> Login_info ;
		Map<String, Object> Role_info ;
		Map<String, Object> Position_info ;
		Map<String, Object> Company_info ;
		String Id = (String) data.get("Id");
		try{
			Employee_info = (dbutil.select(connection,"employee","Emp_id",Id));
			if (Employee_info == null) {
				responseBodyStr.put("status", 404);
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
				responseBodyStr.put("message",ConfigConstants.ID_NOT_FOUND);
			}
			else {
				employee_model.setModel(Employee_info);
				int id = employee_model.getId();
				Login_info = dbutil.select(connection, "login", "Id", Integer.toString(id));
				login_model.setModel(Login_info);
				String roleid = Integer.toString(employee_model.getRold_id());
				String positionid = Integer.toString(employee_model.getPositionid());
				String companyid = Integer.toString(employee_model.getCompanyid());
				Role_info = (dbutil.select(connection,"user_role","Role_ID",roleid));
				Company_info = (dbutil.select(connection,"company","Comp_ID",companyid));
				Position_info = (dbutil.select(connection,"position","Position_ID",positionid));
				employee_model.setRole((String) Role_info.get("Role_Name"));
				employee_model.setCompany((String) Company_info.get("Company_Name"));
				employee_model.setPosition((String) Position_info.get("Position_Name"));
				String Img = new String(employee_model.getImg_Path());
				responseBodyStr.put("status", 200);
				responseBodyStr.put("Emp_id", employee_model.getEmpid());
				responseBodyStr.put("Title", employee_model.getTitle());
				responseBodyStr.put("Firstname", employee_model.getFirstname());
				responseBodyStr.put("Lastname", employee_model.getLastname());
				responseBodyStr.put("BirthDate", employee_model.getBirthdate());
				responseBodyStr.put("Gender", employee_model.getGender());
				responseBodyStr.put("Phone", employee_model.getPhone());
				responseBodyStr.put("Id", employee_model.getId());
				responseBodyStr.put("Email", employee_model.getEmail());
				responseBodyStr.put("Address", employee_model.getAddress());
				responseBodyStr.put("Role", employee_model.getRole());
				responseBodyStr.put("Img", Img);
				responseBodyStr.put("Position", employee_model.getPosition());
				responseBodyStr.put("Company", employee_model.getCompany());
				responseBodyStr.put("Username", login_model.getUsername());
				Team_info = dbutil.selectArray(connection,"emp_team","Emp_id",String.valueOf(employee_model.getEmpid()));
				if(Team_info != null) {
					int index = 1;
					for(Map<String, Object> Team_temp : Team_info){
						Map<String, Object> team_ans = new HashMap<>();
						Map<String, Object> Team_data ;
						Map<String, Object> Host_info ;
						Team_data = dbutil.select(connection,"team","Team_id",String.valueOf((Integer) Team_temp.get("Team_id")));
						team_model.setModel(Team_data);
						Host_info = dbutil.select(connection,"employee","Emp_id",Integer.toString(team_model.getHost()));
						host_model.setModel(Host_info);
						String Img2 = new String(host_model.getImg_Path());
						team_ans.put("TeamName", team_model.getTeamName());
						team_ans.put("HostName", host_model.getFirstname()+" "+host_model.getLastname());
						team_ans.put("HostImg",Img2);
						team_ans.put("index", index);
						team_datail.add(team_ans);
						index++;
					}
					responseBodyStr.put("Team_Info", team_datail);
				}
				else {
					responseBodyStr.put("Team_Info", team_datail);
				}
				responseBodyStr.put("status", 200);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}
	
}
