package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;

import th.co.techberry.util.DatabaseUtil;
import th.co.techberry.model.*;
public class LocationCtrl {
	
	public Map<String, Object> Location() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Location = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		LocationModel model = new LocationModel();
		try {
			Location = dbutil.selectAll(connection,"location");
			if(Location != null) {
				for(Map<String,Object> temp : Location) {
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("Location_id",model.getLocationId());
					ans.put("Location_Name",model.getLocationName());
					ans.put("Description",model.getDescription());
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
				responseBodyStr.put("Message","success");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_Location(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		if(data.get("Location_Name").equals("")) {
			result.put("status",400);
			result.put("message","Please input required field");
		}
		else {
			String Name = (String) data.get("Location_Name");
			String description = (String) data.get("Description");
			try {
				dbutil.AddLocation(connection, Name, description);
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
	
	public Map<String, Object> Update_Location(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> Location = new HashMap<String, Object>();
		LocationModel model = new LocationModel();
		String Location_Name = (String) data.get("Location_Name");
		String Description = (String) data.get("Description");
		try {
			Location = dbutil.select(connection,"location","Location_id",(String) data.get("Location_id"));
		}catch (SQLException e) {
			e.printStackTrace();
		}
		model.setModel(Location);
		if(!Location_Name.equals("")) {
			model.setLocationName(Location_Name);
		}
		if(!Description.equals("")) {
			model.setDescription(Description);
		}
		try {
			dbutil.UpdateLocation(connection,model);
			result.put("status",200);
			result.put("message","Update success");
		}catch (SQLException e) {
			e.printStackTrace();
			result.put("status",400);
			result.put("message","Add fail");
			return result;
		}
		return result;
	}
	
	public Map<String, Object> Delete_DayOff(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		String Location_id = (String)data.get("Location_id");
		if(Location_id.equals("")) {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		else {
			try {
				dbutil.Delete(connection,"location","Location_id",Location_id);
				result.put("status",200);
				result.put("message","Delete success");
			} catch(SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put("message","Delete fail");
				return result;
			}
		}
		return result;
	}
}
