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
 * Servlet implementation class ChargeCode
 */
public class ChargeCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChargeCode() {
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
//		Gson gson = new Gson();
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		int id_in_token = apiUtil.getIdInToken(request);
		ChargeCodeCtrl data = new ChargeCodeCtrl();
		try{
			if(responseBodyStr.get("Option").equals("Main")) {
				result.putAll(data.ChargeCode());
			}
			else if(responseBodyStr.get("Option").equals("Add")) {
				result.putAll(data.Add_ChargeCode(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Delete")) {
				result.putAll(data.Delete_ChargeCode(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Update")) {
				result.putAll(data.Update_ChargeCode(responseBodyStr,id_in_token));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
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
