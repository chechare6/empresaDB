package empresaDB;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
	private int id; 
	private String nombre;
	private double salario;
	private Date fechaN;
	private Departamento departamento;
	
	public Empleado(int id, String nombre, double salario, Date fechaN) {
		this.id = id;
		this.nombre = nombre;
		this.salario = salario;
		this.fechaN = fechaN;
	}
	
}
