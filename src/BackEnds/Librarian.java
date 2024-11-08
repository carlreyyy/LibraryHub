package BackEnds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Librarian extends Books{
	
	public void processNextRequest(Boolean add) {
		String query = "SELECT * FROM reserved_books ORDER BY id ASC LIMIT 1";
		try(PreparedStatement statement = con.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery()){
			if(resultSet.next()) {
				int id = resultSet.getInt("id");
				if(add == true) acceptReservedBook(id);
				else denyReservedBook(id);
			} else {
				System.out.println("No pending requests to process.");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void denyReservedBook(int id) {
		String delete = "DELETE FROM reserved_books WHERE id = ?";
		
		try(PreparedStatement statement = con.prepareStatement(delete)){
			statement.setInt(1, id);
			
			int rows = statement.executeUpdate();
			
			if(rows>0) System.out.println("Reservation deleted successfully.");
			else  System.out.println("No reservation found with the specified ID.");
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void acceptReservedBook(int id) {
		String delete = "DELETE FROM reserved_books WHERE id = ?";
		String insert = "INSERT INTO issued_books(borrower_userName,borrower_email,book_serialNumber,book_name) VALUES(?,?,?,?)";
		String query = "SELECT * FROM reserved_books WHERE id = ?";
		String available = "UPDATE books SET quantity = quantity - 1 WHERE serialNumber = ?";
		
		try(PreparedStatement statement = con.prepareStatement(query)){
			statement.setInt(1, id);
			
			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next()) {
					String bookName = resultSet.getString("book_name");
					String userName = resultSet.getString("borrower_userName");
					String email = resultSet.getString("borrower_email");
					int bookSerial = resultSet.getInt("book_serialNumber");
					
					if(isAvailable(bookSerial)) {
						try(PreparedStatement deleteStatement = con.prepareStatement(delete); 
							PreparedStatement insertStatement = con.prepareStatement(insert);
							PreparedStatement updateStatement = con.prepareStatement(available)) {
							insertStatement.setString(1, userName);
							insertStatement.setString(2, email);
							insertStatement.setInt(3, bookSerial);
							insertStatement.setString(4, bookName);
									
							deleteStatement.setInt(1, id);
							
							updateStatement.setInt(1, bookSerial);
							
							insertStatement.executeUpdate();
							deleteStatement.executeUpdate();
							updateStatement.executeUpdate();
							
	                        System.out.println("Reservation accepted and book issued successfully.");
							
						} catch(SQLException e) {
							e.printStackTrace();
						}
					} 		
				}
				else System.out.println("No reserved Books");
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getReservedBookId(String userName, String bookName) {
		 int id = -1;
		 
		 String query = "SELECT * FROM reserved_books WHERE borrower_userName = ? AND book_name = ?";
		 try(PreparedStatement statement  = con.prepareStatement(query)){
			 statement.setString(1, userName);
			 statement.setString(2, bookName);
			 
			 try(ResultSet resultSet = statement.executeQuery()){
				 if(resultSet.next()) id = resultSet.getInt("id");
				 else System.out.println("No reservation with the specific details provided");
			 }
			 
		 } catch(SQLException e) {
			 e.printStackTrace();
		 }
		 
		 return id;
	}
	
	public static void main(String[] args) {
		Librarian l = new Librarian();
		l.processNextRequest(true);
	}
	
}
