package com.example.tc_robots.backend.network;

import com.example.tc_robots.backend.monitoring.Robot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class TCPClientSet {
    private static TCPClientSet instance;
    //private final OnClientStatusChange onClientStatusChange;
    //List<OnClientStatusChange> onClientStatusChangeList = new ArrayList<>();

    List<TCPClient> tcpClientList = new ArrayList<>();
    ExecutorService executorService;


    public static TCPClientSet getInstance() {
        return instance;
    }

    private TCPClientSet(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static TCPClientSet initInstance(ExecutorService executorService) {
        if (getInstance() != null) {
            return getInstance();
        }
        instance = new TCPClientSet(executorService);
        return instance;
    }

    public void createClient(Robot robot) {
        tcpClientList.add(robot.getTCPClient() == null ? robot.setTPClient(new TCPClient(robot, executorService)) : robot.getTCPClient());
        //notifyClientAdded(client);
    }

    public void removeClient(TCPClient client) {
        tcpClientList.remove(client);
        // notifyClientRemoved(client);
    }

    public List<TCPClient> getTcpClientList() {
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
