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
	
	static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
	
	public Map<String, Object> News() throws SQLException, ClassNotFoundException ,UnsupportedEncodingException{
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> News = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		LocalDateTime currentDateTime = LocalDateTime.now();
		String formattedDateTime = currentDateTime.format(formatter);
		NewsModel news_model = new NewsModel();
		EmployeeModel emp_model = new EmployeeModel();

		try{
			News = dbutil.selectNews(connection,"news",formattedDateTime);
			if(News != null) {
				for(Map<String, Object> temp :  News) {
					Map<String, Object> ans = new HashMap<String, Object>();
					news_model.setModel(temp);
					Emp_info = (dbutil.select(connection,"Employee","Emp_id",Integer.toString(news_model.getCreator())));
					emp_model.setModel(Emp_info);
					PrintStream ps = new PrintStream(System.out, true, StandardCharsets.UTF_8.name());
					ps.println("news_model.getDetail() "+ news_model.getDetail());
					ans.put("News_id",String.valueOf(news_model.getId()));
					ans.put("Detail",news_model.getDetail());
					ans.put("Topic",news_model.getTopic());
					ans.put("Img",news_model.getImg());
					ans.put("Creator",emp_model.getFirstname()+" "+emp_model.getLastname());
					ans.put("Date",news_model.getCreatedate());
					ans.put("Start",news_model.getStart());
					ans.put("End",news_model.getEnd());
					res.add(ans);
			}
		}
		}catch(SQLException e){
			e.printStackTrace();
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		responseBodyStr.put("data",res);
		responseBodyStr.put("status",200);
		responseBodyStr.put("Message","success");
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_News(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		NewsModel new_model = new NewsModel();
		Map<String, Object> result = new HashMap<String, Object>();
		LocalDateTime currentDateTime = LocalDateTime.now();
		Timestamp Create_date = Timestamp.valueOf(currentDateTime);
		String Detail = (String) data.get("Detail");
		String Topic = (String) data.get("Topic");
		Topic = Topic.replace("\"","\\\"");
		Topic = Topic.replace("\'","\\\'");
		Detail = Detail.replace("\"","\\\"");
		Detail = Detail.replace("\'","\\\'");
//		Detail = Detail.replace(","," ");
		System.out.println("Detail "+Detail);
		System.out.println("Topic "+Topic);
		new_model.setDetail(Detail);
		new_model.setTopic(Topic);
		new_model.setImg((String) data.get("Img"));
		new_model.setStart(Timestamp.valueOf((String) data.get("start_at")));
		new_model.setEnd(Timestamp.valueOf((String) data.get("end_at")));
		new_model.setImg((String) data.get("Img"));
		new_model.setCreator(id);
		new_model.setCreatedate(Create_date);
		try{
			dbutil.AddNews(connection,new_model);
		}catch (SQLException e){
			e.printStackTrace();
		}
		result.put("status",200);
		result.put("message","Add success");
		return result;
	}
	
	public Map<String, Object> Update_News(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> News_info = new HashMap<String, Object>();
		NewsModel new_model = new NewsModel();
		try{
			News_info = (dbutil.select(connection,"news","News_id",(String)data.get("News_id")));
			new_model.setModel(News_info);
			if(!data.get("Detail").equals("")){
				new_model.setDetail((String)data.get("Detail"));
			}
			if(!data.get("Topic").equals("")){
				new_model.setTopic((String)data.get("Topic"));
			}
			if(!data.get("Img").equals("")){
				new_model.setImg((String)data.get("Img"));
			}
			if(!data.get("start_at").equals("")){
				new_model.setStart(Timestamp.valueOf((String)data.get("start_at")));
			}
			if(!data.get("end_at").equals("")){
				new_model.setEnd(Timestamp.valueOf((String)data.get("end_at")));
			}
			dbutil.UpdateNews(connection,new_model);
		} catch(SQLException e){
			e.printStackTrace();
		}
		result.put("status",200);
		result.put("message","Update success");
		return result;
	}
	
	public Map<String, Object> Delete_News(Map<String, Object> data) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		Map<String, Object> result = new HashMap<String, Object>();
		List Target = new ArrayList();
		Target = ((ArrayList)data.get("Value"));
		int size = 0;
		if(!Target.isEmpty()) {
			while(Target.size() > size) {
				dbutil.Delete(connection,"news","News_id",(String)Target.get(size));
				size++;
			}
			result.put("status",200);
			result.put("message","Delete Complete");
		}
		else {
			result.put("status",400);
			result.put("message","Delete Fail");
		}
		return result;
	}
	
	public Map<String, Object> NewsAll() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> News = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		NewsModel news_model = new NewsModel();
		EmployeeModel emp_model = new EmployeeModel();
		LocalDateTime currentDateTime = LocalDateTime.now();
		String formattedDateTime = currentDateTime.format(formatter);
		try{
			News = dbutil.selectAll(connection,"news");
			if(News != null) {
				for(Map<String, Object> temp :  News) {
					Map<String, Object> ans = new HashMap<String, Object>();
					news_model.setModel(temp);
					Emp_info = (dbutil.select(connection,"Employee","Emp_id",Integer.toString(news_model.getCreator())));
					emp_model.setModel(Emp_info);
					ans.put("News_id",String.valueOf(news_model.getId()));
					ans.put("Detail",news_model.getDetail());
					ans.put("Topic",news_model.getTopic());
					ans.put("Img",news_model.getImg());
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
			responseBodyStr.put("Message","Success");
		return responseBodyStr;
	}
}
