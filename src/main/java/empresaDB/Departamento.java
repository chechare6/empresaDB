package empresaDB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {
	private int id;
	private String nombre;
	private Empleado jefe;
	
	public Departamento(String nombre, Empleado jefe) {
		this.nombre = nombre;
		if(jefe instanceof Empleado)
			this.jefe = jefe;
		else
			this.jefe = null;
	}
	
	public Departamento(String nombre) {
		this.nombre = nombre;
	}
}
