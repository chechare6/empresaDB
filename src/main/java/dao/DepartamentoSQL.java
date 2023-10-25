package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import model.Departamento;
import model.Empleado;

public class DepartamentoSQL {
	private Connection conn = null;

	public DepartamentoSQL() {
		conn = BD.getConnection();
		createTables();
	}

	public void close() {
		BD.close();
	}

	/**
	 * MÉTODO PARA CREAR LAS TABLAS
	 */
	private void createTables() {
		String sql = "";
		if (BD.typeDB.equals("sqlite"))
			sql = """
					CREATE TABLE IF NOT EXISTS DEPARTAMENTOS (
					ID INTEGER PRIMARY KEY AUTOINCREMENT,
					NOMBRE TEXT NOT NULL,
					JEFE INTEGER,
					FOREIGN KEY(JEFE) REFERENCES EMPLEADOS(ID)
					)
					""";
		if (BD.typeDB.equals("mariadb"))
			sql = """
					CREATE TABLE IF NOT EXISTS EMPLEADOS (
					    ID INT AUTO_INCREMENT PRIMARY KEY,
					    NOMBRE VARCHAR(255) NOT NULL,
					    JEFE INT,
					    FOREIGN KEY (JEFE) REFERENCES EMPLEADOS(ID)

					);
					""";
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * MUESTRA TABLA DEPARTAMENTOS
	 * 
	 * @return String
	 */
	public String verDepartamentos() {
		String sql = """
				SELECT * FROM DEPARTAMENTOS
				""";
		try {
			StringBuffer sb = new StringBuffer();
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				Departamento d = read(rs);
				sb.append(d.toString());
				sb.append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AÑADE DEPARTAMENTO
	 * 
	 * @param d
	 * @return boolean
	 */
	public boolean add(Departamento d) {
		String sql = """
				INSERT INTO DEPARTAMENTOS (NOMBRE, JEFE)
				VALUES (?, ?)
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, d.getNombre());
			if (d.getJefe() == null)
				ps.setNull(2, Types.INTEGER);
			else
				ps.setInt(2, d.getJefe().getId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ELIMINA EMPLADO SEGÚN SU ID
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean deleteByID(Integer id) {
		String sql = """
				DELETE FROM DEPARTAMENTOS WHERE ID = ?
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * VACÍA LA TABLA DE DEPARTAMENTOS
	 */
	public void drop() {
		String sql = """
				DELETE FROM DEPARTAMENTOS
				""";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * MÉTODO DE LECTURA DE DEPARTAMENTO
	 * 
	 * @param rs
	 * @return Departamento d
	 */
	private Departamento read(ResultSet rs) {
		try {
			Integer id = rs.getInt("ID");
			String nombre = rs.getString("NOMBRE");
			Empleado emp = searchEmp(rs.getInt("JEFE"));
			return new Departamento(id, nombre, emp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BUSCA EMPLEADO SEGÚN SU ID
	 * 
	 * @param id
	 * @return Empleado
	 */
	public Empleado searchEmp(Integer id) {
		String sql = """
				SELECT * FROM EMPLEADOS WHERE ID = ?
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				return new Empleado(id, nombre);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
