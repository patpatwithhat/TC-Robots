package com.example.tc_robots.backend.network;

import static org.apache.harmony.awt.internal.nls.Messages.getString;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.tc_robots.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class TCPClient {
    public static final String TAG = "TCPClient";
    public static final String SERVER_IP = "192.168.125.1"; //server IP address
    //public static final String SERVER_IP = "192.168.0.200"; //server IP address
    public static final int SERVER_PORT = 1025;
    // message to send to the server
    private String mServerMessage;
    private List<OnMessageReceived> messageReceivedListeners = new ArrayList<>();
    // while this is true, the server will continue running
    public MutableLiveData<Boolean> isActive = new MutableLiveData<>();
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    private final Executor executor;
    private static TCPClient instance;

    private TCPClient(Executor executor) {
        this.executor = executor;
        setIsInactive();
        run();
    }

    public static void initInstance(Executor executor) {
        if (instance == null) {
            instance = new TCPClient(executor);
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
                        setIsActive();
                        //in this while the client listens for the messages sent by the server
                        while (Objects.requireNonNull(isActive.getValue())) {
                            mServerMessage = mBufferIn.readLine();

                            if (mServerMessage != null && messageReceivedListeners.size() > 0) {
                                //call the method messageReceived from MyActivity class
                                sendMessageToListeners(mServerMessage);
                                // mMessageListener.messageReceived(mServerMessage);
                            }
                        }
                    } catch (SocketException se) {
                        Log.e(TAG, String.valueOf(R.string.ERROR_TCP_CLIENT));
                        setIsInactive();
                        sendSocketErrorToRestartClient();
                    } catch (Exception e) {
                        setIsInactive();
                        Log.e(TAG, "S: Error", e);
                    } finally {
                        Log.e(TAG, "Socket closed");
                        socket.close();
                    }
                } catch (Exception e) {
                    Log.e(TAG, String.valueOf(R.string.ERROR_TCP_CLIENT));
                    setIsInactive();
                    sendSocketErrorToRestartClient();
                }
            }

        });
    }

    /**
     * Sends the message entered by client to the server
     *
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

    private void setIsActive() {
        isActive.setValue(true);
    }

    private void setIsInactive() {
        isActive.setValue(false);
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                setIsInactive();
                if (mBufferOut != null) {
                    mBufferOut.flush();
                    mBufferOut.close();
                }
                mBufferIn = null;
                mBufferOut = null;
                mServerMessage = null;
                instance = null;
            }
        });
    }

    public void addOnMessageReceivedListener(OnMessageReceived messageReceived) {
        messageReceivedListeners.add(messageReceived);
    }

    public void removeMessageReceivedListener(OnMessageReceived messageReceived) {
        messageReceivedListeners.remove(messageReceived);
    }

    private void sendMessageToListeners(String message) {
        for (OnMessageReceived listener : messageReceivedListeners) {
            listener.messageReceived(message);
        }
    }

    private void sendSocketErrorToRestartClient() {
        sendMessageToListeners(String.valueOf(R.string.ERROR_TCP_CLIENT));
    }

    //Declare the interface. The method messageReceived(String message) can be implemented at any point
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
