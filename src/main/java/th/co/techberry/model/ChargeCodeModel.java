package th.co.techberry.model;

import java.util.Map;

public class ChargeCodeModel {

	private int Charge_Code_id;
	private String Charge_Code_Name;
	private String description;
    
	public void setChargeCodeId(int id) {
		Charge_Code_id = id;
	}
	
	public void setChargeCodeName(String name) {
		Charge_Code_Name = name;
	}

	public void setDescription(String data) {
		description = data;
	}
	
/// Get
	public int getChargeCodeId() {
		return Charge_Code_id;
	}
	
	public String getChargeCodeName() {
		return Charge_Code_Name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setChargeCodeId((Integer) data.get("Charge_code_id"));
			setChargeCodeName((String) data.get("Charge_code_name"));
			setDescription((String) data.get("description"));
		}
		else {
			return;
		}
	}
}
