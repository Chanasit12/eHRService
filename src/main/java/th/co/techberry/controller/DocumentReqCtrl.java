package th.co.techberry.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import th.co.techberry.util.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DocumentReqCtrl {

    public Map<String, Object> Get_Document_Type() throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Map<String, Object> responseBodyStr = new HashMap<>();
        List<Map<String, Object>> Document_type ;
        List<Map<String, Object>> res = new ArrayList<>();
        DocumentTypeModel model = new DocumentTypeModel();
        try{
            Document_type = dbutil.selectAll(connection,"document_type");
            for( Map<String, Object> temp : Document_type){
                Map<String, Object> ans = new HashMap<>();
                model.setModel(temp);
                ans.put("Type_Id",model.getId());
                ans.put("Type_name",model.getName());
                res.add(ans);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        responseBodyStr.put("data",res);
        responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.RESPONSE_KEY_SUCCESS);
        return responseBodyStr;
    }

    public Map<String, Object> Get_Document_Type_By_Type_Id(Map<String, Object> data) throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> Document_type ;
        DocumentTypeModel model = new DocumentTypeModel();
        try{
            Document_type = dbutil.select(connection,"document_type","Type_ID",(String) data.get("Type_ID"));
            model.setModel(Document_type);
            responseBodyStr.put("Type_Id",model.getId());
            responseBodyStr.put("Type_name",model.getName());
        }catch (SQLException e){
            e.printStackTrace();
        }
        responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.RESPONSE_KEY_SUCCESS);
        return responseBodyStr;
    }

    public Map<String, Object> Add_Document_Type(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> Log_detail ;
        Map<String, Object> Document_type ;
        DocumentTypeModel model = new DocumentTypeModel();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        String Type_name = (String) data.get("Type_name");
        try{
            dbutil.AddDocumentType(connection,Type_name);
            Document_type = dbutil.select(connection,"document_type","Type_name",Type_name);
            model.setModel(Document_type);
            dbutil.Document_Type_Detail_log(connection,model,Time);
            Log_detail = dbutil.select2con(connection,"document_type_detail_log",
                    "Type_ID","Time",Integer.toString(model.getId()),Time);
            dbutil.Addlog(connection,"document_type_log","Type_ID",Integer.toString(model.getId()),
                    Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
        }catch (SQLException e){
            e.printStackTrace();
        }
        responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.RESPONSE_KEY_SUCCESS);
        return responseBodyStr;
    }

    public Map<String, Object> Update_Document_Type(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> Log_detail ;
        Map<String, Object> Document_type ;
        DocumentTypeModel model = new DocumentTypeModel();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        try{
            Document_type = dbutil.select(connection,"document_type","Type_ID",(String) data.get("Type_ID"));
            model.setModel(Document_type);
            if(!data.get("Type_name").equals("")){
                model.setName((String)data.get("Type_name"));
                dbutil.UpdateDocumentType(connection,model);
                dbutil.Update_Log_Status(connection,"document_type_log","Type_ID",Integer.toString(model.getId()));
                dbutil.Document_Type_Detail_log(connection,model,Time);
                Log_detail = dbutil.select2con(connection,"document_type_detail_log",
                        "Type_ID","Time",Integer.toString(model.getId()),Time);
                dbutil.Addlog(connection,"document_type_log","Type_ID",Integer.toString(model.getId()),
                        Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.RESPONSE_KEY_SUCCESS);
            }
            else{
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.PLEASE_INPUT_REQUIRED_FIELD);
            }
        }catch (SQLException e){
            e.printStackTrace();
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
        }
        return responseBodyStr;
    }

    public Map<String, Object> Delete_Document_Type(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        List<Map<String, Object>> Document ;
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> Document_type ;
        DocumentTypeModel model = new DocumentTypeModel();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        try{
            Document = dbutil.selectArray(connection,"document_request","Type_ID",(String) data.get("Type_ID"));
            if(Document == null){
                Document_type = dbutil.select(connection,"document_type","Type_ID",(String) data.get("Type_ID"));
                model.setModel(Document_type);
                dbutil.UpdateDocumentType(connection,model);
                dbutil.Update_Log_Status(connection,"document_type_log","Type_ID",Integer.toString(model.getId()));
                dbutil.Addlog(connection,"document_type_log","Type_ID",Integer.toString(model.getId()),
                        Time, Integer.toString(id),"1","Delete",0);
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.RESPONSE_KEY_SUCCESS);
                dbutil.Delete(connection,"document_type","Type_ID",(String) data.get("Type_ID"));
            }
            else{
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"There are still have document request in this type");
            }
        }catch (SQLException e){
            e.printStackTrace();
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE, ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
        }
        return responseBodyStr;
    }

    public Map<String, Object> Get_All_Document_Request() throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        List<Map<String, Object>> Data ;
        List<Map<String, Object>> res = new ArrayList<>();
        List<Map<String, Object>> File_List ;
        Map<String, Object> responseBodyStr = new HashMap<>();
        DocumentReqModel req_model = new DocumentReqModel();
        try {
            Data = dbutil.selectAll(connection,"document_request");
            if(Data != null) {
                for(Map<String, Object> temp : Data){
                    Map<String, Object> ans = new HashMap<>();
                    List<Map<String, Object>> File = new ArrayList<>();
                    req_model.setModel(temp);
                    File_List = dbutil.selectArray(connection,"document_request_file","Req_id",Integer.toString(req_model.getReqId()));
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
                    ans.put("Req_id",req_model.getReqId());
                    ans.put("Emp_id",req_model.getEmpId());
                    ans.put("Type_ID",req_model.getTypeId());
                    ans.put("create_at",req_model.getCreate());
                    ans.put("File",File);
                    ans.put("status",req_model.getStatus());
                    ans.put("remark",req_model.getRemark());
                    ans.put("Detail",req_model.getDetail());
                    if(req_model.getCancel() == null){
                        ans.put("cancel_at","");
                    }
                    else{
                        ans.put("cancel_at",req_model.getCancel());
                    }
                    if(req_model.getComplete() == null){
                        ans.put("complete_at","");
                    }
                    else{
                        ans.put("complete_at",req_model.getComplete());
                    }
                    res.add(ans);
                }
            }
            responseBodyStr.put("data",res);
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return responseBodyStr;
    }

    public Map<String, Object> Send_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Gson gson = new Gson();
        List<Map<String, Object>> Employee ;
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> req_data ;
        Map<String, Object> Log_detail ;
        Map<String, Object> File_inDb ;
        Map<String, Object> mailmap = new HashMap<>();
        Map<String, Object> Role_data ;
        DocumentReqModel req_model = new DocumentReqModel();
        EmployeeModel emp_model = new EmployeeModel();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        ArrayList File = (ArrayList)data.get("File");
        try{
            req_model.setStatus(ConfigConstants.REQUESTED);
            req_model.setStrCreate(Time);
            req_model.setDetail((String) data.get("Detail"));
            req_model.setEmpId(id);
            req_model.setTypeId(Integer.valueOf((String) data.get("Type_ID")));
            dbutil.AddDocumentReqest(connection,req_model);
            req_data = dbutil.select2con(connection,"document_request","Emp_id","create_at",
                    Integer.toString(id),Time);
            req_model.setModel(req_data);
            if(File.size() != 0){
                for(Object temp : File){
                    JsonObject jsonObject = gson.toJsonTree(temp).getAsJsonObject();
                    FileModel Data = new Gson().fromJson(jsonObject,FileModel.class);
                    dbutil.Add_DocumentRequest_File(connection,Integer.toString(req_model.getReqId()),Data.getdata(),Data.getname());
                    File_inDb = dbutil.select2con(connection,"document_request_file",
                            "Req_id","File_name",Integer.toString(req_model.getReqId()),Data.getname());
                    dbutil.Document_Request_File_Detail_log(connection,(Integer) File_inDb.get("File_id"),Time);
                    Log_detail = dbutil.select2con(connection,"document_request_file_detail_log",
                            "File_id","Time",Integer.toString((Integer) File_inDb.get("File_id")),Time);
                    dbutil.Add_Document_File_log(connection,"document_request_file_log","File_id",Integer.toString((Integer) File_inDb.get("File_id")),
                            Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"),req_model.getReqId());
                }
            }
            dbutil.Document_Request_Detail_log(connection,req_model,Time);
            Log_detail = dbutil.select2con(connection,"document_request_detail_log",
                    "Req_id","Time",Integer.toString(req_model.getReqId()),Time);
            dbutil.Addlog(connection,"document_request_log","Req_id",Integer.toString(req_model.getReqId()),
                    Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
            Role_data = dbutil.select(connection,"user_role","Role_Name",ConfigConstants.ROLE_HR_NAME);
            Employee = dbutil.selectArray(connection,"employee","Role_ID",Integer.toString((Integer) Role_data.get("Role_ID")));
            if(Employee != null) {
                for (Map<String, Object> temp : Employee) {
                    MailUtil mail = new MailUtil();
                    emp_model.setModel(temp);
                    mailmap.put("to", emp_model.getFirstname() + " " + emp_model.getLastname());
                    mailmap.put("Detail", ConfigConstants.MESSAGE_IN_EMAIL_DOCUMENT_REQUEST_MGMT);
                    mail.sendMail(emp_model.getEmail(), ConfigConstants.SUBJECT_DOCUMENT_REQUEST_MAIL,
                            mailmap, ConfigConstants.MAIL_TEMPLATE_DOCUMENT_REQUEST_MGMT);
                }
            }
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,req_model.getStatus());
        } catch(SQLException e){
            e.printStackTrace();
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_CONTACT_ADMIN);
        } catch (Exception e) {
            e.printStackTrace();
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.PLEASE_CONTACT_ADMIN);
        }
        responseBodyStr.put("status",200);
        return responseBodyStr;
    }

    public Map<String, Object> Response_Request(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Gson gson = new Gson();
        List<Map<String, Object>> List_File = new ArrayList<>();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> mailmap = new HashMap<>();
        Map<String, Object> req_data ;
        Map<String, Object> Log_detail ;
        Map<String, Object> Emp_detail ;
        Map<String, Object> Role_detail ;
        MailUtil mail = new MailUtil();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DocumentReqModel req_model = new DocumentReqModel();
        EmployeeModel emp_model = new EmployeeModel();
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        Boolean Status = Boolean.parseBoolean((String)data.get("Status"));
        ArrayList File = (ArrayList)data.get("File");
        try{
            req_data = dbutil.select(connection,"document_request","Req_id",(String) data.get("Req_id"));
            req_model.setModel(req_data);
            Emp_detail = dbutil.select(connection,"employee","Emp_id",Integer.toString(id));
            emp_model.setModel(Emp_detail);
            Role_detail = dbutil.select(connection,"user_role","Role_ID",Integer.toString(emp_model.getRold_id()));
            if(!data.get("Remark").equals("")){
                req_model.setRemark((String)data.get("Remark"));
            }
            if(Status){
                req_model.setStrComplete(Time);
                req_model.setStatus(ConfigConstants.APPROVED_BY+" "+Role_detail.get("Role_Name"));
                dbutil.Update_Document_Request(connection,req_model,"complete_at",req_model.getStrComplete());
                Emp_detail = dbutil.select(connection,"employee","Emp_id",Integer.toString(req_model.getEmpId()));
                emp_model.setModel(Emp_detail);
                mailmap.put("to", emp_model.getFirstname() + " " + emp_model.getLastname());
                mailmap.put("Detail",ConfigConstants.MESSAGE_IN_EMAIL_DOCUMENT_REQUEST);
                mailmap.put("Remark",req_model.getRemark());
                if(File.size() != 0){
                    System.out.println("Ch "+1);
                    for(Object temp : File){
                        Map<String, Object> File_detail  = new HashMap<>();
                        JsonObject jsonObject = gson.toJsonTree(temp).getAsJsonObject();
                        FileModel Data = new Gson().fromJson(jsonObject,FileModel.class);
                        File_detail.put("name",Data.getname());
                        File_detail.put("data",Data.getdata());
                        List_File.add(File_detail);
                    }
                }
                System.out.println("File_detail :"+File);
                mail.sendMailWIthFile(emp_model.getEmail(), ConfigConstants.SUBJECT_DOCUMENT_REQUEST_MAIL,
                        mailmap, ConfigConstants.MAIL_TEMPLATE_DOCUMENT_REQUEST,List_File);
            }
            else{
                req_model.setStrCancel(Time);
                req_model.setStatus(ConfigConstants.DECLINED_BY+" "+Role_detail.get("Role_Name"));
                dbutil.Update_Document_Request(connection,req_model,"cancel_at",req_model.getStrCancel());
            }
            dbutil.Document_Request_Detail_log(connection,req_model,Time);
            dbutil.Update_Log_Status(connection,"document_request_log","Req_id",Integer.toString(req_model.getReqId()));
            Log_detail = dbutil.select2con(connection,"document_request_detail_log",
                    "Req_id","Time",Integer.toString(req_model.getReqId()),Time);
            dbutil.Addlog(connection,"document_request_log","Req_id",Integer.toString(req_model.getReqId()),
                    Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
            responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,req_model.getStatus());
        } catch(Exception e){
            e.printStackTrace();
        }
        responseBodyStr.put("status",200);
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DocumentReqModel req_model = new DocumentReqModel();
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        String Detail = (String) data.get("Detail");
        String Type_Id = (String) data.get("Type_ID");
        ArrayList Target = (ArrayList)data.get("File");
        try{
            req_data = dbutil.select(connection,"document_request","Req_id",(String) data.get("Req_id"));
            req_model.setModel(req_data);
            if(req_model.getStatus().equals(ConfigConstants.REQUESTED)){
                if(!Detail.equals("")){
                    req_model.setDetail(Detail);
                }
                if(!Type_Id.equals("")){
                    req_model.setTypeId(Integer.valueOf(Type_Id));
                }
                dbutil.Update_Document_Request(connection,req_model,"create_at",req_model.getCreate().toString());
                dbutil.Document_Request_Detail_log(connection,req_model,Time);
                dbutil.Update_Log_Status(connection,"document_request_log","Req_id",Integer.toString(req_model.getReqId()));
                Log_detail = dbutil.select2con(connection,"document_request_detail_log",
                        "Req_id","Time",Integer.toString(req_model.getReqId()),Time);
                dbutil.Addlog(connection,"document_request_log","Req_id",Integer.toString(req_model.getReqId()),
                        Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
                dbutil.Delete(connection,"document_request_file","Req_id",Integer.toString(req_model.getReqId()));
                if(Target.size() != 0){
                    dbutil.Update_Log_Status(connection,"document_request_file_log","Req_id",Integer.toString(req_model.getReqId()));
                    for(Object temp : Target){
                        JsonObject jsonObject = gson.toJsonTree(temp).getAsJsonObject();
                        FileModel Data = new Gson().fromJson(jsonObject,FileModel.class);
                        dbutil.Add_DocumentRequest_File(connection,Integer.toString(req_model.getReqId()),Data.getdata(),Data.getname());
                        File_inDb = dbutil.select2con(connection,"document_request_file",
                                "Req_id","File_name",Integer.toString(req_model.getReqId()),Data.getname());
                        dbutil.Document_Request_File_Detail_log(connection,(Integer) File_inDb.get("File_id"),Time);
                        dbutil.Update_Log_Status(connection,"document_request_file_log","File_id",Integer.toString((Integer) File_inDb.get("File_id")));
                        Log_detail = dbutil.select2con(connection,"document_request_file_detail_log",
                                "File_id","Time",Integer.toString((Integer) File_inDb.get("File_id")),Time);
                        dbutil.Add_Document_File_log(connection,"document_request_file_log","File_id",Integer.toString((Integer) File_inDb.get("File_id")),
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

    public Map<String, Object> Cancel_Request_By_User(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException{
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        Map<String, Object> responseBodyStr = new HashMap<>();
        Map<String, Object> req_data ;
        Map<String, Object> Log_detail ;
       DocumentReqModel req_model = new DocumentReqModel();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        try{
            req_data = dbutil.select(connection,"document_request","Req_id",(String) data.get("Req_id"));
            req_model.setModel(req_data);
            if(req_model.getStatus().equals(ConfigConstants.REQUESTED)){
                req_model.setStatus(ConfigConstants.CANCELLATION);
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,req_model.getStatus());
            }
            else{
                responseBodyStr.put(ConfigConstants.RESPONSE_KEY_MESSAGE,"Can't Canceled");
            }
            req_model.setStrCancel(Time);
            dbutil.Update_Document_Request(connection,req_model,"cancel_at",req_model.getStrCancel());
            dbutil.Document_Request_Detail_log(connection,req_model,Time);
            dbutil.Update_Log_Status(connection,"document_request_log","Req_id",Integer.toString(req_model.getReqId()));
            Log_detail = dbutil.select2con(connection,"document_request_detail_log",
                    "Req_id","Time",Integer.toString(req_model.getReqId()),Time);
            dbutil.Addlog(connection,"document_request_log","Req_id",Integer.toString(req_model.getReqId()),
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
        DocumentReqModel model = new DocumentReqModel();
        try {
            Data = dbutil.selectArray(connection,"document_request","status",ConfigConstants.REQUESTED);
            if(Data != null) {
                for(Map<String, Object> temp : Data){
                    Map<String, Object> ans = new HashMap<>();
                    List<Map<String, Object>> File = new ArrayList<>();
                    model.setModel(temp);
                    File_List = dbutil.selectArray(connection,"document_request_file","Req_id",Integer.toString(model.getReqId()));
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
                    ans.put("Type_ID",model.getTypeId());
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
        DocumentReqModel model = new DocumentReqModel();
        try {
            Data = dbutil.selectArray(connection,"document_request","Emp_id",Integer.toString(id));
            if(Data != null) {
                for(Map<String, Object> temp : Data){
                    List<Map<String, Object>> File = new ArrayList<>();
                    Map<String, Object> ans = new HashMap<>();
                    model.setModel(temp);
                    File_List = dbutil.selectArray(connection,"document_request_file","Req_id",Integer.toString(model.getReqId()));
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
                    ans.put("Type_ID",model.getTypeId());
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
        DocumentReqModel model = new DocumentReqModel();
        try {
            Data = dbutil.selectArray(connection,"document_request","Emp_id",(String) data.get("Emp_id"));
            if(Data != null) {
                for(Map<String, Object> temp : Data){
                    List<Map<String, Object>> File = new ArrayList<>();
                    Map<String, Object> ans = new HashMap<>();
                    model.setModel(temp);
                    File_List = dbutil.selectArray(connection,"document_request_file","Req_id",Integer.toString(model.getReqId()));
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
                    ans.put("Type_ID",model.getTypeId());
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
}
