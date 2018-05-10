import java.net.Socket;

public class SocketInfo {
	public static void main(String[] args) throws Throwable {
		for (int i = 0; i < args.length; i++) {
			Socket s = new Socket(args[i], 80);
			System.out.println("Destination IP: " + s.getInetAddress() + 
			    "\nDestination Port: " + s.getPort() + "\nSource IP: " +
			    s.getLocalAddress() + "\nSource Port: " + s.getLocalPort()
			);
			s.close();
		}
	}
}
