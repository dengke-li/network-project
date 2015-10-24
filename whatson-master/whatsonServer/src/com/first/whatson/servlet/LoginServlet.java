package com.first.whatson.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.first.whatson.connection.ConnexionForm;
import com.first.whatson.connection.User;


/**
 * Servlet to connect the user and start a new session
 * 
 *
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String ATT_USER = "user";
	public static final String ATT_FORM = "form";
	public static final String ATT_SESSION_USER = "userSession";
	public static final int CONNECTED = 1;
	public static final int DISCONNECTED = 0;

    public static Map<String,User> connectedUsers = new HashMap<String,User>();
    
    public static boolean isUserConnected(String user){
    	return connectedUsers.containsKey(user);
    }
	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("====LoginServlet servlet====");
		response.setContentType("text/html;charset=UTF-8");

		//check authentication
		try {
			/* Prepare form connection */
			ConnexionForm form = new ConnexionForm();

			/* Treat request due to formular and response a new bean (user) */
			User user = form.connecterUser( request );

			/* Get session from the request */
			HttpSession session = request.getSession();

			/* Store the formular and the bean in the request object */
			request.setAttribute( ATT_FORM, form );
			request.setAttribute( ATT_SESSION_USER, user );

			/**
			 * If no errors, add the user in the session
			 * if not, delete this user bean, and put null in the session variable for the bean
			 */
			PrintWriter out = response.getWriter();
			if ( form.getErrors().isEmpty() ) {
				if(!connectedUsers.containsKey(user.getEmail()))
	        		connectedUsers.put(user.getEmail(), new User(user.getEmail()));
				System.out.println("Connexion servlet : no error detected, session user created. You are now connected to the app.");
				out.print(CONNECTED);
			} else {
				System.err.println("Connexion servlet : error(s) detected. You are not connected to the app.");
				session.setAttribute( ATT_SESSION_USER, null );
				out.print("Authentication error, couldn't connect");
			}
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
