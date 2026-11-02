package log_analyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static final String URL = System.getenv()
	        .getOrDefault("DB_URL",
	                "jdbc:mysql://log-db:3306/log_analyzer");

	private static final String USER = System.getenv()
	        .getOrDefault("DB_USER", "loguser");

	private static final String PASSWORD = System.getenv("DB_PASSWORD");
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(URL,USER,PASSWORD);
	}

	
}
