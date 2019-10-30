package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main extends JFrame {
    private ServerSocket server;
    private Socket connection;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private int counter = 1;
    private String message;
    private JTextField editField;
    private JTextArea textView;

    public Main() {
        super("Server");
        //create GUI container
        Container container = getContentPane();
        //create editText and attach listener to it
        editField = new JTextField();
        editField.setEditable(true);
        editField.addActionListener(e -> {
            sendMessage(e.getActionCommand());
            editField.setText("");
        });
        //create Text view display area
        container.add(editField, BorderLayout.NORTH);
        textView = new JTextArea();
        container.add(new JScrollPane(textView), BorderLayout.CENTER);
        setSize(500, 500);
        setVisible(true);

    }

    public static void main(String[] args) {
        // write your code here
        Main server = new Main();
        server.createServer();

    }

    //Step1: create server
    public void createServer() {

        try {
            server = new ServerSocket(1234, 50);
            while (true) {
                try {

                    //step 2: wait for connection to connect
                    acceptConnection();
                    //step 3: get input and output streams
                    getStreams();
                    //step 4: process the connection
                    processConnection();
                    //step 5: close streams and socket
                } catch (EOFException eofException) {
                    System.err.println("server terminated connection");
                } finally {
                    //step 5: close connection
                    closeConnection();
                    ++counter;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //step 2: wait for connection to connect and display message
    public void acceptConnection() throws IOException {
        displayMessage("Waiting for connection\n");
        connection = server.accept();
        displayMessage("connection " + counter + " received from: " + connection.getInetAddress().getHostAddress());

    }

    //step 3: get input and output streams
    public void getStreams() throws IOException {
        displayMessage("\nGetting streams\n");
        objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(connection.getInputStream());
        displayMessage("\nGotten streams\n");

    }

    //step 4: process the connection
    public void processConnection() throws IOException {
        String processMessage = "connection successful";
        sendMessage(processMessage);
        do{
        try {
            message = (String) objectInputStream.readObject();
            displayMessage("\n "+message);
        } catch (ClassNotFoundException classNotFoundException) {
            displayMessage("\nUnknown object type found\n");
        }
        } while (!processMessage.equals("CLIENT>>> TERMINATE"));

    }

    //step 5: close streams and socket
    public void closeConnection() {
        try {
        objectOutputStream.close();
        objectInputStream.close();
        connection.close();
        } catch (IOException io){
            io.printStackTrace();
        }
    }

    //step 6: create method for sending messages
    public void sendMessage(String send) {
        try {
           objectOutputStream.writeObject("SERVER>>> " + send);
            objectOutputStream.flush();
            displayMessage("\nSERVER>>> \n" + send);
        } catch (IOException io){
            io.printStackTrace();
        } finally {
            textView.append("\nError writing object\n");
        }

    }

    //step 7: update GUI with streams in another thread
    public void displayMessage(String display) {
        SwingUtilities.invokeLater(() -> {
            textView.append(display);
            //textView.setCaretPosition(textView.getText().length());
        });


    }


}
