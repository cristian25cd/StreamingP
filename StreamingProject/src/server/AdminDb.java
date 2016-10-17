package server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.persistence.*;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import model.*;

public class AdminDb {
	// ----------------------------------------------------
	// Atributos
	// ----------------------------------------------------
	/**
	 * conexion con la base de datos
	 */
	public static Connection conexion;

	/**
	 * nombre del usuario para conectarse a la base de datos.
	 */
	private static String usuario;

	/**
	 * clave de conexiÛn a la base de datos.
	 */
	private static String clave;

	/**
	 * URL al cual se debe conectar para acceder a la base de datos.
	 */
	private static String cadenaConexion;

	private MysqlDataSource dataSource = new MysqlDataSource();
	
	public AdminDb() {
		cadenaConexion="jdbc:mysql://localhost:3306/videostreamingdb";
		usuario="root";
		clave="1q2w3e4r5t";
		dataSource.setUser(usuario);
		dataSource.setPassword(clave);
		dataSource.setServerName(cadenaConexion);
		
	}

	private void establecerConexion() throws SQLException {
		conexion= dataSource.getConnection();

	}
	public void closeConnection(Connection connection) throws Exception {
		try {
			connection.close();
			connection = null;
		} catch (SQLException exception) {
			throw new Exception(
					"ERROR: ConsultaDAO: closeConnection() = cerrando una conexiÛn.");
		}
	}
	
	public boolean login(String usr, String pass) throws Exception{
		PreparedStatement prepStmt = null;
		String sql = "SELECT * FROM usuario u WHERE u.login = "+usr;
		System.out.println(sql);
		establecerConexion();
		prepStmt = conexion.prepareStatement(sql);
		ResultSet rs = prepStmt.executeQuery();
		
		if(rs.next()){
			String password= rs.getString("password");
			if(password.equals(pass)){
				return true;
			}
		}
		closeConnection(conexion);
		return false;
	}
	
	public boolean registrarse(String usr, String pass) throws Exception{
		PreparedStatement prepStmt = null;
		String sql = "INSERT INTO usuario VALUES('"+usr+"','"+pass+"')";
		System.out.println(sql);
		establecerConexion();
		prepStmt = conexion.prepareStatement(sql);
		ResultSet rs = prepStmt.executeQuery();
		
		if(rs.next()){
			String password= rs.getString("password");
			if(password.equals(pass)){
				return true;
			}
		}
		closeConnection(conexion);
		return false;
	}
	
	public ArrayList<Video> getAllVideos() throws Exception{
		ArrayList<Video> retorno = new ArrayList<Video>();
		PreparedStatement prepStmt = null;
		String sql = "SELECT * FROM video";
		System.out.println(sql);
		establecerConexion();
		prepStmt = conexion.prepareStatement(sql);
		ResultSet rs = prepStmt.executeQuery();
		
		while(rs.next()){
			long id = rs.getLong("id");
			Date add_date = rs.getDate("add_date");
			int duration = rs.getInt("duration");
			String format = rs.getString("format");
			String name = rs.getString("name");
			String url = rs.getString("url");
			String user_login = rs.getString("user_login");
			Video v = new Video(id, add_date,duration, format, name, url, user_login);
			retorno.add(v);
		}
		closeConnection(conexion);
		return retorno;
	}
}
