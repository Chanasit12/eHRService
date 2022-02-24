package th.co.techberry.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import th.co.techberry.model.*;
import th.co.techberry.constants.*;

public class DatabaseUtil {
	public Connection connectDB() throws SQLException, ClassNotFoundException {
		Connection connection = null;
		String constr = ConfigConstants.DB_DRIVER;
		String host = ConfigConstants.DB_HOST;
		String dbName = ConfigConstants.DB_NAME;
		String user = ConfigConstants.DB_USER;
		String pass = ConfigConstants.DB_PASS;
		System.out.println("connect");
		{
			Class.forName(constr);
			connection = (Connection) DriverManager.getConnection(
					"jdbc:mysql://" + host + "/" + dbName
							+ "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
					user, pass);
			if (connection != null) {
				return connection;
			} else {
				throw new SQLException("connection is null [" + host + ";" + dbName + "]");
			}
		}
	}
	public void closeDB(Connection dbCon) throws SQLException {
		if (dbCon != null) {
			dbCon.close();
			dbCon = null;
		}
	}
	
	public Map<String, Object> select(Connection dbconnet, String tableName, String condition, String value)
			throws SQLException {
			String db_query = "SELECT * FROM " + tableName + " WHERE " + condition + " = " + "'" + value + "'" + ";";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
	}
	
	public Map<String, Object> select2con(Connection dbconnet,String tablename,String condition1, String condition2,String value1,String value2)
			throws SQLException {
			String db_query = "SELECT * FROM `"+tablename+"` WHERE `"+condition1+"` = '"+value1+"' AND `"+condition2+"` = '"+value2+"';";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
	}
	
