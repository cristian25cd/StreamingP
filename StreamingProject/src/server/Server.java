package server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.List;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;

public class Server extends JFrame 
{
	private static ServerSocket listenSocket;
	private ArrayList<ClienteRemoto> clientes;

	//--------------------------------
	//Constructor
	//--------------------------------
	public Server() throws Exception{

		listenSocket=new ServerSocket(1051);
		clientes= new ArrayList<>();
	}

	//------------------------------------
	//main
	//------------------------------------
	public static void main(String argv[]) throws Exception
	{
		//create a Server object
		Server theServer = new Server();

		while(true){
			Socket client = listenSocket.accept();
			theServer.clientes.add(new ClienteRemoto(client));
		}
	}
}