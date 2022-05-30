package th.co.techberry.model;

import java.util.Map;

public class DocumentTypeModel {

    private int Type_ID;
    private String Type_name;

    public void setType_id(int id) {
        Type_ID = id;
    }

    public void setName(String name) {
            Type_name = name;
    }

    /// Get
    public int getId() { return Type_ID;}

    public String getName() {
        return Type_name;
    }

    public void setModel(Map<String, Object> data) {
        if(data != null) {
            setType_id((Integer) data.get("Type_ID"));
            setName((String) data.get("Type_name"));
        }
        else {
            return;
        }
    }
}

