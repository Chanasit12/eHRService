package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;

import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;

public class TeamCtrl {
	
	public Map<String, Object> Team() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Team = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Host_info = new HashMap<String, Object>();
		Map<String, Object> Creator_info = new HashMap<String, Object>();
		try {
			Team = dbutil.selectAll(connection,"team");
			int size = 0;
			if(Team != null) {
				while(size<Team.size()) {
					Map<String, Object> ans = new HashMap<String, Object>();
					String Team_id = String.valueOf((Integer) Team.get(size).get("Team_id"));
					String Teamname = (String) Team.get(size).get("Team_name");
					String Team_host = String.valueOf((Integer) Team.get(size).get("Team_Host"));
					Host_info = dbutil.select(connection,"Employee","Emp_id",Team_host);
					String H_Firstname = (String)Host_info.get("Firstname");
					String H_Lastname = (String)Host_info.get("Lastname");
					String H_Fullname = H_Firstname + " " + H_Lastname;
					String Creator = String.valueOf((Integer) Team.get(size).get("Creator"));
					Creator_info = dbutil.select(connection,"Employee","Emp_id",Creator);
					String C_Firstname = (String)Creator_info.get("Firstname");
					String C_Lastname = (String)Creator_info.get("Lastname");
					String C_Fullname = C_Firstname + " " + C_Lastname;
					ans.put("Team_id",Team_id);
					ans.put("Teamname",Teamname);
					ans.put("Team_host",H_Fullname);
					ans.put("Creator",C_Fullname);
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
	
	public Map<String, Object> Team_Add(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> check = new HashMap<String, Object>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		Map<String, Object> Team_info = new HashMap<String, Object>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		if(data.get("Team_name").equals("") || data.get("Creator").equals("") || data.get("Team_Host").equals("")) {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		else {
			String Teamname = (String) data.get("Team_name");
			String Creator = (String) data.get("Creator");
			String Team_Host = (String) data.get("Team_Host");
			try {
				dbutil.AddTeam(connection,Teamname,Creator,Team_Host);
				Team_info = dbutil.select(connection,"team","Team_name",Teamname); 
				String Team_id = String.valueOf((Integer) Team_info.get("Team_id"));
				dbutil.AddTeamMember(connection,Team_Host,Team_id);
				int size = 0;
				if(!Target.isEmpty()) {
					while(Target.size() > size) {
						try {
							check = dbutil.select2con(connection,"emp_team","Emp_id","Team_id",(String)Target.get(size),(String)data.get("Team_id"));
							if(check != null) {
								Emp_info = dbutil.select(connection,"Employee","Emp_id",(String)Target.get(size));
								String Firstname = (String)Emp_info.get("Firstname");
								String Lastname = (String)Emp_info.get("Lastname");
								String Fullname = Firstname + " " + Lastname;
								result.put("status",400);
								result.put("Name",Fullname);
								result.put("message","This Employee Has Already in This Team");
								return result;
							}
							else {
								dbutil.AddTeamMember(connection,(String)Target.get(size),Team_id);
							}
							result.put("status",200);
							result.put("message","Add success");
						}catch (SQLException e) {
							e.printStackTrace();
							result.put("status",400);
							result.put("message","Add Fail");
						}
						size++;
				}
			}
				else {
					result.put("status",200);
					result.put("message","Add success");
				}
		}catch (SQLException e) {
			e.printStackTrace();
			result.put("status",400);
			result.put("message","Add Fail");
		}
		}
		return result;
	}
	
	public Map<String, Object> Team_Update(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> Team_info = new HashMap<String, Object>();
		TeamModel model = new TeamModel();
		Team_info = (dbutil.select(connection,"team","Team_id",(String)data.get("Team_id"))); 
		model.setModel(Team_info);
        if(!data.get("Team_name").equals("")) {
        	model.setTeamName((String)data.get("Team_name"));
        }
        if(!data.get("Team_Host").equals("")) {
        	model.setHost(Integer.valueOf((String)data.get("Team_Host")));
        }
        try {
        		dbutil.UpdateTeam(connection,model);
        }catch (SQLException e) {
    		e.printStackTrace();
    		result.put("status",400);
    		result.put("message","Update fail");
    	}
		result.put("status",200);
		result.put("message","Update success");
		return result;
	}
	
	public Map<String, Object> Member(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Team_member = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		Map<String, Object> Position_info = new HashMap<String, Object>();
		try {
			Team_member = dbutil.selectArray(connection,"emp_team","Team_id",(String) data.get("Team_id"));
			System.out.println("Team_member " + Team_member);
			int size = 0;
			if(Team_member != null) {
				while(size<Team_member.size()) {
					Map<String, Object> ans = new HashMap<String, Object>();
					String Emp_id = String.valueOf((Integer) Team_member.get(size).get("Emp_id"));
					Emp_info = dbutil.select(connection,"Employee","Emp_id",Emp_id);
					String Firstname = (String)Emp_info.get("Firstname");
					String Lastname = (String)Emp_info.get("Lastname");
					String Fullname = Firstname + " " + Lastname;
					String Position_id  = String.valueOf((Integer) Emp_info.get("Position_ID"));
					Position_info = dbutil.select(connection,"position","Position_ID",Position_id);
					String Position = (String)Position_info.get("Position_Name");
					ans.put("index",size+1);
					ans.put("Name",Fullname);
					ans.put("id",Emp_id);
					ans.put("Position",Position);
					res.add(ans);
					size++;
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
				responseBodyStr.put("Message","success");
			}
			else {
				responseBodyStr.put("status",200);
				responseBodyStr.put("data",res);
				responseBodyStr.put("Message","This Team didn't have any member yet");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println("responseBodyStr " + responseBodyStr);
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_member(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> check = new HashMap<String, Object>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					check = dbutil.select2con(connection,"emp_team","Emp_id","Team_id",(String)Target.get(size),(String)data.get("Team_id"));
					if(check != null) {
						Emp_info = dbutil.select(connection,"Employee","Emp_id",(String)Target.get(size));
						String Firstname = (String)Emp_info.get("Firstname");
						String Lastname = (String)Emp_info.get("Lastname");
						String Fullname = Firstname + " " + Lastname;
						result.put("status",400);
						result.put("Name",Fullname);
						result.put("message","This Employee Has Already in This Team");
						return result;
					}
					else {
						dbutil.AddTeamMember(connection,(String)Target.get(size),(String)data.get("Team_id"));
					}
				}catch (SQLException e) {
					e.printStackTrace();
					System.out.println("check " + "1");
					result.put("status",400);
					result.put("message","Add fail");
					return result;
				}
				size++;
			}
			result.put("status",200);
			result.put("message","Add Complete");
		}
		else {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		return result;
	}
	
	public Map<String, Object> Delete_Team(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					dbutil.Delete(connection,"emp_team","Team_id",(String)Target.get(size));
					dbutil.Delete(connection,"team","Team_id",(String)Target.get(size));
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
	
	public Map<String, Object> Delete_Member(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					dbutil.Delete_2con(connection,"emp_team","Emp_id",(String)Target.get(size),"Team_id",(String) data.get("Team_id"));
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
	
	public Map<String, Object> Get_Team_By_Host(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Team = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		TeamModel team = new TeamModel();
		String Emp_id = Integer.toString(id);
		try {
			Team = dbutil.selectArray(connection,"team","Team_Host",Emp_id);
			for(Map<String,Object> temp : Team) {
				Map<String, Object> ans = new HashMap<String, Object>();
				team.setModel(temp);
				ans.put("Team_id",team.getTeamId());
				ans.put("Teamname",team.getTeamName());
				ans.put("Team_host",team.getHost());
				res.add(ans);
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message","success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseBodyStr;
	}
}
	