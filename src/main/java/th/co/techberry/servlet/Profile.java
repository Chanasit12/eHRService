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
        super();
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

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ApidataUtil apiUtil = new ApidataUtil();
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		System.out.print("responseBodyStr"+responseBodyStr);
			if(responseBodyStr.isEmpty()) {
				int id_in_token = apiUtil.getIdInToken(request);
				System.out.println("responseBodyStr"+responseBodyStr);
				ProfileCtrl ctrl = new ProfileCtrl();
				Gson gson = new Gson();
				Map<String, Object> result = new HashMap<String, Object>();
				try {
					result.putAll(ctrl.Profile(id_in_token));
					System.out.print("result"+result);
					int ch = (Integer)result.get("status");
					if(ch == 200) {
						response.setStatus(HttpServletResponse.SC_OK);
						response.setContentType("application/json");
						System.out.print("gson.toJson(result)"+gson.toJson(result));
						response.getOutputStream().print(gson.toJson(result));
					}
					else if(ch == 404) {
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.setContentType("application/json");
						System.out.print("gson.toJson(result)"+gson.toJson(result));
						response.getOutputStream().print(gson.toJson(result));
					}
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(responseBodyStr.get("Option").equals("Update")){
				int id_in_token = apiUtil.getIdInToken(request);
				System.out.println("responseBodyStr"+responseBodyStr);
				ProfileCtrl ctrl = new ProfileCtrl();
				Gson gson = new Gson();
				Map<String, Object> result = new HashMap<String, Object>();
				try {
				result.putAll(ctrl.UpdateProfile(responseBodyStr,id_in_token));
					System.out.print("result"+result);
					int ch = (Integer)result.get("status");
					System.out.println("ch"+ch);
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
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(responseBodyStr.get("Option").equals("Get_Profile_By_Id")){
				System.out.println("responseBodyStr"+responseBodyStr);
				ProfileCtrl ctrl = new ProfileCtrl();
				Gson gson = new Gson();
				Map<String, Object> result = new HashMap<String, Object>();
				try {
				result.putAll(ctrl.GetProfileById(responseBodyStr));
					System.out.print("result"+result);
					int ch = (Integer)result.get("status");
					System.out.println("ch"+ch);
					if(ch == 200) {
						response.setStatus(HttpServletResponse.SC_OK);
						response.setContentType("application/json");
						response.getOutputStream().print(gson.toJson(result));
					}
					else if(ch == 404) {
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.setContentType("application/json");
						response.getOutputStream().print(gson.toJson(result));
					}
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(responseBodyStr.get("Option").equals("Update_Profile_By_Id")){
				System.out.println("responseBodyStr"+responseBodyStr);
				ProfileCtrl ctrl = new ProfileCtrl();
				Gson gson = new Gson();
				Map<String, Object> result = new HashMap<String, Object>();
				try {
				result.putAll(ctrl.UpdateProfileById(responseBodyStr));
					System.out.print("result"+result);
					int ch = (Integer)result.get("status");
					System.out.println("ch"+ch);
					if(ch == 200) {
						response.setStatus(HttpServletResponse.SC_OK);
						response.setContentType("application/json");
						response.getOutputStream().print(gson.toJson(result));
					}
					else if(ch == 404) {
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.setContentType("application/json");
						response.getOutputStream().print(gson.toJson(result));
					}
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(responseBodyStr.get("Option").equals("Get_Profile_Leave")){
				System.out.println("responseBodyStr"+responseBodyStr);
				ProfileCtrl ctrl = new ProfileCtrl();
				Gson gson = new Gson();
				Map<String, Object> result = new HashMap<String, Object>();
				try {
				result.putAll(ctrl.UpdateProfileById(responseBodyStr));
					System.out.print("result"+result);
					int ch = (Integer)result.get("status");
					System.out.println("ch"+ch);
					if(ch == 200) {
						response.setStatus(HttpServletResponse.SC_OK);
						response.setContentType("application/json");
						response.getOutputStream().print(gson.toJson(result));
					}
					else if(ch == 404) {
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.setContentType("application/json");
						response.getOutputStream().print(gson.toJson(result));
					}
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}
