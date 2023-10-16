package empresaDB;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
	private String id, nombre;
	private int salario;
	private LocalDate fechaN;
	private Departamento departamento;
	
	public Empleado(String id, String nombre, int salario, LocalDate fechaN) {
		this.id = id;
		this.nombre = nombre;
		this.salario = salario;
		this.fechaN = fechaN;
	}
	
}
