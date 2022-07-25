package Model;

import java.util.Map;

public class LeaveCountModel {
    private int Emp_id;
    private int Type_ID;
    private int Leaved;

    public void setEmpid(int id) {
        Emp_id = id;
    }

    public void setTypeId(int id) {
        Type_ID = id;
    }

    public void setLeaved(int id) {
        Leaved = id;
    }

    public int getLeaved() {
        return Leaved;
    }

    public int getTypeId() {
        return Type_ID;
    }

    public int getEmpId() {
        return Emp_id;
    }

    public void setModel(Map<String, Object> data) {
        if(data != null) {
            setLeaved((Integer) data.get("Leaved"));
            setTypeId((Integer) data.get("Type_ID"));
            setEmpid((Integer) data.get("Emp_id"));
        }
        else {
            return;
        }
    }
}
