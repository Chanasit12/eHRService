package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;

import th.co.techberry.model.EmployeeModel;
import th.co.techberry.model.LoginModel;
import th.co.techberry.model.TeamModel;
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
		EmployeeModel host_model = new EmployeeModel();
		LoginModel login_model = new LoginModel();
		TeamModel team_model = new TeamModel();
		try {
			Emp = dbutil.selectAll(connection,"employee");
			if(Emp != null) {
				for(Map<String, Object> Emp_temp : Emp){
					employee_model.setModel(Emp_temp);
					Map<String, Object> ans = new HashMap<String, Object>();
					List<Map<String, Object>> team_info = new ArrayList<Map<String, Object>>();
					int id = employee_model.getId();
					Login_info = dbutil.select(connection, "login", "Id", Integer.toString(id));
					login_model.setModel(Login_info);
					Position_info = dbutil.select(connection,"position","Position_ID",String.valueOf(employee_model.getPositionid()));
					Comp_info = dbutil.select(connection,"company","Comp_ID",String.valueOf(employee_model.getCompanyid()));
					Role_info = dbutil.select(connection,"user_role","Role_ID",String.valueOf(employee_model.getRold_id()));
					String Position = (String) Position_info.get("Position_Name");
					String Company = (String) Comp_info.get("Company_Name");
					String Role = (String) Role_info.get("Role_Name");
					List<Map<String, Object>> Team_info = new ArrayList<Map<String, Object>>();
					Team_info = dbutil.selectArray(connection,"emp_team","Emp_id",String.valueOf(employee_model.getEmpid()));
					if(Team_info != null) {
						int index = 1;
						for(Map<String, Object> Team_temp : Team_info){
							Map<String, Object> team_ans = new HashMap<String, Object>();
							Map<String, Object> Team_data = new HashMap<String, Object>();
							Map<String, Object> Host_info = new HashMap<String, Object>();
							Team_data = dbutil.select(connection,"team","Team_id",String.valueOf((Integer) Team_temp.get("Team_id")));
							team_model.setModel(Team_data);
							Host_info = dbutil.select(connection,"Employee","Emp_id",Integer.toString(team_model.getHost()));
							host_model.setModel(Host_info);
							team_ans.put("TeamName", team_model.getTeamName());
							team_ans.put("HostName", host_model.getFirstname()+" "+host_model.getLastname());
							team_ans.put("HostImg",host_model.getImg_Path());
							team_ans.put("index", index);
							team_info.add(team_ans);
							index++;
						}
						ans.put("Team_Info", team_info);
					}
					else {
						ans.put("Team_Info", team_info);
					}
					ans.put("Name",employee_model.getFirstname()+" "+employee_model.getLastname());
					ans.put("Title",employee_model.getTitle());
					ans.put("Phone",employee_model.getPhone());
					ans.put("Emp_id",String.valueOf(employee_model.getEmpid()));
					ans.put("Email",employee_model.getEmail());
					ans.put("Img",employee_model.getImg_Path());
					ans.put("Company",Company);
					ans.put("Position",Position);
					ans.put("Role",Role);
					ans.put("Active_Status", login_model.getActivestatus());
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
			responseBodyStr.put("status",404);
			responseBodyStr.put("message","Not Found");
		}
		return responseBodyStr;
	}
	
}
