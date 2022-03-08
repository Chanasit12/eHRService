package th.co.techberry.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		LeaveTypeModel type_model = new LeaveTypeModel();
		String Id = String.valueOf(id);
		try{
			Employee_info = dbutil.select(connection,"Employee","id",Id);
			if(Employee_info != null) {
				String emp_id = String.valueOf(Employee_info.get("Emp_id"));
				Leave_info = dbutil.selectArray(connection,"leave_day_count","Emp_id",emp_id);
				for(Map<String, Object> temp : Leave_info){
					Map<String, Object> leave_type = new HashMap<String, Object>();
					Map<String, Object> ans = new HashMap<String, Object>();
					String leave_id = String.valueOf((Integer) temp.get("Type_ID"));
					leave_type = dbutil.select(connection,"leave_type","Type_ID",leave_id);
					type_model.setModel(leave_type);
					String Amount_leave = String.valueOf((Integer) temp.get("Leaved"));
					String Amount_add = String.valueOf((Integer) temp.get("Added"));
					ans.put("Type_name",type_model.getName());
					ans.put("Leaved",Amount_leave);
					ans.put("Added",Amount_add);
					res.add(ans);
				}
				responseBodyStr.put("data",res);
			}
			else {
				responseBodyStr.put("data",res);
			}
		} catch(SQLException e){
			e.printStackTrace();
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
		LeaveModel Leave_model = new LeaveModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		String Id = String.valueOf(id);
		int number = 1;
		try{
			Employee_info = dbutil.select(connection,"Employee","id",Id);
			if(Employee_info != null) {
				String emp_id = String.valueOf(Employee_info.get("Emp_id"));
				Leave_request = dbutil.selectArray(connection,"leave_request","Emp_id",emp_id);
				for(Map<String, Object> temp : Leave_request){
					Map<String, Object> leave_type = new HashMap<String, Object>();
					Map<String, Object> ans = new HashMap<String, Object>();
					Leave_model.setModel(temp);
					leave_type = dbutil.select(connection,"leave_type","Type_ID",String.valueOf(Leave_model.getTypeId()));
					type_model.setModel(leave_type);
					System.out.println("test "+ Leave_model.getBegin());
 					ans.put("id", number);
					ans.put("Type_name",type_model.getName());
					ans.put("Begin",Leave_model.getBegin());
					ans.put("End",Leave_model.getEnd());
					ans.put("Leave_status",Leave_model.getStatus());
					ans.put("Emergency",Leave_model.getEmergency());
					number++;
					res.add(ans);
				}
				System.out.println("res "+res);
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
			}
			else {
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
			}
		} catch (SQLException e){
			e.printStackTrace();
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
		LeaveTypeModel model = new LeaveTypeModel();
		try {
			Leave_type = dbutil.selectAll(connection,"leave_type");
			if(Leave_type != null) {
				for(Map<String, Object> temp : Leave_type){
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("id",model.getId());
					ans.put("Type_name",model.getName());
					ans.put("Num_per_year",model.getNum_per_year());
					ans.put("Num_can_add",model.getNum_can_add());
					res.add(ans);
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
				if(Employee != null) {
					for(Map<String, Object> temp : Employee){
							try {
								String Emp_id = String.valueOf((Integer) temp.get("Emp_id"));
								dbutil.AddLeave_Day(connection,Emp_id,Type_id,Num_per_year,"0");
							}catch (SQLException e) {
								e.printStackTrace();
								result.put("status",400);
								result.put("message","Add Fail");
							}
					}
					result.put("status",200);
					result.put("message","Add success");
				}
				else {
					result.put("status",400);
					result.put("message","Add Fail");
				}
		}	catch (SQLException e) {
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
            	Employee_Leave = dbutil.selectArray(connection,"leave_day_count","Type_ID",(String)data.get("Type_ID"));
        		if(!Employee_Leave.isEmpty()) {
					for(Map<String, Object> temp : Employee_Leave ){
						int leave = (Integer) temp.get("Leaved");
						int Emp_id = (Integer) temp.get("Emp_id");
						if(leave > model.getNum_per_year()) {
        						dbutil.UpdateLeaveCount(connection,model,Emp_id);
        					}
        				}
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

	public Map<String, Object> Send_Request(Map<String, Object> data) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Role_detail = new HashMap<String, Object>();
		Map<String, Object> Emp_detail = new HashMap<String, Object>();
		LeaveModel leave_model = new LeaveModel();
		EmployeeModel emp_model = new EmployeeModel();
		leave_model.setModelByReq(data);
		int Emergency;
		if(leave_model.getEmergency()){
			Emergency = 1;
		} else{
			Emergency = 0;
		}
		try{
			Emp_detail = dbutil.select(connection,"employee","Emp_id",Integer.toString(leave_model.getDepend()));
			emp_model.setModel(Emp_detail);
			Role_detail = dbutil.select(connection,"user_role","Role_ID",(String) data.get("Depend"));
			leave_model.setStatus("Waiting for " + Role_detail.get("Role_Name"));
			dbutil.AddLeave_Req(connection,leave_model,Emergency);
			responseBodyStr.put("status",200);
			responseBodyStr.put("message","Send Leave Request Complete");
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Get_Request(int id) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Leave_request = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Emp_detail = new HashMap<String, Object>();
		LeaveModel leave_model = new LeaveModel();
		LeaveTypeModel type_model = new LeaveTypeModel();
		EmployeeModel emp_model = new EmployeeModel();
//		int number = 1;
		try{
			Leave_request = dbutil.selectArray(connection,"leave_request","Depend",Integer.toString(id));
			if(Leave_request != null){
				for(Map<String, Object> temp : Leave_request){
					leave_model.setModel(temp);
					Map<String, Object> leave_type = new HashMap<String, Object>();
					Map<String, Object> ans = new HashMap<String, Object>();
					leave_model.setModel(temp);
					leave_type = dbutil.select(connection,"leave_type","Type_ID",String.valueOf(leave_model.getTypeId()));
					Emp_detail = dbutil.select(connection,"employee","Emp_id",Integer.toString(leave_model.getEmpId()));
					emp_model.setModel(Emp_detail);
					type_model.setModel(leave_type);
//					ans.put("No.", number);
					ans.put("Request_id",leave_model.getReqId());
					ans.put("Sender_Name",emp_model.getFirstname()+" "+emp_model.getLastname());
					ans.put("Type_name",type_model.getName());
					ans.put("Begin",leave_model.getBegin());
					ans.put("End",leave_model.getEnd());
					ans.put("Detail",leave_model.getDetail());
					ans.put("Leave_status",leave_model.getStatus());
					ans.put("Emergency",leave_model.getEmergency());
//					number++;
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
			}
			else{
				responseBodyStr.put("data",res);
				responseBodyStr.put("status",200);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Response_Leave_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Approver_detail = new HashMap<String, Object>();
		Map<String, Object> Role_detail = new HashMap<String, Object>();
		Map<String, Object> Leave_request = new HashMap<String, Object>();
		LeaveModel leave_model = new LeaveModel();
		EmployeeModel Approver_model = new EmployeeModel();
//		int Req_id = Integer.parseInt((String) data.get("Req_id"));
//		String Status = (String) data.get("Status");
		String Req_id = (String) data.get("Req_id");
		String Comment = (String) data.get("Comment");
		String Leave_id = (String) data.get("Leave_type");
		String Depend = (String) data.get("Depend");
		Boolean Status = Boolean.parseBoolean((String) data.get("Status"));
 		try{
			Approver_detail = dbutil.select(connection,"employee","Emp_id",Integer.toString(id));
			Approver_model.setModel(Approver_detail);
			Role_detail = dbutil.select(connection,"user_role","Role_ID",Integer.toString(Approver_model.getRold_id()));
			Leave_request = dbutil.select(connection,"leave_request","Req_id",Req_id);
			leave_model.setModel(Leave_request);
			if(!Comment.equals("")){
				leave_model.setComment(Comment);
			}
			if(!Leave_id.equals("")){
				leave_model.setTypeId(Integer.parseInt(Leave_id));
			}
			if( Role_detail.get("Role_Name").equals("Approver")){
				leave_model.setDepend(leave_model.getEmpId());
				if(Status){
					String Status_Message = "Approve by "+Role_detail.get("Role_Name");
					leave_model.setStatus(Status_Message);
					responseBodyStr.put("message","Approve Complete");
				}
				else{
					String Status_Message = "DisApprove by "+Role_detail.get("Role_Name");
					leave_model.setStatus(Status_Message);
					responseBodyStr.put("message","DisApprove Complete");
				}
			}
			else{
				int Depend_id = Integer.parseInt(Depend);
				leave_model.setDepend(Depend_id);

				if(Status){
					String Status_Message = "Waiting For Approver";
					leave_model.setStatus(Status_Message);
					responseBodyStr.put("message","Approve Complete");
					System.out.println("res "+1);
				}
				else{
					String Status_Message = "DisApprove by "+Role_detail.get("Role_Name");
					leave_model.setStatus(Status_Message);
					responseBodyStr.put("message","DisApprove Complete");
					System.out.println("res "+2);
				}
			}
			dbutil.UpdateLeaveReq(connection,leave_model);
			responseBodyStr.put("status",200);
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}

	public Map<String, Object> Delete_Leave_Request(Map<String, Object> data) throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Approver_detail = new HashMap<String, Object>();
		Map<String, Object> Role_detail = new HashMap<String, Object>();
		Map<String, Object> Leave_request = new HashMap<String, Object>();
		LeaveModel leave_model = new LeaveModel();
		EmployeeModel Approver_model = new EmployeeModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String current_date_time = dtf.format(now);
		String Req_id = (String) data.get("Req_id");
		try{
			Leave_request = dbutil.select(connection,"leave_request","Req_id",Req_id);
			leave_model.setModel(Leave_request);
			dbutil.Delete(connection,"leave_request","Req_id",Req_id);
			responseBodyStr.put("message","Delete Request Complete");
			responseBodyStr.put("status",200);
		} catch(SQLException e){
			e.printStackTrace();
		}
		return responseBodyStr;
	}
}
