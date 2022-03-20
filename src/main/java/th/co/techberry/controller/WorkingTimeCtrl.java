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
		WorkingTimeModel model = new WorkingTimeModel();
		try {
			WorkingTime = dbutil.selectAll(connection,"working_time");
			if(WorkingTime != null) {
				for(Map<String, Object> temp : WorkingTime){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("Day_Name",model.getName());
					ans.put("start_work",model.getStart());
					ans.put("off_work",model.getEnd());
					ans.put("over_time",model.getOvertime());
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
	
	public Map<String, Object> Update_Time(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> WorkingTime_info = new HashMap<String, Object>();
		WorkingTimeModel model = new WorkingTimeModel();
		try {
			WorkingTime_info = (dbutil.select(connection,"working_time","Day_Name",(String)data.get("Day_Name")));
			model.setModel(WorkingTime_info);
        	if(!data.get("start_work").equals("")) {
        		Time time_start = Time.valueOf((String)data.get("start_work"));
        		model.setStart(time_start);
       		 }
      		if(!data.get("off_work").equals("")) {
        		Time time_stop = Time.valueOf((String)data.get("off_work"));
        		model.setEnd(time_stop);
      		  }
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
