package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;

public class InterfazEditar extends JFrame implements ActionListener{

	private InterfazApp principal;

	private ListaReproduccion lista;

	private JTextField titulo;

	private JList actuales;

	private JList existentes;
	
	private ArrayList originales;

	public InterfazEditar(InterfazApp main, ListaReproduccion lista, ArrayList todos)
	{
		principal = main;
		this.lista = lista;
		originales = lista.darVideos();

		setSize(500,700);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		titulo = new JTextField(lista.darNombre());

		JPanel arriba = new JPanel();
		arriba.setLayout(new GridLayout(1,2));
		arriba.add(new JLabel("Título"));
		arriba.add(titulo);
		
		add(arriba, BorderLayout.NORTH);
		
		JPanel panelIzq = new JPanel();
        JPanel panelDer = new JPanel();
        JPanel centro = new JPanel();
        centro.setLayout(new GridLayout(1,2));
        centro.add(panelIzq);
        centro.add(panelDer);
        add(centro, BorderLayout.CENTER);
        
        panelIzq.setLayout(new BorderLayout());
        JButton quitar = new JButton("Quitar");
        quitar.setActionCommand("QUITAR");
        quitar.addActionListener(this);
        
        panelDer.setLayout(new BorderLayout());
        JButton agregar = new JButton("Agregar");
        agregar.addActionListener(this);
        agregar.setActionCommand("AGREGAR");
       
        actuales = new JList( );
        actuales.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        JScrollPane scroll1 = new JScrollPane( actuales );
        scroll1.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scroll1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        scroll1.setBorder( new TitledBorder("En la lista") );
        
        existentes = new JList( );
        existentes.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        JScrollPane scroll2 = new JScrollPane( existentes );
        scroll2.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scroll2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        scroll2.setBorder( new TitledBorder("Existentes") );

        panelIzq.add(scroll1, BorderLayout.CENTER);
        panelDer.add(scroll2, BorderLayout.CENTER);
        panelIzq.add(quitar, BorderLayout.SOUTH);
        panelDer.add(agregar, BorderLayout.SOUTH);
        
        JPanel abajo = new JPanel();
        abajo.setLayout(new GridLayout(1,2));
        JButton guardar = new JButton("Guardar");
        guardar.setActionCommand("GUARDAR");
        guardar.addActionListener(this);
        JButton cancelar = new JButton("Cancelar");
        cancelar.setActionCommand("CANCELAR");
        cancelar.addActionListener(this);
        abajo.add(guardar);
        abajo.add(cancelar);
        add(abajo, BorderLayout.SOUTH);
        
		actuales.setListData(lista.darVideos().toArray());
		existentes.setListData(todos.toArray());

        
	}

	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		if(comando.equals("GUARDAR"))
		{
			
			principal.guardarLista(lista);
			actuales.setListData(originales.toArray());
			super.dispose();
			principal.volver();
		}
		else if(comando.equals("AGREGAR"))
		{
			Video video = (Video) existentes.getSelectedValue();
			lista.agregarVideo(video);
			actuales.setListData(lista.darVideos().toArray());
		}
		else if(comando.equals("QUITAR"))
		{
			Video video = (Video) actuales.getSelectedValue();
			lista.quitarVideo(video);
			actuales.setListData(lista.darVideos().toArray());
		}
		else
		{
			actuales.setListData(originales.toArray());
			actuales.repaint();
			super.dispose();
			principal.volver();
		}
		
	}

}
