package BackEnds;
import java.sql.*;

public class Login extends Database{
	protected int userId;
	protected String userName;
	
	public void setId(int userId) {
		this.userId = userId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	
	public void checkInfo(String userName, String password) {
		String query = "SELECT * FROM users_informations WHERE userName = ? AND password = ?";
		try(PreparedStatement statement = con.prepareStatement(query)){
			statement.setString(1, userName);
			statement.setString(2, password);
			
			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next()) {
					setId(resultSet.getInt("id"));
					setUserName(resultSet.getString("userName"));
					System.out.println("Log-in succesful!");
					if(userName.equals("KaaruRee")) {
						//call the admin page
						//Librarian librariran = new Librarian;
					}
					else {
						//call the user page
						//USerPage userPage = new UserPage();
					}
				}
				else {
					System.out.println("Log-in unsuccesful!");
				}
			}
			
		}catch(SQLException e){
			System.out.println("Database error: " + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		Login log = new Login();
		log.checkInfo("reyy", "12345");
		System.out.println(log.getUserId());
	}
	
}
