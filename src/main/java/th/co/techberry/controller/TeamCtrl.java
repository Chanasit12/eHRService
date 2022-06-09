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

public class TeamCtrl {
	
	public Map<String, Object> Team() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Team ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Host_info ;
		Map<String, Object> Creator_info ;
		TeamModel team_model = new TeamModel();
		EmployeeModel host_model = new EmployeeModel();
		EmployeeModel creator_model = new EmployeeModel();
		try {
			Team = dbutil.selectAll(connection,"team");
			if(Team != null) {
				for(Map<String, Object> temp : Team){
					Map<String, Object> ans = new HashMap<>();
					team_model.setModel(temp);
					Host_info = dbutil.select(connection,"employee","Emp_id",String.valueOf(team_model.getHost()));
					host_model.setModel(Host_info);
					Creator_info = dbutil.select(connection,"employee","Emp_id",String.valueOf(team_model.getCreator()));
					creator_model.setModel(Creator_info);
					ans.put("Team_id",String.valueOf(team_model.getTeamId()));
					ans.put("Teamname",team_model.getTeamName());
					ans.put("Team_host",host_model.getFirstname()+" "+host_model.getLastname());
					ans.put("Creator",creator_model.getFirstname()+" "+creator_model.getLastname());
					res.add(ans);
				}
				responseBodyStr.put("Message","success");
			}
			else {
				responseBodyStr.put("Message","Not found");
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Team_Add(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> check ;
		Map<String, Object> Emp_info ;
		Map<String, Object> Team_info ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		TeamModel model = new TeamModel();
		ArrayList<String> Target = (ArrayList)data.get("Value");
		if(data.get("Team_name").equals("") || data.get("Creator").equals("") || data.get("Team_Host").equals("")) {
			result.put("status",401);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		else {
			String Teamname = (String) data.get("Team_name");
			String Creator = (String) data.get("Creator");
			String Team_Host = (String) data.get("Team_Host");
			try {
				Team_info = dbutil.select(connection,"team","Team_name",Teamname);
				if(Team_info == null){
					dbutil.AddTeam(connection,Teamname,Creator,Team_Host);
					Team_info = dbutil.select(connection,"team","Team_name",Teamname);
					model.setModel(Team_info);
					dbutil.Team_Detail_log(connection,model,Time);
					Log_detail = dbutil.select2con(connection,"team_detail_log",
							"Team_id","Time",Integer.toString(model.getTeamId()),Time);
					dbutil.Addlog(connection,"team_log","Team_id",Integer.toString(model.getTeamId()),
							Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
					Target.add(Team_Host);
					if(!Target.isEmpty()) {
						dbutil.Emp_Team_log(connection,Integer.toString(model.getTeamId()),Time,Integer.toString(id),"1","Add");
						Log_detail = dbutil.select(connection,"emp_team_log","Team_id",Integer.toString(model.getTeamId()));
						for(String temp : Target){
							check = dbutil.select2con(connection,"emp_team","Emp_id","Team_id",temp,(String)data.get("Team_id"));
							if(check != null) {
								Emp_info = dbutil.select(connection,"employee","Emp_id",temp);
								result.put("status",400);
								result.put("Name",Emp_info.get("Firstname")+" "+Emp_info.get("Lastname"));
								result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"This Employee Has Already in This Team");
								return result;
							}
							else {
								dbutil.AddTeamMember(connection,temp,Integer.toString(model.getTeamId()));
								dbutil.Emp_Team_Detail_log(connection,temp,model.getTeamId(),(Integer)Log_detail.get("Log_id"),Time);
							}
							result.put("status",200);
							result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
						}
					}
					else {
						result.put("status",200);
						result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
					}
				}
				else{
					result.put("status",400);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"This Team Name has already use");
				}
		}catch (SQLException e) {
			e.printStackTrace();
			result.put("status",400);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
		}
		System.out.println("result "+result);
		return result;
	}
	
	public Map<String, Object> Team_Update(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Team_info ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		TeamModel model = new TeamModel();
        try {
			Team_info = (dbutil.select(connection,"team","Team_id",(String)data.get("Team_id")));
			model.setModel(Team_info);
			if(!data.get("Team_name").equals("")) {
				model.setTeamName((String)data.get("Team_name"));
			}
			if(!data.get("Team_Host").equals("")) {
				model.setHost(Integer.valueOf((String)data.get("Team_Host")));
			}
			dbutil.UpdateTeam(connection,model);
			dbutil.Team_Detail_log(connection,model,Time);
			Log_detail = dbutil.select2con(connection,"team_detail_log",
					"Team_id","Time",Integer.toString(model.getTeamId()),Time);
			dbutil.Addlog(connection,"team_log","Team_id",Integer.toString(model.getTeamId()),
					Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
        }catch (SQLException e) {
    		e.printStackTrace();
    		result.put("status",400);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
    	}
		result.put("status",200);
		result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		return result;
	}
	
	public Map<String, Object> Member(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Team_member ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Emp_info ;
		Map<String, Object> Position_info ;
		EmployeeModel emp_model = new EmployeeModel();
		try {
			Team_member = dbutil.selectArray(connection,"emp_team","Team_id",(String) data.get("Team_id"));
			int index = 1;
			if(Team_member != null) {
				for(Map<String, Object> temp : Team_member){
					Map<String, Object> ans = new HashMap<String, Object>();
					String Emp_id = String.valueOf((Integer) temp.get("Emp_id"));
					Emp_info = dbutil.select(connection,"employee","Emp_id",Emp_id);
					emp_model.setModel(Emp_info);
					String Position_id  = String.valueOf(emp_model.getPositionid());
					Position_info = dbutil.select(connection,"position","Position_ID",Position_id);
					String Position = (String)Position_info.get("Position_Name");
					ans.put("index",index);
					ans.put("Name",emp_model.getFirstname() + " " + emp_model.getLastname());
					ans.put("id",Emp_id);
					ans.put("Position",Position);
					res.add(ans);
					index++;
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
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_member(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> check ;
		Map<String, Object> Emp_info ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		ArrayList<String> Target = (ArrayList)data.get("Value");
		if(!Target.isEmpty()) {
			dbutil.Update_Log_Status(connection,"emp_team_log","Team_id",(String)data.get("Team_id"));
			dbutil.Emp_Team_log(connection,(String)data.get("Team_id"),Time,Integer.toString(id),"1","Update");
			Log_detail = dbutil.select(connection,"emp_team_log","Team_id",(String)data.get("Team_id"));
			for(String temp : Target){
				try {
					check = dbutil.select2con(connection,"emp_team","Emp_id","Team_id",temp,(String)data.get("Team_id"));
					if(check != null) {
						Emp_info = dbutil.select(connection,"employee","Emp_id",temp);
						String Firstname = (String)Emp_info.get("Firstname");
						String Lastname = (String)Emp_info.get("Lastname");
						String Fullname = Firstname + " " + Lastname;
						result.put("status",400);
						result.put("Name",Fullname);
						result.put("message","This Employee Has Already in This Team");
						return result;
					}
					else {
						dbutil.AddTeamMember(connection,temp,(String)data.get("Team_id"));
						dbutil.Emp_Team_Detail_log(connection,temp,Integer.valueOf((String)data.get("Team_id")),(Integer)Log_detail.get("Log_id"),Time);
					}
				}catch (SQLException e) {
					e.printStackTrace();
					result.put("status",400);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
					return result;
				}
			}
			result.put("status",200);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		}
		else {
			result.put("status",401);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		return result;
	}
	
	public Map<String, Object> Delete_Team(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		ArrayList<String> Target = (ArrayList)data.get("Value");
		if(!Target.isEmpty()) {
			for(String temp : Target){
				try {
					dbutil.Update_Log_Status(connection,"emp_team_log","Team_id",temp);
					dbutil.Update_Log_Status(connection,"emp_team_log","Team_id",temp);
					dbutil.Emp_Team_log(connection,temp,Time,Integer.toString(id),"1","Delete");
					dbutil.Addlog(connection,"team_log","Team_id",temp,
							Time, Integer.toString(id),"1","Delete",0);
					dbutil.Delete(connection,"emp_team","Team_id",temp);
					dbutil.Delete(connection,"team","Team_id",temp);
				}catch (SQLException e) {
					e.printStackTrace();
					result.put("status",400);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
					break;
				}
			}
			result.put("status",200);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		}
		else {
			result.put("status",401);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		return result;
	}
	
	public Map<String, Object> Delete_Member(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		ArrayList<String> Target = (ArrayList)data.get("Value");
		if(!Target.isEmpty()) {
			dbutil.Update_Log_Status(connection,"emp_team_log","Team_id",(String)data.get("Team_id"));
			dbutil.Emp_Team_log(connection,(String)data.get("Team_id"),Time,Integer.toString(id),"1","Delete");
			Log_detail = dbutil.select(connection,"emp_team_log","Team_id",(String)data.get("Team_id"));
			for(String temp : Target){
				try {
					dbutil.Emp_Team_Detail_log(connection,temp,Integer.valueOf((String)data.get("Team_id")),(Integer)Log_detail.get("Log_id"),Time);
					dbutil.Delete_2con(connection,"emp_team","Emp_id",temp,"Team_id",(String) data.get("Team_id"));
				}catch (SQLException e) {
					e.printStackTrace();
					result.put("status",400);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
					break;
				}
			}
			result.put("status",200);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		}
		else {
			result.put("status",401);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		return result;
	}
	
	public Map<String, Object> Get_Team_By_Host(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Team ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		TeamModel team = new TeamModel();
		String Emp_id = Integer.toString(id);
		try {
			Team = dbutil.selectArray(connection,"team","Team_Host",Emp_id);
			if(Team != null){
				for(Map<String,Object> temp : Team) {
					Map<String, Object> ans = new HashMap<>();
					team.setModel(temp);
					ans.put("Team_id",team.getTeamId());
					ans.put("Teamname",team.getTeamName());
					ans.put("Team_host",team.getHost());
					res.add(ans);
				}
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message","success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Get_Team_By_Empid(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Team ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Team_detail ;
		TeamModel team = new TeamModel();
		String Emp_id = (String) data.get("Emp_id");
		try {
			Team = dbutil.selectArray(connection,"emp_team","Emp_id",Emp_id);
			if(Team != null){
				for(Map<String,Object> temp : Team) {
					String team_id = temp.get("Team_id").toString();
					Team_detail = dbutil.select(connection,"team","Team_id",team_id);
					Map<String, Object> ans = new HashMap<>();
					team.setModel(Team_detail);
					ans.put("Team_id",team.getTeamId());
					ans.put("Teamname",team.getTeamName());
					ans.put("Team_host",team.getHost());
					res.add(ans);
				}
				responseBodyStr.put("data",res);
			}
			else{
				responseBodyStr.put("data",res);
			}
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message","success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseBodyStr;
	}
}
	