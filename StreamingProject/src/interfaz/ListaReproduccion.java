package interfaz;

import java.util.ArrayList;

public class ListaReproduccion {
	
	private String nombre;
	
	private ArrayList videos;
	
	public ListaReproduccion(String nombre, ArrayList videos)
	{
		this.nombre = nombre;
		this.videos = videos;
	}
	
	public String darNombre()
	{
		return nombre;
	}
	
	public void cambiarNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	public void agregarVideo(Video video)
	{
		videos.add(video);
	}
	
	public ArrayList darVideos()
	{
		return videos;
	}
	
	public String toString()
	{
		return nombre;
	}

	public void quitarVideo(Video video) {
		videos.remove(video);		
	}

}
