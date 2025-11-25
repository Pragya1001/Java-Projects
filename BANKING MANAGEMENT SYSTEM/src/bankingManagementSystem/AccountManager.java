package bankingManagementSystem;
import java.util.Scanner;
import java.sql.*;
//import java.math.BigDecimal;

public class AccountManager {

		private Connection connection;
		private Scanner scanner;
		 public AccountManager(Connection connection,Scanner scanner)
		 {
			 this.connection=connection;
			 this.scanner=scanner;
		 }
		 
		 public void creditMoney(long accountNumber) throws SQLException
		 {
			 scanner.nextLine();
			 System.out.println("Enter amount: ");
			 double amount=scanner.nextDouble();
			 scanner.nextLine();
			 System.out.println("Enter security pin: ");
			 String securityPin=scanner.nextLine();
			 
			 try {
				 connection.setAutoCommit(false);
				 if(accountNumber!=0)
				 {
					 PreparedStatement preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
					 preparedStatement.setLong(1, accountNumber);
					 preparedStatement.setString(2, securityPin);
					 ResultSet resultSet=preparedStatement.executeQuery();
					 
					 if(resultSet.next())
					 {
							 String creditQuery="update accounts set balance=balance+? where account_number=?";
							 PreparedStatement preparedStatement1=connection.prepareStatement(creditQuery);
							 preparedStatement1.setDouble(1, amount);
							 preparedStatement1.setLong(2, accountNumber);
							 int rowsAffected=preparedStatement1.executeUpdate();
							 if(rowsAffected>0)
							 {
								 System.out.println("Rs. "+ amount+ "credited successfully. ");
								 connection.commit();
								 connection.setAutoCommit(true);
								 return;
							 }else
							 {
								 System.out.println("Transaction failed ");
								 connection.rollback();
								 connection.setAutoCommit(true);
							 }
						 }else {
							 System.out.println("Invalid pin ");
					 }
				}
				 
			 }catch(SQLException e )
			 {
				 e.printStackTrace();
			 }
			 connection.setAutoCommit(true);
		 }
		 
		 public void debitMoney(long accountNumber) throws SQLException
		 {
			 scanner.nextLine();
			 System.out.print("Enter amount: ");
			 double amount=scanner.nextDouble();
			 scanner.nextLine();
			 System.out.print("enter the security pin: ");
			 String securityPin=scanner.nextLine();
			 try {
				 connection.setAutoCommit(false);
				 if(accountNumber!=0)
				 {
					 PreparedStatement preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
					 preparedStatement.setLong(1, accountNumber);
					 preparedStatement.setString(2, securityPin);
					 ResultSet resultSet=preparedStatement.executeQuery();
					 
					 if(resultSet.next())
					 {
						 double currentBalance= resultSet.getDouble("balance");
						 if(amount<=currentBalance)
						 {
							 String debitQuery="update accounts set balance=balance-? where account_number=?";
							 PreparedStatement preparedStatement1=connection.prepareStatement(debitQuery);
							 preparedStatement1.setDouble(1, amount);
							 preparedStatement1.setLong(2, accountNumber);
							 int rowsAffected=preparedStatement1.executeUpdate();
							 if(rowsAffected>0)
							 {
								 System.out.println("Rs. "+ amount+ "debited successfully. ");
								 connection.commit();
								 connection.setAutoCommit(true);
								 return;
							 }else
							 {
								 System.out.println("Transaction failed ");
								 connection.rollback();
								 connection.setAutoCommit(true);
							 }
						 }else 
						 {
							 System.out.println("Insufficient balance ");
						 }
					 }else {
						 System.out.println("Invalid pin ");
					 }
				 }
				 
			 }catch(SQLException e )
			 {
				 e.printStackTrace();
			 }
			 connection.setAutoCommit(true);
		 }
		 
		 public void transferMoney(long senderAccountNumber )throws SQLException
		 {
			 scanner.nextLine();
			 System.out.println("Enter the receiver's account number: ");
			 Long receiverAccountNumber=scanner.nextLong();
			 System.out.println("Enter amount: ");
			 double amount= scanner.nextDouble();
			 scanner.nextLine();
			 System.out.println("Enter security pin: ");
			 String securityPin=scanner.nextLine();
			 
			 try {

				 connection.setAutoCommit(false);
				 if(senderAccountNumber!=0 && receiverAccountNumber!=0)
				 {
					PreparedStatement preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
					preparedStatement.setLong(1, senderAccountNumber);
					preparedStatement.setString(2, securityPin);
					ResultSet resultSet=preparedStatement.executeQuery();
					
					if(resultSet.next()) {
						double currentBalance=resultSet.getDouble("balance");
						if(amount<=currentBalance)
						{
							String debitQuery="update accounts set balance=balance-? where account_number=?";
							String creditQuery="update accounts set balance=balance+? where account_number=?";
							
							PreparedStatement creditPreparedStatement= connection.prepareStatement(creditQuery);
							PreparedStatement debitPreparedStatement=connection.prepareStatement(debitQuery);
							
							creditPreparedStatement.setDouble(1, amount);
							creditPreparedStatement.setLong(2, receiverAccountNumber);
							
							debitPreparedStatement.setDouble(1,amount);
							debitPreparedStatement.setLong(2, senderAccountNumber);
							
							int rowsAffected1=debitPreparedStatement.executeUpdate();
							int rowsAffected2=creditPreparedStatement.executeUpdate();
							
							if(rowsAffected1>0 && rowsAffected2>0)
							{
								System.out.println("TransactionSuccessfull");
								System.out.println("Rs." + amount+" transferred successfully");
								connection.commit();
								connection.setAutoCommit(true);
								return;
							}else
							{
								System.out.println("Transaction failed");
								connection.rollback();
								connection.setAutoCommit(true);
							}
						}else {
							System.out.println("Insufficient balance.");
						}
					}else
					{
						System.out.println("Invalid security pin ");
					}
				 }else
				 {
					 System.out.println("Inavlid ccount number ");
				 }
				 

			 }catch(SQLException e)
			 {
				 e.printStackTrace();
			 }
			 connection.setAutoCommit(true);
		 }

		 
		 public void getBalance(long accountNumber)
		 {
			 scanner.nextLine();
			 System.out.println("Enter security pin: ");
			 String securityPin=scanner.nextLine();
			 try {
				 PreparedStatement preparedStatement= connection.prepareStatement("select balance from accounts where account_number=? and security_pin=? ");
				 preparedStatement.setLong(1, accountNumber);
				 preparedStatement.setString(2, securityPin);
				 ResultSet resultSet=preparedStatement.executeQuery();
				 if(resultSet.next())
				 {
					 double balance= resultSet.getDouble("balance");
					 System.out.println("Balance: "+balance);
				 }else
				 {
					 System.out.print("Invalid pin" );
				 }
			 }catch(SQLException e)
			 {
				 e.printStackTrace();
			 }
		 }
		 
}
