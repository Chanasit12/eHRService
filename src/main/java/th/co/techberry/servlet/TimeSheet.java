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
 * Servlet implementation class TimeSheet
 */
public class TimeSheet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeSheet() {
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
		TimeSheetCtrl data = new TimeSheetCtrl();
		int id_in_token = apiUtil.getIdInToken(request);
		try {
			if(responseBodyStr.get("Option").equals("Show_Time_Sheet")) {
				result.putAll(data.Show_Time_Sheet(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Add_Time_Sheet")) {
				result.putAll(data.Add_Time_Sheet(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Show_Time_Sheet_By_Date")) {
				result.putAll(data.Show_Time_Sheet_By_Date(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Update_Time_Sheet")) {
				result.putAll(data.Updated_Time_Sheet(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Delete_Time_Sheet")) {
				result.putAll(data.Delete_Time_Sheet(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Show_Time_Sheet_By_Id")) {
				result.putAll(data.Show_Time_Sheet_By_id(responseBodyStr));
			}
		}  catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ch = (Integer)result.get("status");
		if(ch == 200) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else if(ch == 400) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
