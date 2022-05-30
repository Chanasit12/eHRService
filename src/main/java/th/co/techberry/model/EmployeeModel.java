package th.co.techberry.model;

import java.util.Map;
import java.util.Date;
public class EmployeeModel {
	private int Emp_id;
	private String Title;
	private String Firstname;
	private String Lastname;
	private String Birth_date;
	private String Gender;
	private String Phone;
	private int Id;
	private String Email;
	private String Address;
	private int Role_id;
	private int Company_id;
	private int Position_id;
	private byte[] Img_Path;
	private String Role;
	private String Position;
	private String Company;
	private String StrImg;

	public void setEmpid(int id) {
		Emp_id = id;
	}
	
	public void setCompanyid(int id) {
		Company_id = id;
	}
	
	public void setPositionid(int id) {
		Position_id = id;
	}
	
	public void setRole(String name) {
			Role = name;
	}
	
	public void setPosition(String name) {
			Position = name;
	}
	
	public void setCompany(String name) {
			Company = name;
	}
	public void setTitle(String name) {
		if(name != "") {
			Title = name;
		}
	}

	public void setFirstname(String name) {
		if(name != "") {
			Firstname = name;
		}
	}

	public void setLastname(String name) {
		if(name != "") {
			Lastname = name;
		}
	}

	public void setBirth(String day) {
		if(day != "") {
			Birth_date = day;
		}
	}
	
	public void setGender(String gen) {
		if(gen != "") {
			Gender = gen;
		}
	}

	public void setPhone(String num) {
		if(num != "") {
			Phone = num;
		}
	}
	
	public void setId(int id) {
		Id = id;
	}

	public void setEmail(String mail) {
		if(mail != "") {
			Email = mail;
		}
	}
	
	public void setAddress(String add) {
		if(add != "") {
			Address = add;
		}
	}

	public void setRole(Integer id) {
		Role_id = id;
	}

	public void setImg(byte[] path) {
			Img_Path = path;
	}

	public void setStrImg(String img) {
		StrImg = img;
	}

/// Get
	public int getEmpid() {
		return Emp_id;
	}

	public int getCompanyid() {
		return Company_id;
	}
	
	public int getPositionid() {
		return Position_id;
	}
	
	public String getTitle() {
		return Title;
	}

	public String getFirstname() {
		return Firstname;
	}

	public String getLastname() {
		return Lastname;
	}

	public String getBirthdate() {
		return Birth_date;
	}

	public String getGender() {
		return Gender;
	}
	
	public String getPhone() {
		return Phone;
	}
	
	public int getId() {
		return Id;
	}
	
	public String getEmail() {
		return Email;
	}

	public String getAddress() {
		return Address;
	}

	public Integer getRold_id() {
		return Role_id;
	}

	public byte[] getImg_Path() {
		return Img_Path;
	}
	
	public String getRole() {
		return Role;
	}
	public String getPosition() {
		return Position;
	}
	public String getCompany() {
		return Company;
	}

	public String getStrImg() {
		return StrImg;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			Date date = (Date) data.get("Birth_date");
			setEmpid((Integer)data.get("Emp_id"));
			setTitle((String) data.get("Title"));
			setFirstname((String) data.get("Firstname"));
			setLastname((String)data.get("Lastname"));
			setBirth(date.toString());
			setGender((String) data.get("Gender"));
			setPhone((String)data.get("Phone"));
			setId((Integer) data.get("Id"));
			setEmail((String) data.get("Email"));
			setAddress((String)data.get("Address"));
			setRole((Integer) data.get("Role_ID"));
			setImg((byte[]) data.get("Img"));
			setCompanyid((Integer) data.get("Comp_ID"));
			setPositionid((Integer) data.get("Position_ID"));
			String Img = new String(getImg_Path());
			setStrImg(Img);
		}
		else {
			return;
		}
	}
}
