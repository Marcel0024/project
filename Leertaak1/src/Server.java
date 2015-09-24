import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String args[]){
		try {
	
			ServerSocket sock = new ServerSocket(6052);
			Socket a = sock.accept();
			Thread temp = new Thread(new Weather(a));
			
		} 
		
		catch (IOException e) {System.out.println("Port is unavailable.");}
		
		}
}
	
	class Weather implements Runnable{

		private Socket socket;
		
		public Weather(Socket socket){
			this.socket=socket;
		}
		
		@Override
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
				
				String line;
				while((line = in.readLine()) != null){
					
				//data in sql here
					
				}
			} catch (IOException e) {}
			
		}
		
		
		
	}

