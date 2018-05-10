package chat313;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public Server() {
    }

    //set up and run the server
    public void startRunning() {
        try {
            server = new ServerSocket(6789, 100);//Port, Queue(people on server)
            while (true) {
                try {
                    waitForConnection();//allow chat when at least one person is in
                    setupStreams();//Set up connections between computers
                    whileChatting();//when connection is true allows chatting
                } catch (EOFException eofException) {
                    showMessage("Server ended the connection!");
                } finally {
                    close();
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //Wait for connection, then display connection information
    private void waitForConnection() throws IOException {
        showMessage("Waiting for someone to connect...\n");
        connection = server.accept();//only creates connection if connected to someone
        showMessage("Now Connected to " + connection.getInetAddress().getHostName());
    }

    //get stream to send and receive data
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();//clears buffer
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now setup! \n");
    }

    //During the chat conversation
    private void whileChatting() throws IOException {
        String message = "You are now connected! ";
        sendMessage(message);
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("Error on info Recieved");
            }
        } while (!message.equals("CLIENT - END"));
    }

    //closes streams and sockets
    private void close() {
        showMessage("\n Closing connections...\n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
    }

    //send message to client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " + message);//server = username edit
            output.flush();
            showMessage("\n SERVER - " + message);
        } catch (IOException ioE) {
            //output error cant send message
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

}
