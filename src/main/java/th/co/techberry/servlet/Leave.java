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
 * Servlet implementation class Leave
 */
public class Leave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Leave() {
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
		if(responseBodyStr.isEmpty()) {
				try {
					result.putAll(ctrl.Profile_leave(id_in_token));
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
		else if(responseBodyStr.get("Option").equals("Show_Leave_Count")) {
			try {
				result.putAll(ctrl.Profile_leave(id_in_token));
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}
		else if(responseBodyStr.get("Option").equals("Show_Leave_Type")) {
			System.out.println("responseBodyStr"+responseBodyStr);
			try {
				result.putAll(ctrl.Leave_type_mgmt());
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(responseBodyStr.get("Option").equals("Add_Leave_Type")) {
			System.out.println("responseBodyStr"+responseBodyStr);
			try {
				result.putAll(ctrl.Add_Leave_type(responseBodyStr));
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(responseBodyStr.get("Option").equals("Delete_Leave_Type")) {
			System.out.println("responseBodyStr"+responseBodyStr);
			try {
				result.putAll(ctrl.Delete_Leave_Type(responseBodyStr));
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(responseBodyStr.get("Option").equals("Update_Leave_Type")) {
			System.out.println("responseBodyStr"+responseBodyStr);
			try {
				result.putAll(ctrl.Update_Leave_Type(responseBodyStr));
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(responseBodyStr.get("Option").equals("Send_Leave_Req")) {
			System.out.println("responseBodyStr"+responseBodyStr);
			try {
				result.putAll(ctrl.Send_Request(responseBodyStr));
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(responseBodyStr.get("Option").equals("Get_Leave_Req")) {
			System.out.println("responseBodyStr"+responseBodyStr);
			try {
				result.putAll(ctrl.Get_Request(id_in_token));
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(responseBodyStr.get("Option").equals("Response_Leave_Req")) {
			System.out.println("responseBodyStr"+responseBodyStr);
			try {
				result.putAll(ctrl.Response_Leave_Request(responseBodyStr,id_in_token));
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(responseBodyStr.get("Option").equals("Delete_Leave_Req")) {
			System.out.println("responseBodyStr"+responseBodyStr);
			try {
				result.putAll(ctrl.Delete_Leave_Request(responseBodyStr));
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		else if(ch == 404) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setContentType("application/json");
			response.getOutputStream().print(gson.toJson(result));
		}
	}
}
