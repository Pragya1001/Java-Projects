package bankingManagementSystem;

import java.util.Scanner;
import java.sql.*;


public class Accounts {
	
	private Connection connection;
	private Scanner scanner;
	public Accounts(Connection connection,Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner;
		
	}
	
	public long openAccount(String email)
	{
		if(!accountExist(email))
		{
			String openAccountSql="insert into accounts (account_number,full_name,email,balance,security_pin) values (?,?,?,?,?)";
			scanner.nextLine();
			System.out.print("Enter full name: ");
			String fullName=scanner.nextLine();
			System.out.println("Enter Initial Amount: ");
			double balance=scanner.nextDouble();
			scanner.nextLine();
			System.out.print("Enter security pin: ");
			String securityPin=scanner.nextLine();
			
			try {
				long accountNumber=generateAccountNumber();
				PreparedStatement preparedStatement=connection.prepareStatement(openAccountSql);
				preparedStatement.setLong(1,accountNumber);
				preparedStatement.setString(2, fullName);
				preparedStatement.setString(3, email);
				preparedStatement.setDouble(4, balance);
				preparedStatement.setString(5, securityPin);
				
				int rowsAffected=preparedStatement.executeUpdate();
				if(rowsAffected>0)
				{
					return accountNumber;
				}else
				{
					throw new RuntimeException("Account creation failed ");
				}
			}catch(SQLException e)
			{
				e.printStackTrace();
			}
			
		}
		throw new RuntimeException("Account already exists ");
	}
	public long getAccountNumber(String email)
	{
		String query="Select account_number from accounts where email=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			ResultSet resultSet= preparedStatement.executeQuery();
			if(resultSet.next())
			{
				return resultSet.getLong("account_number");
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		throw new RuntimeException("Account number doesn't exist. ");
	}
	
	public long generateAccountNumber()
	{
		try {
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery("select account_number from accounts order by account_number desc limit 1");
			if(resultSet.next())
			{
				long lastAccountNumber=resultSet.getLong("account_number");
				return lastAccountNumber+1;
			}
			else
			{
				return 1000100;
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 1000100;
	}
	
	
	public boolean accountExist(String email)
	{
		String sql="select account_number from accounts where email=?";
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next())
			{
				return true;
			}else
			{
				return false;
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
