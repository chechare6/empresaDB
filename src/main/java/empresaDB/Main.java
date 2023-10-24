package empresaDB;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;

import IO.IO;

public class Main {

	static Connection conn;
	static boolean bdAbierta;
//	static int opcion;

	public static void main(String[] args) {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:empresaDB.db");
			Statement s = conn.createStatement();
			s.execute(
					"CREATE TABLE IF NOT EXISTS EMPLEADOS (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR NOT NULL, SALARIO INT NOT NULL, NACIMIENTO DATE NOT NULL, DEPARTAMENTO INTEGER, FOREIGN KEY(DEPARTAMENTO) REFERENCES DEPARTAMENTOS(ID));");
			s.execute(
					"CREATE TABLE IF NOT EXISTS DEPARTAMENTOS (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR NOT NULL,IDJEFE INTEGER, FOREIGN KEY(IDJEFE) REFERENCES EMPLEADOS(ID));");
			bdAbierta = true;
			System.out.println("Conexión realizada");
			while (bdAbierta) {
				System.out.println("-------------------------");
				System.out.println("BBDD DE EMPRESA\n");
				System.out.print(
						"DEPARTAMENTOS:\n 1. Ver | 2. Añadir | 3. Eliminar\nEMPLEADOS:\n 4. Ver | 5. Añadir | 6. Eliminar \nSALIR:\n 7. Salir\nEliga una opción: ");
				int opcion = IO.readInt();// sc.nextLine();
				switch (opcion) {
				case 1:
					verDepartamento();
					break;
				case 2:
					addDepartamento();
					break;
				case 3:
					eliminarDepartamento();
					break;
				case 4:
					verEmpleado();
					break;
				case 5:
					addEmpleado();
					break;
				case 6:
					eliminarEmpleado();
					break;
				case 7:
					bdAbierta = false;
					System.out.println("-------------------------");
					System.out.println("Saliendo de la BBDD");
					break;
				default:
					System.out.println("Introduzca una opción correcta.");
					break;
				}
			}
			// FIN DE LA BBDD
			conn.close();
			System.out.println("-------------------------");
			System.out.println("Conexión cerrada");
		} catch (Exception e) {
			System.out.println("Conexión fallida: " + e.getMessage());
		}
	}

	public static void verDepartamento() {
		System.out.println("-------------------------");
		System.out.println("Has elegido ver departamentos.");
		try {
			String sql = "SELECT * FROM DEPARTAMENTOS";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				Departamento d = new Departamento(rs.getInt("ID"), rs.getString("NOMBRE"),
						buscaEmpleado(rs.getInt("IDJEFE")));
				if (d.getJefe() != null)
					System.out.println("	ID: " + d.getId() + " | Nombre Departamento: " + d.getNombre()
							+ " | Jefe Departamento: " + d.getJefe().getNombre() + ", ID: " + d.getJefe().getId());
				else
					System.out.println("	ID: " + d.getId() + " | Nombre Departamento: " + d.getNombre()
							+ " | Jefe Departamento: " + d.getJefe());
			}
			s.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void addDepartamento() {
		System.out.println("-------------------------");
		System.out.println("Has elegido crear un departamento.");
		System.out.print("Nombre del nuevo departamento: ");
		String nombre = IO.readString();
		System.out.print("¿Tiene jefe? (S/N): ");
		char hayJefe = Character.toUpperCase(IO.readChar());
		switch (hayJefe) {
		case 'S':
			System.out.print("Id del jefe: ");
			int idJefe = IO.readInt();
			Departamento dConJefe = new Departamento(nombre, buscaEmpleado(idJefe));
			try {
				String sql = "INSERT INTO DEPARTAMENTOS(NOMBRE, IDJEFE) VALUES (?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, dConJefe.getNombre());
				ps.setInt(2, idJefe);
				int add = ps.executeUpdate();
				if (add > 0) {
					System.out.println("Departamento añadido con éxito");
				} else {
					System.out.println("No se pudo añadir el departamento");
				}
				ps.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;
		case 'N':
			Departamento dSinJefe = new Departamento(nombre);
			try {
				String sql = "INSERT INTO DEPARTAMENTOS(NOMBRE) VALUES (?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, dSinJefe.getNombre());
				int add = ps.executeUpdate();
				if (add > 0)
					System.out.println("Departamento añadido con éxito.");
				else
					System.out.println("No se pudo añadir el departamento");
				ps.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;
		default:
			System.out.println("Introduzca una opción correcta.");
			break;
		}
	}

	public static void eliminarDepartamento() {
		System.out.print("¿Qué departamento quieres eliminar?: ");
		Integer depElim = IO.readInt();
		try {
			String sql = "DELETE FROM DEPARTAMENTOS WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, depElim);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void verEmpleado() {
		System.out.println("-------------------------");
		System.out.println("Has elegido ver empleados.");
		try {
			String sql = "SELECT * FROM EMPLEADOS";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				Empleado e = new Empleado(rs.getInt("ID"), rs.getString("NOMBRE"), rs.getDouble("SALARIO"), rs.getObject("NACIMIENTO", LocalDate.class), buscaDep(rs.getInt("DEPARTAMENTO")));
				if(e.getDepartamento() != null)
				System.out.println("	ID: " + e.getId() + " | Nombre Empleado: " + e.getNombre() + " | Salario: " + e.getSalario()
						+ "€ | Fecha de nacimiento: " + e.getFechaN() + " | Departamento: " + e.getDepartamento().getNombre());
				else
					System.out.println("	ID: " + e.getId() + " | Nombre Empleado: " + e.getNombre() + " | Salario: " + e.getSalario()
						+ "€ | Fecha de nacimiento: " + e.getFechaN() + " | Departamento: " + e.getDepartamento());
			}
			s.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addEmpleado() {
		System.out.println("-------------------------");
		System.out.println("Has elegido añadir un empleado.");
		System.out.print("Nombre del nuevo empleado: ");
		String nombre = IO.readString();
		System.out.print("Salario del empleado: ");
		double salario = IO.readDouble();
		System.out.print("Fecha de nacimiento (YYYY-MM-dd): ");
		LocalDate fechaN = IO.readLocalDate();
		System.out.print("¿Tiene departamento? (S/N): ");
		char departamento = Character.toUpperCase(IO.readChar());
		switch (departamento) {
		case 'S':
			System.out.print("Id del departamento: ");
			int idDepart = IO.readInt();
			Empleado empl = new Empleado(nombre, salario, fechaN, buscaDep(idDepart));
			System.out.println(buscaDep(idDepart));
			try {
				String sql = "INSERT INTO EMPLEADOS(NOMBRE, SALARIO, NACIMIENTO, DEPARTAMENTO) VALUES (?, ?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, empl.getNombre());
				ps.setDouble(2, empl.getSalario());
				ps.setObject(3, empl.getFechaN());
				ps.setInt(4, buscaDep(idDepart).getId());
				int add = ps.executeUpdate();
				if (add > 0) {
					System.out.println("Empleado añadido con éxito");
				} else {
					System.out.println("No se pudo añadir el empleado");
				}
				ps.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;
		case 'N':
			try {
				String sql = "INSERT INTO EMPLEADOS(NOMBRE, SALARIO, NACIMIENTO) VALUES (?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, nombre);
				ps.setDouble(2, salario);
				ps.setObject(3, fechaN);
				int add = ps.executeUpdate();
				if (add > 0) {
					System.out.println("Empleado añadido con éxito.");
				} else {
					System.out.println("No se pudo añadir al empleado");
				}
				ps.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;
		default:
			System.out.println("Introduzca una opción correcta.");
			break;
		}
	}

	public static void eliminarEmpleado() {
		System.out.println("¿Qué empleado quieres eliminar?: ");
		Integer empElim = IO.readInt();
		try {
			String sql = "DELETE FROM EMPLEADOS WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, empElim);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static Empleado buscaEmpleado(Integer id) {
		try {
			String sql = "SELECT * FROM EMPLEADOS WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Integer idEmpleado = rs.getInt("ID");
				String nombre = rs.getString("NOMBRE");
				double salario = rs.getDouble("SALARIO");
				LocalDate fechaN = rs.getObject("NACIMIENTO", LocalDate.class);
				Departamento departamento = null; // CAMBIAR
				Empleado e = new Empleado(idEmpleado, nombre, salario, fechaN, departamento);
				return e;
			}
			ps.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	private static Departamento buscaDep(Integer id) {
		try {
			String sql = "SELECT * FROM DEPARTAMENTOS WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String nombre = rs.getString("NOMBRE");
				Departamento d = new Departamento(id, nombre);
				return d;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
