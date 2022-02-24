package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;

import th.co.techberry.util.DatabaseUtil;

public class PositionCtrl {

	public Map<String, Object> Position() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Position = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		try {
			Position = dbutil.selectAll(connection,"position");
			int size = 0;
			if(Position != null) {
				while(size<Position.size()) {
					Map<String, Object> ans = new HashMap<String, Object>();
					String Position_ID = String.valueOf((Integer) Position.get(size).get("Position_ID"));
					String Position_Name	 = (String) Position.get(size).get("Position_Name");
					ans.put("ID",Position_ID);
					ans.put("Position_Name",Position_Name);
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
			responseBodyStr.put("message","Add fail");
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Add＿Position(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		if(data.get("Position_Name").equals("")) {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		else {
			String Name = (String) data.get("Position_Name");
			try {
				dbutil.AddPosition(connection,Name);
				result.put("status",200);
				result.put("message","Add success");
			}catch (SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put("message","Add fail");
				return result;
			}
		}
		return result;
	}
	
	public Map<String, Object> Update＿Position(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
			try {
				dbutil.UpdatePosition(connection,(String) data.get("Position_ID"),(String) data.get("Position_Name"));
				result.put("status",200);
				result.put("message","Update success");
			}catch (SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put("message","Update fail");
				return result;
			}
		return result;
	}
	
	public Map<String, Object> Delete＿Position(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> Employee = new ArrayList<Map<String, Object>>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					Employee = dbutil.selectArray(connection,"employee","Position_ID",(String)Target.get(size));
					if(Employee == null) {
						try {
							dbutil.Delete(connection,"position","Position_ID",(String)Target.get(size));
							result.put("status",200);
							result.put("message","Delete Complete");
						}catch (SQLException e) {
							e.printStackTrace();
							result.put("status",400);
							result.put("message","Delete fail");
							return result;
						}
					}
					else {
						result.put("status",400);
						result.put("message","There are still employees in this Position.");
					}
				}catch (SQLException e) {
					e.printStackTrace();
				}
				size++;
			}
		}
		else {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		return result;
	}
}
