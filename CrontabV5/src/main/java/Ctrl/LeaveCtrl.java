package Ctrl;

import Constant.DatabaseUtil;
import Model.HolidayModel;
import com.mysql.jdbc.Connection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LeaveCtrl {
	public boolean Check_Holliday(String Date) throws SQLException, ClassNotFoundException {
		DatabaseUtil dbutil = new DatabaseUtil();
		Connection connection = dbutil.connectDB();
		List<Map<String, Object>> Holiday ;
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		HolidayModel model = new HolidayModel();
		String[] date = Date.split("-");
		String DayAndMonth = date[1]+"-%";
		boolean result = true;
		try{
			Holiday = dbutil.selectHoliday(connection,DayAndMonth);
			Date d1 = sdformat.parse(Date);
			if(Holiday != null){
				for(Map<String, Object> temp : Holiday){
					model.setModel(temp);
					d1.compareTo(model.getStart());
					if(d1.compareTo(model.getStart()) >= 0 && d1.compareTo(model.getEnd()) <= 0) {
						result = false;
					}
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		} catch (ParseException e){
			e.printStackTrace();
		}
		return result;
	}

}
