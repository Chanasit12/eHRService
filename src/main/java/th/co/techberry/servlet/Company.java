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
 * Servlet implementation class Company
 */
public class Company extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Company() {
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
//		Gson gson = new Gson();
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		int id_in_token = apiUtil.getIdInToken(request);
		CompanyCtrl data = new CompanyCtrl();
		try{
			if(responseBodyStr.isEmpty()) {
				result.putAll(data.Company());
			}
			else if(responseBodyStr.get("Option").equals("Add")) {
				result.putAll(data.Add_Company(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Update")) {
				result.putAll(data.Update_Company(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Delete")) {
				result.putAll(data.Delete_Company(responseBodyStr,id_in_token));
			}
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int status = (Integer)result.get("status");
		if(status == 200){
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		response.setContentType("application/json");
		String jsonString = new Gson().toJson(result);
		byte[] utf8JsonString = jsonString.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString);
	}

}
