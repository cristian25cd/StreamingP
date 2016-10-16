package interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelLogin extends JPanel implements ActionListener{
	
private InterfazInicio interfaz;
	
	private JTextField login;
	
	private JTextField password;
	
	private JButton registrarse;
	
	private JButton ingresar;
	
	private JTextField ip;
	
	private JTextField puerto;
	
	public PanelLogin(InterfazInicio main)
	{
		interfaz = main;
		setLayout(new GridLayout(7, 2));
		
		ip = new JTextField("157.253.226.12");
		puerto = new JTextField("9999");
		
		add(new JLabel("IP: "));
		add(ip);
		add(new JLabel("PUERTO: "));
		add(puerto);
		
		add(new JLabel());
		add(new JLabel());
		
		login = new JTextField();
		password = new JTextField();
		ingresar = new JButton("Ingresar");
		ingresar.setActionCommand("INGRESAR");
		ingresar.addActionListener(this);
		registrarse = new JButton("Registrarse");
		registrarse.setActionCommand("REGISTRARSE");
		registrarse.addActionListener(this);
		
		add(new JLabel("Login"));
		add(login);
		add(new JLabel("Contraseña"));
		add(password);	
		add(new JLabel());
		add(new JLabel());
		add(ingresar);
		add(registrarse);
	}

	
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		String laIp = ip.getText();
		String elPuerto = puerto.getText();
		String elLogin = login.getText();
		String elPassword = password.getText();
		if(comando.equals("REGISTRARSE"))
		{
			interfaz.registrarse(laIp, elPuerto, elLogin, elPassword);
		}
	}

}
