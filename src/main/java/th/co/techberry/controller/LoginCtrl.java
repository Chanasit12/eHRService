package th.co.techberry.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
//import com.google.gson.Gson;
import com.mysql.jdbc.Connection;
import th.co.techberry.util.*;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import th.co.techberry.util.Encryption;

public class LoginCtrl {
	public String input_password, input_username;

	public Map<String, Object> Login(Map<String, Object> data)
			throws NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException, SQLException {
		Map<String, Object> responseBodyStr = new HashMap<>();
		DatabaseUtil dbutil = new DatabaseUtil();
		LoginModel login_model = new LoginModel();
		input_password = (String) data.get("password");
		input_username = (String) data.get("username");
		Map<String, Object> Login_info = new HashMap<>();
		Connection connection = dbutil.connectDB();
		try{
			Login_info = dbutil.Login(connection,"Username",input_username);
		}catch (SQLException e){
			e.printStackTrace();
		}
		if(Login_info == null) {
			responseBodyStr.put("status", 404);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put("message",ConfigConstants.USERNAME_NOT_FOUND);
			return responseBodyStr;
		}
		else {
			login_model.setModel(Login_info);
			if(login_model.getActivestatus() == false) {
				responseBodyStr.put("status", 404);
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
				responseBodyStr.put("message",ConfigConstants.LOGIN_INACTIVE_USER);
				return responseBodyStr;
			}
			else {
				return this.checkLogin(login_model,input_password,input_username);
			}
		}
	}

	Map<String, Object> checkLogin(LoginModel login_model,String password,String username) throws SQLException, ClassNotFoundException {
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Login ;
		Map<String, Object> Employee ;
		Map<String, Object> Log_detail ;
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(Encryption.verifyPassword(login_model.getPassword(),username, password)) {
			responseBodyStr.putAll(createSession(login_model));
			dbutil.Last_Login(connection,Time,login_model.getId());
			Login = dbutil.select(connection,"login","Id",Integer.toString(login_model.getId()));
			Employee = dbutil.select(connection,"employee","Id",Integer.toString(login_model.getId()));
			login_model.setModel(Login);
			dbutil.Login_Detail_log(connection,login_model.getId(),Time);
			dbutil.Update_Log_Status(connection,"login_log","Login_id",Integer.toString(login_model.getId()));
			Log_detail = dbutil.select2con(connection,"login_detail_log",
					"Login_id","Time",Integer.toString(login_model.getId()),Time);
			dbutil.Addlog(connection,"login_log","Login_id",Integer.toString(login_model.getId()),
					Time,Integer.toString((Integer)Employee.get("Emp_id")),"1","Update",(Integer)Log_detail.get("Log_id"));
		}
		else {
			if (password.equals("")) {
				responseBodyStr.put("status", 404);
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
				responseBodyStr.put("message", ConfigConstants.PASSWORD_NOT_FOUND);
				
			} else {
				responseBodyStr.put("status", 401);
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
				responseBodyStr.put("message", ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
			}
		}
		return responseBodyStr;
	}

	private Map<String, Object> createSession(LoginModel LoginModel) throws JWTCreationException ,ClassNotFoundException,SQLException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		EmployeeModel model = new EmployeeModel();
		Map<String, Object> Emp_detail;
		Map<String, Object> Role = new HashMap<>();
		long afterAddingTenMins = new Date().getTime() + (30 * 60000);
		Algorithm algorithm = Algorithm.HMAC256(ConfigConstants.SECRET_KEY);
		try{
			Emp_detail = dbutil.select(connection,"employee","Id",Integer.toString(LoginModel.getId()));
			model.setModel(Emp_detail);
			Role = dbutil.select(connection,"user_role","Role_ID",Integer.toString(model.getRold_id()));
		} catch(SQLException e){
			e.printStackTrace();
		}
		String token = JWT.create().withClaim("id", model.getEmpid())
				.withClaim("username", LoginModel.getUsername())
				.withClaim("exp", afterAddingTenMins).withIssuer("auth0")
				.sign(algorithm);
		Map<String, Object> data = new HashMap<>();
		data.put("status", 200);
		data.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
		data.put("access_token", token);
		data.put("NeedResetPassword",LoginModel.getResetpassword());
		data.put("ID",LoginModel.getId());
		data.put("Role",Role.get("Role_Name"));
		return data;
	}
}
