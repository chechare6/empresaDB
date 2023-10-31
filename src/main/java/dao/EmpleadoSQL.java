package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;

import IO.IO;
import lombok.Data;
import model.Departamento;
import model.Empleado;

@Data
public class EmpleadoSQL {
	private Connection conn = null;

	public EmpleadoSQL() {
		conn = BD.getConnection();
		createTables();
	}

	public void close() {
		BD.close();
	}

	private void createTables() {
		String sql = "";
		if (BD.typeDB.equals("sqlite"))
			sql = """
					CREATE TABLE IF NOT EXISTS EMPLEADOS (
					ID INTEGER PRIMARY KEY AUTOINCREMENT,
					NOMBRE TEXT NOT NULL,
					SALARIO REAL NOT NULL,
					NACIMIENTO DATE NOT NULL,
					DEPARTAMENTO INTEGER,
					FOREIGN KEY(DEPARTAMENTO) REFERENCES DEPARTAMENTOS(ID)
					)
					""";
		if (BD.typeDB.equals("mariadb"))
			sql = """
					CREATE TABLE IF NOT EXISTS EMPLEADOS (
					    ID INT AUTO_INCREMENT PRIMARY KEY,
					    NOMBRE VARCHAR(255) NOT NULL,
					    SALARIO DOUBLE NOT NULL,
					    NACIMIENTO DATE NOT NULL,
					    DEPARTAMENTO INT
					)
					""";
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (Exception e) {
			IO.println(e.getMessage());
		}
	}

	public Empleado buscaEmpleado(Integer id) {
		String sql = """
				SELECT * FROM EMPLEADOS WHERE ID = ?
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return read(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// MUESTRA TABLA EMPLEADOS
	public String verEmpleados() {
		String sql = """
				SELECT * FROM EMPLEADOS
				""";
		try {
			StringBuffer sb = new StringBuffer();
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				Empleado e = read(rs);
				sb.append(e.toString());
				sb.append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// AÑADE EMPLEADO
	public boolean add(Empleado e) {
		String sql = """
				INSERT INTO EMPLEADOS(NOMBRE, SALARIO, NACIMIENTO, DEPARTAMENTO)
				VALUES (?, ?, ?, ?)
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, e.getNombre());
			ps.setDouble(2, e.getSalario());
			ps.setObject(3, e.getFechaN());
			if (e.getDepartamento() == null)
				ps.setNull(4, Types.INTEGER);
			else
				ps.setInt(4, e.getDepartamento().getId());
			return ps.executeUpdate() > 0;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return false;
	}

	// ELIMINA EMPLEADO SEGÚN SU ID
	public boolean deleteByID(Integer id) {
		String sql = """
				DELETE FROM EMPLEADOS WHERE ID = ?
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

	// VACIA TABLA EMPLEADOS
	public void drop() {
		String sql = """
				DELETE FROM EMPLEADOS
				""";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// METODO DE LECTURA DE EMPLEADO
	private Empleado read(ResultSet rs) {
		try {
			Integer id = rs.getInt("ID");
			String nombre = rs.getString("NOMBRE");
			double salario = rs.getDouble("SALARIO");
			LocalDate fecha = rs.getObject("NACIMIENTO", LocalDate.class);
			Departamento dep = searchDep(rs.getInt("DEPARTAMENTO"));
			return new Empleado(id, nombre, salario, fecha, dep);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BUSCA DEPARTAMENTO SEGÚN ID
	 * 
	 * @param id
	 * @return Departamento
	 */
	public Departamento searchDep(Integer id) {
		String sql = """
				SELECT * FROM DEPARTAMENTOS WHERE ID = ?
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				return new Departamento(id, nombre);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
