package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;

import th.co.techberry.util.DatabaseUtil;

public class RoleCtrl {

	public Map<String, Object> Role() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Role = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		try {
			Role = dbutil.selectAll(connection,"user_role");
			int size = 0;
			if(Role != null) {
				while(size<Role.size()) {
					Map<String, Object> ans = new HashMap<String, Object>();
					String Role_id = String.valueOf((Integer) Role.get(size).get("Role_ID"));
					String Role_Name = (String) Role.get(size).get("Role_Name");
					ans.put("ID",Role_id);
					ans.put("Role_Name",Role_Name);
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
	
	
}
