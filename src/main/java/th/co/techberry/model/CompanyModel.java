package th.co.techberry.model;

import java.util.Map;

public class CompanyModel {

    private int Comp_ID;
    private String Company_Name;

    public void setCompId(int id) {
        Comp_ID = id;
    }

    public void setCompanyName(String name) {
        Company_Name = name;
    }

    /// Get
    public int getCompId() {
        return Comp_ID;
    }

    public String getCompanyName() {
        return Company_Name;
    }

    public void setModel(Map<String, Object> data) {
        if(data != null) {
            setCompId((Integer) data.get("Comp_ID"));
            setCompanyName((String) data.get("Company_Name"));
        }
        else {
            return;
        }
    }
}
