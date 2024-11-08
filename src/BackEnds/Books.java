package BackEnds;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;

public class Books extends Database{
	private LinkedList<Object[]> userReservedBooks = new LinkedList<>();
	private LinkedList<Object[]> availableBookDetails = new LinkedList<>();
	private String bookName;
	private int serialNumber;
	private int quantity;
	private String category;
	private String author;
	
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
	
	public LinkedList<Object[]> availableBooks() {
		String query = "SELECT * FROM books";
		
		try(PreparedStatement statement =  con.prepareStatement(query);ResultSet resultSet = statement.executeQuery()){
				while(resultSet.next()) {
					Object[] bookDetails = new Object[4];
					bookDetails[0] = resultSet.getString("bookName");
					bookDetails[1] = resultSet.getInt("quantity");
					bookDetails[2] = resultSet.getString("category");
					bookDetails[3] = resultSet.getString("author");
					
					availableBookDetails.add(bookDetails);
				}	
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return availableBookDetails;
	}
	
	
	public LinkedList<Object[]> getUserIssuedBooks(String userName){
		userReservedBooks.clear();
		
		String query = "SELECT * FROM issued_books WHERE borrower_userName = ?";
	
		try(PreparedStatement statement = con.prepareStatement(query)){
			statement.setString(1, userName);
			
			try(ResultSet resultSet = statement.executeQuery()){
				while(resultSet.next()) {
					int num = getSerialNumber(resultSet.getString("book_name"));
					
					String getDetails = "SELECT * FROM books WHERE serialNumber = ?";
					try(PreparedStatement getDetailsQuery = con.prepareStatement(getDetails)){
						getDetailsQuery.setInt(1, num);
						try(ResultSet detailsResultSet = getDetailsQuery.executeQuery()){
							while(detailsResultSet.next()) {
								Object[] reservedBooks = new Object[3];
								reservedBooks[0] = detailsResultSet.getString("bookName");
								reservedBooks[1] = detailsResultSet.getString("category");
								reservedBooks[2] = detailsResultSet.getString("author");
								
								userReservedBooks.add(reservedBooks);
							}
							
						}
						
					} catch(SQLException e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return userReservedBooks;
		
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
		 LinkedList<Object[]> books = b.getUserIssuedBooks("Tibon");
		
		 for (Object[] book : books) {
		        System.out.println(Arrays.toString(book));  // Print each book's details as a string array
		    }
	}
	
}
