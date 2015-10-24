//package com.first.whatson.servlet;
//
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import com.first.whatson.connection.ConnexionForm;
//import com.first.whatson.connection.User;
//
///**
// * Connexion servlet to connect and to manage search processus
// * 
// *
// */
//public class Connexion extends HttpServlet {
//
//	private static final long serialVersionUID = 1L;
//	public static final String ATT_USER = "user";
//    public static final String ATT_FORM = "form";
//    private static final String URL = "url";
//    public static final String ATT_SESSION_USER = "userSession";
//    public static final String VUE_CONNEXION = "/WEB-INF/connexion.jsp";
//    public static final String VUE_ACCUEIL = "/WEB-INF/index.jsp";
//
//    
//    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {   	
//    	/* Display connection form */
//    	System.out.println("====Connexion doGet====");
//
//    	/* Get session */
//        HttpSession session = request.getSession();
//
//        //if user is connected (user bean not null)
//        //System.out.println("user : "+session.getAttribute(ATT_SESSION_USER));
//        if ( session.getAttribute( ATT_SESSION_USER ) != null ){
//        	request.setAttribute( ATT_SESSION_USER, session.getAttribute(ATT_SESSION_USER) );
//        	//this.getServletContext().getRequestDispatcher( VUE_ACCUEIL ).forward( request, response );	
//        }
//        else{
//        	System.err.println("Not connected! (couldn't authenticate you in doGet Connexion servlet)");
//        	//this.getServletContext().getRequestDispatcher( VUE_CONNEXION ).forward( request, response );
//        }
//    }
//
//    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
//    	System.out.println("====Connexion doPost====");
//    	/* Prepare form connection */
//        ConnexionForm form = new ConnexionForm();
//
//        /* Treat request due to formular and response a new bean (user) */
//        User user = form.connecterUser( request );
//
//        /* Get session from the request */
//        HttpSession session = request.getSession();
//
//        /* Store the formular and the bean in the request object */
//        request.setAttribute( ATT_FORM, form );
//        request.setAttribute( ATT_SESSION_USER, user );
//        
//        /**
//         * If no errors, add the user in the session
//         * if not, delete this user bean, and put null in the session variable for the bean
//         */
//        if ( form.getErrors().isEmpty() ) {
//        	session.setAttribute( ATT_SESSION_USER, user );
//        
//        	System.out.println("Connexion servlet : no error detected, session user created. You are now connected to the app.");
//        	 //response.sendRedirect("http://google.com");
//        	String url = form.getFieldValue( request, URL );
//	//        	if(url!=null)
//	//        		response.sendRedirect(form.getFieldValue( request, URL ));
//	//        	else
//	//        		this.getServletContext().getRequestDispatcher( VUE_ACCUEIL ).forward( request, response );
//        } else {
//        	System.err.println("Connexion servlet : error(s) detected. You are not connected to the app.");
//            session.setAttribute( ATT_SESSION_USER, null );
//            //this.getServletContext().getRequestDispatcher( VUE_CONNEXION ).forward( request, response );
//        }        
//    }
//}
