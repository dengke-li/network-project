package com.first.whatson.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.first.whatson.database.Database;
/**
 * Class that makes the connection of the user to the server, and open a session by instantiating a user bean
 * 
 *
 */
public final class ConnexionForm {
    public static final String EMAIL_FIELD  = "username";
    private static final String PASS_FIELD   = "password";
    private static final String MATCH_FIELD   = "matching";

    private String result;
    private Map<String, String> errors = new HashMap<String, String>();

    public String getResult() {
        return result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    /**
     * Check if user exists and can open a session, based on the received HttpServletRequest
     * @param request
     * @return
     */
    public User connecterUser( HttpServletRequest request ) {
        /* Get form fields */
        String email = getFieldValue( request, EMAIL_FIELD );
        String password = getFieldValue( request, PASS_FIELD );

        User user = new User(email);

        /* Validation of email field. */
        try {
            emailValidation( email );
        } catch ( Exception e ) {
            setError( EMAIL_FIELD, e.getMessage() );
        }
        user.setEmail(email);

        /* Validation of password field. */
        try {
            passwordValidation( password );
        } catch ( Exception e ) {
            setError( PASS_FIELD, e.getMessage() );
        }

        /* Matching user email/password. */
        try {
            matchAuthentication(email, password);
        } catch ( Exception e ) {
            setError( MATCH_FIELD, e.getMessage() );
        }
       
        
        /* Initialisation of the connection. */
        if ( errors.isEmpty() ) {
            result = "Connection success.";
        } else {
            result = "Connection failed.";
        }

        return user;
    }

    /**
     * Check if email is correct format (format of the string)
     */
    private void emailValidation( String email ) throws Exception {
        if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
            throw new Exception( "Put a valid email please." );
        }
    }

    /**
     * Check if password is correct (in the database)
     */
    private void passwordValidation( String motDePasse ) throws Exception {
        if ( motDePasse != null ) {
            if ( motDePasse.length() < 3 ) {
                throw new Exception( "Password needs at least 3 caracters." );
            }
        } else if(!motDePasse.equals("")){
            throw new Exception( "Put the correct password" );
        }
    }

    /**
     * Check if user exists for the password and email couple
     */
    private void matchAuthentication( String email, String password ) throws Exception {
    	 ResultSet rs = null;
         try {
         	Connection con = Database.getConnection();
             PreparedStatement ps=null;
 			ps=con.prepareStatement("select id from users where email= ? and password= ?");
 			ps.setObject(1, email);
 			ps.setObject(2, password);
 			rs = ps.executeQuery();	
         } catch (SQLException e) {
             e.printStackTrace();
         } catch (Exception e) {
 			e.printStackTrace();
 		}
         if(!rs.first())
        	 throw new Exception( "Cannot find email/password couple in database." );
    }
    
    /**
     * Add error message corresponding to the current error
     */
    private void setError( String champ, String message ) {
        errors.put( champ, message );
    }

    /**
     * Return null if field is empty, field value if not, get the value of a field in the HttpServletRequest
     */
    public static String getFieldValue( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}
