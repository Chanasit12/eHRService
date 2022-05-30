package th.co.techberry.model;

import java.util.Date;
import java.util.Map;

public class ExpenseReqModel {
    private int Req_id;
    private int Emp_id;
    private String Detail;
    private Date create_at;
    private Date cancel_at;
    private Date complete_at;
    private String status;
    private String remark;
    private String Str_create_at;
    private String Str_cancel_at;
    private String Str_complete_at;

    public void setReqId(int id) {
        Req_id = id;
    }

    public void setEmpId(int id) {
        Emp_id = id;
    }

    public void setStatus(String data) {
        status = data;
    }

    public void setRemark(String data) {
        remark = data;
    }

    public void setDetail(String data) {
        Detail = data;
    }

    public void setCreate(Date time) {
        create_at = time;
    }

    public void setCancel(Date time) {
        cancel_at = time;
    }

    public void setComplete(Date time) {
        complete_at = time;
    }

    public void setStrCreate(String data) {
        Str_create_at = data;
    }

    public void setStrCancel(String data) {
        Str_cancel_at = data;
    }

    public void setStrComplete(String data) {
        Str_complete_at = data;
    }
//get

    public int getReqId() {
        return Req_id;
    }

    public int getEmpId() {
        return Emp_id;
    }

    public String getDetail() {
        return Detail;
    }

    public String getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    public Date getCreate() {
        return create_at;
    }

    public Date getCancel() {
        return cancel_at;
    }

    public Date getComplete() {
        return complete_at;
    }

    public String getStrCreate() {
        return Str_create_at;
    }

    public String getStrCancel() {
        return Str_cancel_at;
    }

    public String getStrComplete() {
        return Str_complete_at;
    }

    public void setModel(Map<String, Object> data) {
        if(data != null) {
            setReqId((Integer) data.get("Req_id"));
            setEmpId((Integer) data.get("Emp_id"));
            setDetail((String) data.get("Detail"));
//            setFile((String) data.get("File"));
            setStatus((String) data.get("status"));
            setRemark((String) data.get("remark"));
            setCreate((Date)data.get("create_at"));
            setCancel((Date)data.get("cancel_at"));
            setComplete((Date)data.get("complete_at"));
            if(getCancel() != null){
                setStrCancel(getCancel().toString());
            }
            if(getComplete() != null){
                setStrComplete(getComplete().toString());
            }
        }
        else {
            return;
        }
    }


}
