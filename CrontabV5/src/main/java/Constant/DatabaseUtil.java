package Constant;

import Model.*;
import com.mysql.jdbc.Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil
{
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

    public void UpdateLeaveCount(Connection dbconnet,LeaveCountModel model) throws SQLException {
        String q = "UPDATE `leave_day_count` SET `Leaved`= "+model.getLeaved() +" WHERE `Type_ID` = '"+model.getTypeId()+"' AND `Emp_id` = '"+model.getEmpId()+"';";
        System.out.println("sql " + q);
        PreparedStatement ps = dbconnet.prepareStatement(q);
        ps.executeUpdate();
    }

    public void Addlog(Connection dbconnet,String table,String key,String id,String time,String operator_id,String Status,String Action,int Detail_id) throws SQLException {
        String q = "INSERT INTO `"+table+"`(`"+key+"`, `Time`, `Operator`, `Status`, `Action`,`Detail_id`) " +
                "VALUES ('"+id+"','"+time+"','"+operator_id+"','"+Status+"','"+Action+"','"+Detail_id+"');";
        System.out.println("sql " + q);
        PreparedStatement ps_log = dbconnet.prepareStatement(q);
        ps_log.executeUpdate();
    }


    private List<Map<String, Object>> convertResultSetFromDB(ResultSet rs) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            int numColumns = rsmd.getColumnCount();
            Map<String, Object> obj = new HashMap<>();
            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = rsmd.getColumnName(i);
                if (rsmd.getColumnType(i) == Types.ARRAY) {
                    obj.put(column_name, rs.getArray(column_name));
                } else if (rsmd.getColumnType(i) == Types.BIGINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
                    obj.put(column_name, rs.getBoolean(column_name));
                } else if (rsmd.getColumnType(i) == Types.BLOB) {
                    obj.put(column_name, rs.getBlob(column_name));
                } else if (rsmd.getColumnType(i) == Types.DOUBLE) {
                    obj.put(column_name, rs.getDouble(column_name));
                } else if (rsmd.getColumnType(i) == Types.FLOAT) {
                    obj.put(column_name, rs.getFloat(column_name));
                } else if (rsmd.getColumnType(i) == Types.INTEGER) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == Types.NVARCHAR) {
                    obj.put(column_name, rs.getNString(column_name));
                } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
                    obj.put(column_name, rs.getString(column_name));
                } else if (rsmd.getColumnType(i) == Types.TINYINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == Types.SMALLINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == Types.DATE) {
                    obj.put(column_name, rs.getDate(column_name));
                } else if (rsmd.getColumnType(i) == Types.TIMESTAMP) {
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
