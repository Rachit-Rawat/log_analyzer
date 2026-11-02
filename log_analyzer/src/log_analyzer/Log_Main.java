package log_analyzer;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Log_Main {

	public static void main(String[] args) throws IOException {
		
	    readLogFile();

	}
	
	public static void readLogFile() throws IOException{
		
		Map<String, Integer> sevCount = new HashMap<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("application.log"))){
			
		String line;
				
				while((line=br.readLine()) != null) {
					String[] parts = line.trim().split("\\s+",4);
					
					if(parts.length <4) {
						continue;
					}
					
					String severity = parts[2];
					
					sevCount.put(severity, sevCount.getOrDefault(severity,0)+1);
					
					// Extract timestamp
					LocalDateTime timestamp = LocalDateTime.parse(
					    parts[0] + "T" + parts[1]
					);

					// Extract service and message
					String rest = parts[3];

					int closingBracket = rest.indexOf(']');

					if (rest.startsWith("[") && closingBracket > 0) {
					    String service = rest.substring(1, closingBracket);
					    String message = rest.substring(closingBracket + 1).trim();

					    insertLogEntry(timestamp, severity, service, message);
					}
					
					
				}
			       System.out.println("===== LOG ANALYSIS REPORT =====");
		            System.out.println("Severity Counts:");
		            
		            for(Map.Entry<String, Integer> entry : sevCount.entrySet()) {
		            	System.out.println(entry.getKey()+":"+entry.getValue());
		            }
		
	}
	catch(IOException e) {
		System.out.println("Error reading file:" +e.getMessage());
	}
}
	
	public static void insertLogEntry( LocalDateTime timestamp,
	        String severity,
	        String service,
	        String message) {
		
		String sql="INSERT INTO log_entries" +"(log_timestamp, severity, service, message) "+"VALUES (?,?,?,?)";
		
		try(Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)){
			 statement.setObject(1, timestamp);
		        statement.setString(2, severity);
		        statement.setString(3, service);
		        statement.setString(4, message);

		        statement.executeUpdate();
		}catch(SQLException e) {
			System.out.println("failed to insert log:" +e.getMessage());
		}
	}
}