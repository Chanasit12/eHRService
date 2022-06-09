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
import th.co.techberry.model.*;
public class LocationCtrl {
	
	public Map<String, Object> Location() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Location ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
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
	
	public Map<String, Object> Add_Location(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Location ;
		Map<String, Object> Log_detail ;
		LocationModel model = new LocationModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(data.get("Location_Name").equals("")) {
			result.put("status",400);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		else {
			String Name = (String) data.get("Location_Name");
			String description = (String) data.get("Description");
			try {
				dbutil.AddLocation(connection, Name, description);
				Location = dbutil.select2con(connection,"location",
						"Location_name","description",Name,description);
				model.setModel(Location);
				dbutil.Location_Detail_log(connection,model,Time);
				Log_detail = dbutil.select2con(connection,"location_detail_log",
						"Location_id","Time",Integer.toString(model.getLocationId()),Time);
				dbutil.Addlog(connection,"location_log","Location_id",
						Integer.toString(model.getLocationId()),Time,Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				result.put("status",200);
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
			}catch (SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
				return result;
			}
		}
		return result;
	}
	
	public Map<String, Object> Update_Location(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Location ;
		Map<String, Object> Log_detail ;
		LocationModel model = new LocationModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try {
			Location = dbutil.select(connection,"location","Location_id",(String) data.get("Location_id"));
			model.setModel(Location);
			if(!data.get("Location_Name").equals("")) {
				model.setLocationName((String)data.get("Location_Name"));
			}
			if(!data.get("Description").equals("")) {
				model.setDescription((String)data.get("Description"));
			}
			dbutil.UpdateLocation(connection,model);
			Location = dbutil.select(connection,"location","Location_id",(String) data.get("Location_id"));
			model.setModel(Location);
			dbutil.Location_Detail_log(connection,model,Time);
			dbutil.Update_Log_Status(connection,"location_log","Location_id",Integer.toString(model.getLocationId()));
			Log_detail = dbutil.select2con(connection,"location_detail_log",
					"Location_id","Time",Integer.toString(model.getLocationId()),Time);
			dbutil.Addlog(connection,"location_log","Location_id",
					Integer.toString(model.getLocationId()),Time,Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
		}catch (SQLException e) {
			e.printStackTrace();
		}
		result.put("status",200);
		return result;
	}
	
	public Map<String, Object> Delete_Location(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> Location ;
		LocationModel model = new LocationModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		String Location_id = (String)data.get("Location_id");
		if(Location_id.equals("")) {
			result.put("status",400);
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		else {
			try {
				Location = dbutil.selectArray(connection,"timesheet","Location_id",Location_id);
				if(Location == null){
					dbutil.Update_Log_Status(connection,"location_log","Location_id",Integer.toString(model.getLocationId()));
					dbutil.Addlog(connection,"location_log","Location_id",
							Integer.toString(model.getLocationId()),Time,Integer.toString(id),"1","Delete",0);
					dbutil.Delete(connection,"location","Location_id",Location_id);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
					result.put("status",200);
				}
				else{
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
					result.put("status",400);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"This Location is being used ");
				}
			} catch(SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
				return result;
			}
		}
		return result;
	}
}
