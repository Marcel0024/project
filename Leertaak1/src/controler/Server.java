package controler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import model.XMLReader;
import model.DBConnection;


public class Server {
	private ServerSocket serverSocket;
	private final static  Semaphore CONNECTIONS = new Semaphore(10, true);
	Socket socket;
	
	public Server(){
		try {
			// Make new sever socket.
			serverSocket = new ServerSocket(7789);
			
			// Run forever
			while (true){
				try {
					CONNECTIONS.acquire();
					socket = serverSocket.accept();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							new XMLReader(socket);
							
						}
					}).start();
					CONNECTIONS.release();
				} catch (IOException | InterruptedException e){
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
