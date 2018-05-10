package chat313;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//127.0.0.1 IP for server on same pc 
public class Client {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    private String username;

    //constructor
    public Client(String host, String un) {
        serverIP = host;
        username = un;
    }

    //connect to server
    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();

        } catch (EOFException eofE) {
            showMessage("\n Cliented terminated connection");
        } catch (IOException ioE) {
            ioE.printStackTrace();
        } finally {
            close();
        }
    }

    //Connect to server
    private void connectToServer() throws IOException {
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        //showMessage("Stream set up");
    }

    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);

            } catch (ClassNotFoundException classNotFoundE) {
                showMessage("Error on info Recieved");
            }
        } while (!message.equals("Server - END"));
    }

    private void sendMessage(String message) {
        try {
            output.writeObject(username + " - " + message);
            output.flush();
            showMessage("\n" + username + " - " + message);
        } catch (IOException ioE) {
            //display message send error
        }
    }

    private void showMessage(final String text) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                //add text to display
                //chatWindow.append(text);
            }
        }
        );
    }

    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                //userText.setEditable(tof);
            }
        }
        );
    }

    private void close() {
        showMessage("\n Closing down Client...");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
    }

}
