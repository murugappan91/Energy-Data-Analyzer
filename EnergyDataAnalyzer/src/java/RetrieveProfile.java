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
import java.sql.*;

/**
 *
 * @author Murugappan
 * Servlet to retrieve user profile details from DB and store in HTML recognizable format
 */
public class RetrieveProfile extends HttpServlet {

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
			out.println("<title>Servlet RetrieveProfile</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet RetrieveProfile at "
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
		StringBuilder res = new StringBuilder();

		try {

			//retrieve all user details from Database
			System.out.println("inside servlet Retrieve Profile ");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:DBCON");
			Statement stcheck = con.createStatement();
			String CurentUID = request.getParameter("user");
			System.out.println(CurentUID);
			String checkprof = "select Nickname, Country, Website, Comments, ImageURL from [UserProfile] where [UID]='"
					+ CurentUID + "'"; // to check if already UID exists in DB
			System.out.println("\n Step 2 ");
			ResultSet rs = stcheck.executeQuery(checkprof);

			//iterate through result set and set variables in string builder
			while (rs.next()) {
				System.out.println("\n inside result set");
				String nickname = rs.getString("Nickname");
				String country = rs.getString("Country");
				String website = rs.getString("Website");
				String comments = rs.getString("Comments");
				String imgURL = rs.getString("ImageURL");
				res.append(nickname);
				res.append("*");
				res.append(country);
				res.append("*");
				res.append(website);
				res.append("*");
				res.append(comments);
				res.append("*");
				res.append(imgURL);

			}

			System.out.println(res);

		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setContentType("text/plain");
		// response.setCharacterEncoding("UTF-8");
		response.getWriter().write(res.toString());
		System.out.println("Servlet ending");

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
