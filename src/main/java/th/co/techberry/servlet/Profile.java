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
 * Servlet implementation class Profile
 */
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
//        super();
        // TODO Auto-generated constructor stub
    }
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApidataUtil apiUtil = new ApidataUtil();
		apiUtil.setAccessControlHeaders(resp);
		resp.setStatus(HttpServletResponse.SC_OK);
//		System.out.println("resp " + resp);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ApidataUtil apiUtil = new ApidataUtil();
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		ProfileCtrl ctrl = new ProfileCtrl();
		Gson gson = new Gson();
		Map<String, Object> result = new HashMap<>();
		int id_in_token = apiUtil.getIdInToken(request);
		try{
			if(responseBodyStr.isEmpty()) {
				result.putAll(ctrl.Profile(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Update")){
				result.putAll(ctrl.UpdateProfile(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Profile_By_Id")){
				result.putAll(ctrl.GetProfileById(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Update_Profile_By_Id")){
				result.putAll(ctrl.UpdateProfileById(responseBodyStr,id_in_token));
			}
		}catch (ClassNotFoundException e) {
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
		else if(ch == 404) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		response.setContentType("application/json");
		String jsonString = new Gson().toJson(result);
		byte[] utf8JsonString = jsonString.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString);
	}
}