	public List<Map<String, Object>> select2conArray(Connection dbconnet,String tablename,String condition1, String condition2,String value1,String value2)
			throws SQLException {
			String db_query = "SELECT * FROM `"+tablename+"` WHERE `"+condition1+"` = '"+value1+"' AND `"+condition2+"` = '"+value2+"';";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public List<Map<String, Object>> selectMeetingByEmpId(Connection dbconnet,String Emp_id, String Date)
			throws SQLException {
			String db_query = "SELECT * FROM `meeting` JOIN meeting_room_booking "
					+ "ON meeting.Meet_id = meeting_room_booking.Meet_id "
					+ "where `Status` = 1 "
					+ "AND `Emp_id` = '"+Emp_id+"'  AND Date >= '"+Date+"';";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public List<Map<String, Object>> CheckRoomMeeting(Connection dbconnet,String Room_Id,String Date, String Start,String End)
			throws SQLException {
			String db_query = "SELECT * FROM `meeting_room_booking` WHERE "
					+ "`Status` = 1 AND `Date` = '"+Date+"' AND `Room_Id` = '"+Room_Id+"'"
					+ "AND (((`Start_at` BETWEEN  '"+Start+"' AND  '"+End+"') OR (`END_at` BETWEEN  '"+Start+"' AND  '"+End+"')) "
					+ "OR (`Start_at` < '"+Start+"' AND `END_at` > '"+Start+"') "
					+ "OR (`Start_at` < '"+End+"' AND `END_at` > '"+End+"') "
					+ "OR (`Start_at` < '"+Start+"' AND `END_at` > '"+End+"'));";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public List<Map<String, Object>> CheckEmpMeetingDuration(Connection dbconnet,String Emp_Id,String Date, String Start,String End)
			throws SQLException {
			String db_query = "SELECT * FROM `meeting` JOIN meeting_room_booking "
					+ "ON meeting.Meet_id = meeting_room_booking.Meet_id where `Status` = 1 "
					+ "AND `Emp_id` = '"+Emp_Id+"'  AND Date = '"+Date+"' AND (((`Start_at` BETWEEN  '"+Start+"' AND  '"+End+"') "
					+ "OR (`END_at` BETWEEN  '"+Start+"' AND  '"+End+"')) OR "
					+ "(`Start_at` < '"+Start+"' AND `END_at` > '"+Start+"') "
					+ "OR (`Start_at` < '"+End+"' AND `END_at` > '"+End+"') "
					+ "OR (`Start_at` < '"+Start+"' AND `END_at` > '"+End+"'));";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public List<Map<String, Object>> selectArray(Connection dbconnet, String tableName, String condition, String value)
			throws SQLException {
			String db_query = "SELECT * FROM " + tableName + " WHERE " + condition + " = " + "'" + value + "'" + ";";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public List<Map<String, Object>> selectNews(Connection dbconnet, String tableName,String value)
			throws SQLException {
			String db_query = "SELECT * FROM " + tableName + " WHERE start_at <= " + "'" + value + "' AND end_at >='"+ value+"' "+ ";";
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			System.out.println("rs"+rs);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public List<Map<String, Object>> selectAll(Connection dbconnet, String tableName)
			throws SQLException {
			String db_query = "SELECT * FROM " + tableName + ";";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public  Map<String, Object> selectLastCheckIn(Connection dbconnet,String date,String id)
			throws SQLException {
			String db_query = "SELECT * FROM `check_in/check_out` WHERE `Emp_id` = '"+id+"' AND `Checkin_at` LIKE '"+date+"%';";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
	}
	
	public List<Map<String, Object>> selectEmpMeetingFromCreate(Connection dbconnet,String target,String id)
			throws SQLException {
			String db_query = "SELECT * FROM `meeting` JOIN meeting_room_booking "
					+ "ON meeting.Meet_id = meeting_room_booking.Meet_id "
					+ "where `Status` = 1 "
					+ "AND `"+target+"` = '"+id+"';";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public List<Map<String, Object>> selectEmpMeetingFromDate(Connection dbconnet,String target,String id,String Date)
			throws SQLException {
			String db_query = "SELECT * FROM `meeting` JOIN meeting_room_booking "
					+ "ON meeting.Meet_id = meeting_room_booking.Meet_id "
					+ "where `Status` = 1 "
					+ "AND `"+target+"` = '"+id+"'  AND Date >= '"+Date+"';";
			System.out.println("sql " + db_query);
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public Map<String, Object> Login(Connection dbconnet , String condition, String value)
			throws SQLException {
//			String db_query1 = "UPDATE Login SET Active_status = " + true + " WHERE " + condition + " = " + "'" + value + "'" + ";" ;
			String db_query2 = "SELECT * FROM " + "Login" + " WHERE " + condition + " = " + "'" + value + "'" + ";";
//			PreparedStatement ps1 = dbconnet.prepareStatement(db_query1);
			PreparedStatement ps2 = dbconnet.prepareStatement(db_query2);
//			ps1.executeUpdate();
			ResultSet rs = ps2.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
	}
	
	public void Changepassword(Connection dbconnet,int id,String password,boolean status) throws SQLException {
		if(status) {
			String db_query1 = "UPDATE Login SET Need_reset_password = " + false + " WHERE " + " Id " + " = " + "'" + id + "'" + ";" ;
			PreparedStatement ps = dbconnet.prepareStatement(db_query1);
			ps.executeUpdate();
		}
			String q = "UPDATE Login SET Password_hash = '" + password + "' WHERE " + "Id" + " = " + "'" + id + "'" + ";" ;
			System.out.println("sql " + q);
			PreparedStatement ps = dbconnet.prepareStatement(q);
			ps.executeUpdate();
	}
	
	public void AddNews(Connection dbconnet,NewsModel model) throws SQLException {
		String q = "INSERT INTO news (News_id, Img, Topic, Detail, Creator, start_at, end_at,Create_date) "
				+ "VALUES (NULL, '"+model.getImg()+"', '"+model.getTopic()+"', '"+model.getDetail()+"', '"+model.getCreator()+"', '"+model.getStart()+"', '"
				+model.getEnd()+"', '"+model.getCreatedate()+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void AddDayOff(Connection dbconnet,String id,String Detail,String hour) throws SQLException {
		String q = "INSERT INTO `day_off_list` (`Day_off_id`, `Emp_id`, `Detail`, `Hours`) "
				+ "VALUES (NULL, '"+id+"', '"+Detail+"', '"+hour+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void AddTeamMember(Connection dbconnet,String Empid,String Teamid) throws SQLException {
		String q = "INSERT INTO `emp_team`(`Emp_id`, `Team_id`) VALUES ('"+Empid+"','"+Teamid+"')";
		System.out.println("sql " + q);
		try {
			PreparedStatement ps = dbconnet.prepareStatement(q);
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int AddHoliday(Connection dbconnet,String name,String start,String stop) throws SQLException {
		String q = "INSERT INTO `holiday_list` (`ID`, `Holiday_Name`, `begin_date`, `end_date`)"
				+ "VALUES (NULL, '"+name+"', '"+start+"', '"+stop+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddCompany(Connection dbconnet,String name) throws SQLException {
		String q = "INSERT INTO `company` (`Company_Name`)"
				+ "VALUES ('"+name+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddPosition(Connection dbconnet,String name) throws SQLException {
		String q = "INSERT INTO `position` (`Position_Name`)"
				+ "VALUES ('"+name+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddTeam(Connection dbconnet,String name,String creator,String host) throws SQLException {
		String q = "INSERT INTO `team`(`Team_name`, `Creator`, `Team_Host`) VALUES ('"+name+"','"+creator+"','"+host+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}

	public int AddMeeting(Connection dbconnet,String meet_id,String Emp_id) throws SQLException {
		String q = "INSERT INTO `meeting`(`Emp_id`, `Meet_id`) VALUES ('"+Emp_id+"','"+meet_id+"')";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddLeave_type(Connection dbconnet,String name,String leaved,String add) throws SQLException {
		String q = "INSERT INTO `leave_type`(`Type_name`, `Num_per_year`, `Num_can_add`) VALUES ('"+name+"','"+leaved+"','"+add+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddLeave_Day(Connection dbconnet,String Emp_id,String Type_id,String Leave,String Add) throws SQLException {
		String q = "INSERT INTO `leave_day_count`(`Emp_id`, `Type_ID`, `Leaved`, `Added`) "
				+ "VALUES ('"+Emp_id+"','"+Type_id+"','"+Leave+"','"+Add+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddLogin(Connection dbconnet,String Username,String Password) throws SQLException {
		String q = "INSERT INTO `login` (`Username`, `Password_hash`, `Last_Login`, `Need_reset_password`, `Active_status`) "
				+ "VALUES ('"+Username+"', '"+Password+"', NULL, '1', '1');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddEmployee(Connection dbconnet,String Title,String Firstname,String Lastname,String Birth,String Gender,
			String Phone,int id,String Email,String add,String Role_ID,String img,String Position_Id,String Comp_ID) throws SQLException {
		System.out.println("add " + add);
		String q = "INSERT INTO `employee` "
				+ "(`Title`, `Firstname`, `Lastname`, `Birth_date`, `Gender`, `Phone`, `Id`, `Email`, `Address`, `Role_ID`, `Img`, `Position_ID`, `Comp_ID`)"
				+ " VALUES ('"+Title+"', '"+Firstname+"', '"+Lastname+"', '"+Birth+"', '"+Gender+"', '"+Phone+"', '"+id+"', '"+Email+
				"', '"+add+"', '"+Role_ID+"', '"+img+"', '"+Position_Id+"', '"+Comp_ID+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddTimeSheet(Connection dbconnet,String id,String Date,String Start,String End) throws SQLException {
		String q = "INSERT INTO `timesheet`(`Emp_id`, `Start_at`, `End_at`, `Date`)"
				+ " VALUES ('"+id+"','"+Start+"','"+End+"','"+Date+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddLocation(Connection dbconnet,String Name,String Description) throws SQLException {
		String q = "INSERT INTO `location` (`Location_name`, `description`) VALUES ('"+Name+"', '"+Description+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddChargeCode(Connection dbconnet,String Name,String Description) throws SQLException {
		String q = "INSERT INTO `charge_code` (`Charge_code_name`, `description`) VALUES ('"+Name+"','"+Description+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddCheckIn(Connection dbconnet,CheckInCheckOutModel model) throws SQLException {
		String q = "INSERT INTO `check_in/check_out` "
				+ "(`Check_id`, `Checkin_at`,  `Emp_id`, `Status_CheckIn`) "
				+ "VALUES (NULL, '"+model.getCheckin()+"', '"+model.getEmpId()+"', '"+model.getStatusCheckIn()+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public Map<String, Object> AddMeeting(Connection dbconnet,String Subject,String Create,String Creator,String Room_Id,String Date,
			String Start,String End,String des) throws SQLException {
		String q = "INSERT INTO `meeting_room_booking`"
				+ "(`Subject`, `Create_at`, `Cancel_at`, `Creator`, `Room_Id`, `Date`, `Start_at`, `End_at`, `Status`, `description`) "
				+ "VALUES ('"+Subject+"','"+Create+"',NULL,'"+Creator+"','"+Room_Id+"','"+Date+"','"+Start+"','"+End+"','1','"+des+"')";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
		return select2con(dbconnet,"meeting_room_booking","Create_at","Creator",Create,Creator);
	}
	
	public void UpdateProfile(Connection dbconnet,EmployeeModel model) throws SQLException {
		String q = "UPDATE `employee` SET Title ='"+model.getTitle()+"' , Firstname='"+model.getFirstname()+"' , Lastname='"+model.getLastname()+"' ,Birth_date='"
	+model.getBirthdate()+"',Gender='"+model.getGender()+"',Phone='"+model.getPhone()+"',Email='"+model.getEmail()+"',Address='"+model.getAddress()+
	"',Img='"+model.getImg_Path()+"', Position_ID = '"+model.getPositionid()+"', Comp_ID = '"+model.getCompanyid()+"' WHERE Emp_id ='"+model.getEmpid()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateNews(Connection dbconnet,NewsModel model) throws SQLException {
		String q = "UPDATE `news` SET `Img`='"+model.getImg()+"',`Topic`='"+model.getTopic()+"',`Detail`='"+model.getDetail()+"',"
				+ "`Creator`='"+model.getCreator()+"',`start_at`='"+model.getStart()+"',`end_at`='"+model.getEnd()+"' WHERE News_id = '"+model.getId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateHoliday(Connection dbconnet,HolidayModel model) throws SQLException {
		String q = "UPDATE `holiday_list` SET `Holiday_Name`='"+model.getName()+"',"
				+ "`begin_date`='"+model.getStart()+"',`end_date`='"+model.getEnd()+"' WHERE `ID` = '"+model.getId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateLocation(Connection dbconnet,LocationModel model) throws SQLException {
		String q = "UPDATE `location` SET `Location_name`='"+model.getLocationName()+"',`description`='"+model.getDescription()+"' "
				+ "WHERE `Location_id`='"+model.getLocationId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateChageCode(Connection dbconnet,ChargeCodeModel model) throws SQLException {
		String q = "UPDATE `charge_code` SET `Charge_code_name`='"+model.getChargeCodeName()+"',`description`='"+model.getDescription()+"' "
				+ "WHERE `Charge_code_id`='"+model.getChargeCodeId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateWorkingTime(Connection dbconnet,WorkingTimeModel model) throws SQLException {
		String q = "UPDATE `working_time` SET `start_work`='"+model.getStart()+"',`off_work`='"+model.getEnd()+"' WHERE `Day_Name`='"+model.getName()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateDayOff(Connection dbconnet,DayOffModel model) throws SQLException {
		String q = "UPDATE `day_off_list` SET `Detail`='"+model.getDetail()+"',`Hours`='"+model.getHour()+"' WHERE `Day_off_id`='"+model.getDayOffId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateRole(Connection dbconnet,EmployeeModel model) throws SQLException {
		String q = "UPDATE `employee` SET `Role_ID`='"+model.getRole()+"' WHERE `Emp_id` = '"+model.getEmpid()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateTeam(Connection dbconnet,TeamModel model) throws SQLException {
		String q = "UPDATE `team` SET `Team_name`='"+model.getTeamName()+"',`Team_Host`='"+model.getHost()+"' WHERE `Team_id`='"+model.getTeamId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateCompany(Connection dbconnet,String id,String name) throws SQLException {
		String q = "UPDATE `company` SET `Company_Name`='"+name+"' WHERE `Comp_ID`='"+id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdatePosition(Connection dbconnet,String id,String name) throws SQLException {
		String q = "UPDATE `position` SET `Position_Name`='"+name+"' WHERE `Position_ID`='"+id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateLeaveType(Connection dbconnet,LeaveTypeModel model) throws SQLException {
		String q = "UPDATE `leave_type` SET `Type_name`='"+model.getName()+"',`Num_per_year`='"+model.getNum_per_year()+"',`Num_can_add`='"+model.getNum_can_add()
				+ "' WHERE `Type_ID` = '"+model.getId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateLeaveCount(Connection dbconnet,LeaveTypeModel model,int emp_id) throws SQLException {
		String q = "UPDATE `leave_day_count` SET `Leaved`= "+model.getNum_per_year() +" WHERE `Type_ID` = '"+model.getId()+"' AND `Emp_id` = '"+emp_id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateTimeSheet(Connection dbconnet,TimeSheetModel model) throws SQLException {
		String q = "UPDATE `timesheet` SET "
				+ "`Start_at`='"+model.getStart()+"',`End_at`='"+model.getEnd()+"',"
				+ "`Detail`='"+model.getDetail()+"',`Location_id`='"+model.getLocationId()+"',"
				+ "`Charge_code_id`='"+model.getChargeCodeId()+"',`Remark`='"+model.getRemark()+"' "
				+ "WHERE `Sheet_id` = '"+model.getSheetId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateMeeting(Connection dbconnet,MeetingModel model,String meet_id) throws SQLException {
		String q = "UPDATE `meeting_room_booking` "
				+ "SET `Subject`='"+model.getSubject()+"',`Room_Id`='"+model.getRoomId()+"',`Date`='"+model.getDate()+"',"
						+ "`Start_at`='"+model.getStart()+"',`End_at`='"+model.getEnd()+"',"
				+ "`description`='"+model.getDescription()+"' WHERE `Meet_id` = '"+meet_id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateUsername(Connection dbconnet,String username,String id) throws SQLException {
		String q = "UPDATE `login` SET `Username`='"+username+"' WHERE `Id` = '"+id+"';";
		String q2 = "UPDATE `employee` SET `Email`='"+username+"' WHERE `Id` = '"+id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		PreparedStatement ps1 = dbconnet.prepareStatement(q2);
		ps.executeUpdate();
		ps1.executeUpdate();
	}
	
	public void ActiveEmployee(Connection dbconnet,String id) throws SQLException {
		String q = "UPDATE `login` SET `Active_status`='1' WHERE `Id` = '"+id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void Delete(Connection dbconnet,String tablename,String col,String value) throws SQLException {
		String q = "DELETE FROM `"+tablename+"` WHERE `"+col+"` = '"+value+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void DeleteMeeting(Connection dbconnet,String time,String Deleter,String meet_id) throws SQLException {
		String q = "UPDATE `meeting_room_booking` SET `Cancel_at`='"+time+"',`Status`='0' ,"
				+ "`Deleter` = '"+Deleter+"' WHERE `Meet_id` = '"+meet_id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void Meeting_log(Connection dbconnet,String meet_id) throws SQLException {
		String log = "INSERT INTO `meeting_room_booking_log`(`Meet_id`, `Creator`, `Create_at`, `Cancel_at`,`Deleter`) "
				+ "SELECT `Meet_id`, `Creator`, `Create_at`, `Cancel_at`,`Deleter` "
				+ "FROM `meeting_room_booking` WHERE `Meet_id` = '"+meet_id+"'";
		PreparedStatement ps_log = dbconnet.prepareStatement(log);
		ps_log.executeUpdate();
	}
	
	public void Delete_2con(Connection dbconnet,String tablename,String col,String value,String col2,String value2) throws SQLException {
		String q = "DELETE  FROM `"+tablename+"` WHERE `"+col+"` = '"+value+"' AND `"+col2+"` = '"+value2+"'";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void DeleteEmployee(Connection dbconnet,String id) throws SQLException {
		String q = "UPDATE `login` SET `Active_status`='0' WHERE `Id` = '"+id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	private List<Map<String, Object>> convertResultSetFromDB(ResultSet rs) throws SQLException {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		ResultSetMetaData rsmd = rs.getMetaData();

		while (rs.next()) {
			int numColumns = rsmd.getColumnCount();
			Map<String, Object> obj = new HashMap<String, Object>();

			for (int i = 1; i < numColumns + 1; i++) {
				String column_name = rsmd.getColumnName(i);
				if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
					obj.put(column_name, rs.getArray(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
					obj.put(column_name, rs.getBoolean(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
					obj.put(column_name, rs.getBlob(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
					obj.put(column_name, rs.getDouble(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
					obj.put(column_name, rs.getFloat(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
					obj.put(column_name, rs.getNString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
					obj.put(column_name, rs.getString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
					obj.put(column_name, rs.getDate(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
					obj.put(column_name, rs.getTimestamp(column_name));
				} else {
					obj.put(column_name, rs.getObject(column_name));
				}
			}
			resultList.add(obj);
		}
		return resultList;
	}
}