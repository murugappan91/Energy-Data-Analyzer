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

/**
 *
 * @author Murugappan
 */
/**
 *
 * Servlet to retrieve user details and validate security question and answer in the event of user forgets password.
 */
public class ForgotPassword extends HttpServlet {

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
			out.println("<title>Servlet ForgotPassword</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet ForgotPassword at "
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
		String Secqa = "";
		// to check if already UID exists in DB
		String checkdup = "select [SecQue],[SecAns] from [UserReg] where [UID]='"
				+ uid + "'"; 
		int chk = 0;
		try {

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:DBCON");
			Statement stcheck = con.createStatement();

			ResultSet rs = stcheck.executeQuery(checkdup);
			//retrieve the security question and answer from Database
			while (rs.next()) {
				{
					Secqa += rs.getString("SecQue");
					Secqa += "*";
					Secqa += rs.getString("SecAns");
					chk = 1;
				}
			}

			rs.close(); // close all connections
			stcheck.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e);

		}

		
		response.setContentType("text/plain");
		// response.setCharacterEncoding("UTF-8");
		//send security question, amswer detais to HTML
		if (chk == 1)
			response.getWriter().write(Secqa);
		else if (chk == 0)
			response.getWriter().write("invalid");

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
