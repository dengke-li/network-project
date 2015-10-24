package fr.uv1.bettingServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing the connection to the database (PostgreSQL).
 * 
 */
public class DatabaseConnection {
	// -----------------------------------------------------------------------------
	// Connection parameters
	private static String username = "bdd1";
	private static String password = "bdd1";
	private static String host = "localhost";
	private static String numPort = "54321";
	private static String base = "BSPORT";
	private static String connectString = "jdbc:postgresql://" + host + ":"
			+ numPort + "/" + base;
	// -----------------------------------------------------------------------------
	// Registration of the PostgreSQL JDBC Driver.
	static {
		try {
			DriverManager.registerDriver(new org.postgresql.Driver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------------------------
	/**
	 * Obtaining a connection to the database.
	 * 
	 * @return an instance of the Connection class.
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		// Instantiation de la connexion
		return DriverManager.getConnection(connectString, username, password);
	}
	// -----------------------------------------------------------------------------
}
