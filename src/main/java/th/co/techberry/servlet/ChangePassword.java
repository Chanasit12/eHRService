package th.co.techberry.servlet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import th.co.techberry.controller.ChangePasswordCtrl;
import th.co.techberry.util.ApidataUtil;
/**
 * Servlet implementation class ChangePassoword
 */
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
//        super();
        // TODO Auto-generated constructor stub
    }
    @Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	ApidataUtil apiUtil = new ApidataUtil();
		apiUtil.setAccessControlHeaders(resp);
		resp.setStatus(HttpServletResponse.SC_OK);
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ApidataUtil apiUtil = new ApidataUtil();
		System.out.println("request : "+request);
		String username_in_token = apiUtil.getUsernameInToken(request);
		int id_in_token = apiUtil.getIdInToken(request);
		Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
		try {
			apiUtil.setAccessControlHeaders(response);
			Map<String, Object> responseBodyStr = new HashMap<String, Object>();
			responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
			ChangePasswordCtrl ctrl = new ChangePasswordCtrl();
			result.putAll(ctrl.ChangePassword(responseBodyStr,username_in_token,id_in_token));
			int ch = (Integer)result.get("status");
			System.out.println("ch"+ch);
			if(ch == 200) {
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else if(ch == 401) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			else if(ch == 404) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
		String jsonString = new Gson().toJson(result);
		byte[] utf8JsonString = jsonString.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString);
	}

}
