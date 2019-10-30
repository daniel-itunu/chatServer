package com.company;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private ServerSocket server;
    private Socket connection;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private int counter = 1;
    private String message;

     {
         Scanner input = new Scanner(System.in);
         input.nextLine();

    }

    public static void main(String[] args) {
	// write your code here
        Main main = new Main();
        main.createServer();

    }

    //Step1: create server
    public void createServer() {
        try {
        try {
            server = new ServerSocket(1234, 50);
            while (true) {
                //step 2: wait for connection to connect
                acceptConnection();
                //step 3: get input and output streams
                getStreams();
                //step 4: process the connection
                //step 5: close streams and socket
            }
        } catch (EOFException eofException) {
            System.err.println("server terminated connection");
        } finally {
            //step 5: close connection
            ++counter;
        }
    } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //step 2: wait for connection to connect and display message
    public void acceptConnection() throws  IOException {
        System.out.println("connecting...");
        connection = server.accept();
        System.out.println("connection "+counter+" received from: "+connection.getInetAddress().getHostAddress() );

    }

    //step 3: get input and output streams
    public void getStreams() throws  IOException {
        System.out.println("getting streams");
        objectOutputStream = new ObjectOutputStream((connection.getOutputStream()));
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(connection.getInputStream());
        System.out.println("gotten streams");

    }

    //step 4: process the connection
    public void processConnection() throws IOException, ClassNotFoundException {
       objectOutputStream.writeObject("Server>>>> connection successful");
       objectOutputStream.flush();

        message = (String) objectInputStream.readObject();
        System.out.println("client said "+message);

    }

    //step 5: close streams and socket
    public void closeConnection() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        connection.close();
    }



}
