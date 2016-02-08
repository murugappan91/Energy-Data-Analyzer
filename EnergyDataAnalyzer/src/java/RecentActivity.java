/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Murugappan
 * Servlet to store all the recent graph generation and comparison activities of a user and display in dashboard. 
 */
public class RecentActivity extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet RecentActivity</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet RecentActivity at "
					+ request.getContextPath() + "</h1>");
			out.println("</body>");
			out.println("</html>");
		}
	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String uid = request.getParameter("user");
		StringBuffer res = new StringBuffer();
		try {

			System.out.println("\n Inside Servlet");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:DBCON");
			Statement stcheck = con.createStatement();
			String checkdup = "select * from [UserActivity] where [UID]='"
					+ uid + "' order by [URLRequest]";
			ResultSet rs = stcheck.executeQuery(checkdup);
			//iterate thorough all results, parse  data and store in string builder.
			while (rs.next()) {
				String time = rs.getString("Time");
				String operation = rs.getString("Operation");
				String country = rs.getString("Country");
				String param = rs.getString("Parameter");
				String requrl = rs.getString("URLRequest");
				res.append("<p><a href='");
				res.append(requrl);
				res.append("' target='_blank' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' style='font-weight:bold;color: #3399F2'");
				res.append(">View Graph >> </a>");
				res.append(" On ");
				res.append(time);
				res.append(" <i class='subheading2'>");
				res.append(operation);
				res.append("</i> on <b class='subheading'>");
				res.append(country);
				res.append("</b>&nbsp;with Parameters <b class='subheading'>");
				res.append(param);
				res.append("</b></p><br>");

			}

			

			rs.close(); // close all connections
			stcheck.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e);

		}

		int check = res.toString().length();
		response.setContentType("text/plain");
		// response.setCharacterEncoding("UTF-8");
		//write the recent activity results that could be accessed by HTML
		if (check > 0)
			response.getWriter().write(res.toString());
		else
			response.getWriter().write("<b><i>No Data to Show.</i></b>");
		HttpSession session = request.getSession(true);
		session.setAttribute("mailcontent", res);

	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
