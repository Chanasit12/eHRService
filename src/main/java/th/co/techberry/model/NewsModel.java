package th.co.techberry.model;

import java.util.Map;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
public class NewsModel {
	static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
	private String Detail;
	private String Topic;
	private String Img;
	private int Creator;
	private Timestamp start_at;
	private Timestamp end_at;
	private Timestamp Create_date;
	private int News_id;
	
	public void setid(int id) {
		News_id = id;
	}
	
	public void setDetail(String detail) {

			Detail = detail;
	}
	
	public void setTopic(String topic) {
			Topic = topic;
	}

	public void setImg(String img) {
			Img = img;
	}

	public void setStart(Timestamp time) {
			start_at = time;
	}

	public void setEnd(Timestamp time) {
			end_at = time;
	}
	
	public void setCreator(int id) {
			Creator = id;
	}
	
	public void setCreatedate(Timestamp time) {
		Create_date = time;
}

/// Get
	public int getId() {
		return News_id;
	}
	
	public String getDetail() {
		return Detail;
	}

	public String getTopic() {
		return Topic;
	}

	public String getImg() {
		return Img;
	}

	public Timestamp getStart() {
		return start_at;
	}

	public Timestamp getEnd() {
		return end_at;
	}

	public int getCreator() {
		return Creator;
	}
	
	public Timestamp getCreatedate() {
		return Create_date;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setDetail((String) data.get("Detail"));
			setTopic((String) data.get("Topic"));
			setImg((String) data.get("Img"));
			setCreator((Integer) data.get("Creator"));
			setStart((Timestamp)data.get("start_at"));
			setEnd((Timestamp)data.get("end_at"));
			setCreatedate((Timestamp)data.get("Create_date"));
			setid((Integer) data.get("News_id"));
		}
		else {
			return;
		}
	}
}
