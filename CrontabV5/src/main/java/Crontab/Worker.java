package Crontab;

import Constant.*;
import Model.*;
import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
public class Worker {

    public void Add_Leavecount() throws SQLException, ClassNotFoundException {
        DatabaseUtil dbutil = new DatabaseUtil();
        Connection connection = dbutil.connectDB();
        List<Map<String, Object>> Employees ;
        List<Map<String, Object>> Leave_count ;
        Map<String, Object> Leave_type ;
        Map<String, Object> Role;
        Map<String, Object> Log_detail ;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        EmployeeModel emp_model = new EmployeeModel();
        LeaveCountModel count_model = new LeaveCountModel();
        LeaveTypeModel type_model = new LeaveTypeModel();
        String[] current_date_time = dtf.format(now).split(" ");
        String[] current_date_raw = current_date_time[0].split("-");
        String Time = current_date_time[0]+" "+"00:00:00";
        String current_time = dtf2.format(now);
        try {
            Employees = dbutil.selectAll(connection,"employee");
            for(Map<String, Object> Employee : Employees){
                emp_model.setModel(Employee);
                Leave_count = dbutil.selectArray(connection,"leave_day_count","Emp_id",Integer.toString(emp_model.getEmpid()));
                for(Map<String, Object> leave : Leave_count){
                    count_model.setModel(leave);
                    Leave_type = dbutil.select(connection,"leave_type","Type_ID",Integer.toString(count_model.getTypeId()));
                    type_model.setModel(Leave_type);
                    if(type_model.getName() != "Marriage" && type_model.getName() != "Maternity"){
                        if(count_model.getLeaved() > type_model.getNum_can_add() || count_model.getLeaved() == type_model.getNum_can_add()){
                            count_model.setLeaved(type_model.getNum_per_year()+ type_model.getNum_can_add());
                        }else{
                            count_model.setLeaved(type_model.getNum_per_year()+ count_model.getLeaved());
                        }
                        dbutil.UpdateLeaveCount(connection,count_model);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}