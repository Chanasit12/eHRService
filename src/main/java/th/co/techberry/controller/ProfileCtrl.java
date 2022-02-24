package th.co.techberry.controller;

import java.sql.SQLException;
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
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		Map<String, Object> Role_info = new HashMap<String, Object>();
		Map<String, Object> Position_info = new HashMap<String, Object>();
		Map<String, Object> Company_info = new HashMap<String, Object>();
		String Id = String.valueOf(id);
		Employee_info = (dbutil.select(connection,"Employee","id",Id));
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
			Role_info = (dbutil.select(connection,"user_role","Role_ID",roleid));
			Company_info = (dbutil.select(connection,"company","Comp_ID",companyid));
			Position_info = (dbutil.select(connection,"position","Position_ID",positionid));
			employee_model.setRole((String) Role_info.get("Role_Name"));
			employee_model.setCompany((String) Company_info.get("Company_Name"));
			employee_model.setPosition((String) Position_info.get("Position_Name"));
			System.out.println("employee_model.getPosition()"+employee_model.getPosition());
			System.out.println("employee_model.getCompany()"+employee_model.getCompany());
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
			responseBodyStr.put("Img", employee_model.getImg_Path());
			responseBodyStr.put("Position", employee_model.getPosition());
			responseBodyStr.put("Company", employee_model.getCompany());
			System.out.println("responseBodyStr"+responseBodyStr);
			return responseBodyStr;
		}
	}
	
	public Map<String, Object> UpdateProfile(Map<String, Object> data,int id)
			throws SQLException, ClassNotFoundException{
		String Email,Address,Lastname,Title,Gender,Firstname,Phone,BirthDate,Img,Company,Position;
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		EmployeeModel employee_model = new EmployeeModel();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		String Id = String.valueOf(id);
		Employee_info = (dbutil.select(connection,"Employee","id",Id));
		employee_model.setModel(Employee_info);
		if(!data.get("Email").equals("")) {
			Email = (String) data.get("Email");
			employee_model.setEmail(Email);
		}
		if(!data.get("Address").equals("")) {
			Address = (String) data.get("Address");
			employee_model.setAddress(Address);
		}
		if(!data.get("Lastname").equals("")) {
			Lastname = (String) data.get("Lastname");
			employee_model.setLastname(Lastname);
		}
		if(!data.get("Title").equals("")) {
			Title = (String) data.get("Title");
			employee_model.setTitle(Title);
		}
		if(!data.get("Gender").equals("")) {
			Gender = (String) data.get("Gender");
			employee_model.setGender(Gender);
		}
		if(!data.get("Firstname").equals("")) {
			Firstname = (String) data.get("Firstname");
			employee_model.setFirstname(Firstname);
		}
		if(!data.get("Phone").equals("")) {
			Phone = (String) data.get("Phone");
			employee_model.setPhone(Phone);
		}
		if(!data.get("BirthDate").equals("")) {
			BirthDate = (String) data.get("BirthDate");
			employee_model.setBirth(BirthDate);
		}
		if(!data.get("BirthDate").equals("")) {
			BirthDate = (String) data.get("BirthDate");
			employee_model.setBirth(BirthDate);
		}
		if(!data.get("Img").equals("")) {
			Img = (String) data.get("Img");
			employee_model.setImg(Img);
		}
//		if(!data.get("Company").equals("")) {
//			Company = (String) data.get("Company");
//			employee_model.setCompanyid(Integer.valueOf(Company));
//		}
//		if(!data.get("Position").equals("")) {
//			Position = (String) data.get("Position");
//			employee_model.setPositionid(Integer.valueOf(Position));
//		}
		try {
			dbutil.UpdateProfile(connection,employee_model);
			responseBodyStr.put("status", 200);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "change Profile success");
		}catch(SQLException e) {
			responseBodyStr.put("status", 400);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "change Profile fail");
			return responseBodyStr;
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> UpdateProfileById(Map<String, Object> data)
			throws SQLException, ClassNotFoundException{
		String Email,Address,Lastname,Title,Gender,Firstname,Phone,BirthDate,Img,Company,Position;
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		EmployeeModel employee_model = new EmployeeModel();
		Email = (String) data.get("Email");
		Address = (String) data.get("Address");
		Lastname = (String) data.get("Lastname");
		Title = (String) data.get("Title");
		Gender = (String) data.get("Gender");
		Firstname = (String) data.get("Firstname");
		Phone = (String) data.get("Phone");
		BirthDate = (String) data.get("BirthDate");
		Img = (String) data.get("Img");
		Company = (String) data.get("Company");
		Position = (String) data.get("Position");
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		String Id = (String) data.get("Id");
		Employee_info = (dbutil.select(connection,"Employee","Emp_id",Id));
		employee_model.setModel(Employee_info);
		System.out.println("Emp id"+employee_model.getEmpid());
		employee_model.setAddress(Address);
		employee_model.setBirth(BirthDate);
		employee_model.setEmail(Email);
		employee_model.setFirstname(Firstname);
		employee_model.setGender(Gender);
		employee_model.setImg(Img);
		employee_model.setLastname(Lastname);
		employee_model.setPhone(Phone);
		employee_model.setTitle(Title);
		employee_model.setCompanyid(Integer.valueOf(Company));
		employee_model.setPositionid(Integer.valueOf(Position));
		dbutil.UpdateProfile(connection,employee_model);
		System.out.println("responseBodyStr"+responseBodyStr);
		responseBodyStr.put("status", 200);
		responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
		responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "change Profile success");
		return responseBodyStr;
	}
	
	public Map<String, Object> GetProfileById(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		Map<String, Object> Login_info = new HashMap<String, Object>();
		Map<String, Object> Role_info = new HashMap<String, Object>();
		Map<String, Object> Position_info = new HashMap<String, Object>();
		Map<String, Object> Company_info = new HashMap<String, Object>();
		String Id = (String) data.get("Id");
		Employee_info = (dbutil.select(connection,"Employee","Emp_id",Id));
		if (Employee_info == null) {
			responseBodyStr.put("status", 404);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put("message",ConfigConstants.ID_NOT_FOUND);
			return responseBodyStr;
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
			System.out.println("employee_model.getPosition()"+employee_model.getPosition());
			System.out.println("employee_model.getCompany()"+employee_model.getCompany());
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
			responseBodyStr.put("Img", employee_model.getImg_Path());
			responseBodyStr.put("Position", employee_model.getPosition());
			responseBodyStr.put("Company", employee_model.getCompany());
			responseBodyStr.put("Username", login_model.getUsername());
			System.out.println("responseBodyStr"+responseBodyStr);
			return responseBodyStr;
		}
	}
	
}
