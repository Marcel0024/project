import java.io.*;
import java.net.*;

public class Server {
	public static void main(String args[]){
		try {
	
			ServerSocket sock = new ServerSocket(7789);
			Socket a = sock.accept();
			Thread temp = new Thread(new Weather(a));
			temp.start();
			
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
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				String line;
				while((line = in.readLine()) != null){
					
					System.out.println(line);
				
				}
				
			} catch (IOException e) {}
		}
	}

