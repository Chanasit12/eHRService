package th.co.techberry.controller;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;

public class WorkingTimeCtrl {
	
	public Map<String, Object> WorkingTime() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> WorkingTime = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		try {
			WorkingTime = dbutil.selectAll(connection,"working_time");
			int size = 0;
			if(WorkingTime != null) {
				while(size<WorkingTime.size()) {
					Map<String, Object> ans = new HashMap<String, Object>();
					String Day_Name = (String) WorkingTime.get(size).get("Day_Name");
					Time start_work = (Time)WorkingTime.get(size).get("start_work");
					Time off_work = (Time)WorkingTime.get(size).get("off_work");
					ans.put("Day_Name",Day_Name);
					ans.put("start_work",start_work);
					ans.put("off_work",off_work);
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

		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Update_Time(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> WorkingTime_info = new HashMap<String, Object>();
		WorkingTimeModel model = new WorkingTimeModel();
		WorkingTime_info = (dbutil.select(connection,"working_time","Day_Name",(String)data.get("Day_Name"))); 
		System.out.println("WorkingTime_info " + WorkingTime_info);
		model.setModel(WorkingTime_info);
        if(!data.get("start_work").equals("")) {
        	System.out.println("check " + 1);
        	Time time_start = Time.valueOf((String)data.get("start_work"));
        	model.setStart(time_start);
        }
       if(!data.get("off_work").equals("")) {
    	   System.out.println("check " + 2);
        	Time time_stop = Time.valueOf((String)data.get("off_work"));
        	model.setEnd(time_stop);
//        	System.out.println("model.time " + 2);
        }
       try {
            	dbutil.UpdateWorkingTime(connection,model);
        	}
        catch (SQLException e) {
			e.printStackTrace();
			result.put("status",400);
			result.put("message","Update fail");
		}
		result.put("status",200);
		result.put("message","Update success");
		return result;
	}
}
