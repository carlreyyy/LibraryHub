package BackEnds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.time.LocalDate;
import java.sql.Date;

public class Borrower extends Books{
	
	public void reserveBooks(int serialNumber, String userName) {
		String serial = "SELECT * FROM books WHERE serialNumber = ?";
		String user = "SELECT * FROM users_informations WHERE userName = ?";
		try(PreparedStatement serialStatement = con.prepareStatement(serial) ; PreparedStatement userStatement = con.prepareStatement(user)){
			serialStatement.setInt(1, serialNumber);
			userStatement.setString(1, userName);
			
			try(ResultSet serialSet = serialStatement.executeQuery(); ResultSet userSet = userStatement.executeQuery()){
				if(serialSet.next()) {
					if(isAvailable(serialNumber)) {
						if(userSet.next()) {
							String email = userSet.getString("email");
							String bookName = serialSet.getString("bookName");
							
							LocalDate reservationDate = LocalDate.now();
							LocalDate dueDate = reservationDate.plusWeeks(2);
							
							String reserve = "INSERT INTO reserved_books(borrower_userName, borrower_email, book_serialNumber, book_name, reservation_date, due_date) VALUES(?,?,?,?,?,?)";
							
							try(PreparedStatement insertReserve = con.prepareStatement(reserve)){
								insertReserve.setString(1, userName);
								insertReserve.setString(2, email);
								insertReserve.setInt(3, serialNumber);
								insertReserve.setString(4, bookName);
								insertReserve.setDate(5, java.sql.Date.valueOf(reservationDate));
								insertReserve.setDate(6, java.sql.Date.valueOf(dueDate));
								
								insertReserve.executeUpdate();
							} catch(SQLException e) {
								e.printStackTrace();
							}
							
							System.out.println("Book reserved successfuly");
						} else System.out.println("User not found");
					} else System.out.println("Book not available");
				} else System.out.println("Book not found");
			} 		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		Borrower b = new Borrower();
		b.reserveBooks(1005, "Tibon");
	}
	
}
