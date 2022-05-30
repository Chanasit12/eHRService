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
 * Servlet implementation class Team
 */
public class Team extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Team() {
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
		TeamCtrl data = new TeamCtrl();
		int id_in_token = apiUtil.getIdInToken(request);
		try{
			if(responseBodyStr.isEmpty()) {
				result.putAll(data.Team());
			}
			else if(responseBodyStr.get("Option").equals("Add")) {
				result.putAll(data.Team_Add(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Team_Update")) {
				result.putAll(data.Team_Update(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Member")) {
				result.putAll(data.Member(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Add_Member")) {
				result.putAll(data.Add_member(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Delete_Team")) {
				result.putAll(data.Delete_Team(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Delete_Member")) {
				result.putAll(data.Delete_Member(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Host_Team")) {
				result.putAll(data.Get_Team_By_Host(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Team_By_Emp_id")) {
				result.putAll(data.Get_Team_By_Empid(responseBodyStr));
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
//			response.setContentType("application/json");
//			response.getOutputStream().print(gson.toJson(result));
		}
		else if(ch == 401) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.setContentType("application/json");
//			response.getOutputStream().print(gson.toJson(result));
		}
		else if(ch == 400) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			response.setContentType("application/json");
//			response.getOutputStream().print(gson.toJson(result));
		}
		response.setContentType("application/json");
		String jsonString = new Gson().toJson(result);
		byte[] utf8JsonString = jsonString.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString);
	}
}
