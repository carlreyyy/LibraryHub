package BackEnds;

import java.sql.*;

public class Database {
	protected Connection con;
	
	public Database() {
		try {
			connectToDatabase();
		} catch(SQLException e) {
			System.out.println("Database connection failed: " + e.getMessage());
		}
	}
	
	public void connectToDatabase() throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryhub","root","");
		} catch(ClassNotFoundException e) {
			System.out.println("JDBC Driver not found: " + e.getMessage());
		}
	}
	
	public static void main(String[] args) throws SQLException {
		Database db = new Database();
		db.connectToDatabase();
	}
	
}
