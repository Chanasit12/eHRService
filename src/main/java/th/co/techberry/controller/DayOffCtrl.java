package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
public class DayOffCtrl {

	public Map<String, Object> DayOff() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> DayOff = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		try {
			DayOff = dbutil.selectAll(connection,"day_off_list");
			int size = 0;
			if(DayOff != null) {
				while(size<DayOff.size()) {
					Map<String, Object> ans = new HashMap<String, Object>();
					String Day_off_id = String.valueOf((Integer) DayOff.get(size).get("Day_off_id"));
					String Emp_id = String.valueOf((Integer) DayOff.get(size).get("Emp_id"));
					Emp_info = dbutil.select(connection,"Employee","Emp_id",Emp_id);
					String Firstname = (String)Emp_info.get("Firstname");
					String Lastname = (String)Emp_info.get("Lastname");
					String Fullname = Firstname + " " + Lastname;
					String Detail = (String)DayOff.get(size).get("Detail");
					System.out.println("Detail " + Detail);
					int Hour = (Integer) DayOff.get(size).get("Hours");
					ans.put("ID",Day_off_id);
					ans.put("Name",Fullname);
					ans.put("Detail",Detail);
					ans.put("Hour",Hour);
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
	
	public Map<String, Object> DayOff_Add(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		if(data.get("Hours").equals("") || data.get("Emp_id").equals("")) {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		else {
			String hour = (String) data.get("Hours");
			String id = (String) data.get("Emp_id");
			String detail = (String) data.get("Detail");
			try {
				dbutil.AddDayOff(connection,id,detail,hour);
				result.put("status",200);
				result.put("message","Add success");
			}catch (SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put("message","Add fail");
			}
		}
		return result;
	}
	
	public Map<String, Object> Delete_DayOff(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					dbutil.Delete(connection,"day_off_list","Day_off_id",(String)Target.get(size));
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
	
	public Map<String, Object> Update_DayOff(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> DayOff_info = new HashMap<String, Object>();
		DayOffModel model = new DayOffModel();
		DayOff_info = (dbutil.select(connection,"day_off_list","Day_off_id",(String)data.get("ID"))); 
		model.setModel(DayOff_info);
        try {
        	if(!data.get("Detail").equals("")) {
        		model.setDetail((String)data.get("Detail"));
        	}
        	if(!data.get("Hours").equals("")) {
        		model.setHour(Integer.valueOf((String)data.get("Hours")));
        	}
    		dbutil.UpdateDayOff(connection,model);
        }catch (SQLException e) {
			e.printStackTrace();
			result.put("status",400);
			result.put("message","Update fail");
		}
		result.put("status",200);
		result.put("message","Update success");
		return result;
	}
	
}
