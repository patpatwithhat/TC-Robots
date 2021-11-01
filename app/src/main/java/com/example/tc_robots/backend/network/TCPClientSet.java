package com.example.tc_robots.backend.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TCPClientSet {
    private static TCPClientSet instance;
    //private final OnClientStatusChange onClientStatusChange;
    //List<OnClientStatusChange> onClientStatusChangeList = new ArrayList<>();

    Set<TCPClient> tcpClientList = new HashSet<>();



    public static TCPClientSet getInstance() {
        return instance;
    }

    private TCPClientSet() {
    }

    public static TCPClientSet initInstance() {
        if (getInstance() != null) {
            return getInstance();
        }
        return new TCPClientSet();
    }

    public void addClient(TCPClient client) {
        tcpClientList.add(client);
        //notifyClientAdded(client);
    }

    public void removeClient(TCPClient client) {
        tcpClientList.remove(client);
       // notifyClientRemoved(client);
    }

    public Set<TCPClient> getTcpClientList() {
        return tcpClientList;
    }
/*
    private void notifyClientAdded(TCPClient client) {
        onClientStatusChange.clientAdded(client);
    }

    private void notifyClientRemoved(TCPClient client) {
        onClientStatusChange.clientRemoved(client);
    }

    public void addOnClientStatusChangeListener(OnClientStatusChange listener) {
        onClientStatusChangeList.add(listener);
    }

    public void removeOnClientStatusChangeListener(OnClientStatusChange listener) {
        onClientStatusChangeList.remove(listener);
    }

    public interface OnClientStatusChange {
        TCPClient clientAdded(TCPClient client);

        TCPClient clientRemoved(TCPClient client);
    }*/
}
