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
public class NewsCtrl {
	
	static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
	
	public Map<String, Object> News() throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> News = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> Emp_info = new HashMap<String, Object>();
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		LocalDateTime currentDateTime = LocalDateTime.now();
		String formattedDateTime = currentDateTime.format(formatter);
		News = dbutil.selectNews(connection,"news",formattedDateTime);
		int size = 0;
		if(News != null) {
			while(size<News.size()) {
				Map<String, Object> ans = new HashMap<String, Object>();
				String Id = String.valueOf((Integer) News.get(size).get("Creator"));
				Emp_info = (dbutil.select(connection,"Employee","Emp_id",Id));
				String Firstname = (String) Emp_info.get("Firstname");
				String Lastname = (String) Emp_info.get("Lastname");
				String Creator = Firstname+" "+Lastname;
				String news_id = String.valueOf((Integer) News.get(size).get("News_id"));
				String Detail = (String) News.get(size).get("Detail");
				String Topic = (String) News.get(size).get("Topic");
				String Img = (String) News.get(size).get("Img");
				Timestamp Start = (Timestamp)News.get(size).get("start_at");
				Timestamp End = (Timestamp)News.get(size).get("end_at");
				Timestamp create = (Timestamp)News.get(size).get("Create_date");
				ans.put("News_id",news_id);
				ans.put("Detail",Detail);
				ans.put("Topic",Topic);
				ans.put("Img",Img);
				ans.put("Creator",Creator);
				ans.put("Date",create);
				ans.put("Start",Start);
				ans.put("End",End);
				res.add(ans);
				size++;
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message","success");
		}
		else {
			responseBodyStr.put("status",404);
			responseBodyStr.put("Message","Not found");
		}
		return responseBodyStr;
	}
	
	public Map<String, Object> Add_News(Map<String, Object> data,int id) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		NewsModel new_model = new NewsModel();
		Map<String, Object> result = new HashMap<String, Object>();
		LocalDateTime currentDateTime = LocalDateTime.now();
		Timestamp Create_date = Timestamp.valueOf(currentDateTime);
		new_model.setDetail((String) data.get("Detail"));
		new_model.setTopic((String) data.get("Topic"));
		new_model.setImg((String) data.get("Img"));
		new_model.setStart(Timestamp.valueOf((String) data.get("start_at")));
		new_model.setEnd(Timestamp.valueOf((String) data.get("end_at")));
		new_model.setImg((String) data.get("Img"));
		new_model.setCreator(id);
		new_model.setCreatedate(Create_date);
		dbutil.AddNews(connection,new_model);
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
		News_info = (dbutil.select(connection,"news","News_id",(String)data.get("News_id")));
		new_model.setModel(News_info);
		new_model.setDetail((String)data.get("Detail"));
		new_model.setTopic((String)data.get("Topic"));
		new_model.setImg((String)data.get("Img"));
		new_model.setStart(Timestamp.valueOf((String)data.get("start_at")));
		new_model.setEnd(Timestamp.valueOf((String)data.get("end_at")));
		dbutil.UpdateNews(connection,new_model);
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
		System.out.println("data  "+data);
		System.out.println("data.value  "+ data.get("Value").getClass().getSimpleName());
		System.out.println("data.value.size()  "+ Target.size());
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
		LocalDateTime currentDateTime = LocalDateTime.now();
		String formattedDateTime = currentDateTime.format(formatter);
		News = dbutil.selectAll(connection,"news");
		int size = 0;
		if(News != null) {
			while(size<News.size()) {
				Map<String, Object> ans = new HashMap<String, Object>();
				String Id = String.valueOf((Integer) News.get(size).get("Creator"));
				Emp_info = (dbutil.select(connection,"Employee","Emp_id",Id));
				String Firstname = (String) Emp_info.get("Firstname");
				String Lastname = (String) Emp_info.get("Lastname");
				String Creator = Firstname+" "+Lastname;
				String news_id = String.valueOf((Integer) News.get(size).get("News_id"));
				String Detail = (String) News.get(size).get("Detail");
				String Topic = (String) News.get(size).get("Topic");
				String Img = (String) News.get(size).get("Img");
				System.out.println("Img From req  "+ Img);
				Timestamp Start = (Timestamp)News.get(size).get("start_at");
				Timestamp End = (Timestamp)News.get(size).get("end_at");
				Timestamp create = (Timestamp)News.get(size).get("Create_date");
				ans.put("News_id",news_id);
				ans.put("Detail",Detail);
				ans.put("Topic",Topic);
				ans.put("Img",Img);
				ans.put("Creator",Creator);
				ans.put("Date",create);
				ans.put("Start",Start);
				ans.put("End",End);
				res.add(ans);
				size++;
			}
			responseBodyStr.put("data",res);
			responseBodyStr.put("status",200);
			responseBodyStr.put("Message","Success");
		}
		else {
			responseBodyStr.put("status",404);
			responseBodyStr.put("Message","Not found");
		}
		return responseBodyStr;
	}
}
