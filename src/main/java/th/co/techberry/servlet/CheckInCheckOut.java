package th.co.techberry.servlet;

import java.io.IOException;
import java.sql.SQLException;
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
 * Servlet implementation class CheckInCheckOut
 */
public class CheckInCheckOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckInCheckOut() {
//        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	ApidataUtil apiUtil = new ApidataUtil();
		apiUtil.setAccessControlHeaders(resp);
		resp.setStatus(HttpServletResponse.SC_OK);
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
		System.out.println("request " + request);
		Gson gson = new Gson();
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		CheckInCheckOutCtrl data = new CheckInCheckOutCtrl();
		int id_in_token = apiUtil.getIdInToken(request);
		try{
			if(responseBodyStr.get("Option").equals("Main")) {
					result.putAll(data.CheckInCheckOut_Data(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Check_In")) {
				result.putAll(data.CheckIn(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Check_Out")) {
				result.putAll(data.CheckOut(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Check_List_Show_Emp_id")) {
				result.putAll(data.Get_CheckInCheckOut_By_Emp_Id(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Get_Check_List_Show_Emp_id")) {
				result.putAll(data.Get_CheckInCheckOut_By_Emp_Id(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Get_All_Check_List")) {
				result.putAll(data.Get_ALL_CheckInCheckOut(responseBodyStr));
			}
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		int ch = (Integer)result.get("status");
//		if(ch == 200) {
//		}
//		else if(ch == 400) {
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//		}
//		else {
//			response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
//		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		String jsonString = new Gson().toJson(result);
		byte[] utf8JsonString = jsonString.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString);
	}

}
