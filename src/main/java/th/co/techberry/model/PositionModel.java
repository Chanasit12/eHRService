package th.co.techberry.model;

import java.util.Map;
public class PositionModel {
    private int Position_ID;
    private String Position_Name;

    public void setPositionId(int id) {
        Position_ID = id;
    }

    public void setPositionName(String name) {
        Position_Name = name;
    }

    // Get

    public int getPositionId() {
        return Position_ID;
    }

    public String getPositionName() {
        return Position_Name;
    }

    public void setModel(Map<String, Object> data) {
        if(data != null) {
            setPositionId((Integer) data.get("Position_ID"));
            setPositionName((String) data.get("Position_Name"));
        }
        else {
            return;
        }
    }

}
