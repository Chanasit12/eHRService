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
							+ "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
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
			List<Map<String, Object>> list = new ArrayList<>();
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
			List<Map<String, Object>> list = new ArrayList<>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
	}

	public Map<String, Object> select3con(Connection dbconnet,String tablename,String condition1, String condition2,String condition3,String value1,String value2,String value3)
			throws SQLException {
		String db_query = "SELECT * FROM `"+tablename+"` WHERE `"+condition1+"` = '"+value1+"' AND `"+condition2+"` = '"+value2+"' AND `"+condition3+"` = '"+value3+"';";
		System.out.println("sql " + db_query);
		PreparedStatement ps = dbconnet.prepareStatement(db_query);
		ResultSet rs = ps.executeQuery();
		List<Map<String, Object>> list = new ArrayList<>();
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
			List<Map<String, Object>> list = new ArrayList<>();
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
			List<Map<String, Object>> list = new ArrayList<>();
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
			List<Map<String, Object>> list = new ArrayList<>();
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
			List<Map<String, Object>> list = new ArrayList<>();
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
			List<Map<String, Object>> list = new ArrayList<>();
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
			List<Map<String, Object>> list = new ArrayList<>();
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
			List<Map<String, Object>> list = new ArrayList<>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list;
			}
	}
	
	public  Map<String, Object> selectLastCheckIn(Connection dbconnet,String date,String id)
			throws SQLException {
			String db_query = "SELECT * FROM `checkin_checkout` WHERE `Emp_id` = '"+id+"' AND `Checkin_at` LIKE '"+date+"%';";
			PreparedStatement ps = dbconnet.prepareStatement(db_query);
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
	}

	public  Map<String, Object> selectCheckinByEmpid(Connection dbconnet,String date,String id)
			throws SQLException {
		String db_query = "SELECT * FROM `checkin_checkout` WHERE `Emp_id` = '"+id+"' AND `Checkin_at` LIKE '"+date+"%';";
		PreparedStatement ps = dbconnet.prepareStatement(db_query);
		System.out.println("sql " + db_query);
		ResultSet rs = ps.executeQuery();
		List<Map<String, Object>> list = new ArrayList<>();
		list.addAll(convertResultSetFromDB(rs));
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public  List<Map<String, Object>> selectCheckin(Connection dbconnet,String date,String id)
			throws SQLException {
		String db_query = "SELECT * FROM `checkin_checkout` WHERE `Emp_id` = '"+id+"' AND `Checkin_at` LIKE '"+date+"';";
		PreparedStatement ps = dbconnet.prepareStatement(db_query);
		ResultSet rs = ps.executeQuery();
		List<Map<String, Object>> list = new ArrayList<>();
		list.addAll(convertResultSetFromDB(rs));
		if (list.size() == 0) {
			return null;
		} else {
			return list;
		}
	}

	public  List<Map<String, Object>> selectHoliday(Connection dbconnet,String date)
			throws SQLException {
		String db_query = "SELECT * FROM `holiday_list` WHERE `begin_date` LIKE '%-"+date+"' OR `end_date` LIKE '%-"+date+"'";
		System.out.println("sql " + db_query);
		PreparedStatement ps = dbconnet.prepareStatement(db_query);
		ResultSet rs = ps.executeQuery();
		List<Map<String, Object>> list = new ArrayList<>();
		list.addAll(convertResultSetFromDB(rs));
		if (list.size() == 0) {
			return null;
		} else {
			return list;
		}
	}

	public  List<Map<String, Object>> selectCheckinAll(Connection dbconnet,String date)
			throws SQLException {
		String db_query = "SELECT * FROM `checkin_checkout` WHERE `Checkin_at` LIKE '"+date+"';";
		System.out.println("sql " + db_query);
		PreparedStatement ps = dbconnet.prepareStatement(db_query);
		ResultSet rs = ps.executeQuery();
		List<Map<String, Object>> list = new ArrayList<>();
		list.addAll(convertResultSetFromDB(rs));
		if (list.size() == 0) {
			return null;
		} else {
			return list;
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

	public List<Map<String, Object>> selectEmpMeetingFromDate(Connection dbconnet,String target,String id)
			throws SQLException {
		String db_query = "SELECT * FROM `meeting` JOIN meeting_room_booking "
				+ "ON meeting.Meet_id = meeting_room_booking.Meet_id "
				+ "where `Status` = 1 "
				+ "AND `"+target+"` = '"+id+"';";
		System.out.println("sql " + db_query);
		PreparedStatement ps = dbconnet.prepareStatement(db_query);
		ResultSet rs = ps.executeQuery();
		List<Map<String, Object>> list = new ArrayList<>();
		list.addAll(convertResultSetFromDB(rs));
		if (list.size() == 0) {
			return null;
		} else {
			return list;
		}
	}

	public Map<String, Object> Select_Leave_req(Connection dbconnet ,String emp_id,String date)
			throws SQLException {
		String db_query2 = "SELECT * FROM `leave_request` WHERE `Emp_id` = '"+emp_id+"' " +
				"AND (`Begin` LIKE '"+date+"%' OR `End` LIKE '"+date+"%')";
		PreparedStatement ps2 = dbconnet.prepareStatement(db_query2);
		ResultSet rs = ps2.executeQuery();
		List<Map<String, Object>> list = new ArrayList<>();
		list.addAll(convertResultSetFromDB(rs));
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public Map<String, Object> Login(Connection dbconnet , String condition, String value)
			throws SQLException {
			String db_query2 = "SELECT * FROM " + "login" + " WHERE " + condition + " = " + "'" + value + "'" + ";";
			PreparedStatement ps2 = dbconnet.prepareStatement(db_query2);
			ResultSet rs = ps2.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.addAll(convertResultSetFromDB(rs));
			if (list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
	}

	public void Last_Login(Connection dbconnet ,String time,int id)
			throws SQLException {
		String q = "UPDATE `login` SET `Last_Login`='"+time+"' WHERE `Id` = '"+id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void Changepassword(Connection dbconnet,int id,String password,boolean status) throws SQLException {
		if(status) {
			String db_query1 = "UPDATE login SET Need_reset_password = " + false + " WHERE " + " Id " + " = " + "'" + id + "'" + ";" ;
			PreparedStatement ps = dbconnet.prepareStatement(db_query1);
			ps.executeUpdate();
		}
			String q = "UPDATE login SET Password_hash = '" + password + "' WHERE " + "Id" + " = " + "'" + id + "'" + ";" ;
			System.out.println("sql " + q);
			PreparedStatement ps = dbconnet.prepareStatement(q);
			ps.executeUpdate();
	}
	
	public void AddNews(Connection dbconnet,NewsModel model) throws SQLException {
		String q = "INSERT INTO news (Img, Topic, Detail, Creator, start_at, end_at,Create_date) "
				+ "VALUES ('"+model.getStrImg()+"', '"+model.getTopic()+"', '"+model.getDetail()+"', '"+model.getCreator()+"', '"+model.getStrStart()+"', '"
				+model.getStrEnd()+"', '"+model.getStrCreatedate()+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void AddDayOff(Connection dbconnet,LeaveCountModel model) throws SQLException {
		String q = "UPDATE `leave_day_count` SET `Leaved`='"+model.getLeaved()+"' " +
				"WHERE `Emp_id`='"+model.getEmpId()+"' AND `Type_ID`='"+model.getTypeId()+"'";
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
	
	public int AddCompany(Connection dbconnet,CompanyModel model) throws SQLException {
		String q = "INSERT INTO `company` (`Company_Name`)"
				+ "VALUES ('"+model.getCompanyName()+"')";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}

	public int AddMeeting_Room(Connection dbconnet,MeetingRoomModel model) throws SQLException {
		String q = "INSERT INTO `meeting_room`(`Room_Name`, `description`) VALUES ('"+model.getRoomName()+"','"+model.getDescription()+"')";
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

	public int AddLeave_Req(Connection dbconnet,LeaveModel model,int Emergency) throws SQLException {
		String q = "INSERT INTO `leave_request`(`Type_ID`, `Emp_id`, `Begin`, `End`, `Detail`,`Depend`,`Status`,`Emergency`,`Amount`) " +
				"VALUES ('"+model.getTypeId()+"','"+model.getEmpId()+"','"+model.getBegin()+"'," +
				"'"+model.getEnd()+"','"+model.getDetail()+"','"+model.getDepend()+"','"+model.getStatus()+"','"+Emergency+"','"+model.getAmount()+"')";
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
	
	public int AddLeave_Day(Connection dbconnet,String Emp_id,LeaveTypeModel model) throws SQLException {
		String q = "INSERT INTO `leave_day_count`(`Emp_id`, `Type_ID`, `Leaved`) "
				+ "VALUES ('"+Emp_id+"','"+model.getId()+"','"+model.getNum_per_year()+"');";
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
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}

	public int AddTimeSheet(Connection dbconnet,String Date,String start,String end,TimeSheetModel model) throws SQLException {
		String q = "INSERT INTO `timesheet`(`Emp_id`,`Start_at`, `End_at`, `Date`, `Detail`, `Location_id`, `Charge_code_id`, `Remark`) " +
				"VALUES ('"+model.getEmpId()+"','"+start+"','"+end+"','"+Date+"','"+model.getDetail()+"','"+model.getLocationId()+"','"+model.getChargeCodeId()+"','"+model.getRemark()+"')";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}

	public int AddExpenseReqest(Connection dbconnet,ExpenseReqModel model) throws SQLException {
		String q = "INSERT INTO `expense_request`( `Emp_id`,`Detail`, `create_at`, `status`) " +
				"VALUES ('"+model.getEmpId()+"','"+model.getDetail()+"','"+model.getStrCreate()+"','"+model.getStatus()+"')";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}

	public int Add_ExpenseRequest_File(Connection dbconnet,ExpenseReqModel model,String data,String name) throws SQLException {
		String q = "INSERT INTO `expense_request_file`(`Req_id`, `data`, `File_name`) VALUES " +
				"('"+model.getReqId()+"','"+data+"','"+name+"')";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}

	public int AddDocumentReqest(Connection dbconnet,DocumentReqModel model) throws SQLException {
		String q = "INSERT INTO `document_request`(`Emp_id`, `Type_ID`, `Detail`, `create_at`, `status`) " +
				"VALUES ('"+model.getEmpId()+"','"+model.getTypeId()+"','"+model.getDetail()+"','"+model.getStrCreate()+"','"+model.getStatus()+"')";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}

	public int Add_DocumentRequest_File(Connection dbconnet,String id,String data,String name) throws SQLException {
		String q = "INSERT INTO `document_request_file`(`Req_id`, `File_name`, `data`) " +
				"VALUES ('"+id+"','"+name+"','"+data+"')";
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

	public int AddDocumentType(Connection dbconnet,String Name) throws SQLException {
		String q = "INSERT INTO `document_type` (`Type_name`) VALUES ('"+Name+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}
	
	public int AddCheckIn(Connection dbconnet,CheckInCheckOutModel model) throws SQLException {
		String q = "INSERT INTO `checkin_checkout`(`Checkin_at`, `Checkout_at`, `Emp_id`, `Status_CheckIn`, `Status_CheckOut`,`Detail`) " +
				"VALUES ('"+model.getCheckInStr()+"','"+model.getCheckoutStr()+"','"+model.getEmpId()+"'," +
				"'"+model.getStatusCheckIn()+"','"+model.getStatusCheckOut()+"','"+model.getDetail()+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}



	public void Update_Checkin_Checkout(Connection dbconnet,CheckInCheckOutModel model,String date) throws SQLException {
		String q = "UPDATE `checkin_checkout` SET `Checkin_at`='"+model.getCheckInStr()+"',`Checkout_at`='"+model.getCheckoutStr()+"'" +
				",`Status_CheckIn`='"+model.getStatusCheckIn()+"'" +
				",`Status_CheckOut`='"+model.getStatusCheckOut()+"',`Detail`='"+model.getDetail()+"' WHERE `Emp_id` = '"+model.getEmpId()+"'" +
				" AND `Checkin_at` LIKE '"+date+"%'";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
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

	public void Update_Expense_Request(Connection dbconnet,ExpenseReqModel model,String type,String value) throws SQLException {
		String q = "UPDATE `expense_request` SET `Detail`='"+model.getDetail()+"',`"+type+"`='"+value+"'," +
				"`status`='"+model.getStatus()+"',`remark`='"+model.getRemark()+"' WHERE `Req_id` = '"+model.getReqId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}

	public void Update_Document_Request(Connection dbconnet,DocumentReqModel model, String type, String value) throws SQLException {
		String q = "UPDATE `document_request` SET `Detail`='"+model.getDetail()+"',`"+type+"`='"+value+"'," +
				"`status`='"+model.getStatus()+"',`remark`='"+model.getRemark()+"',`Type_ID`='"+model.getTypeId()+"' WHERE `Req_id` = '"+model.getReqId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}

	public void UpdateLeaveReq(Connection dbconnet,LeaveModel model) throws SQLException {
		String q = "UPDATE `leave_request` SET `Type_ID`='"+model.getTypeId()+"'," +
				"`Status`='"+model.getStatus()+"',`Depend`='"+model.getDepend()+"',`Comment`='"+model.getComment()+"' WHERE `Req_id` = '"+model.getReqId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}

	public void UpdateProfile(Connection dbconnet,EmployeeModel model) throws SQLException {
		String q = "UPDATE `employee` SET Title ='"+model.getTitle()+"' , Firstname='"+model.getFirstname()+"' , Lastname='"+model.getLastname()+"' ,Birth_date='"
	+model.getBirthdate()+"',Gender='"+model.getGender()+"',Phone='"+model.getPhone()+"',Email='"+model.getEmail()+"',Address='"+model.getAddress()+
	"',Img='"+model.getStrImg()+"', Position_ID = '"+model.getPositionid()+"', Comp_ID = '"+model.getCompanyid()+"' WHERE Emp_id ='"+model.getEmpid()+"';";
//		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateNews(Connection dbconnet,NewsModel model) throws SQLException {
		String q = "UPDATE `news` SET `Img`='"+model.getStrImg()+"',`Topic`='"+model.getTopic()+"',`Detail`='"+model.getDetail()+"',"
				+ "`Creator`='"+model.getCreator()+"',`start_at`='"+model.getStrStart()+"',`end_at`='"+model.getStrEnd()+"' WHERE News_id = '"+model.getId()+"';";
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
	
	public void UpdateChargeCode(Connection dbconnet,ChargeCodeModel model) throws SQLException {
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
	
	public void UpdateDayOff(Connection dbconnet,LeaveCountModel model) throws SQLException {
		String q = "UPDATE `leave_day_count` SET `Leaved`='"+model.getLeaved()+"' " +
				"WHERE `Emp_id`='"+model.getEmpId()+"' AND `Type_ID`='"+model.getTypeId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateRole(Connection dbconnet,EmployeeModel model) throws SQLException {
		String q = "UPDATE `employee` SET `Role_ID`='"+model.getRole()+"' WHERE `Emp_id` = '"+model.getEmpid()+"';";
//		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateTeam(Connection dbconnet,TeamModel model) throws SQLException {
		String q = "UPDATE `team` SET `Team_name`='"+model.getTeamName()+"',`Team_Host`='"+model.getHost()+"' WHERE `Team_id`='"+model.getTeamId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateCompany(Connection dbconnet,CompanyModel model) throws SQLException {
		String q = "UPDATE `company` SET `Company_Name`='"+model.getCompanyName()+"' WHERE `Comp_ID`='"+model.getCompId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}

	public void UpdateMeetingRoom(Connection dbconnet,MeetingRoomModel model) throws SQLException {
		String q = "UPDATE `meeting_room` SET `Room_Name`='"+model.getRoomName()+"',`description`='"+model.getDescription()+"' WHERE `Room_Id` = '"+model.getRoomId()+"';";
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
	
	public void UpdateLeaveCount(Connection dbconnet,LeaveCountModel model) throws SQLException {
		String q = "UPDATE `leave_day_count` SET `Leaved`= "+model.getLeaved() +" WHERE `Type_ID` = '"+model.getTypeId()+"' AND `Emp_id` = '"+model.getEmpId()+"';";
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
	
	public void UpdateMeeting(Connection dbconnet,MeetingModel model) throws SQLException {
		String q = "UPDATE `meeting_room_booking` "
				+ "SET `Subject`='"+model.getSubject()+"',`Room_Id`='"+model.getRoomId()+"',`Date`='"+model.getDate()+"',"
						+ "`Start_at`='"+model.getStart()+"',`End_at`='"+model.getEnd()+"',"
				+ "`description`='"+model.getDescription()+"' WHERE `Meet_id` = '"+model.getMeetId()+"';";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}

	public void UpdateDocumentType(Connection dbconnet,DocumentTypeModel model) throws SQLException {
		String q = "UPDATE `document_type` SET `Type_name`='"+model.getName()+"' WHERE `Type_ID`='"+model.getId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void UpdateUsername(Connection dbconnet,String username,String id) throws SQLException {
		String q = "UPDATE `login` SET `Username`='"+username+"' WHERE `Id` = '"+id+"';";
		String q2 = "UPDATE `employee` SET `Email`='"+username+"' WHERE `Id` = '"+id+"';";
//		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		PreparedStatement ps1 = dbconnet.prepareStatement(q2);
		ps.executeUpdate();
		ps1.executeUpdate();
	}
	
	public void ActiveEmployee(Connection dbconnet,String id) throws SQLException {
		String q = "UPDATE `login` SET `Active_status`='1' WHERE `Id` = '"+id+"';";
//		System.out.println("sql " + q);
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
	
	public void Delete_2con(Connection dbconnet,String tablename,String col,String value,String col2,String value2) throws SQLException {
		String q = "DELETE  FROM `"+tablename+"` WHERE `"+col+"` = '"+value+"' AND `"+col2+"` = '"+value2+"'";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}
	
	public void DeleteEmployee(Connection dbconnet,String id) throws SQLException {
		String q = "UPDATE `login` SET `Active_status`='0' WHERE `Id` = '"+id+"';";
//		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		ps.executeUpdate();
	}

	// Log

	public void Addlog(Connection dbconnet,String table,String key,String id,String time,String operator_id,String Status,String Action,int Detail_id) throws SQLException {
		String q = "INSERT INTO `"+table+"`(`"+key+"`, `Time`, `Operator`, `Status`, `Action`,`Detail_id`) " +
				"VALUES ('"+id+"','"+time+"','"+operator_id+"','"+Status+"','"+Action+"','"+Detail_id+"');";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Update_Log_Status(Connection dbconnet,String table,String condition,String id) throws SQLException {
		String q = "UPDATE `"+table+"` SET `Status`='0' WHERE `"+condition+"` = '"+id+"';";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

		// Leave Log

		public void Leave_Request_Detail_log(Connection dbconnet,LeaveModel model,String time) throws SQLException {
			String q = "INSERT INTO `leave_request_detail_log`(`Req_id`, `Type_ID`, `Emp_id`, `Begin`, `End`, `Detail`, `Emergency`, `Status`, `Depend`, `Comment`, `Amount`, `Time`) " +
					"SELECT `Req_id`, `Type_ID`, `Emp_id`, `Begin`, `End`, `Detail`, `Emergency`, `Status`, `Depend`, `Comment`, `Amount` , '"+time+"' AS `Time` FROM" +
					"`leave_request` WHERE `Req_id` = "+model.getReqId()+";";
			System.out.println("sql " + q);
			PreparedStatement ps_log = dbconnet.prepareStatement(q);
			ps_log.executeUpdate();
		}

		public void Leave_count_log(Connection dbconnet,LeaveCountModel count_model,LeaveModel leave_model,String time,String Action,int id) throws SQLException {
			String log = "INSERT INTO `leave_day_count_log` (`Emp_id`,`Type_ID`,`Leaved` ,`Time` ,`Action`,`Operator`)" +
					"SELECT `Emp_id` ,`Type_ID`, `Leaved`,'"+time+"' AS `Time` ,'" +
					Action+"' AS `Action` , '"+id+"' AS `Operator`  " +
					"FROM `leave_day_count` WHERE `Emp_id` = '"+count_model.getEmpId()+"' AND `Type_ID` = '"+count_model.getTypeId()+"'; ";
			PreparedStatement ps_log = dbconnet.prepareStatement(log);
			ps_log.executeUpdate();
		}

		// Charge Code Log

	public void Charge_Code_Detail_log(Connection dbconnet,ChargeCodeModel model,String time) throws SQLException {
		String q = "INSERT INTO `charge_code_detail_log`(`Charge_code_id`, `Charge_code_name`, `description`, `Time`) " +
				"SELECT `Charge_code_id`,`Charge_code_name`,`description`, '"+time+"' AS `Time` FROM" +
				"`charge_code` WHERE `Charge_code_id` = '"+model.getChargeCodeId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

		// Location

	public void Location_Detail_log(Connection dbconnet,LocationModel model,String time) throws SQLException {
		String q = "INSERT INTO `location_detail_log`(`Location_id`, `Location_name`, `description`, `Time`) " +
				"SELECT `Location_id`,`Location_name`,`description`,'"+time+"' AS `Time` FROM `location` WHERE `Location_id` = '"+model.getLocationId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

		// Company Log

	public void Company_Detail_log(Connection dbconnet,CompanyModel model,String time) throws SQLException {
		String q = "INSERT INTO `company_detail_log`(`Comp_ID`, `Company_Name`, `Time`) " +
				"SELECT `Comp_ID`,`Company_Name`,'"+time+"' AS `Time` FROM `company` WHERE `Comp_ID` = '"+model.getCompId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}
		//Meeting Room
		public void MeetingRoom_Detail_log(Connection dbconnet,MeetingRoomModel model,String time) throws SQLException {
			String q = "INSERT INTO `meeting_room_detail_log`(`Room_Id`, `Room_Name`, `description`, `Time`) " +
					"SELECT `Room_Id`,`Room_Name`,`description`,'"+time+"' AS `Time` FROM `meeting_room` WHERE `Room_Id` = '"+model.getRoomId()+"'";
			System.out.println("sql " + q);
			PreparedStatement ps_log = dbconnet.prepareStatement(q);
			ps_log.executeUpdate();
		}
		// Expense Request

	public void Expense_Request_Detail_log(Connection dbconnet,ExpenseReqModel model,String time) throws SQLException {
		String q = "INSERT INTO `expense_request_detail_log`(`Req_id`, `Detail`, `create_at`, `cancel_at`, `complete_at`, `status`, `remark`, `Time`) " +
				"SELECT `Req_id`,`Detail`,`create_at`,`cancel_at`,`complete_at`,`status`,`remark`,'"+time+"' AS `Time` " +
				"FROM `expense_request` WHERE `Req_id` = '"+model.getReqId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Expense_Request_File_Detail_log(Connection dbconnet,int id,String time,String req_id) throws SQLException {
		String q = "INSERT INTO `expense_request_file_detail_log`(`File_id`, `Req_id`, `File_name`, `data`, `Time`)" +
				" SELECT `File_id`,`Req_id`,`File_name`,`data`, '"+time+"' AS `Time`  FROM " +
				"`expense_request_file` WHERE `File_id` = '"+id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Add_Expense_File_log(Connection dbconnet,String table,String key,String id,String time,
									 String operator_id,String Status,String Action,int Detail_id,int Req_id) throws SQLException {
		String q = "INSERT INTO `"+table+"`(`"+key+"`, `Time`, `Operator`, `Status`, `Action`,`Detail_id`,`Req_id`) " +
				"VALUES ('"+id+"','"+time+"','"+operator_id+"','"+Status+"','"+Action+"','"+Detail_id+"','"+Req_id+"');";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	//Meeting
	public void Add_Meeting_Booking_Detail_log(Connection dbconnet,int id,String time) throws SQLException {
		String log = "INSERT INTO `meeting_room_booking_detail_log`(`Meet_id`, `Subject`, `Create_at`, `Cancel_at`, `Date`, `Room_Id`, `Start_at`, `End_at`, `Status`, `description`, `Time`) " +
				"SELECT `Meet_id`,`Subject`,`Create_at`,`Cancel_at`,`Date`,`Room_Id`,`Start_at`,`End_at`,`Status`,`description`" +
				", '"+time+"' AS `Time` FROM `meeting_room_booking` WHERE `Meet_id` = '"+id+"'";
		PreparedStatement ps_log = dbconnet.prepareStatement(log);
		ps_log.executeUpdate();
	}

	public void Add_Meeting_log(Connection dbconnet,int meet_id,String time,String Action,int Operator,String Status) throws SQLException {
		String log = "INSERT INTO `meeting_log`(`Meet_id`, `Time`, `Action`, `Operator`, `Status`) " +
				"VALUES ('"+meet_id+"','"+time+"','"+Action+"','"+Operator+"','"+Status+"')";
		PreparedStatement ps_log = dbconnet.prepareStatement(log);
		ps_log.executeUpdate();
	}

	public void Add_Meeting_detail_log(Connection dbconnet,int emp_id,int meet_id,String time,int detail_id) throws SQLException {
		String log = "INSERT INTO `meeting_detail_log`(`Emp_id`, `Meet_id`, `Time`, `Detail_id`) " +
				"VALUES ('"+emp_id+"','"+meet_id+"','"+time+"','"+detail_id+"')";
		PreparedStatement ps_log = dbconnet.prepareStatement(log);
		ps_log.executeUpdate();
	}

	// Team

	public void Team_Detail_log(Connection dbconnet,TeamModel model,String time) throws SQLException {
		String q = "INSERT INTO `team_detail_log`(`Team_id`, `Team_name`, `Creator`, `Team_Host`, `Time`) " +
				"SELECT `Team_id`,`Team_name`,`Creator`,`Team_Host`,'"+time+"' AS `Time` FROM `team` WHERE `Team_id` = '"+model.getTeamId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Emp_Team_log(Connection dbconnet,String id,String time,String operator_id,String Status,String Action) throws SQLException {
		String q = "INSERT INTO `emp_team_log`(`Team_id`, `Time`, `Operator`, `Status`, `Action`) " +
				"VALUES ('"+id+"','"+time+"','"+operator_id+"','"+Status+"','"+Action+"')";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Emp_Team_Detail_log(Connection dbconnet,String emp_id,int team_id,int detail_id,String time) throws SQLException {
		String q = "INSERT INTO `emp_team_detail_log`(`Emp_id`, `Team_id`, `Detail_id`, `Time`) " +
				"VALUES ('"+emp_id+"','"+team_id+"','"+detail_id+"','"+time+"')";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	// Employee

	public void Employee_Detail_log(Connection dbconnet,int id,String time) throws SQLException {
		String q = "INSERT INTO `employee_detail_log`(`Emp_id`, `Title`, `Firstname`, `Lastname`, `Birth_date`, `Gender`, `Phone`, `Id`, `Email`, `Address`, `Role_ID`, `Img`, `Position_ID`, `Comp_ID`, `Time`) " +
				"SELECT `Emp_id`,`Title`,`Firstname`,`Lastname`,`Birth_date`,`Gender`,`Phone`,`Id`," +
				"`Email`,`Address`,`Role_ID`,`Img`,`Position_ID`,`Comp_ID`,'"+time+"' AS `Time` FROM" +
				"`employee` WHERE `Emp_id` = '"+id+"'";
//		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	//Login

	public void Login_Detail_log(Connection dbconnet,int id,String time) throws SQLException {
		String q = "INSERT INTO `login_detail_log`(`Login_id`, `Username`, `Password_hash`, `Last_Login`, `Need_reset_password`, `Active_status`, `Time`) " +
				"SELECT `Id`,`Username`,`Password_hash`,`Last_Login`,`Need_reset_password`,`Active_status`,'"+time+"' " +
				"AS `Time` FROM `login` WHERE `Id` = '"+id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	// News
	public void News_Detail_log(Connection dbconnet,int id,String time) throws SQLException {
		String q = "INSERT INTO `news_detail_log`(`News_id`, `Img`, `Topic`, `Detail`, `Creator`, `start_at`, `end_at`, `Create_date`,`Time`) " +
				"SELECT `News_id`,`Img`,`Topic`,`Detail`,`Creator`,`start_at`,`end_at`,`Create_date`,'"+time+"' AS `Time` FROM `news` WHERE `News_id` = '"+id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	// Document

	public void Document_Type_Detail_log(Connection dbconnet,DocumentTypeModel model,String time) throws SQLException {
		String q = "INSERT INTO `document_type_detail_log`(`Type_ID`, `Type_name`, `Time`) " +
				"SELECT `Type_ID`,`Type_name`,'"+time+"' AS `Time` FROM" +
				"`document_type` WHERE `Type_ID` = '"+model.getId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Document_Request_Detail_log(Connection dbconnet,DocumentReqModel model,String time) throws SQLException {
		String q = "INSERT INTO `document_request_detail_log`(`Req_id`,`Type_ID`, `Detail`, `create_at`, `cancel_at`, `complete_at`, `status`, `remark`, `Time`) " +
				"SELECT `Req_id`,`Type_ID`,`Detail`,`create_at`,`cancel_at`,`complete_at`,`status`," +
				"`remark`,'"+time+"' AS `Time` FROM `document_request` WHERE `Req_id` = '"+model.getReqId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Document_Request_File_Detail_log(Connection dbconnet,int id,String time) throws SQLException {
		String q = "INSERT INTO `document_request_file_detail_log`(`File_id`, `Req_id`, `File_name`, `data`, `Time`)" +
				" SELECT `File_id`,`Req_id`,`File_name`,`data`, '"+time+"' AS `Time`  FROM " +
				"`document_request_file` WHERE `File_id` = '"+id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Add_Document_File_log(Connection dbconnet,String table,String key,String id,String time,
									 String operator_id,String Status,String Action,int Detail_id,int Req_id) throws SQLException {
		String q = "INSERT INTO `"+table+"`(`"+key+"`, `Time`, `Operator`, `Status`, `Action`,`Detail_id`,`Req_id`) " +
				"VALUES ('"+id+"','"+time+"','"+operator_id+"','"+Status+"','"+Action+"','"+Detail_id+"','"+Req_id+"');";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	// Position

	public void Position_Detail_log(Connection dbconnet,int position_id,String time) throws SQLException {
		String q = "INSERT INTO `position_detail_log`(`Position_ID`, `Position_Name`, `Time`) " +
				"SELECT `Position_ID`,`Position_Name`,'"+time+"' AS `Time` FROM `position` WHERE `Position_ID` = '"+position_id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	// Timesheet

	public void TimeSheet_Detail_log(Connection dbconnet,int id,String time) throws SQLException {
		String q = "INSERT INTO `timesheet_detail_log`(`Sheet_id`, `Start_at`, `End_at`, `Date`, `Detail`, `Emp_id`, `Location_id`, `Charge_code_id`, `Remark`, `Time`) " +
				"SELECT`Sheet_id`,`Start_at`,`End_at`,`Date`,`Detail`,`Emp_id`,`Location_id`,`Charge_code_id`,`Remark`," +
				" '"+time+"' AS `Time` FROM `timesheet`WHERE `Sheet_id` = '"+id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	// Checkin_Checkout

	public void Add_Checkin_Checkout_Detail_log(Connection dbconnet,int id,String time) throws SQLException {
		String q = "INSERT INTO `checkin_checkout_detail_log`(`Check_id`, `Checkin_at`, `Checkout_at`, `Emp_id`, `Status_CheckIn`, `Status_CheckOut`, `Detail`, `Time`) " +
				"SELECT `Check_id`,`Checkin_at`,`Checkout_at`,`Emp_id`,`Status_CheckIn`,`Status_CheckOut`,`Detail`," +
				"'"+time+"' AS `Time` FROM `checkin_checkout` WHERE Check_id = '"+id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	public void Update_Checkin_Checkout_Detail_log(Connection dbconnet,CheckInCheckOutModel model,String time) throws SQLException {
		String q = "UPDATE `checkin_checkout_detail_log` SET `Checkin_at`='"+model.getCheckInStr()+"',`Checkout_at`='"+model.getCheckoutStr()+"',`Emp_id`='"+model.getEmpId()+"'," +
				"`Status_CheckIn`='"+model.getStatusCheckIn()+"',`Status_CheckOut`='"+model.getStatusCheckOut()+"',`Detail`='"+model.getDetail()+"',`Time`='"+time+"' WHERE `Check_id`='"+model.getCheckId()+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	// Holiday

	public void Holiday_Detail_log(Connection dbconnet,int id,String time) throws SQLException {
		String q = "INSERT INTO `holiday_list_detail_log`(`Holiday_ID`, `Holiday_Name`, `begin_date`, `end_date`, `Time`) " +
				"SELECT `ID`,`Holiday_Name`,`begin_date`,`end_date`,'"+time+"' AS `Time` FROM `holiday_list` WHERE `ID` = '"+id+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}

	// Working Time
	public void Working_time_Detail_log(Connection dbconnet,String name,String time) throws SQLException {
		String q = "INSERT INTO `working_time_detail_log`(`Day_Name`, `start_work`, `off_work`, `OverTime`, `Time`) " +
				"SELECT `Day_Name`,`start_work`,`off_work`,`OverTime`, '"+time+"' AS `Time` FROM  `working_time` WHERE `Day_Name` = '"+name+"'";
		System.out.println("sql " + q);
		PreparedStatement ps_log = dbconnet.prepareStatement(q);
		ps_log.executeUpdate();
	}
	// Crontab

	public int AddCheckIn_In_Crontab(Connection dbconnet,CheckInCheckOutModel model) throws SQLException {
		String q = "INSERT INTO `checkin_checkout`(`Checkin_at`, `Checkout_at`, `Emp_id`, `Status_CheckIn`, `Status_CheckOut`,`Detail`) " +
				"VALUES ('"+model.getCheckin()+"','"+model.getCheckout()+"','"+model.getEmpId()+"'," +
				"'"+model.getStatusCheckIn()+"','"+model.getStatusCheckOut()+"','"+model.getDetail()+"');";
		System.out.println("sql " + q);
		PreparedStatement ps = dbconnet.prepareStatement(q);
		return ps.executeUpdate();
	}




	private List<Map<String, Object>> convertResultSetFromDB(ResultSet rs) throws SQLException {
		List<Map<String, Object>> resultList = new ArrayList<>();
		ResultSetMetaData rsmd = rs.getMetaData();

		while (rs.next()) {
			int numColumns = rsmd.getColumnCount();
			Map<String, Object> obj = new HashMap<>();

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