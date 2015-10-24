//package com.first.whatson.servlet;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// * Deconnexion servlet to close a session (not used??)
// * 
// *
// */
//public class Deconnexion extends HttpServlet {
//
//	private static final long serialVersionUID = 1L;
//	public static final String VUE = "/connexion";
//
//
//	
//    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
//        /* Get and destruct current session */
//        System.out.println("====Deconnexion doGet====");
//    	HttpSession session = request.getSession();
//        session.invalidate();
//        System.out.println("You are now deconnected from the app.");
//        
//        /* Display connection page */
//        //this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
//    }
//}
