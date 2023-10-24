package empresaDB;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
	private Integer id; 
	private String nombre;
	private double salario;
	private LocalDate fechaN;
	private Departamento departamento;
	
	public Empleado(String nombre, double salario, LocalDate fechaN, Departamento departamento) {
		this.nombre = nombre;
		this.salario = salario;
		this.fechaN = fechaN;
		this.departamento = departamento;
	}
}
