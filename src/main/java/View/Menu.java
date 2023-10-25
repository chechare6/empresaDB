package View;

import java.time.LocalDate;
import java.util.List;

import IO.IO;
import dao.DepartamentoSQL;
import dao.EmpleadoSQL;
import model.Departamento;
import model.Empleado;

public class Menu {

	public static void main(String[] args) {
		EmpleadoSQL e = new EmpleadoSQL();
		DepartamentoSQL d = new DepartamentoSQL();

		List<String> opciones = List.of("1. Ver departamentos", "2. Añadir departamentos", "3. Eliminar departamentos",
				"4. Ver empleados", "5. Añadir empleados", "6. Eliminar empleados", "7. Salir");
		while (true) {
			System.out.println(opciones);
			switch (IO.readString().charAt(0)) {
			case '1':
				verDepartamentos(d);
				break;
			case '2':
				addDepartamentos(d);
				break;
			case '3':
				deleteDepartamentos(d);
				break;
			case '4':
				verEmpleados(e);
				break;
			case '5':
				addEmpleado(e);
				break;
			case '6':
				deleteEmpleados(e);
				break;
			case '7':
				cerrarEmp(e);
				cerrarDep(d);
				IO.println("CERRANDO BBDD");
				return;
			default:
			}
		}
	}

	/**
	 * MÉTODO PARA VER TABLA DEPARTAMENTOS
	 * 
	 * @param d
	 */
	public static void verDepartamentos(DepartamentoSQL d) {
		System.out.println(d.verDepartamentos());
	}

	/**
	 * MÉTODO PARA VER TABLA EMPLEADOS
	 * 
	 * @param e
	 */
	public static void verEmpleados(EmpleadoSQL e) {
		System.out.println(e.verEmpleados());
	}

	/**
	 * MÉTODO PARA AÑADIR DEPARTAMENTOS
	 * @param d
	 */
	public static void addDepartamentos(DepartamentoSQL d) {
		boolean add = false;
		IO.print("Nombre del nuevo departamento: ");
		String nombre = IO.readString();
		IO.print("¿Tiene jefe de departamento? (S/N): ");
		switch (IO.readString().toUpperCase().charAt(0)) {
		case 'S':
			IO.print("ID del Jefe: ");
			int idEmp = IO.readInt();
			// Resolver
			add = d.add(new Departamento(nombre, d.searchEmp(idEmp)));
			IO.println(add ? "Añadido" : "No se ha podido añadir");
			break;
		case 'N':
			add = d.add(new Departamento(nombre));
			IO.println(add ? "Añadido" : "No se ha podido añadir");
			break;
		default:
			break;
		}
	}

	/**
	 * MÉTODO PARA AÑADIR EMPLEADOS
	 * 
	 * @param e
	 */
	public static void addEmpleado(EmpleadoSQL e) {
		boolean add = false;
		IO.print("Nombre del nuevo empleado: ");
		String nombre = IO.readString();
		IO.print("Salario del empleado: ");
		double salario = IO.readDouble();
		IO.print("Fecha de nacimiento (YYYY-MM-dd): ");
		LocalDate fecha = IO.readLocalDate();
		IO.print("¿Tiene departamento? (S/N): ");
		switch (IO.readString().toUpperCase().charAt(0)) {
		case 'S':
			IO.print("ID del departamento: ");
			int idDep = IO.readInt();
			// Resolver
			add = e.add(new Empleado(nombre, salario, fecha, e.searchDep(idDep)));
			IO.println(add ? "Añadido" : "No se ha podido añadir");
			break;
		case 'N':
			add = e.add(new Empleado(nombre, salario, fecha));
			IO.println(add ? "Añadido" : "No se ha podido añadir");
			break;
		default:
			break;
		}
	}

	/**
	 * MÉTODO QUE BORRA DEPARTAMENTOS SEGÚN SU ID
	 * @param d
	 */
	public static void deleteDepartamentos(DepartamentoSQL d) {
		boolean deleted = false;
		IO.print("¿Qué departamento desea eliminar? (Introduce su ID): ");
		deleted = d.deleteByID(IO.readInt());
		IO.println(deleted ? "Eliminado con éxito" : "No se ha podido eliminar");
	}
	
	/**
	 * MÉTODO QUE BORRA EMPLEADOS SEGÚN SU ID
	 * @param e
	 */
	public static void deleteEmpleados(EmpleadoSQL e) {
		boolean deleted = false;
		IO.print("¿Qué empleado desea eliminar? (Introduce su ID): ");
		deleted = e.deleteByID(IO.readInt());
		IO.println(deleted ? "Eliminado con éxito" : "No se ha podido eliminar");
	}
	
	/**
	 * MÉTODO PARA CERRAR LA TABLA EMPLEADOS
	 * @param e
	 */
	public static void cerrarEmp(EmpleadoSQL e) {
		e.close();
	}

	/**
	 * MÉTODO PARA CERRAR LA TABLA DEPARTAMENTOS
	 * @param d
	 */
	public static void cerrarDep(DepartamentoSQL d) {
		d.close();
	}

}
