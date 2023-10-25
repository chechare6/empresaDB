package View;

import java.time.LocalDate;
import java.util.List;

import IO.IO;
import dao.DepartamentoSQL;
import dao.EmpleadoSQL;
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
				// verDepartamentos(dep);
				break;
			case '2':
				// addDepartamentos(dep)
				break;
			case '3':
				// eliminarDepartamentos(dep)
				break;
			case '4':
				verEmpleados(e);
				break;
			case '5':
				addEmpleado(e);
				break;
			case '6':
				// eliminarEmpleados(emp)
				break;
			case '7':
				// cerrarBD(emp)
				// cerrarBD(dep)
				return;
			default:
			}
		}
	}
	
	public static void verEmpleados(EmpleadoSQL e) {
		System.out.println(e.verEmpleados());
	}
	
	public static void addEmpleado(EmpleadoSQL e) {
		boolean add = false;
		IO.print("Nombre del nuevo empleado: ");
		String nombre = IO.readString();
		IO.print("Salario del empleado: ");
		double salario = IO.readDouble();
		IO.print("Fecha de nacimiento (YYYY-MM-dd): ");
		LocalDate fecha = IO.readLocalDate();
		IO.print("Tiene departamento (S/N)");
		switch (IO.readString().toUpperCase().charAt(0)) {
		case 'S':
			IO.print("ID del departamento: ");
			int idDep = IO.readInt();
			//Resolver
			add = e.addCDep(new Empleado(nombre, salario, fecha, e.searchDep(idDep)));
			IO.println(add ? "Añadido" : "No se ha podido añadir");
			break;
		case 'N':
			add = e.addSDep(new Empleado(nombre, salario, fecha));
			IO.println(add ? "Añadido" : "No se ha podido añadir");
			break;
		default:
			break;
		}
	}
}
