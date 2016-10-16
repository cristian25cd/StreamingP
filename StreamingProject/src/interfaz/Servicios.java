package interfaz;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Servicios {
	
	private boolean loggeado;
	
	private Socket socket;
	
	private BufferedReader in;
	
	private DataOutputStream out;
	
	public boolean ingresar(String ip, String puerto, String login, String password) throws Exception
	{
		socket = new Socket(ip, Integer.parseInt(puerto));
		out = new DataOutputStream(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		out.writeBytes("INGRESAR::" + login + "::" + password);
		String respuesta = in.readLine();
		
		if(respuesta.startsWith("OK"))
		{
			loggeado = true;
		}
		else
		{
			loggeado = false;
		}
		
		return loggeado;
	} 
	
	public void registrar(String ip, String puerto, String login, String password) throws Exception
	{
		socket = new Socket(ip, Integer.parseInt(puerto));
		out = new DataOutputStream(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out.writeBytes("REGISTRAR::" + login + "::" + password+"\n");
		String respuesta = in.readLine();
		
		if(respuesta.startsWith("OK"))
		{
			loggeado = true;
		}
		else
		{
			loggeado = false;
		}
	}
	
	public void close()
	{
		try
		{
			in.close();
			out.close();
			socket.close();
		}
		catch(Exception e)
		{
			
		}
		
	}

}
