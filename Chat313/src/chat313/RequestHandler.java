import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * CONTAINS TWO CLASSES:
 * 
 * RequestHandler - Looping thread to handle HTTP requests. 
 *                  i.e. one instance for all requests.
 * 
 * ResponseHandler - Task thread to respond to individual requests. 
 *                   i.e. one instance per response.
 */
public class RequestHandler extends Thread {
  
	public RequestHandler(byte[] data, String mimeType, int port) {
		this.content = data;
		this.header = new String("HTTP/1.0 200 OK\r\n"
			+ "Server: HTTPServer 1.0\r\n" + "Content-length: "
			+ content.length + "\r\n" + "Content-type: " + mimeType
			+ "\r\n\r\n").getBytes();
		this.port = port;
		this.running = true;
	}
	
	public void run() {
	  ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("My IP Address: " + serverSocket.getInetAddress());
			System.out.println("My Port: " + port);
			System.out.println("My Content: " + new String(content));
			while (running) {
				try {
				  /*
				   * Accept browser connection:
				   */
					Socket connection = serverSocket.accept();
					/*
					 * Handle browser request in another thread:
					 */
					new ResponseHandler(connection, header, content).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
  private byte[] content = null;
  private byte[] header = null;
  private int port = -1;
  private boolean running = false; 
}

/**
 * Thread to handle HTTP response. (launched by request handler thread) 
 */
class ResponseHandler extends Thread {
  
  ResponseHandler(Socket conn, byte[] head, byte[] content) {
    this.conn = conn;
    this.head = head;
    this.data = content;
  }
  
  public void run() { 
    try {
      BufferedReader isReader = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String line = isReader.readLine();
      if (line != null && line.contains("HTTP")) {
        OutputStream os = conn.getOutputStream();
        os.write(head);
        os.write(data);
        os.flush();
        os.close(); // also closes socket
      } // else respond with error HTTP code header
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private Socket conn = null;
  private byte[] head = null;
  private byte[] data = null;
}