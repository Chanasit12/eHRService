package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
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
		Map<String, Object> Leave_type = new HashMap<String, Object>();
		LeaveTypeModel Type_model = new LeaveTypeModel();
		try {
			Leave_type = dbutil.select(connection,"leave_type","Type_name","DayOff");
			Type_model.setModel(Leave_type);
			DayOff = dbutil.selectArray(connection,"leave_day_count","Type_ID",Integer.toString(Type_model.getId()));
			if(DayOff != null) {
				for(Map<String, Object> temp : DayOff){
					Map<String, Object> ans = new HashMap<String, Object>();
					String Type_ID = String.valueOf((Integer) temp.get("Type_ID"));
					String Emp_id = String.valueOf((Integer) temp.get("Emp_id"));
					Emp_info = dbutil.select(connection,"Employee","Emp_id",Emp_id);
					String Firstname = (String)Emp_info.get("Firstname");
					String Lastname = (String)Emp_info.get("Lastname");
					String Fullname = Firstname + " " + Lastname;
					int Hour = (Integer) temp.get("Leaved");
					ans.put("Emp_id",Emp_id);
					ans.put("Name",Fullname);
					ans.put("Type_ID",Type_ID);
					ans.put("Hour",Hour);
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
	
	public Map<String, Object> DayOff_Add(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> Leave_type = new HashMap<String, Object>();
		Map<String, Object> Leave_count = new HashMap<String, Object>();
		LeaveTypeModel Type_model = new LeaveTypeModel();
		LeaveCountModel leave_count = new LeaveCountModel();
		if(data.get("Hours").equals("") || data.get("Emp_id").equals("")) {
			result.put("status",401);
			result.put("message", ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		else {
			String hour = (String) data.get("Hours");
			String id = (String) data.get("Emp_id");
			try {
				Leave_type = dbutil.select(connection,"leave_type","Type_name",ConfigConstants.DAY_OFF_NAME);
				Type_model.setModel(Leave_type);
				Leave_count = dbutil.select2con(connection,"leave_day_count","Emp_id","Type_ID",id,Integer.toString(Type_model.getId()));
				leave_count.setModel(Leave_count);
				leave_count.setLeaved(leave_count.getLeaved()+Integer.parseInt(hour));
				dbutil.AddDayOff(connection,leave_count);
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
	
	public Map<String, Object> Update_DayOff(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> Leave_count_info = new HashMap<String, Object>();
		Map<String, Object> Leave_type = new HashMap<String, Object>();
		LeaveTypeModel Type_model = new LeaveTypeModel();
		LeaveCountModel model = new LeaveCountModel();
		String Emp_id = (String) data.get("Emp_id");
        try {
			Leave_type = dbutil.select(connection,"leave_type","Type_name",ConfigConstants.DAY_OFF_NAME);
			Type_model.setModel(Leave_type);
			Leave_count_info = dbutil.select2con(connection,"leave_day_count","Emp_id","Type_ID",
					Emp_id,Integer.toString(Type_model.getId()));
			model.setModel(Leave_count_info);
        	if(!data.get("Hours").equals("")) {
        		model.setLeaved(Integer.valueOf((String)data.get("Hours")));
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
