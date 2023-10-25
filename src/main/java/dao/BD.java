package dao;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class BD {

	private static Connection conn;
	public static String typeDB;

	private BD() {
		try {
			Properties prop = new Properties();
			prop.load(new FileReader("properties.database.prop"));
			typeDB = prop.getProperty("db");
			String driver = prop.getProperty("driver");
			String dsn = prop.getProperty("dsn");
			String user = prop.getProperty("user", "");
			String pass = prop.getProperty("pass", "");
			Class.forName(driver);
			conn = DriverManager.getConnection(dsn, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		if (conn == null)
			new BD();
		return conn;
	}
	
	public static void close() {
		if(conn != null)
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
