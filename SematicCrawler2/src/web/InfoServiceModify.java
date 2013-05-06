package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dal.ServiceInfoDAL;

/**
 * Servlet implementation class InfoServiceModify
 */
@WebServlet("/InfoServiceModify")
public class InfoServiceModify extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoServiceModify() {
        super();
        // TODO Auto-generated constructor stub
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
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		int id = Integer.parseInt(request.getParameter("id"));
		String type = request.getParameter("type");
		String url = request.getParameter("url");
		String name = request.getParameter("name");
		String desc = request.getParameter("desc");
		ServiceInfoDAL serviceInfoDAL = new ServiceInfoDAL();
		serviceInfoDAL.updateById(id, name, type, desc, url);
		serviceInfoDAL.closeConnection();
		type = java.net.URLEncoder.encode(type,"UTF-8");
		response.sendRedirect("InfoService.jsp?type="+type);
	}

}
