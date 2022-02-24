package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;

import th.co.techberry.model.EmployeeModel;
import th.co.techberry.model.LoginModel;
import th.co.techberry.util.DatabaseUtil;

public class EmployeeInfoCtrl {
	
	public Map<String, Object> Employee_info() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Emp = new ArrayList<Map<String, Object>>();
		Map<String, Object> Login_info = new HashMap<String, Object>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Position_info = new HashMap<String, Object>();
		Map<String, Object> Comp_info = new HashMap<String, Object>();
		Map<String, Object> Role_info = new HashMap<String, Object>();
		EmployeeModel employee_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		try {
			Emp = dbutil.selectAll(connection,"employee");
			int size = 0;
			if(Emp != null) {
				while(size<Emp.size()) {
					employee_model.setModel(Emp.get(size));
					Map<String, Object> ans = new HashMap<String, Object>();
					List<Map<String, Object>> team_info = new ArrayList<Map<String, Object>>();
					int id = employee_model.getId();
					Login_info = dbutil.select(connection, "login", "Id", Integer.toString(id));
					login_model.setModel(Login_info);
					String Emp_id = String.valueOf(employee_model.getEmpid());
					String Firstname = employee_model.getFirstname();
					String Lastname = employee_model.getLastname();
					String Fullname = Firstname + " " + Lastname;
					String Phone = employee_model.getPhone();
					String Email = employee_model.getEmail();
					String Img = employee_model.getImg_Path();
					String Title = employee_model.getTitle();
					String Role_id = String.valueOf(employee_model.getRold_id());
					try {
						Position_info = dbutil.select(connection,"position","Position_ID",String.valueOf(employee_model.getPositionid()));
						Comp_info = dbutil.select(connection,"company","Comp_ID",String.valueOf(employee_model.getCompanyid()));
						Role_info = (dbutil.select(connection,"user_role","Role_ID",Role_id));
					}catch(SQLException e) {
						e.printStackTrace();
						responseBodyStr.put("status",404);
						responseBodyStr.put("message","Not Found");
					}
					String Position = (String) Position_info.get("Position_Name");
					String Company = (String) Comp_info.get("Company_Name");
					String Role = (String) Role_info.get("Role_Name");
					List<Map<String, Object>> Team_info = new ArrayList<Map<String, Object>>();
					Team_info = dbutil.selectArray(connection,"emp_team","Emp_id",Emp_id);
					int team_size = 0;
					if(Team_info != null) {
						while(team_size<Team_info.size()) {
							Map<String, Object> team_ans = new HashMap<String, Object>();
							Map<String, Object> Team_data = new HashMap<String, Object>();
							Map<String, Object> Host_info = new HashMap<String, Object>();
							Team_data = dbutil.select(connection,"team","Team_id",String.valueOf((Integer) Team_info.get(team_size).get("Team_id")));
							String TeamName = (String) Team_data.get("Team_name");
							String Host = String.valueOf((Integer) Team_data.get("Team_Host"));
							Host_info = dbutil.select(connection,"Employee","Emp_id",Host);
							String H_Firstname = (String)Host_info.get("Firstname");
							String H_Lastname = (String)Host_info.get("Lastname");
							String H_Fullname = H_Firstname + " " + H_Lastname;
							String H_img = (String)Host_info.get("Img");
							team_ans.put("TeamName", TeamName);
							team_ans.put("HostName", H_Fullname);
							team_ans.put("HostImg",H_img);
							team_ans.put("index", team_size+1);
							team_info.add(team_ans);
							team_size++;
						}
						ans.put("Team_Info", team_info);
					}
					else {
						ans.put("Team_Info", team_info);
					}
					ans.put("Name",Fullname);
					ans.put("Title",Title);
					ans.put("Phone",Phone);
					ans.put("Emp_id",Emp_id);
					ans.put("Email",Email);
					ans.put("Img",Img);
					ans.put("Company",Company);
					ans.put("Position",Position);
					ans.put("Role",Role);
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
			responseBodyStr.put("status",404);
			responseBodyStr.put("message","Not Found");
		}
		return responseBodyStr;
	}
	
}
