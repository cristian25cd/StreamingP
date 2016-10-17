/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: InterfazJugador.java 602 2006-11-06 17:16:51Z jvillalo2 $
 * Universidad de los Andes (Bogot� - Colombia)
 * Departamento de Ingenier�a de Sistemas y Computaci�n 
 * Licenciado bajo el esquema Academic Free License version 2.1 
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n12_batallaNaval
 * Autor: Mario S�nchez - 21-feb-2006
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package interfaz;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Esta es la ventana principal de la aplicaci�n.
 */
public class InterfazInicio extends JFrame
{
	private static final long serialVersionUID = 1L;

	private PanelLogin panelLogin;

	private Servicios cliente;

	public InterfazInicio()
	{
		cliente = new Servicios();

		setLayout(new BorderLayout());
		panelLogin = new PanelLogin(this);
		setSize(500, 280);
		add(new JLabel("Bienvenido al servicio de streaming"), BorderLayout.CENTER);
		add(panelLogin, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {

//		JFileChooser fileChooser = new JFileChooser();
//
//		// show open file dialog
//		int result = fileChooser.showOpenDialog( null );
//
//		if ( result == JFileChooser.APPROVE_OPTION ) // user chose a file
//		{
//			URL mediaURL = null;
//
//			try
//			{
//				// get the file as URL
//				mediaURL = fileChooser.getSelectedFile().toURL();
//			} // end try
//			catch ( MalformedURLException malformedURLException )
//			{
//				System.err.println( "Could not create URL for the file" );
//			} // end catch
//
//			if ( mediaURL != null ) // only display if there is a valid URL
//			{
//				JFrame mediaTest = new JFrame( "Media Tester" );
//
//				MediaPanel mediaPanel = new MediaPanel( mediaURL );
//				mediaTest.add( mediaPanel );
//
//				mediaTest.setSize( 300, 300 );
//				mediaTest.setVisible( true );
//			} // end inner if
//		} // end outer if

		InterfazInicio interfaz = new InterfazInicio();
		interfaz.setVisible(true);

	}

	public void registrarse(String ip, String puerto, String login, String password) {
		try
		{
			cliente.registrar(ip, puerto, login, password);
			
			setVisible(false);
			InterfazApp principal = new InterfazApp();
			principal.setVisible(true);
			dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}