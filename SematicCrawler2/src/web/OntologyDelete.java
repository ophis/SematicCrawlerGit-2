package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dal.OntologyDAL;

/**
 * Servlet implementation class OntologyDelete
 */
@WebServlet("/OntologyDelete")
public class OntologyDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OntologyDelete() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		int id = Integer.parseInt(request.getParameter("id"));
		OntologyDAL ontologyDAL = new OntologyDAL();
		String type = ontologyDAL.getTypeById(id);
		ontologyDAL.deleteById(id);
		ontologyDAL.closeConnection();
		type = java.net.URLEncoder.encode(type,"UTF-8");
		response.sendRedirect("Ontology.jsp?type="+type);
	}

}
