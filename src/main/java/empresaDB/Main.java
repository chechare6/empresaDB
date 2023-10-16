package empresaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	static Connection conn;

	public static void main(String[] args) {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:empresaDB.db");
			Statement s = conn.createStatement();
			s.execute("CREATE TABLE IF NOT EXISTS EMPLEADOS (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR NOT NULL, SALARIO INT NOT NULL, NACIMIENTO DATE NOT NULL, FOREIGN KEY (NOMBRE) REFERENCES DEPARTAMENTOS(NOMBRE));");
//			s.close();
			s.execute("CREATE TABLE IF NOT EXISTS DEPARTAMENTOS (ID INTEGER PRIMARY KEY AUTOINCREMENT , NOMBRE VARCHAR NOT NULL, FOREIGN KEY (ID) REFERENCES EMPLEADOS(ID));");
			boolean bdAbierta = true;
			Scanner sc = new Scanner(System.in);
			System.out.println("Conexión realizada");
			while (bdAbierta) {
				System.out.println("-------------------------");
				System.out.println("BBDD DE EMPRESA\n");
				System.out.print("DEPARTAMENTOS:\n 1. Ver | 2. Añadir | 3. Eliminar\n"
						+ "EMPLEADOS:\n 4. Ver | 5. Añadir | 6. Eliminar \n"
						+ "SALIR:\n 7. Salir\nEliga una opción: ");
				String opcion = sc.nextLine();
				switch (opcion) {
				case "1":
					System.out.println("Has elegido ver 'Departamentos'");
					break;
				case "2":
					addDepartamento();
					break;
				case "3":
					System.out.println("Has elegido eliminar 'Departamentos'");
					break;
				case "4":
					System.out.println("Has elegido ver 'Empleados'");
					break;
				case "5":
					System.out.println("Has elegido añadir 'Empleados'");
					break;
				case "6":
					System.out.println("Has elegido eliminar 'Empleados'");
					break;
				case "7":
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
			sc.close();
			System.out.println("-------------------------");
			System.out.println("Conexión cerrada");
		} catch (Exception e) {
			System.out.println("Conexión fallida: " + e.getMessage());
		}
	}

	public static void addDepartamento() {
		System.out.println("-------------------------");
		System.out.println("Has elegido crear un departamento.");
		Scanner sc = new Scanner(System.in);
		System.out.print("Nombre del nuevo departamento: ");
		String nombre = sc.nextLine();
		System.out.print("¿Tiene jefe? (Y/N)");
		String hayJefe = sc.next();
		if (hayJefe == "Y") {
			System.out.print("Nombre del jefe: ");
			String nombreJefe = sc.nextLine();
		} else if (hayJefe == "N") {
			try {
				Departamento d = new Departamento(nombre);
				Statement s = conn.createStatement();
				s.execute("INSERT INTO DEPARTAMENTO()");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Introduzca una opción correcta.");
		}
		sc.close();
	}

}
