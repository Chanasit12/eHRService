package th.co.techberry.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class News
 */
public class News extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public News() {
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
		apiUtil.setAccessControlHeaders(response);
		Map<String, Object> responseBodyStr = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		responseBodyStr.putAll(apiUtil.getRequestBodyToMap(request));
		int id_in_token = apiUtil.getIdInToken(request);
		NewsCtrl news = new NewsCtrl();
		if(responseBodyStr.isEmpty()) {
			try {
				result.putAll(news.News());
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				System.out.println("test " + gson.toJson(result));
//				response.getOutputStream().print(gson.toJson(result));
				PrintWriter out = response.getWriter();
				out.println(gson.toJson(result));
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			if(responseBodyStr.get("Option").equals("Add")) {
				try {
					result.putAll(news.Add_News(responseBodyStr,id_in_token));
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("application/json");
					response.getOutputStream().print(gson.toJson(result));
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(responseBodyStr.get("Option").equals("Getall")) {
				try {
					result.putAll(news.NewsAll());
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("application/json");
					response.getOutputStream().print(gson.toJson(result));
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(responseBodyStr.get("Option").equals("Update")){
				try {
					result.putAll(news.Update_News(responseBodyStr));
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("application/json");
					response.getOutputStream().print(gson.toJson(result));
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(responseBodyStr.get("Option").equals("Delete")) {
				try {
					result.putAll(news.Delete_News(responseBodyStr));
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
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(responseBodyStr.get("Option").equals("")) {
				result.put("status",400);
//				result.put("Message","Not found");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setContentType("application/json");
				response.getOutputStream().print(gson.toJson(result));
			}


		}
	}
}

