package th.co.techberry.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import th.co.techberry.controller.*;
import th.co.techberry.util.ApidataUtil;

/**
 * Servlet implementation class Leave
 */
public class Leave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Leave() {
//        super();
        // TODO Auto-generated constructor stub
    }
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApidataUtil apiUtil = new ApidataUtil();
		apiUtil.setAccessControlHeaders(resp);
		resp.setStatus(HttpServletResponse.SC_OK);
		System.out.println("resp " + resp);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ApidataUtil apiUtil = new ApidataUtil();
		Gson gson = new Gson();
		Map<String, Object> result = new HashMap<String, Object>();
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		LeaveCtrl ctrl = new LeaveCtrl();
		int id_in_token = apiUtil.getIdInToken(request);
		try {
			if(responseBodyStr.isEmpty()) {
				result.putAll(ctrl.Profile_leave(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Show_Leave_Type")) {
				result.putAll(ctrl.Leave_type_mgmt());
			}
			else if(responseBodyStr.get("Option").equals("Add_Leave_Type")) {
				result.putAll(ctrl.Add_Leave_type(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Delete_Leave_Type")) {
				result.putAll(ctrl.Delete_Leave_Type(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Update_Leave_Type")) {
				result.putAll(ctrl.Update_Leave_Type(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Send_Leave_Req")) {
				result.putAll(ctrl.Send_Request(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Leave_Req_Mgmt")) {
				result.putAll(ctrl.Get_Request(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Leave_Req")) {
				result.putAll(ctrl.Leave_request_By_Emp_id(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Response_Leave_Req")) {
				result.putAll(ctrl.Response_Leave_Request(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Leave_info_By_Empid")) {
				result.putAll(ctrl.Emp_leave_information_By_id(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Send_Cancellation")) {
				result.putAll(ctrl.Send_Cancellation(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Response_Cancellation")) {
				result.putAll(ctrl.Response_Cancelled_Leave_Request(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_All_Leaved_Req")) {
				result.putAll(ctrl.All_Leave_Request());
			}
			else if(responseBodyStr.get("Option").equals("Get_All_Leaved_Count")) {
				result.putAll(ctrl.All_Leave_Count());
			}
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e){
			e.printStackTrace();
		}
		int ch = (Integer)result.get("status");
		if(ch == 200) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else if(ch == 400) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		else if(ch == 404) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		response.setContentType("application/json");
		String jsonString = new Gson().toJson(result);
		byte[] utf8JsonString = jsonString.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString);
	}
}
