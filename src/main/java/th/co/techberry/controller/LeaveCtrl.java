package th.co.techberry.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import java.util.*;
public class LeaveCtrl {
	
	public Map<String, Object> Leave_info_Profile(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_info = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		String Id = String.valueOf(id);
		Employee_info = dbutil.select(connection,"Employee","id",Id);
		if(Employee_info != null) {
			String emp_id = String.valueOf(Employee_info.get("Emp_id"));
			Leave_info = dbutil.selectArray(connection,"leave_day_count","Emp_id",emp_id);
			int size = 0;
			while(size<Leave_info.size()) {
				Map<String, Object> leave_type = new HashMap<String, Object>();
				Map<String, Object> ans = new HashMap<String, Object>();
				String leave_id = String.valueOf((Integer) Leave_info.get(size).get("Type_ID"));
				leave_type = dbutil.select(connection,"leave_type","Type_ID",leave_id);
				String leave_name = (String) leave_type.get("Type_name");
				String Amount_leave = String.valueOf((Integer) Leave_info.get(size).get("Leaved"));
				String Amount_add = String.valueOf((Integer) Leave_info.get(size).get("Added"));
				ans.put("Type_name",leave_name);
				ans.put("Leaved",Amount_leave);
				ans.put("Added",Amount_add);
				res.add(ans);
				size++;
			}
			responseBodyStr.put("data",res);
		}
		else {
			responseBodyStr.put("data",res);
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Leave_request(int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_request = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> Employee_info = new HashMap<String, Object>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		String Id = String.valueOf(id);
		Employee_info = dbutil.select(connection,"Employee","id",Id);
		if(Employee_info != null) {
			String emp_id = String.valueOf(Employee_info.get("Emp_id"));
			Leave_request = dbutil.selectArray(connection,"leave_request","Emp_id",emp_id);
			int size = 0;
			while(size<Leave_request.size()) {
				Map<String, Object> leave_type = new HashMap<String, Object>();
				Map<String, Object> ans = new HashMap<String, Object>();
				String leave_id = String.valueOf((Integer) Leave_request.get(size).get("Type_ID"));
				leave_type = dbutil.select(connection,"leave_type","Type_ID",leave_id);
				String leave_name = (String) leave_type.get("Type_name");
				Date Begin_date = (Date) Leave_request.get(size).get("Begin_date");
				Time Begin_time = (Time) Leave_request.get(size).get("Begin_time");
				Date End_date = (Date) Leave_request.get(size).get("End_date");
				Time End_time = (Time) Leave_request.get(size).get("End_time");
				String Leave_status = (String) Leave_request.get(size).get("Status");
				ans.put("id", size+1);
				ans.put("Type_name",leave_name);
				ans.put("Begin_date",Begin_date);
				ans.put("Begin_time",Begin_time);
				ans.put("End_date",End_date);
				ans.put("End_time",End_time);
				ans.put("Leave_status",Leave_status);
				res.add(ans);
				size++;
			}
			System.out.println("res "+res);
			responseBodyStr.put("data",res);
		}
		else {
			responseBodyStr.put("data",res);
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Profile_leave(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> leave_info = new HashMap<String, Object>();
		Map<String, Object> leave_request = new HashMap<String, Object>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		leave_info.putAll(Leave_info_Profile(id));
		leave_request.putAll(Leave_request(id));
		System.out.println("leave_request "+leave_request);
		System.out.println("leave_info "+leave_info);
		if(leave_info != null && leave_request != null) {
			responseBodyStr.put("status", 200);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
			responseBodyStr.put("Leave_infomation",leave_info);
			responseBodyStr.put("Leave_request",leave_request);
		}
		else {
			responseBodyStr.put("status", 404);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put("message",ConfigConstants.ID_NOT_FOUND);
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Leave_type_mgmt() throws ClassNotFoundException, SQLException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> Leave_type = new ArrayList<Map<String, Object>>();
		try {
			Leave_type = dbutil.selectAll(connection,"leave_type");
			if(Leave_type != null) {
				int size = 0;
				while(size<Leave_type.size()) {
					Map<String, Object> ans = new HashMap<String, Object>();
					String leave_id = String.valueOf((Integer) Leave_type.get(size).get("Type_ID"));
					String leave_name = (String) Leave_type.get(size).get("Type_name");
					String Num_per_year = String.valueOf((Integer) Leave_type.get(size).get("Num_per_year"));
					String Num_can_add = String.valueOf((Integer) Leave_type.get(size).get("Num_can_add"));
					ans.put("id", leave_id);
					ans.put("Type_name",leave_name);
					ans.put("Num_per_year",Num_per_year);
					ans.put("Num_can_add",Num_can_add);
					res.add(ans);
					size++;
				}
				System.out.println("res "+res);
				responseBodyStr.put("status",200);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		responseBodyStr.put("data",res);
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_Leave_type(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Employee = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> Leave_type = new HashMap<String, Object>();
		if(data.get("Type_name").equals("") || data.get("Num_per_year").equals("") || data.get("Num_can_add").equals("")) {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		else {
			String Type_name = (String) data.get("Type_name");
			String Num_per_year = (String) data.get("Num_per_year");
			String Num_can_add = (String) data.get("Num_can_add");
			try {
				dbutil.AddLeave_type(connection,Type_name,Num_per_year,Num_can_add);
				Leave_type = dbutil.select(connection,"leave_type","Type_name",Type_name); 
				String Type_id = String.valueOf((Integer) Leave_type.get("Type_ID"));
				Employee = dbutil.selectAll(connection,"employee");
				int size = 0;
				if(Employee != null) {
					while(Employee.size() > size) {
							try {
								String Emp_id = String.valueOf((Integer) Employee.get(size).get("Emp_id"));
								dbutil.AddLeave_Day(connection,Emp_id,Type_id,Num_per_year,"0");
							}catch (SQLException e) {
								e.printStackTrace();
								result.put("status",400);
								result.put("message","Add Fail");
							}
							size++;
					}
					result.put("status",200);
					result.put("message","Add success");
				}
				else {
					result.put("status",400);
					result.put("message","Add Fail");
				}
		}catch (SQLException e) {
			e.printStackTrace();
			result.put("status",400);
			result.put("message","Add Fail");
		}
		}
		return result;
	}
	
	public Map<String, Object> Delete_Leave_Type(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				try {
					dbutil.Delete(connection,"leave_type","Type_ID",(String)Target.get(size));
					dbutil.Delete(connection,"leave_day_count","Type_ID",(String)Target.get(size));
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
	
	public Map<String, Object> Update_Leave_Type(Map<String, Object> data) throws SQLException, ClassNotFoundException ,NumberFormatException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Employee_Leave = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> Leave_info = new HashMap<String, Object>();
		LeaveTypeModel model = new LeaveTypeModel();
		Leave_info = (dbutil.select(connection,"leave_type","Type_ID",(String)data.get("Type_ID"))); 
		model.setModel(Leave_info);
        if(!data.get("Type_name").equals("")) {
        	model.setName((String)data.get("Type_name"));
        }
        if(!data.get("Num_per_year").equals("")) {
        	try {
            	model.setNum_per_year(Integer.valueOf((String)data.get("Num_per_year")));
        	}catch(NumberFormatException e) {
    			e.printStackTrace();
        		result.put("status",400);
        		result.put("message","Please input Number");
        		return result;
    		}
        }
        if(!data.get("Num_can_add").equals("")) {
        	try {
            	model.setNum_can_add(Integer.valueOf((String)data.get("Num_can_add")));
        	}catch(NumberFormatException e) {
    			e.printStackTrace();
        		result.put("status",400);
        		result.put("message","Please input Number");
        		return result;
    		}
        }
        try {
        		dbutil.UpdateLeaveType(connection,model);
        		try {
            		Employee_Leave = dbutil.selectArray(connection,"leave_day_count","Type_ID",(String)data.get("Type_ID"));
        			if(!Employee_Leave.isEmpty()) {
        				int size = 0;
        				while(size<Employee_Leave.size()) {
        					int leave = (Integer) Employee_Leave.get(size).get("Leaved");
        					int Emp_id = (Integer) Employee_Leave.get(size).get("Emp_id");
        					if(leave > model.getNum_per_year()) {
        						dbutil.UpdateLeaveCount(connection,model,Emp_id);
        					}
        					size++;
        				}
        			}
        		} catch(SQLException e) {
        			e.printStackTrace();
        		}
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
