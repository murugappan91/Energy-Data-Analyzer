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
 * Servlet to update user profile photo and other details.
 */
public class ProfilePageServlet extends HttpServlet {

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
			/* TODO output your page here. You may use following sample code. */
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet ProfilePageServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet ProfilePageServlet at "
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
		// processRequest(request, response);

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
// retrieve all fields from client side
		PrintWriter print1 = response.getWriter();
		String upUID = request.getParameter("UIDToUpdateProfile");
		String upimageurl = request.getParameter("userimageurl");
		String upnickname = request.getParameter("usernickname");
		String upcountry = request.getParameter("usercountry");
		String upwebsite = request.getParameter("userwebsite");
		String upcomments = request.getParameter("comments");
	
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:server");
			System.out.println("\n Step 1");
			Statement stToCheck = con.createStatement();
			String Checkstmt = " select UID from [UserProfile] where UID='"
					+ upUID + "'";
			String UIDchk = null;
			ResultSet re = stToCheck.executeQuery(Checkstmt);

			while (re.next()) {
				{
					UIDchk = re.getString("UID");
				}
			}

			if (UIDchk == null) {

				// Code to Save Profile details to DB for first time
				Statement stToInsertProfileValues = con.createStatement();
				String insertstring = "Insert into [UserProfile] values('"
						+ upUID + "','" + upnickname + "','" + upcountry
						+ "','" + upwebsite + "','" + upcomments + "','"
						+ upimageurl + "')";
				stToInsertProfileValues.executeUpdate(insertstring);
				stToInsertProfileValues.close();
			}

			else
			// update existing profile of user
			{
				Statement stToUpdateProfileValues = con.createStatement();
				String updatestring = "Update  [UserProfile] set [Nickname]='"
						+ upnickname + "',[Country]='" + upcountry
						+ "',[Website]='" + upwebsite + "',[Comments]='"
						+ upcomments + "',[ImageURL]='" + upimageurl
						+ "' where UID='" + upUID + "'";
				stToUpdateProfileValues.executeUpdate(updatestring);
				stToUpdateProfileValues.close();
			}

			String contextPath = "http://localhost:8084/Project/home.html?user="
					+ upUID;

			response.sendRedirect(response.encodeRedirectURL(contextPath));
		
			stToCheck.close(); // close all connections
			
			con.close();
		}

		catch (Exception e) {
			print1.print(e);
		}

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
