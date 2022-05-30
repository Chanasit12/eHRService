package th.co.techberry.servlet;

import com.google.gson.Gson;
import th.co.techberry.controller.*;
import th.co.techberry.util.ApidataUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DocumentRequest
 */
public class DocumentRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentRequest() {
//		super();
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
		request.setCharacterEncoding("UTF-8");
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		int id_in_token = apiUtil.getIdInToken(request);
		DocumentReqCtrl ctrl = new DocumentReqCtrl();
		try{
			if(responseBodyStr.get("Option").equals("Respond_request")) {
				result.putAll(ctrl.Response_Request(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Send_request")) {
				result.putAll(ctrl.Send_Request(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_ALL_Requested")) {
				result.putAll(ctrl.Get_All_Document_Request());
			}
			else if(responseBodyStr.get("Option").equals("Canceled_Request_By_Sender")) {
				result.putAll(ctrl.Cancel_Request_By_User(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_All_Requested")) {
				result.putAll(ctrl.Get_All_Document_Request());
			}
			else if(responseBodyStr.get("Option").equals("Get_Requested_For_Mgmt")) {
				result.putAll(ctrl.Get_Requested_for_mgmt());
			}
			else if(responseBodyStr.get("Option").equals("Get_My_Request")) {
				result.putAll(ctrl.Get_My_Requested(id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Request_By_EmpId")) {
				result.putAll(ctrl.Get_Requested_By_Empid(responseBodyStr));
			}
			else if(responseBodyStr.get("Option").equals("Update_Request")) {
				result.putAll(ctrl.Update_Request(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_All_Document_Type")) {
				result.putAll(ctrl.Get_Document_Type());
			}
			else if(responseBodyStr.get("Option").equals("Add_Document_Type")) {
				result.putAll(ctrl.Add_Document_Type(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Update_Document_Type")) {
				result.putAll(ctrl.Update_Document_Type(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Delete_Document_Type")) {
				result.putAll(ctrl.Delete_Document_Type(responseBodyStr,id_in_token));
			}
			else if(responseBodyStr.get("Option").equals("Get_Document_Type_By_Id")) {
				result.putAll(ctrl.Get_Document_Type_By_Type_Id(responseBodyStr));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json;");
		String jsonString = new Gson().toJson(result);
		byte[] utf8JsonString = jsonString.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString);
	}

}
