package com.example.tc_robots.backend;

import static org.apache.harmony.awt.internal.nls.Messages.getString;

import android.util.Log;

import com.example.tc_robots.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executor;

public class TCPClient {
    public static final String TAG = "TCPClient";
    //public static final String SERVER_IP = "192.168.125.1"; //server IP address
    public static final String SERVER_IP = "192.168.0.200"; //server IP address
    public static final int SERVER_PORT = 1025;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private static boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    private final Executor executor;
    private static TCPClient instance;

    private TCPClient(OnMessageReceived listener, Executor executor) {
        mMessageListener = listener;
        this.executor = executor;
        run();
    }

    public static void initInstance(OnMessageReceived listener, Executor executor) {
        if (instance == null) {
            instance = new TCPClient(listener, executor);
        }
    }

    public static TCPClient getInstance() {
        return instance;
    }

    public void run() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                    Log.d(TAG, "C: Connecting... to port: " + SERVER_PORT);
                    Socket socket = new Socket(serverAddr, SERVER_PORT);
                    try {
                        mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        mRun = true;
                        //in this while the client listens for the messages sent by the server
                        while (mRun) {
                            mServerMessage = mBufferIn.readLine();

                            if (mServerMessage != null && mMessageListener != null) {
                                //call the method messageReceived from MyActivity class
                                mMessageListener.messageReceived(mServerMessage);
                            }
                        }
                    }catch (SocketException se){
                        Log.e(TAG, String.valueOf(R.string.ERROR_TCP_CLIENT));
                        sendSocketErrorToRestartClient();
                    } catch (Exception e) {
                        Log.e(TAG, "S: Error", e);
                    } finally {
                        Log.e(TAG, "Socket closed");
                        socket.close();
                    }
                } catch (Exception e) {
                    Log.e(TAG, String.valueOf(R.string.ERROR_TCP_CLIENT));
                    sendSocketErrorToRestartClient();
                }
            }
        });
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(final String message) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    Log.d(TAG, "Sending: " + message);
                    mBufferOut.println(message);
                    mBufferOut.flush();
                }
            }
        });
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mRun = false;
                if (mBufferOut != null) {
                    mBufferOut.flush();
                    mBufferOut.close();
                }
                mMessageListener = null;
                mBufferIn = null;
                mBufferOut = null;
                mServerMessage = null;
                instance = null;
            }
        });
    }

    private void sendSocketErrorToRestartClient() {
        mMessageListener.messageReceived(String.valueOf(R.string.ERROR_TCP_CLIENT));
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
