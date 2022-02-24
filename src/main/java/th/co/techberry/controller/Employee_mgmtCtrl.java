package th.co.techberry.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;

import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import th.co.techberry.util.Encryption;
import th.co.techberry.util.MailUtil2;
import th.co.techberry.util.RandomUtil;

public class Employee_mgmtCtrl {

	public Map<String, Object> Employee_mgmt() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Employee = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Role_info = new HashMap<String, Object>();
		Map<String, Object> Position_info = new HashMap<String, Object>();
		Map<String, Object> Company_info = new HashMap<String, Object>();
		Map<String, Object> Login_info = new HashMap<String, Object>();
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		try {
			Employee = dbutil.selectAll(connection,"employee");
			int size = 0;
			if(Employee != null) {
				while(size<Employee.size()) {
					employee_model.setModel(Employee.get(size));
					Login_info = dbutil.select(connection, "login", "Id", Integer.toString(employee_model.getId()));
					login_model.setModel(Login_info);
					Map<String, Object> ans = new HashMap<String, Object>();
					String roleid = Integer.toString(employee_model.getRold_id());
					String positionid = Integer.toString(employee_model.getPositionid());
					String companyid = Integer.toString(employee_model.getCompanyid());
					Role_info = dbutil.select(connection,"user_role","Role_ID",roleid);
					Company_info = (dbutil.select(connection,"company","Comp_ID",positionid));
					Position_info = (dbutil.select(connection,"position","Position_ID",companyid));
					employee_model.setRole((String) Role_info.get("Role_Name"));
					employee_model.setCompany((String) Company_info.get("Company_Name"));
					employee_model.setPosition((String) Position_info.get("Position_Name"));
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
					ans.put("Img", employee_model.getImg_Path());
					ans.put("Position", employee_model.getPosition());
					ans.put("Company", employee_model.getCompany());
					ans.put("Active_Status", login_model.getActivestatus());
					res.add(ans);
					size++;
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
	
	public Map<String, Object> Employee_mgmt_detail(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Employee = new HashMap<String, Object>();
		Map<String, Object> Login_info = new HashMap<String, Object>();
		Map<String, Object> Role_info = new HashMap<String, Object>();
		Map<String, Object> Position_info = new HashMap<String, Object>();
		Map<String, Object> Company_info = new HashMap<String, Object>();
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
			System.out.println("positionid " + positionid);
			System.out.println("companyid " + companyid);
			Company_info = (dbutil.select(connection,"company","Comp_ID",companyid));
			Position_info = (dbutil.select(connection,"position","Position_ID",positionid));
			employee_model.setRole((String) Role_info.get("Role_Name"));
			employee_model.setCompany((String) Company_info.get("Company_Name"));
			employee_model.setPosition((String) Position_info.get("Position_Name"));
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
			responseBodyStr.put("Img", employee_model.getImg_Path());
			responseBodyStr.put("Position", employee_model.getPosition());
			responseBodyStr.put("Company", employee_model.getCompany());
			responseBodyStr.put("Username", login_model.getUsername());
			responseBodyStr.put("Active_Status", login_model.getActivestatus());
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message","success");
			}
		catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status",400);
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_Employee(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_Day = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> mailmap = new HashMap<String, Object>();
		Map<String, Object> Login_info = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
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
		try {
			dbutil.AddLogin(connection,Username,encryptedPassword);
			Login_info = dbutil.select(connection,"login","Username",Username);
			login_model.setModel(Login_info);
			System.out.println("Address " + Address);
			dbutil.AddEmployee(connection,Title,Firstname,Lastname,BirthDate,Gender,Phone,login_model.getId(),Username,Address,Role_ID,Img,Position_ID,Comp_ID);
			String login_id = Integer.toString(login_model.getId());
			Employee_info = dbutil.select(connection,"employee","Id",login_id);
			if(Employee_info != null) {
				 employee_model.setModel(Employee_info);
				 try {
					 Leave_Day = dbutil.selectAll(connection,"leave_type");
					 int size = 0;
					 if(Leave_Day != null) {
						 while(Leave_Day.size() > size) {
							 String Emp_id = String.valueOf(employee_model.getEmpid());
							 String Type_id = String.valueOf((Integer) Leave_Day.get(size).get("Type_ID"));
							 String Num_per_year = String.valueOf((Integer) Leave_Day.get(size).get("Num_per_year"));
							 try {
								 dbutil.AddLeave_Day(connection,Emp_id,Type_id,Num_per_year,"0");
							 }catch(SQLException e) {
								 e.printStackTrace();
							 }
							 size++;
						 }
					 }
				 } catch(SQLException e) {
					 e.printStackTrace();
				 }
				 MailUtil2 mail = new MailUtil2();
	             mailmap.put("to", employee_model.getFirstname()+" "+employee_model.getLastname());
	             mailmap.put("username", Username);
	             mailmap.put("password", Password);
	             try {
						mail.sendMail(employee_model.getEmail(),ConfigConstants.MAIL_SUBJECT_ADD_EMPLOYEE,mailmap,ConfigConstants.MAIL_TEMPLATE_ADD_EMPLOYEE);
						responseBodyStr.put("status", 200);
						responseBodyStr.put("message", "Add Complete");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			else {
				responseBodyStr.put("status", 400);
				responseBodyStr.put("message", "Fail To ADD");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status", 400);
			responseBodyStr.put("message", "Plese check your information");
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Update_Employee(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Login_info = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
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
		        employee_model.setRole(String.valueOf((String) data.get("Role")));
	        }
	        if(!data.get("Address").equals("")) {
		        employee_model.setAddress((String)data.get("Address"));
	        }
	        if(!data.get("Img").equals("")) {
		        employee_model.setImg((String)data.get("Img"));
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
			dbutil.UpdateRole(connection,employee_model);
	        if(!data.get("Username").equals("")) {
	        	String login_id = Integer.toString(employee_model.getId());
	        	login_model.setUsername((String)data.get("Username"));
	        	try {
		        	dbutil.UpdateUsername(connection,login_model.getUsername(),login_id);
	        	} catch(SQLException e) {
	    			e.printStackTrace();
	    			responseBodyStr.put("status", 400);
	    			responseBodyStr.put("message", "Plese check your information");
	    			return responseBodyStr;
	        	}
	        }
			responseBodyStr.put("status", 200);
			responseBodyStr.put("message", "Update Complete");
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put("status", 400);
			responseBodyStr.put("message", "Plese check your information");
			return responseBodyStr;
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Delete_Employee(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		EmployeeModel employee_model = new EmployeeModel();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					Employee_info = dbutil.select(connection,"employee","Emp_id",(String)Target.get(size));
					employee_model.setModel(Employee_info);
					String login_id = Integer.toString(employee_model.getId());
					dbutil.DeleteEmployee(connection,login_id);
					responseBodyStr.put("status", 200);
					responseBodyStr.put("message", "Delete Complete");
				}catch (SQLException e) {
					e.printStackTrace();
					responseBodyStr.put("status",400);
					responseBodyStr.put("message","Delete fail");
					break;
				}
				size++;
			}
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","Delete Complete");
		}
		else {
			responseBodyStr.put("status",401);
			responseBodyStr.put("message","Please input required field");
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Active_Employee(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		EmployeeModel employee_model = new EmployeeModel();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					Employee_info = dbutil.select(connection,"employee","Emp_id",(String)Target.get(size));
					employee_model.setModel(Employee_info);
					System.out.println("employee_model.getId() " + employee_model.getId());
					String login_id = Integer.toString(employee_model.getId());
					dbutil.ActiveEmployee(connection,login_id);
					responseBodyStr.put("status", 200);
					responseBodyStr.put("message", "Delete Complete");
				}catch (SQLException e) {
					e.printStackTrace();
					responseBodyStr.put("status",400);
					responseBodyStr.put("message","Active fail");
					break;
				}
				size++;
			}
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","Active Complete");
		}
		else {
			responseBodyStr.put("status",401);
			responseBodyStr.put("message","Please input required field");
		}
		return responseBodyStr;
	}

}
