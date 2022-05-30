package th.co.techberry.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import th.co.techberry.util.Encryption;

public class ChangePasswordCtrl {
	String Newpassword = null, Oldpassword = null, ConPassword = null;

	public Map<String, Object> ChangePassword(Map<String, Object> data,String Username,int id)
			throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		LoginModel login_model = new LoginModel();
		Map<String, Object> Login_info ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		Oldpassword = (String) data.get("oldPassword");
		Newpassword = (String) data.get("newPassword");
		ConPassword = (String) data.get("confirmNewPassword");
		if (Oldpassword.isEmpty() || Newpassword.isEmpty() || ConPassword.isEmpty()) {
			responseBodyStr.put("status", 401);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
			return responseBodyStr;
		}
		else if(Oldpassword.equals(Newpassword)) {
			responseBodyStr.put("status", 401);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"New Password same as Old Password");
			return responseBodyStr;
		}
		else {
			Connection connection = dbutil.connectDB();
			Login_info = (dbutil.select(connection, "login", "username", Username));
			if(Login_info == null) {
				responseBodyStr.put("status", 404);
				responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
				responseBodyStr.put("message",ConfigConstants.USERNAME_NOT_FOUND);
				return responseBodyStr;
			}
			else {
				login_model.setModel(Login_info);
				return checkPassword(login_model,id);
			}
		}
	}

	Map<String, Object> checkPassword(LoginModel model,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> Login ;
		Map<String, Object> Log_detail ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		Encryption encryptor = new Encryption();
		if (!encryptor.verifyPassword(model.getPassword(), model.getUsername(), Oldpassword)) {
			responseBodyStr.put("status", 404);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "Old passwords do not match");
		} 
		else {
			if (encryptor.verifyPassword(model.getPassword(), model.getUsername(), Oldpassword)) {
				if (Newpassword.equals(ConPassword)) {
					String encryptedPassword = encryptor.encryptPassword(model.getUsername(),Newpassword);
					model.setPassword(encryptedPassword);
					try{
						dbutil.Changepassword(connection,model.getId(), model.getPassword(),model.getResetpassword());
						Login = dbutil.select(connection,"login","Id",Integer.toString(model.getId()));
						model.setModel(Login);
						dbutil.Login_Detail_log(connection,model.getId(),Time);
						dbutil.Update_Log_Status(connection,"login_log","Login_id",Integer.toString(model.getId()));
						Log_detail = dbutil.select2con(connection,"login_detail_log",
								"Login_id","Time",Integer.toString(model.getId()),Time);
						dbutil.Addlog(connection,"login_log","Login_id",Integer.toString(model.getId()),
								Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
					} catch (SQLException e){
						e.printStackTrace();
					}
					responseBodyStr.put("status", 200);
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "change password success");
				}
				 else {
					responseBodyStr.put("status", 404);
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
					responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, "New passwords do not match");
				}
			}
		}
		return responseBodyStr;
	}
}
