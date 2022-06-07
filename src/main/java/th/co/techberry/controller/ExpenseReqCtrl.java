package th.co.techberry.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseReqCtrl {

    public Map<String, Object> Get_All_Req() throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        List<Map<String, Object>> Data ;
        List<Map<String, Object>> res = new ArrayList<>();
        List<Map<String, Object>> File_List ;
        Map<String, Object> responseBodyStr = new HashMap<>();
        ExpenseReqModel model = new ExpenseReqModel();
        try {
            Data = dbutil.selectAll(connection,"expense_request");
            if(Data != null) {
                for(Map<String, Object> temp : Data){
                    Map<String, Object> ans = new HashMap<>();
                    List<Map<String, Object>> File = new ArrayList<>();
                    model.setModel(temp);
                    File_List = dbutil.selectArray(connection,"expense_request_file","Req_id",Integer.toString(model.getReqId()));
                    if(File_List != null){
                        for(Map<String, Object> temp2 : File_List){
                            Map<String, Object> File_data = new HashMap<>();
                            String data = new String((byte[])temp2.get("data"));
                            File_data.put("id",Integer.toString((Integer)temp2.get("File_id")));
                            File_data.put("data",data);
                            File_data.put("name",temp2.get("File_name"));
                            File.add(File_data);
                        }
                    }
                    ans.put("Req_id",model.getReqId());
                    ans.put("Emp_id",model.getEmpId());
                    ans.put("create_at",model.getCreate());
                    ans.put("File",File);
                    ans.put("status",model.getStatus());
                    ans.put("remark",model.getRemark());
                    ans.put("Detail",model.getDetail());
                    if(model.getCancel() == null){
                        ans.put("cancel_at","");
                    }
                    else{
                        ans.put("cancel_at",model.getCancel());
                    }
                    if(model.getComplete() == null){
                        ans.put("complete_at","");
                    }
                    else{
                        ans.put("complete_at",model.getComplete());
                    }
                    res.add(ans);
                }
            }
            responseBodyStr.put("data",res);
            responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
        }catch(SQLException e) {
            e.printStackTrace();
        }
        responseBodyStr.put("status",200);
        return responseBodyStr;
    }

    public Map<String, Object> Send_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Gson gson = new Gson();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> mailmap = new HashMap<>();
        Map<String, Object> req_data ;
        Map<String, Object> Log_detail ;
        Map<String, Object> File_inDb ;
        List<Map<String, Object>> Employee ;
        Map<String, Object> Role_data ;
        EmployeeModel employee_model = new EmployeeModel();
        ExpenseReqModel req_model = new ExpenseReqModel();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        ArrayList File = (ArrayList)data.get("File");
        try{
            req_model.setStatus(ConfigConstants.REQUESTED);
            req_model.setStrCreate(Time);
            req_model.setDetail((String) data.get("Detail"));
            req_model.setEmpId(id);
            dbutil.AddExpenseReqest(connection,req_model);
            req_data = dbutil.select2con(connection,"expense_request","Emp_id","create_at",
                    Integer.toString(id),Time);
            req_model.setModel(req_data);
            if(File.size() != 0){
                for(Object temp : File){
                    JsonObject jsonObject = gson.toJsonTree(temp).getAsJsonObject();
                    FileModel Data = new Gson().fromJson(jsonObject,FileModel.class);
                    dbutil.Add_ExpenseRequest_File(connection,req_model,Data.getdata(),Data.getname());
                    File_inDb = dbutil.select2con(connection,"expense_request_file",
                            "Req_id","File_name",Integer.toString(req_model.getReqId()),Data.getname());
                    dbutil.Expense_Request_File_Detail_log(connection,(Integer) File_inDb.get("File_id"),Time,Integer.toString(req_model.getReqId()));
                    Log_detail = dbutil.select2con(connection,"expense_request_file_detail_log",
                            "File_id","Time",Integer.toString((Integer) File_inDb.get("File_id")),Time);
                    dbutil.Add_Expense_File_log(connection,"expense_request_file_log","File_id",Integer.toString((Integer) File_inDb.get("File_id")),
                            Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"),req_model.getReqId());
                }
            }
            dbutil.Expense_Request_Detail_log(connection,req_model,Time);
            Log_detail = dbutil.select2con(connection,"expense_request_detail_log",
                    "Req_id","Time",Integer.toString(req_model.getReqId()),Time);
            dbutil.Addlog(connection,"expense_request_log","Req_id",Integer.toString(req_model.getReqId()),
                    Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
            Role_data = dbutil.select(connection,"user_role","Role_Name",ConfigConstants.ROLE_HR_NAME);
            Employee = dbutil.selectArray(connection,"employee","Role_ID",Integer.toString((Integer) Role_data.get("Role_ID")));
            if(Employee != null) {
                for (Map<String, Object> temp : Employee) {
                    MailUtil2 mail = new MailUtil2();
                    employee_model.setModel(temp);
                    mailmap.put("to", employee_model.getFirstname() + " " + employee_model.getLastname());
                    mailmap.put("Detail", ConfigConstants.MESSAGE_IN_EMAIL_EXPENSE_REQUEST_MGMT);
                    mail.sendMail(employee_model.getEmail(), ConfigConstants.SUBJECT_EXPENSE_REQUEST_MAIL,
                            mailmap, ConfigConstants.MAIL_TEMPLATE_EXPENSE_REQUEST);
                }
            }
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,req_model.getStatus());
        } catch(Exception e){
            e.printStackTrace();
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_CONTACT_ADMIN);
        }
        responseBodyStr.put("status",200);
        return responseBodyStr;
    }

    public Map<String, Object> Response_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> mailmap = new HashMap<>();
        Map<String, Object> req_data ;
        Map<String, Object> Log_detail ;
        Map<String, Object> Emp_detail ;
        Map<String, Object> Role_detail ;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ExpenseReqModel req_model = new ExpenseReqModel();
        EmployeeModel emp_model = new EmployeeModel();
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        Boolean Status = Boolean.parseBoolean((String)data.get("Status"));
        try{
            req_data = dbutil.select(connection,"expense_request","Req_id",(String) data.get("Req_id"));
            req_model.setModel(req_data);
            Emp_detail = dbutil.select(connection,"employee","Emp_id",Integer.toString(id));
            emp_model.setModel(Emp_detail);
            Role_detail = dbutil.select(connection,"user_role","Role_ID",Integer.toString(emp_model.getRold_id()));
            if(!data.get("Remark").equals("")){
                req_model.setRemark((String)data.get("Remark"));
            }
            if(Status){
                req_model.setStrComplete(Time);
                req_model.setStatus(ConfigConstants.APPROVED_BY+" "+(String) Role_detail.get("Role_Name"));
                dbutil.Update_Expense_Request(connection,req_model,"complete_at",req_model.getStrComplete());
            }
            else{
                req_model.setStrCancel(Time);
                req_model.setStatus(ConfigConstants.DECLINED_BY+" "+(String) Role_detail.get("Role_Name"));
                dbutil.Update_Expense_Request(connection,req_model,"cancel_at",req_model.getStrCancel());
            }
            dbutil.Expense_Request_Detail_log(connection,req_model,Time);
            dbutil.Update_Log_Status(connection,"expense_request_log","Req_id",Integer.toString(req_model.getReqId()));
            Log_detail = dbutil.select2con(connection,"expense_request_detail_log",
                    "Req_id","Time",Integer.toString(req_model.getReqId()),Time);
            dbutil.Addlog(connection,"expense_request_log","Req_id",Integer.toString(req_model.getReqId()),
                    Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
            MailUtil2 mail = new MailUtil2();
            mailmap.put("to", emp_model.getFirstname() + " " + emp_model.getLastname());
            mailmap.put("Detail", ConfigConstants.MESSAGE_IN_EMAIL_EXPENSE_REQUEST);
            mail.sendMail(emp_model.getEmail(), ConfigConstants.SUBJECT_EXPENSE_REQUEST_MAIL,
                    mailmap, ConfigConstants.MAIL_TEMPLATE_EXPENSE_REQUEST);
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,req_model.getStatus());
        } catch(Exception e){
            e.printStackTrace();
        }
        responseBodyStr.put("status",200);
        return responseBodyStr;
    }

    public Map<String, Object> Cancel_Expense_Request_By_User(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> req_data ;
        Map<String, Object> Log_detail ;
        ExpenseReqModel req_model = new ExpenseReqModel();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        try{
            req_data = dbutil.select(connection,"expense_request","Req_id",(String) data.get("Req_id"));
            req_model.setModel(req_data);
            if(req_model.getStatus().equals(ConfigConstants.REQUESTED)){
                req_model.setStatus(ConfigConstants.CANCELLATION);
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,req_model.getStatus());
            }
            else{
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"Can't Canceled");
            }
            req_model.setStrCancel(Time);
            dbutil.Update_Expense_Request(connection,req_model,"cancel_at",req_model.getStrCancel());
            dbutil.Expense_Request_Detail_log(connection,req_model,Time);
            dbutil.Update_Log_Status(connection,"expense_request_log","Req_id",Integer.toString(req_model.getReqId()));
            Log_detail = dbutil.select2con(connection,"expense_request_detail_log",
                    "Req_id","Time",Integer.toString(req_model.getReqId()),Time);
            dbutil.Addlog(connection,"expense_request_log","Req_id",Integer.toString(req_model.getReqId()),
                    Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
        } catch(SQLException e){
            e.printStackTrace();
        }
        responseBodyStr.put("status",200);
        return responseBodyStr;
    }

    public Map<String, Object> Get_Requested_for_mgmt() throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        List<Map<String, Object>> Data ;
        List<Map<String, Object>> res = new ArrayList<>();
        List<Map<String, Object>> File_List ;
        Map<String, Object> responseBodyStr = new HashMap<>();
        ExpenseReqModel model = new ExpenseReqModel();
        try {
            Data = dbutil.selectArray(connection,"expense_request","status",ConfigConstants.REQUESTED);
            if(Data != null) {
                for(Map<String, Object> temp : Data){
                    Map<String, Object> ans = new HashMap<>();
                    List<Map<String, Object>> File = new ArrayList<>();
                    model.setModel(temp);
                    File_List = dbutil.selectArray(connection,"expense_request_file","Req_id",Integer.toString(model.getReqId()));
                    if(File_List != null){
                        for(Map<String, Object> temp2 : File_List){
                            Map<String, Object> File_data = new HashMap<>();
                            String data = new String((byte[])temp2.get("data"));
                            File_data.put("data",data);
                            File_data.put("id",Integer.toString((Integer)temp2.get("File_id")));
                            File_data.put("name",temp2.get("File_name"));
                            File.add(File_data);
                        }
                    }
                    ans.put("Req_id",model.getReqId());
                    ans.put("Emp_id",model.getEmpId());
                    ans.put("create_at",model.getCreate());
                    ans.put("cancel_at",model.getCancel());
                    ans.put("complete_at",model.getComplete());
                    ans.put("File",File);
                    ans.put("status",model.getStatus());
                    ans.put("remark",model.getRemark());
                    ans.put("Detail",model.getDetail());
                    res.add(ans);
                }
            }
            responseBodyStr.put("data",res);
            responseBodyStr.put("status",200);
            responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return responseBodyStr;
    }

    public Map<String, Object> Get_My_Requested(int id) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        List<Map<String, Object>> Data ;
        List<Map<String, Object>> res = new ArrayList<>();
        List<Map<String, Object>> File_List ;
        Map<String, Object> responseBodyStr = new HashMap<>();
        ExpenseReqModel model = new ExpenseReqModel();
        try {
            Data = dbutil.selectArray(connection,"expense_request","Emp_id",Integer.toString(id));
            if(Data != null) {
                for(Map<String, Object> temp : Data){
                    List<Map<String, Object>> File = new ArrayList<>();
                    Map<String, Object> ans = new HashMap<String, Object>();
                    model.setModel(temp);
                    File_List = dbutil.selectArray(connection,"expense_request_file","Req_id",Integer.toString(model.getReqId()));
                    if(File_List != null){
                        for(Map<String, Object> temp2 : File_List){
                            Map<String, Object> File_data = new HashMap<>();
                            String data = new String((byte[])temp2.get("data"));
                            File_data.put("data",data);
                            File_data.put("id",Integer.toString((Integer)temp2.get("File_id")));
                            File_data.put("name",temp2.get("File_name"));
                            File.add(File_data);
                        }
                    }
                    ans.put("Req_id",model.getReqId());
                    ans.put("Emp_id",model.getEmpId());
                    ans.put("create_at",model.getCreate());
                    ans.put("File",File);
                    ans.put("status",model.getStatus());
                    ans.put("remark",model.getRemark());
                    ans.put("Detail",model.getDetail());
                    if(model.getCancel() == null){
                        ans.put("cancel_at","");
                    }
                    else{
                        ans.put("cancel_at",model.getCancel());
                    }
                    if(model.getComplete() == null){
                        ans.put("complete_at","");
                    }
                    else{
                        ans.put("complete_at",model.getComplete());
                    }
                    res.add(ans);
                }
            }
            responseBodyStr.put("data",res);
            responseBodyStr.put("status",200);
            responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return responseBodyStr;
    }

    public Map<String, Object> Get_Requested_By_Empid(Map<String, Object> data) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        List<Map<String, Object>> Data ;
        List<Map<String, Object>> res = new ArrayList<>();
        List<Map<String, Object>> File_List ;
        Map<String, Object> responseBodyStr = new HashMap<>();
        ExpenseReqModel model = new ExpenseReqModel();
        try {
            Data = dbutil.selectArray(connection,"expense_request","Emp_id",(String) data.get("Emp_id"));
            if(Data != null) {
                for(Map<String, Object> temp : Data){
                    List<Map<String, Object>> File = new ArrayList<>();
                    Map<String, Object> ans = new HashMap<>();
                    model.setModel(temp);
                    File_List = dbutil.selectArray(connection,"expense_request_file","Req_id",Integer.toString(model.getReqId()));
                    if(File_List != null){
                        for(Map<String, Object> temp2 : File_List){
                            Map<String, Object> File_data = new HashMap<>();
                            String file_data = new String((byte[])temp2.get("data"));
                            File_data.put("data",file_data);
                            File_data.put("id",Integer.toString((Integer)temp2.get("File_id")));
                            File_data.put("name",temp2.get("File_name"));
                            File.add(File_data);
                        }
                    }
                    ans.put("Req_id",model.getReqId());
                    ans.put("Emp_id",model.getEmpId());
                    ans.put("create_at",model.getCreate());
                    ans.put("File",File);
                    ans.put("status",model.getStatus());
                    ans.put("remark",model.getRemark());
                    ans.put("Detail",model.getDetail());
                    if(model.getCancel() == null){
                        ans.put("cancel_at","");
                    }
                    else{
                        ans.put("cancel_at",model.getCancel());
                    }
                    if(model.getComplete() == null){
                        ans.put("complete_at","");
                    }
                    else{
                        ans.put("complete_at",model.getComplete());
                    }
                    res.add(ans);
                }
            }
            responseBodyStr.put("data",res);
            responseBodyStr.put("status",200);
            responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return responseBodyStr;
    }

    public Map<String, Object> Update_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> req_data ;
        Map<String, Object> Log_detail ;
        Map<String, Object> File_inDb ;
        Gson gson = new Gson();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ExpenseReqModel req_model = new ExpenseReqModel();
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        String Detail = (String) data.get("Detail");
        ArrayList Target = (ArrayList)data.get("File");
        try{
            req_data = dbutil.select(connection,"expense_request","Req_id",(String) data.get("Req_id"));
            req_model.setModel(req_data);
            if(req_model.getStatus().equals(ConfigConstants.REQUESTED)){
                if(!Detail.equals("")){
                    req_model.setDetail(Detail);
                    dbutil.Update_Expense_Request(connection,req_model,"create_at",req_model.getCreate().toString());
                    dbutil.Expense_Request_Detail_log(connection,req_model,Time);
                    dbutil.Update_Log_Status(connection,"expense_request_log","Req_id",Integer.toString(req_model.getReqId()));
                    Log_detail = dbutil.select2con(connection,"expense_request_detail_log",
                            "Req_id","Time",Integer.toString(req_model.getReqId()),Time);
                    dbutil.Addlog(connection,"expense_request_log","Req_id",Integer.toString(req_model.getReqId()),
                            Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
                }
                dbutil.Delete(connection,"expense_request_file","Req_id",Integer.toString(req_model.getReqId()));
                if(Target.size() != 0){
                    dbutil.Update_Log_Status(connection,"expense_request_file_log","Req_id",Integer.toString(req_model.getReqId()));
                    for(Object temp : Target){
                        JsonObject jsonObject = gson.toJsonTree(temp).getAsJsonObject();
                        FileModel Data = new Gson().fromJson(jsonObject,FileModel.class);
                        dbutil.Add_ExpenseRequest_File(connection,req_model,Data.getdata(),Data.getname());
                        File_inDb = dbutil.select2con(connection,"expense_request_file",
                                "Req_id","File_name",Integer.toString(req_model.getReqId()),Data.getname());
                        dbutil.Expense_Request_File_Detail_log(connection,(Integer) File_inDb.get("File_id"),Time,Integer.toString(req_model.getReqId()));
                        dbutil.Update_Log_Status(connection,"expense_request_file_log","File_id",Integer.toString((Integer) File_inDb.get("File_id")));
                        Log_detail = dbutil.select2con(connection,"expense_request_file_detail_log",
                                "File_id","Time",Integer.toString((Integer) File_inDb.get("File_id")),Time);
                        dbutil.Add_Expense_File_log(connection,"expense_request_file_log","File_id",Integer.toString((Integer) File_inDb.get("File_id")),
                                Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"),req_model.getReqId());
                    }
                }
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
            }
            else{
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_CONTACT_ADMIN);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        responseBodyStr.put("status",200);
        return responseBodyStr;
    }
}
