/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Murugappan
 * New User Registration Servlet and generate New user confirmation mail to user
 */
public class RegistrationServlet extends HttpServlet {

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
			out.println("<title>Servlet RegistrationServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet RegistrationServlet at "
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

		String ufname = request.getParameter("fname");
		String ulname = request.getParameter("lname");
		String uid = request.getParameter("name");
		String pass = request.getParameter("password");
		String umailid = request.getParameter("email");
		String usecurityq1 = request.getParameter("secque");
		String usecurityq = usecurityq1.replace('\'', '*');
		String usecurityans = request.getParameter("secans");
		PrintWriter print1 = response.getWriter();

		try {

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:DBCON");
			Statement stinsert = con.createStatement();

			// Mail Confirmation to the user :
			Mailing mail = new Mailing();
			String m_to = umailid;
			String m_subject = "Energy Data Analyzer Registration Confirmation";
			StringBuilder m_text = new StringBuilder();
			m_text.append("Hi ");
			m_text.append(ufname);
			m_text.append(",<br><br>");
			m_text.append("Welcome to Energy Data Analyzer Application. Save this message for your records.");
			m_text.append("<br><br><b>");
			m_text.append("Registered User ID : ");
			m_text.append(uid);
			m_text.append("<br>Security Question : ");
			m_text.append(usecurityq1);
			m_text.append("<br>Your Answer : ");
			m_text.append(usecurityans);
			m_text.append("<br></b>");
			m_text.append("<br><a href='http://localhost:8084/Project/home.html?user=");
			m_text.append(uid);
			m_text.append("'>Go to your home page !</a>");
			m_text.append("<br><br>");
			m_text.append("Thanks,<br>Energy Data Team");
			System.out.println(mail.MailingHTML(m_to, m_subject,
					m_text.toString()));

			// end of code to send Mail
			String insertstring = "Insert into UserReg values('" + ufname
					+ "','" + ulname + "','" + uid + "','" + pass + "','"
					+ umailid + "','" + usecurityq + "','" + usecurityans
					+ "')";
			stinsert.executeUpdate(insertstring);
			String user = uid;
			//redirect to newly created user's home page
			String contextPath = "http://localhost:8084/Project/home.html?user="
					+ user;

			response.sendRedirect(response.encodeRedirectURL(contextPath));

			stinsert.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e);
			print1.print(e);
			print1.print("Exception");
		}
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
