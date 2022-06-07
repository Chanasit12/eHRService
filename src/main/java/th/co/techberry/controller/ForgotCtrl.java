package th.co.techberry.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.*;

public class ForgotCtrl {
	public Map<String, Object> ForgotPassword(Map<String, Object> data)
			throws NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException, SQLException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> Login_info = new HashMap<String, Object>();
		LoginModel model = new LoginModel();
		String username = (String) data.get("username");
		if(username == "") {
			responseBodyStr.put("status", 404);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put("message", ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
			return responseBodyStr;
		}
		try{
			Login_info = dbutil.select(connection, "login", "Username", username);
		}catch (SQLException e){
			e.printStackTrace();
		}
		System.out.println("Login_info!"+Login_info);
		if(Login_info == null) {
			responseBodyStr.put("status", 404);
			responseBodyStr.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			responseBodyStr.put("message",ConfigConstants.USERNAME_NOT_FOUND);
			return responseBodyStr;
		}
		else {
			model.setModel(Login_info);
			return generatepassword(model);
		}
	}

	private Map<String, Object> generatepassword(LoginModel login_model) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> mailmap = new HashMap<>();
        Map<String, Object> Employee_info ;
        EmployeeModel employee_model = new EmployeeModel();
        DatabaseUtil dbutil = new DatabaseUtil();
		if (login_model.getUsername().isEmpty()) {
			requestMap.put("status", 404);
			requestMap.put(ConfigConstants.RESPONSE_KEY_SUCCESS, false);
			requestMap.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.USERNAME_NOT_FOUND);
        }
		else {
			 try {
	                Connection connection = dbutil.connectDB();
	                char[] password = RandomUtil.generatePassword(15);
	                String newPassword = String.valueOf(password);
	    			String encryptedPassword = Encryption.encryptPassword(login_model.getUsername(), newPassword);
	    			login_model.setPassword(encryptedPassword);
	                dbutil.Changepassword(connection,login_model.getId(), login_model.getPassword(),login_model.getResetpassword());
	                String userid = String.valueOf(login_model.getId());
	                Employee_info = (dbutil.select(connection,"Employee","ID",userid));
	                System.out.println("Employee_info"+Employee_info);
	                employee_model.setModel(Employee_info);
	                MailUtil2 mail = new MailUtil2();
	                mailmap.put("to", employee_model.getFirstname()+" "+employee_model.getLastname());
	                mailmap.put("password", newPassword);
	                try {
						mail.sendMail(employee_model.getEmail(),ConfigConstants.MAIL_SUBJECT_RESET_PASSWORD,mailmap, ConfigConstants.MAIL_TEMPLATE_FORGOT_PASSWORD);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                requestMap.put("status", 200);
	                requestMap.put(ConfigConstants.RESPONSE_KEY_SUCCESS, true);
	                requestMap.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.SENT_TO_EMAIL_SUCESS);
	                System.out.println("Sent email success!");
	            } catch (ClassNotFoundException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
		}
		System.out.println("requestMap!"+requestMap);
		return requestMap;
	}
}
