package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCUltil {

	private static final String URL = "jdbc:sqlserver://localhost:1433;encrypt=false;databaseName=ProjectWeb";
	private static final String USER = "sa";
	private static final String PASSWORD = "ps04092003";

	public static Connection getConnection(){
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void closeConnection(Connection c) {
		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException {
		Connection c = JDBCUltil.getConnection();
		System.out.println(c);
	}
}
