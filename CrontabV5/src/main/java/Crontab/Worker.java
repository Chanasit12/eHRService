package Crontab;

import Constant.DatabaseUtil;
import Ctrl.LeaveCtrl;
import Model.CheckInCheckOutModel;
import Model.EmployeeModel;
import com.mysql.jdbc.Connection;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
public class Worker {

    public void Add_Checkin() throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        LeaveCtrl ctrl = new LeaveCtrl();
        List<Map<String, Object>> data ;
        Map<String, Object> Leave;
        Map<String, Object> Role;
        Map<String, Object> Checkin ;
        Map<String, Object> Log_detail ;
        CheckInCheckOutModel check_model = new CheckInCheckOutModel();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EmployeeModel emp_model = new EmployeeModel();
        LocalDateTime now = LocalDateTime.now();
        String[] current_date_time = dtf.format(now).split(" ");
        String[] current_date_raw = current_date_time[0].split("-");
        String Time = current_date_time[0]+" "+"00:00:00";
        String current_time = dtf2.format(now);
        LocalDate localDate = LocalDate.of(Integer.parseInt(current_date_raw[0]),
                Integer.parseInt(current_date_raw[1]),Integer.parseInt(current_date_raw[2]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        String DayName = dayOfWeek.toString();
        try {
            if(!DayName.equals("SATURDAY") && !DayName.equals("SUNDAY") && ctrl.Check_Holliday(current_date_time[0])){
                data = dbutil.selectAll(connection,"employee");
                for(Map<String, Object> temp : data){
                    emp_model.setModel(temp);
                    Leave = dbutil.Check_Add_Checkin_CheckOut(connection,emp_model.getEmpid(),current_date_time[0]);
                    Role = dbutil.select(connection,"user_role","Role_ID",Integer.toString(emp_model.getRole_id()));
                    String role_name = (String) Role.get("Role_Name");
                    if(Leave == null && (role_name.equals("Staff") || role_name.equals("Manager") || role_name.equals("Hr"))){
                        check_model.setEmpId(emp_model.getEmpid());
                        check_model.setCheckInStr(Time);
                        check_model.setCheckoutStr(Time);
                        check_model.setDetail("-");
                        check_model.setStatusCheckOut("-");
                        check_model.setStatusCheckIn("-");
                        dbutil.AddCheckIn(connection,check_model);
                        Checkin = dbutil.select2con(connection,"checkin_checkout","Emp_id","Checkin_at",
                                Integer.toString(check_model.getEmpId()),check_model.getCheckInStr());
                        check_model.setModel(Checkin);
                        dbutil.Add_Checkin_Checkout_Detail_log(connection,check_model.getCheckId(),current_time);
                        Log_detail = dbutil.select2con(connection,"checkin_checkout_detail_log",
                                "Check_id","Time",Integer.toString(check_model.getCheckId()),current_time);
                        dbutil.Addlog(connection,"checkin_checkout_log","Check_id",
                                Integer.toString(check_model.getCheckId()),current_time,"0","1","Add",(Integer)Log_detail.get("Log_id"));
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}