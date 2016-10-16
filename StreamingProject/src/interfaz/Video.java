package interfaz;

public class Video {
	
	private String nombre;
	
	public Video(String nombre)
	{
		this.nombre = nombre;
	}
	
	public String darNombre()
	{
		return nombre;
	}
	
	public void cambiarNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	public String toString()
	{
		return nombre;
	}

}
