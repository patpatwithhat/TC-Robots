package com.example.tc_robots.backend;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectTask extends AsyncTask<String, String, TCPClient> {

    @Override
    protected TCPClient doInBackground(String... message) {

        //we create a TCPClient object
        TCPClient mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                //this method calls the onProgressUpdate
                publishProgress(message);
            }
        });
        mTcpClient.run();

        return mTcpClient;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //response received from server
        Log.d("TCPClient", "response ConnectTask " + values[0]);
        //process server response here....

    }
}