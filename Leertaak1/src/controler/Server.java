package controler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.XMLReader;

public class Server {
	private ServerSocket serverSocket;
	private Socket socket;
	
	public Server() {
		try {
			// Make new sever socket.
			serverSocket = new ServerSocket(7789);
			// Run forever
			while (true){
				try {
					socket = serverSocket.accept();
					new Thread(new Runnable() {
			
						@Override
						public void run() {
							new XMLReader(socket);
						}
					}).start();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
