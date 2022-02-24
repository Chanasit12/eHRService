package th.co.techberry.model;

import java.util.Map;
import java.sql.Timestamp;

public class LoginModel {
	
	private int Id;
	private String username;
	private String password;
	private Timestamp last_login;
	private Boolean reset_password;
	private Boolean active_status;

	public void setId(int id) {
		Id = id;
	}
	
	public void setUsername(String name) {
		username = name;
	}

	public void setPassword(String pass) {
		password = pass;
	}

	public void setLastlogin(Timestamp time) {
		last_login = time;
	}

	public void setResetpassword(Boolean status) {
		reset_password = status;
	}
	
	public void setActivestatus(Boolean status) {
		active_status = status;
	}

/// Get
	public int getId() {
		return Id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Timestamp getLastlogin() {
		return last_login;
	}

	public Boolean getResetpassword() {
		return reset_password;
	}

	public Boolean getActivestatus() {
		return active_status;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setId((Integer)data.get("Id"));
			setUsername((String) data.get("Username"));
			setPassword((String) data.get("Password_hash"));
			setLastlogin((Timestamp)data.get("Last_Login"));
			setResetpassword((Boolean) data.get("Need_reset_password"));
			setActivestatus((Boolean) data.get("Active_status"));
		}
		else {
			return;
		}
	}
}
