package interfaz;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;

public class InterfazApp extends JFrame{
	
	private PanelListas panelListas;
	
	private ListaReproduccion listaActual;
	
	private ArrayList todos;
	
	private Video videoActual;
	
	public InterfazApp()
	{
		
		setSize(500,700);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Video videoPrueba = new Video("Video de prueba");
		ArrayList vid = new ArrayList();
		vid.add(videoPrueba);
		listaActual = new ListaReproduccion("Lista de prueba", vid);
		ArrayList listas = new ArrayList();
		listas.add(listaActual);
		todos = new ArrayList();
		todos.add(videoPrueba);
		
		
		panelListas = new PanelListas(this, listas);
		ClientPanel panelVideo = new ClientPanel();
		add(panelListas, BorderLayout.WEST);
		add(panelVideo, BorderLayout.CENTER);
	}
	
	public void cambiarListaActual(ListaReproduccion lista)
	{
		listaActual = lista;
	}
	
	public void editarLista()
	{
		ArrayList nuevo = new ArrayList();
		for(int i = 0; i < listaActual.darVideos().size(); i++)
		{
			nuevo.add(listaActual.darVideos().get(i));
		}
		ListaReproduccion copia = new ListaReproduccion(listaActual.darNombre(), nuevo);
		
		InterfazEditar inter = new InterfazEditar(this, copia, todos);
		inter.setVisible(true);
		setVisible(false);
	}
	
	public void guardarLista(ListaReproduccion lista)
	{
		listaActual = lista;
	}

	public void volver() {
		setVisible(true);
	}

	public void cambiarVideoActual(Video video) {
		videoActual = video;
	}
	
}
