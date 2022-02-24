package th.co.techberry.model;

import java.util.Map;

public class TeamModel {
	private int Team_id;
	private String Team_name;
	private int Host;
	private int Creator;
    
	public void setTeam_id(int id) {
		Team_id = id;
	}
	
	public void setHost(int id) {
		if(Host != id) {
			Host = id;
		}
	}
	
	public void setCreator(int id) {
		Creator = id;
	}
	
	public void setTeamName(String name) {
		if(name!="") {
			Team_name = name;
		}
	}

/// Get
	public int getTeamId() {
		return Team_id;
	}
	
	public String getTeamName() {
		return Team_name;
	}

	public int getHost() {
		return Host;
	}
	
	public int getCreator() {
		return Creator;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setTeam_id((Integer) data.get("Team_id"));
			setHost((Integer) data.get("Team_Host"));
			setTeamName((String) data.get("Team_name"));
			setCreator((Integer) data.get("Creator"));
		}
		else {
			return;
		}
	}
}
