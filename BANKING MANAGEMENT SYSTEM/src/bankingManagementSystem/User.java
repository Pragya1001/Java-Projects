package bankingManagementSystem;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import javax.xml.transform.Result;

public class User {
	
	private Connection connection;
	private Scanner scanner;
	
	public User(Connection connection,Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner;
		
	}
	
	public void register()
	{
		scanner.nextLine();
		System.out.print("Full Name :");
		String fullName=scanner.nextLine();
		System.out.print("Email: ");
		String email=scanner.nextLine();
		System.out.print("Password: ");
		String password=scanner.nextLine();
		
		if(userExist(email))
		{
			System.out.println("User already exists for this email address. ");
			return;
		}
		
		String registerSql="insert into user (full_name,email,password) values(?,?,?)";
		
		try
		{
			PreparedStatement preparedStatement=connection.prepareStatement(registerSql);
			preparedStatement.setString(1,fullName);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			
			int rowsAffected=preparedStatement.executeUpdate();
			if(rowsAffected>0)
			{
				System.out.println("Registration Successfull! ");
			}else
			{
				System.out.println("Registration Unsuccessfull");
			}
		}catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public String login()
	{
		scanner.nextLine();
		System.out.print("Email: " );
		String email=scanner.nextLine();
		System.out.print("Password: " );
		String password= scanner.nextLine();
		
		String loginSql= "select * from user where email=? and password =?";
		
		try
		{
			PreparedStatement preparedStatement=connection.prepareStatement(loginSql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next())
			{
				return email;
			}else
			{
				return null;
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean userExist(String email)
	{
		String existSql="select * from user where email=?";
		try
		{
			PreparedStatement preparedStatement=connection.prepareStatement(existSql);
			preparedStatement.setString(1, email);
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next())
			{
				return true;
			}
			else
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
