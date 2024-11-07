package BackEnds;
import java.sql.*;
import java.util.Hashtable;

public class Registration extends Database{
	protected Hashtable<String,String> userData;
	
	public void setUserData(Hashtable<String,String> userData) {
		this.userData = userData;
	}
	
	public void register() throws SQLException{
		if(con != null && userData != null) {
			String query = "SELECT * FROM users_informations WHERE userName = ? OR email = ? OR phoneNumber = ?";
			try(PreparedStatement statement = con.prepareStatement(query)){
				statement.setString(1, userData.get("userName"));
				statement.setString(2, userData.get("email"));
				statement.setString(3, userData.get("phoneNumber"));
				
				try(ResultSet resultSet = statement.executeQuery()){
					if(resultSet.next()) {
						System.out.println("User with this username, phone number, or email already exists.");
					}
					else {
						String insertQuery = "INSERT INTO users_informations(userName,firstName,lastName,password,phoneNumber,email) VALUES (?,?,?,?,?,?)";
						try(PreparedStatement insertStatement  = con.prepareStatement(insertQuery)){
							 insertStatement.setString(1, userData.get("userName"));
		                     insertStatement.setString(2, userData.get("firstName"));
		                     insertStatement.setString(3, userData.get("lastName"));
		                     insertStatement.setString(4, userData.get("password"));
		                     insertStatement.setString(5, userData.get("phoneNumber"));
		                     insertStatement.setString(6, userData.get("email"));
		                     
		                     insertStatement.executeUpdate();
		                     System.out.println("User data inserted into the database.");
						} catch(SQLException e) {
	                        System.out.println("Error inserting user data: " + e.getMessage());
						}
					}
				}
			} catch(SQLException e) {
	            System.out.println("Error checking existing user: " + e.getMessage());
			}
		}
	}
	
	
	public static void main(String[] args) throws SQLException {
		Registration reg = new Registration();
		Hashtable<String,String> userData = new Hashtable<>();
		userData.put("userName", "Tibon");
		userData.put("firstName", "Maloy");
		userData.put("lastName", "Tibon");
		userData.put("password", "123");
		userData.put("phoneNumber", "09318367957");
		userData.put("email","rey@gmail.com");
		
		reg.setUserData(userData);
		reg.register();
	}
	
}
