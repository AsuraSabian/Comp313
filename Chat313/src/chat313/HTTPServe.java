package chat313;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * This class is responsible for launching the request handler thread
 * of the HTTP server  
 * @author luke
 */
public class HTTPServe {

	/**
	 * The main method.
	 * @param args the file to serve and the port to listen on.
	 */
	public static void main(String[] args) {
		try { 
			/*
			 * If the file is not an HTML file, use text/plain as the 
			 * content type:
			 */
  		String contentType = "text/plain"; 
  		if (args[0].endsWith(".html") || args[0].endsWith(".htm")) { 
  			contentType = "text/html"; 
  		} 
  		/*
  		 * Load the content file stream into a byte array:
  		 */
  		InputStream in = new FileInputStream(args[0]); 
  		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
  		int b; 
  		while ((b = in.read()) != -1) out.write(b); 
  		in.close();
  		byte[] data = out.toByteArray(); 
  		/*
  		 *  Determine which port to listen on:
  		 */ 
  		int port; 
  		try { 
  			port = Integer.parseInt(args[1]); 
  			if (port < 1 || port > 65535) port = 80; 
  	  } catch (Exception e) { 
  	  	port = 80; 
  	  } 
  	  /*
  	   * Start the HTTP request handler thread:
  	   */
  	  new RequestHandler(data, contentType, port).start(); 
  	  // perform other tasks (gui, logging, etc.)
    } catch (ArrayIndexOutOfBoundsException e) { 
    	System.out.println( "Usage: java httpserver.HTTPServer <filename> <port>"); 
    } catch (Exception e) { 
    	System.err.println(e); 
    }
	}
}
