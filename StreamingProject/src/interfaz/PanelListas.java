package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PanelListas extends JPanel implements ListSelectionListener, ActionListener{
	
	private JList listaVideos;
	
	private JList listaRep;
	
	private InterfazApp interfaz;
	
	public PanelListas(InterfazApp main, ArrayList listas)
	{
		interfaz = main;

        setLayout( new GridLayout(2,1) );
        
        JPanel panelArriba = new JPanel();
        JPanel panelAbajo = new JPanel();
        
        panelArriba.setLayout(new BorderLayout());
        JButton editar = new JButton("Editar");
        editar.setActionCommand("EDITAR");
        editar.addActionListener(this);
        
        panelAbajo.setLayout(new BorderLayout());
        JButton ver = new JButton("Ver");
        ver.addActionListener(this);
        ver.setActionCommand("VER");
        
        listaRep = new JList( );
        listaRep.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listaRep.addListSelectionListener( this );

        JScrollPane scroll1 = new JScrollPane( listaRep );
        scroll1.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scroll1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        scroll1.setBorder( new TitledBorder("Listas de reproducción") );
        
        listaVideos = new JList( );
        listaVideos.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listaVideos.addListSelectionListener( this );

        JScrollPane scroll2 = new JScrollPane( listaVideos );
        scroll2.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scroll2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        scroll2.setBorder( new TitledBorder("Videos") );

        panelArriba.add(scroll1, BorderLayout.CENTER);
        panelAbajo.add(scroll2, BorderLayout.CENTER);
        panelArriba.add(editar, BorderLayout.SOUTH);
        panelAbajo.add(ver, BorderLayout.SOUTH);
        
        add( panelArriba);
        add( panelAbajo);
        
        listaRep.setListData(listas.toArray());
        
        listaVideos.setSelectedIndex(0);
        listaRep.setSelectedIndex(0);
	}
	
	public void valueChanged(ListSelectionEvent e) {
		JList llamado = (JList) e.getSource();
		
		if( llamado == listaRep)
        {
            ListaReproduccion lista = (ListaReproduccion) listaRep.getSelectedValue();
            interfaz.cambiarListaActual(lista);
            listaVideos.setListData(lista.darVideos().toArray());
            listaVideos.setSelectedIndex(0);
        }
		else
		{
			Video video = (Video) listaVideos.getSelectedValue();
			interfaz.cambiarVideoActual(video);
		}
		
	}
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if(comando.equals("EDITAR"))
		{
			interfaz.editarLista();
		}
		else
		{
			
		}
		
	}
}
