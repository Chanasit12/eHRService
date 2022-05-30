package th.co.techberry.model;

import java.util.Map;
import java.util.Date;
public class NewsModel {

	private String Detail;
	private String Topic;
	private byte[] Img;
	private String StrImg;
	private int Creator;
	private Date start_at;
	private Date end_at;
	private Date Create_date;
	private int News_id;
	private String StrStart_at;
	private String StrEnd_at;
	private String StrCreate_date;
	
	public void setid(int id) {
		News_id = id;
	}
	
	public void setDetail(String detail) {
			Detail = detail;
	}
	
	public void setTopic(String topic) {
			Topic = topic;
	}

	public void setStrImg(String img) {
		StrImg = img;
	}

	public void setImg(byte[] img) {
			Img = img;
	}

	public void setStart(Date time) {
			start_at = time;
	}

	public void setEnd(Date time) {
			end_at = time;
	}
	
	public void setCreator(int id) {
			Creator = id;
	}
	
	public void setCreatedate(Date time) {
		Create_date = time;
}

	public void setStrCreatedate(String time) {
		StrCreate_date = time;
	}

	public void setStrStart(String time) {
		StrStart_at = time;
	}

	public void setStrEnd(String time) {
		StrEnd_at = time;
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

	public String getStrImg() {
		return StrImg;
	}

	public byte[] getImg() {
		return Img;
	}

	public Date getStart() {
		return start_at;
	}

	public Date getEnd() {
		return end_at;
	}

	public int getCreator() {
		return Creator;
	}
	
	public Date getCreatedate() {
		return Create_date;
	}

	public String getStrCreatedate() {
		return StrCreate_date;
	}

	public String getStrStart() {
		return StrStart_at;
	}

	public String getStrEnd() {
		return StrEnd_at;
	}

	public void setModel(Map<String, Object> data) {
		if(data != null) {
			setDetail((String) data.get("Detail"));
			setTopic((String) data.get("Topic"));
			setImg((byte[]) data.get("Img"));
			setCreator((Integer) data.get("Creator"));
			setStart((Date)data.get("start_at"));
			setEnd((Date)data.get("end_at"));
			setCreatedate((Date)data.get("Create_date"));
			setid((Integer) data.get("News_id"));
			String Img = new String(getImg());
			setStrImg(Img);
		}
		else {
			return;
		}
	}
}
