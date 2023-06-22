package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import EmployeeDB.clsDBConnection;

public class DBConfig {

	private final String CONNECTION =  "jdbc:mysql://localhost:3306/employeedb?characterEncoding=latin1&useConfigs=maxPerformance";
	private final String PASSWORD = "root";
	private static Connection con = null;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {

		if (null == con) {
			con = (Connection) DriverManager.getConnection(this.CONNECTION, "root", this.PASSWORD);
		}
		return con;
	}
	public static void main(String[] args)
	{
		try
		{
		DBConfig c=new DBConfig();
		System.out.println(c.getConnection());
		System.out.print("Conection Successfully");
		}
		catch(Exception e)
		{
			System.out.print(e);
		}
	}
	
}


