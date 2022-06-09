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
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;

public class ChargeCodeCtrl {
	
	public Map<String, Object> ChargeCode() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Charge_Code ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		ChargeCodeModel model = new ChargeCodeModel();
		try {
			Charge_Code = dbutil.selectAll(connection,"charge_code");
			if(Charge_Code != null) {
				for(Map<String,Object> temp : Charge_Code) {
					Map<String, Object> ans = new HashMap<>();
					model.setModel(temp);
					ans.put("ChargeCode_id",model.getChargeCodeId());
					ans.put("ChargeCode_Name",model.getChargeCodeName());
					ans.put("Description",model.getDescription());
					res.add(ans);
				}
				responseBodyStr.put("Message", ConfigConstants.RESPONSE_KEY_SUCCESS);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		responseBodyStr.put("status",200);
		responseBodyStr.put("data",res);
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_ChargeCode(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Charge_code ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		ChargeCodeModel model = new ChargeCodeModel();
		if(data.get("ChargeCode_Name").equals("")) {
			result.put("status",400);
			result.put("message","Please input required field");
		}
		else {
			String Name = (String) data.get("ChargeCode_Name");
			String description = (String) data.get("Description");
			try {
				dbutil.AddChargeCode(connection,Name,description);
				Charge_code = dbutil.select2con(connection,"charge_code",
						"Charge_code_name","description",Name,description);
				model.setModel(Charge_code);
				dbutil.Charge_Code_Detail_log(connection,model,Time);
				Log_detail = dbutil.select2con(connection,"charge_code_detail_log",
						"Charge_code_id","Time",Integer.toString(model.getChargeCodeId()),Time);
				dbutil.Addlog(connection,"charge_code_log","Charge_code_id",
						Integer.toString(model.getChargeCodeId()),Time,Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
			}catch (SQLException e) {
				e.printStackTrace();
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
		}
		result.put("status",200);
		return result;
	}
	
	public Map<String, Object> Update_ChargeCode(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> ChargeCode ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		ChargeCodeModel model = new ChargeCodeModel();
		String ChargeCode_Name = (String) data.get("ChargeCode_name");
		String Description = (String) data.get("Description");
		try {
			ChargeCode = dbutil.select(connection,"charge_code","Charge_code_id",(String) data.get("ChargeCode_id"));
			model.setModel(ChargeCode);
			if(!ChargeCode_Name.equals("")) {
				model.setChargeCodeName(ChargeCode_Name);
			}
			if(!Description.equals("")) {
				model.setDescription(Description);
			}
			dbutil.UpdateChargeCode(connection,model);
			dbutil.Update_Log_Status(connection,"charge_code_log","Charge_code_id",Integer.toString(model.getChargeCodeId()));
			dbutil.Charge_Code_Detail_log(connection,model,Time);
			Log_detail = dbutil.select2con(connection,"charge_code_detail_log",
					"Charge_code_id","Time",Integer.toString(model.getChargeCodeId()),Time);
			dbutil.Addlog(connection,"charge_code_log","Charge_code_id",
					Integer.toString(model.getChargeCodeId()),Time,Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		}catch (SQLException e) {
			e.printStackTrace();
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
		}
		result.put("status",200);
		return result;
	}
	
	public Map<String, Object> Delete_ChargeCode(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Charge_Code ;
		Map<String, Object> result = new HashMap<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		String ChargeCode_id = (String)data.get("ChargeCode_id");
		if(ChargeCode_id.equals("")) {
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
			result.put("status",400);
		}
		else {
			try {
				Charge_Code = dbutil.selectArray(connection,"timesheet","Charge_code_id",ChargeCode_id);
				if(Charge_Code == null){
					dbutil.Update_Log_Status(connection,"charge_code_log","Charge_code_id",ChargeCode_id);
					dbutil.Addlog(connection,"charge_code_log","Charge_code_id",
							ChargeCode_id,Time,Integer.toString(id),"1","Delete",0);
					dbutil.Delete(connection,"charge_code","Charge_code_id",ChargeCode_id);
					result.put("status",200);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
				}else{
					result.put("status",400);
					result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"This Charge Code is being used ");
				}
			} catch(SQLException e) {
				e.printStackTrace();
				result.put("status",400);
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
		}
		return result;
	}
}