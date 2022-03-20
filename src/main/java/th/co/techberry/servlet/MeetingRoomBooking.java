package th.co.techberry.servlet;

import java.io.CharConversionException;
import java.text.ParseException;
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
 * Servlet implementation class MeetingRoomBooking
 */
public class MeetingRoomBooking extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MeetingRoomBooking() {
        super();
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, 
	IOException,CharConversionException{
		// TODO Auto-generated method stub
		ApidataUtil apiUtil = new ApidataUtil();
		System.out.println("request " + request);
		Gson gson = new Gson();
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		MeetingRoomCtrl data = new MeetingRoomCtrl();
		int id_in_token = apiUtil.getIdInToken(request);
		try{
			if(responseBodyStr.get("Option").equals("Show_Meeting_Room")) {
				result.putAll(data.Show_Meeting_Room());
			}
			else if(responseBodyStr.get("Option").equals("Show_Meeting_Room_Detail")) {
				result.putAll(data.Show_Meeting(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Show_Employee_Meeting_By_ID")) {
				result.putAll(data.Show_Employee_Meeting_By_ID(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Show_Selected_Employee_Meeting_Detail")) {
				result.putAll(data.Show_Selected_Employee_Meeting_Detail(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Add_Meeting")) {
				result.putAll(data.Add_Meeting(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Delete_Meeting")) {
				result.putAll(data.Delete_Meeting(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Create_Meeting")) {
				result.putAll(data.Show_Create_Meeting(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Check")) {
				result.putAll(data.Check_Meeting(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Confirm")) {
				result.putAll(data.Confirm_Add_Meeting(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Update")) {
				result.putAll(data.Update_Meeting(responseBodyStr));
			}
		}  catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int ch = (Integer)result.get("status");
		if(ch == 200) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.getOutputStream().print(gson.toJson(result));
		}
		else if(ch == 400) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			response.getOutputStream().print(gson.toJson(result));
		}
		else {
			response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
		}
		response.setContentType("application/json");
		String jsonString = new Gson().toJson(result);
		byte[] utf8JsonString = jsonString.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString);
	}

}
