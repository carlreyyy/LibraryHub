package BackEnds;

import java.sql.*;
import java.util.LinkedList;

public class Books extends Database{
	LinkedList<String[][]> userData = new LinkedList<>();
	String bookName;
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
	
	public LinkedList<Object[]> bookAvailableBooks() {
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
	//author
	//Category BOok
		 public LinkedList<String> searchBooksByCategory(String[] category) {
		        LinkedList<String> foundBooks = new LinkedList<>();

		        StringBuilder queryBuilder = new StringBuilder("SELECT bookName FROM books WHERE category IN (");
		        for (int i = 0; i < category.length; i++) {
		            queryBuilder.append("?");
		            if (i < category.length - 1) {
		                queryBuilder.append(", ");
		            }
		        }
		        queryBuilder.append(")");

		        String query = queryBuilder.toString();
		        System.out.println("Executing query: " + query);

		        try (PreparedStatement statement = con.prepareStatement(query)) {
		            for (int i = 0; i < category.length; i++) {
		                statement.setString(i + 1, category[i]);
		            }

		            try (ResultSet resultSet = statement.executeQuery()) {
		                while (resultSet.next()) {
		                    foundBooks.add(resultSet.getString("bookName"));
		                }
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }

		        return foundBooks;
		    }
		 //search title
		 public String searchTitle(String bookName) {
			    if (bookName == null || bookName.isEmpty()) {
			        return "No Books Available";
			    }

			    String query = "SELECT * FROM books WHERE bookName = ?";
			    String result = "No Books Available";

			    try (PreparedStatement statement = con.prepareStatement(query)) {
			        statement.setString(1, bookName);

			        try (ResultSet rs = statement.executeQuery()) {
			            if (rs.next()) {
			                String foundName = rs.getString("bookName");
			                String author = rs.getString("author");
			                String category = rs.getString("category");
			                int quantity = rs.getInt("quantity");

			                result = "Title: " + foundName + "\n"
			                       + "Author: " + author + "\n"
			                       + "Category: " + category + "\n"
			                       + "Quantity: " + quantity;
			            }
			        }
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }

			    return result;
			}
	
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
	
	//mark as borrowed
		public void borrowBook(String bookName) {
		    String checkQuery = "SELECT quantity, isBorrowed FROM books WHERE bookName = ?";
		    String updateQuery = "UPDATE books SET isBorrowed = TRUE, quantity = quantity - 1 WHERE bookName = ? AND quantity > 0";

		    try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
		        checkStmt.setString(1, bookName);
		        ResultSet results = checkStmt.executeQuery();

		        if (results.next()) {
		            int quantity = results.getInt("quantity");
		            boolean isBorrowed = results.getBoolean("isBorrowed");

		            if (quantity > 0 && !isBorrowed) {
		                try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
		                    updateStmt.setString(1, bookName);
		                    updateStmt.executeUpdate();
		                        System.out.println("Book borrowed successfully: " + bookName);
		                }
		                    }else if (quantity == 0) {
		                System.out.println("Book is out of stock.");
		            } else {
		                System.out.println("Book is already borrowed.");
		            }
		        } else {
		            System.out.println("Book not found in the library.");
		        }
		    } catch (SQLException e) {
		        System.out.println("Error during borrowing: " + e.getMessage());
		    }
		}
		
		
		//mark as returned
		public void returnBook(String bookName) {
		    String updateQuery = "UPDATE books SET isBorrowed = FALSE, quantity = quantity + 1 WHERE bookName = ? AND isBorrowed = TRUE";
		    
		    try (PreparedStatement statement = con.prepareStatement(updateQuery)) {
		        statement.setString(1, bookName);
		        int rowsUpdated = statement.executeUpdate();
		        
		        if (rowsUpdated > 0) {
		            System.out.println("Book returned successfully: " + bookName);
		        }
		    } catch (SQLException e) {
		        System.err.println("Error during return: " + e.getMessage());
		    }
		}
		
		
		//mark as lost
		public void markAsLost(String bookName) {
		    String updateQuery = "UPDATE books SET quantity = quantity - 1 WHERE bookName = ? AND quantity > 0";
		    
		    try (PreparedStatement statement = con.prepareStatement(updateQuery)) {
		        statement.setString(1, bookName);
		        int rowsUpdated = statement.executeUpdate();
		        
		        if (rowsUpdated > 0) {
		            System.out.println("Book marked as lost: " + bookName);
		        } else {
		            System.out.println("Book not found or no copies available.");
		        }
		    } catch (SQLException e) {
		        System.err.println("Error marking book as lost: " + e.getMessage());
		    }
		}
		 @Override
		    public String toString() {
		        return "Book Details:\n" +
		                "Title: " + bookName + "\n" +
		                "Author: " + author + "\n" +
		                "Category: " + category + "\n" +
		                "Quantity: " + quantity;
		    }
		public static void main(String[] args) {
		    System.out.println("Debug: Entering main method.");
		    Books library = new Books();
		    
		    // Search by category
		    String[] categories = {"Fiction", "Science"};
		    System.out.println("Debug: Calling searchBooksByCategory method.");
		    LinkedList<String> results = library.searchBooksByCategory(categories);
		    
		    if (!results.isEmpty()) {
		        System.out.println("Found books:");
		        for (String bookInfo : results) {
		            System.out.println(bookInfo);
		        }
		    } else {
		        System.out.println("No books found in the selected categories.");
		    }
		}
	}
