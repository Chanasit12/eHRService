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
import th.co.techberry.util.DatabaseUtil;

public class PositionCtrl {

	public Map<String, Object> Position() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Position ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		try {
			Position = dbutil.selectAll(connection,"position");
			if(Position != null) {
				for(Map<String, Object> temp : Position){
					Map<String, Object> ans = new HashMap<>();
					String Position_ID = String.valueOf(temp.get("Position_ID"));
					String Position_Name	 = (String) temp.get("Position_Name");
					ans.put("ID",Position_ID);
					ans.put("Position_Name",Position_Name);
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
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Add＿Position(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Position_detail ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(data.get("Position_Name").equals("")) {
			result.put("status",401);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		else {
			String Name = (String) data.get("Position_Name");
			try {
				dbutil.AddPosition(connection,Name);
				Position_detail = dbutil.select(connection,"position","Position_Name",Name);
				int Position_id = (Integer) Position_detail.get("Position_ID");
				dbutil.Position_Detail_log(connection,Position_id,Time);
				Log_detail = dbutil.select2con(connection,"position_detail_log",
						"Position_ID","Time",Integer.toString(Position_id),Time);
				dbutil.Addlog(connection,"position_log","Position_ID",
						Integer.toString(Position_id),Time,Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				result.put("status",200);
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
			}catch (SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
		}
		return result;
	}
	
	public Map<String, Object> Update＿Position(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Position_detail ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
			try {
				dbutil.UpdatePosition(connection,(String) data.get("Position_ID"),(String) data.get("Position_Name"));
				Position_detail = dbutil.select(connection,"position","Position_ID",(String) data.get("Position_ID"));
				int Position_id = (Integer) Position_detail.get("Position_ID");
				dbutil.Position_Detail_log(connection,Position_id,Time);
				dbutil.Update_Log_Status(connection,"position_log","Position_ID",Integer.toString(Position_id));
				Log_detail = dbutil.select2con(connection,"position_detail_log",
						"Position_ID","Time",Integer.toString(Position_id),Time);
				dbutil.Addlog(connection,"position_log","Position_ID",
						Integer.toString(Position_id),Time,Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
				result.put("status",200);
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
			}catch (SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
		return result;
	}
	
	public Map<String, Object> Delete＿Position(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> Employee ;
		ArrayList<String> Target = (ArrayList)data.get("Value");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(!Target.isEmpty()) {
			for(String temp : Target){
				try {
					Employee = dbutil.selectArray(connection,"employee","Position_ID",temp);
					if(Employee == null) {
						dbutil.Delete(connection,"position","Position_ID",temp);
						dbutil.Update_Log_Status(connection,"position_log","Position_ID",temp);
						dbutil.Addlog(connection,"position_log","Position_ID",
								temp,Time,Integer.toString(id),"1","Delete",0);
						result.put("status",200);
						result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
					}
					else {
						result.put("status",400);
						result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"There are still employees in this Position.");
					}
				}catch (SQLException e) {
					e.printStackTrace();
					result.put("status",400);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
					return result;
				}
			}
		}
		else {
			result.put("status",401);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		return result;
	}
}
