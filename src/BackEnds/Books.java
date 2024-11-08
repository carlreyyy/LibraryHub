package BackEnds;

import java.sql.*;
import java.util.LinkedList;

public class Books extends Database{
	LinkedList<String[][]> userData = new LinkedList<>();
	String bookName;
	int serialNumber;
	int quantity;
	String category;
	String author;
	
	public Books(String bookName, int quantity, String category, String author) {
		this.bookName = bookName;
		this.quantity = quantity;
		this.category = category;
		this.author = author;
	}
	
	public Books() {	
	}
	
	//getters
	public int getSerialNumber(String bookName) {
		int serialNumber = -1;
		String query = "SELECT * FROM books WHERE bookName = ?";
		try(PreparedStatement statement = con.prepareStatement(query)){
			statement.setString(1, bookName);
		
			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next()) serialNumber = resultSet.getInt("serialNumber");
				else System.out.println("No book found with the name: " + bookName);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return serialNumber;
	}
	
	//Database Interaction
	public void updateQuantity(int serialNumber, int quantity) {
		String query = "UPDATE books SET quantity = ? WHERE serialNumber = ?";
			
		try(PreparedStatement statement = con.prepareStatement(query)){
			statement.setInt(1, quantity);
			statement.setInt(2, serialNumber);
				
			int rowsUpdated = statement.executeUpdate();
			if(rowsUpdated > 0) System.out.println("Quantity updated successfully.");
			else  System.out.println("Book not available.");
				
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//bookName
	
	//quantity
	
	//category
	
	//author
	
	
	//Add and Delete Books
	public void addBook(String bookName, int quantity, String category, String author) {
		String query = "INSERT INTO books(bookName,quantity,category,author) VALUES(?,?,?,?)";
		try(PreparedStatement statement = con.prepareStatement(query)){
			statement.setString(1, bookName);
			statement.setInt(2, quantity);
			statement.setString(3, category);
			statement.setString(4, author);
			
			statement.executeUpdate();
			System.out.println("Books data inserted into the database.");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteBook(int serialNumber) {
		String query = "DELETE FROM books where serialNumber = ?";
		
		try(PreparedStatement statement = con.prepareStatement(query)){
			statement.setInt(1, serialNumber);
			int update = statement.executeUpdate();
			 
			 if(update > 0) System.out.println("Succesfully deleted the book");
			 else System.out.println("No book found with the provided serial number.");
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isAvailable(int serialNumber) {
		 boolean flag = false;
		 String query = "SELECT * FROM books WHERE serialNumber = ?";
		 
		 try(PreparedStatement statement = con.prepareStatement(query)){
			 statement.setInt(1, serialNumber);
			 
			 try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next()) {
					int quantity = resultSet.getInt("quantity");
					if(quantity > 0) flag = true;
					else System.out.println("No books available");
				} else {
					System.out.println("No books with serial number " + serialNumber);
				}
			 }
		 } catch(SQLException e) {
			 e.printStackTrace();
		 }
		 
		 return flag;
	}
	
	public static void main(String[] args) {
		Books b = new Books();
		System.out.println(b.isAvailable(10001));
		
	}
	
}
