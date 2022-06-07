package th.co.techberry.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.Connection;
import th.co.techberry.constants.ConfigConstants;
import th.co.techberry.model.*;
import th.co.techberry.util.DatabaseUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
public class NewsCtrl {

	public Map<String, Object> News() throws SQLException, ClassNotFoundException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> News ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> Emp_info ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		NewsModel news_model = new NewsModel();
		EmployeeModel emp_model = new EmployeeModel();

		try{
			News = dbutil.selectNews(connection,"news",Time);
			if(News != null) {
				for(Map<String, Object> temp :  News) {
					Map<String, Object> ans = new HashMap<>();
					news_model.setModel(temp);
					Emp_info = (dbutil.select(connection,"employee","Emp_id",Integer.toString(news_model.getCreator())));
					emp_model.setModel(Emp_info);
					System.out.println("news_model.getImg() "+news_model.getImg());
					String data = new String((byte[])temp.get("Img"));
					System.out.println("data "+data);
					ans.put("News_id",String.valueOf(news_model.getId()));
					ans.put("Detail",news_model.getDetail());
					ans.put("Topic",news_model.getTopic());
					ans.put("Img",data);
					ans.put("Creator",emp_model.getFirstname()+" "+emp_model.getLastname());
					ans.put("Date",news_model.getCreatedate());
					ans.put("Start",news_model.getStart());
					ans.put("End",news_model.getEnd());
					res.add(ans);
			}
		}
		}catch(SQLException e){
			e.printStackTrace();
		}
		responseBodyStr.put("data",res);
		responseBodyStr.put("status",200);
		responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_News(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> Log_detail ;
		Map<String, Object> News_info ;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		NewsModel new_model = new NewsModel();
		String Detail = (String) data.get("Detail");
		String Topic = (String) data.get("Topic");
		Topic = Topic.replace("\"","\\\"");
		Topic = Topic.replace("\'","\\\'");
		Detail = Detail.replace("\"","\\\"");
		Detail = Detail.replace("\'","\\\'");
		new_model.setDetail(Detail);
		new_model.setTopic(Topic);
		new_model.setStrCreatedate(Time);
		new_model.setStrStart((String) data.get("start_at"));
		new_model.setStrEnd((String) data.get("end_at"));
		new_model.setStrImg((String) data.get("Img"));
		new_model.setCreator(id);
		System.out.println("id :"+id);
		try{
			dbutil.AddNews(connection,new_model);
			News_info = dbutil.select2con(connection,"news","Topic","Create_date",new_model.getTopic(),new_model.getStrCreatedate());
			new_model.setModel(News_info);
			dbutil.News_Detail_log(connection,new_model.getId(),Time);
			Log_detail = dbutil.select2con(connection,"news_detail_log",
					"News_id","Time",Integer.toString(new_model.getId()),Time);
			dbutil.Addlog(connection,"news_log","News_id",Integer.toString(new_model.getId()),
					Time, Integer.toString(id),"1","Add",(Integer)Log_detail.get("Log_id"));
		}catch (SQLException e){
			e.printStackTrace();
		}
		result.put("status",200);
		result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		return result;
	}
	
	public Map<String, Object> Update_News(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> News_info ;
		Map<String, Object> Log_detail ;
		NewsModel new_model = new NewsModel();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Detail = (String) data.get("Detail");
		String Topic = (String) data.get("Topic");
		Topic = Topic.replace("\"","\\\"");
		Topic = Topic.replace("\'","\\\'");
		Detail = Detail.replace("\"","\\\"");
		Detail = Detail.replace("\'","\\\'");
		String Time = dtf.format(now);
		try{
			News_info = (dbutil.select(connection,"news","News_id",(String)data.get("News_id")));
			new_model.setModel(News_info);
			if(!data.get("Detail").equals("")){
				new_model.setDetail(Detail);
			}
			if(!data.get("Topic").equals("")){
				new_model.setTopic(Topic);
			}
			if(!data.get("Img").equals("")){
				new_model.setStrImg((String)data.get("Img"));
			}
			if(!data.get("start_at").equals("")){
				new_model.setStrStart((String)data.get("start_at"));
			}
			if(!data.get("end_at").equals("")){
				new_model.setStrEnd((String)data.get("end_at"));
			}
			dbutil.UpdateNews(connection,new_model);
			dbutil.News_Detail_log(connection,new_model.getId(),Time);
			dbutil.Update_Log_Status(connection,"news_log","News_id",Integer.toString(new_model.getId()));
			Log_detail = dbutil.select2con(connection,"news_detail_log",
					"News_id","Time",Integer.toString(new_model.getId()),Time);
			dbutil.Addlog(connection,"news_log","News_id",Integer.toString(new_model.getId()),
					Time, Integer.toString(id),"1","Update",(Integer)Log_detail.get("Log_id"));
		} catch(SQLException e){
			e.printStackTrace();
		}
		result.put("status",200);
		result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		return result;
	}
	
	public Map<String, Object> Delete_News(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<>();
		ArrayList<String> Target = (ArrayList)data.get("Value");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String Time = dtf.format(now);
		if(!Target.isEmpty()) {
			for(String temp : Target){
				dbutil.Delete(connection,"news","News_id",temp);
				dbutil.Update_Log_Status(connection,"news_log","News_id",temp);
				dbutil.Addlog(connection,"news_log","News_id",temp,
						Time, Integer.toString(id),"1","Delete",0);
			}
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_SUCCESS);
		}
		else {
			result.put(ConfigConstants.RESPONSE_KEY_MESSAGE,ConfigConstants.RESPONSE_KEY_ERROR_MESSAGE);
		}
		result.put("status",200);
		return result;
	}
	
	public Map<String, Object> NewsAll() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> News ;
		List<Map<String, Object>> res = new ArrayList<>();
		Map<String, Object> Emp_info ;
		Map<String, Object> responseBodyStr = new HashMap<>();
		NewsModel news_model = new NewsModel();
		EmployeeModel emp_model = new EmployeeModel();
		try{
			News = dbutil.selectAll(connection,"news");
			if(News != null) {
				for(Map<String, Object> temp :  News) {
					Map<String, Object> ans = new HashMap<>();
					news_model.setModel(temp);
					String Img = new String(news_model.getImg());
					Emp_info = (dbutil.select(connection,"employee","Emp_id",
							Integer.toString(news_model.getCreator())));
					emp_model.setModel(Emp_info);
					ans.put("News_id",String.valueOf(news_model.getId()));
					ans.put("Detail",news_model.getDetail());
					ans.put("Topic",news_model.getTopic());
					ans.put("Img",Img);
					ans.put("Creator",emp_model.getFirstname()+" "+emp_model.getLastname());
					ans.put("Date",news_model.getCreatedate());
					ans.put("Start",news_model.getStart());
					ans.put("End",news_model.getEnd());
					res.add(ans);
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message",ConfigConstants.RESPONSE_KEY_SUCCESS);
		return responseBodyStr;
	}
}
