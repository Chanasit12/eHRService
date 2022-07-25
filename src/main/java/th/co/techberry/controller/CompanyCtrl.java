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
import th.co.techberry.model.ChargeCodeModel;
import th.co.techberry.model.CompanyModel;
import th.co.techberry.util.DatabaseUtil;

public class CompanyCtrl {
	
	public Map<String, Object> Company() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Company ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> responseBodyStr = new HashMap<>();
		try {
			Company = dbutil.selectAll(connection,"company");
			if(Company != null) {
				for(Map<String, Object> temp : Company){
					Map<String, Object> ans = new HashMap<>();
					String Comp_ID = String.valueOf(temp.get("Comp_ID"));
					String Company_Name	 = (String) temp.get("Company_Name");
					ans.put("ID",Comp_ID);
					ans.put("Company_Name",Company_Name);
					res.add(ans);
				}
				responseBodyStr.put("data",res);
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"success");
			}
			else {
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"Not found");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"Add fail");
		}
		responseBodyStr.put("status",200);
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_Company(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Company ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		CompanyModel model = new CompanyModel();
		if(data.get("Company_Name").equals("")) {
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
		}
		else {
			String Name = (String) data.get("Company_Name");
			model.setCompanyName((String) data.get("Company_Name"));
			try {
				dbutil.AddCompany(connection,model);
				Company = dbutil.select(connection,"company", "Company_Name",Name);
				model.setModel(Company);
				dbutil.Company_Detail_log(connection,model,Time);
				Log_detail = dbutil.select2con(connection,"company_detail_log",
						"Comp_ID","Time",Integer.toString(model.getCompId()),Time);
				dbutil.Addlog(connection,"company_log","Comp_ID",
						Integer.toString(model.getCompId()),Time,Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
				result.put("status",200);
			}catch (SQLException e) {
				e.printStackTrace();
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
				result.put("status",400);
			}
		}
		return result;
	}
	
	public Map<String, Object> Update_Company(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Company ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		CompanyModel model = new CompanyModel();
			try {
				Company = dbutil.select(connection,"company","Comp_ID",(String) data.get("Comp_ID"));
				model.setModel(Company);
				if(!data.get("Company_Name").equals("")){
					model.setCompanyName((String)data.get("Company_Name"));
				}
				dbutil.UpdateCompany(connection,model);
				Company = dbutil.select(connection,"company","Comp_ID",Integer.toString(model.getCompId()));
				model.setModel(Company);
				dbutil.Update_Log_Status(connection,"company_log","Comp_ID",Integer.toString(model.getCompId()));
				dbutil.Company_Detail_log(connection,model,Time);
				Log_detail = dbutil.select2con(connection,"company_detail_log",
						"Comp_ID","Time",Integer.toString(model.getCompId()),Time);
				dbutil.Addlog(connection,"company_log","Comp_ID",
						Integer.toString(model.getCompId()),Time,Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
			}catch (SQLException e) {
				e.printStackTrace();
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
			}
		result.put("status",200);
		return result;
	}
	
	public Map<String, Object> Delete_Company(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> Employee ;
		ArrayList<String> Target = (ArrayList)data.get("Value");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		try{
			if(!Target.isEmpty()) {
				for(String temp : Target){
					Employee = dbutil.selectArray(connection,"employee","Comp_ID",temp);
					if(Employee == null) {
						dbutil.Delete(connection,"company","Comp_ID",temp);
						dbutil.Update_Log_Status(connection,"company_log","Comp_ID",temp);
						dbutil.Addlog(connection,"company_log","Comp_ID",
								temp,Time,Integer.toString(id),"1","Delete",0);
						result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
					}
					else {
						result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.DELETE_COMPANY_ERROR);
						result.put("status",400);
						return result;
					}
				}
			}
			else {
				result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
			}
		} catch(SQLException e){
			e.printStackTrace();
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
		}
		result.put("status",200);
		return result;
	}


}
