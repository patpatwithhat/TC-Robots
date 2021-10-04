package com.example.tc_robots.backend;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import io.reactivex.rxjava3.exceptions.Exceptions;

public class TCPConnection {
    public static final String SERVER_IP = "192.168.0.200"; //server IP address
    public static final int SERVER_PORT = 1025;
    Socket socket;
    BufferedReader inFromServer;
    DataOutputStream outputStream;
    PrintWriter outWriter;


    public TCPConnection() {
        this(SERVER_IP, SERVER_PORT);
    }

    public TCPConnection(String host, int port) {
        init(host, port);
    }

    private void init(String host, int port) {
        try {
            socket = new Socket(host, port);
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new DataOutputStream(socket.getOutputStream());
            outWriter = new PrintWriter(outputStream, true);
        } catch (IOException ex) {
            Exceptions.propagate(ex);
        }
    }

    public void close() {
        try {
            outWriter.close();
            outputStream.close();
            inFromServer.close();
            socket.close();
        } catch (IOException ex) {
            Exceptions.propagate(ex);
        }
    }
}