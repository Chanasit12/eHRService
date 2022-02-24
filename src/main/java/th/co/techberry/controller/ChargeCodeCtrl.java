package th.co.techberry.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;

import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;

public class ChargeCodeCtrl {
	
	public Map<String, Object> ChargeCode() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Charge_Code = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		ChargeCodeModel model = new ChargeCodeModel();
		try {
			Charge_Code = dbutil.selectAll(connection,"charge_code");
			if(Charge_Code != null) {
				for(Map<String,Object> temp : Charge_Code) {
					Map<String, Object> ans = new HashMap<String, Object>();
					model.setModel(temp);
					ans.put("ChargeCode_id",model.getChargeCodeId());
					ans.put("ChargeCode_Name",model.getChargeCodeName());
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
	
	public Map<String, Object> Add_ChargeCode(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		if(data.get("ChargeCode_Name").equals("")) {
			result.put("status",400);
			result.put("message","Please input required field");
		}
		else {
			String Name = (String) data.get("ChargeCode_Name");
			String description = (String) data.get("Description");
			try {
				dbutil.AddChargeCode(connection, Name, description);
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
	
	public Map<String, Object> Update_ChargeCode(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> ChargeCode = new HashMap<String, Object>();
		ChargeCodeModel model = new ChargeCodeModel();
		String ChargeCode_Name = (String) data.get("ChargeCode_Name");
		String Description = (String) data.get("Description");
		try {
			ChargeCode = dbutil.select(connection,"charge_code","Charge_code_id",(String) data.get("ChargeCode_id"));
		}catch (SQLException e) {
			e.printStackTrace();
		}
		model.setModel(ChargeCode);
		if(!ChargeCode_Name.equals("")) {
			model.setChargeCodeName(ChargeCode_Name);
		}
		if(!Description.equals("")) {
			model.setDescription(Description);
		}
		try {
			dbutil.UpdateChageCode(connection,model);
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
	
	public Map<String, Object> Delete_ChargeCode(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		String ChargeCode_id = (String)data.get("ChargeCode_id");
		if(ChargeCode_id.equals("")) {
			result.put("status",401);
			result.put("message","Please input required field");
		}
		else {
			try {
				dbutil.Delete(connection,"charge_code","Charge_code_id",ChargeCode_id);
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
