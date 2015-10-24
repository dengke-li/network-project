package com.first.whatson.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.first.whatson.connection.ConnexionForm;

/**
 * Servlet to log the user out of the application
 * 
 */
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String ATT_USER = "user";
	public static final String ATT_FORM = "form";
	public static final String ATT_SESSION_USER = "userSession";
	public static final String CONNECTEDTOKEN = "connectedToken";
    
	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("====LogoutServlet servlet====");
		response.setContentType("text/html;charset=UTF-8");

		//check authentication
		try {
			String email = ConnexionForm.getFieldValue( request, CONNECTEDTOKEN );		
			PrintWriter out = response.getWriter();
			System.out.println("the email account that is trying to log out : "+email);
			if(LoginServlet.isUserConnected(email)){
				LoginServlet.connectedUsers.remove(email);
				out.print(LoginServlet.DISCONNECTED);
			}
			else
				out.print(LoginServlet.CONNECTED);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}
}
