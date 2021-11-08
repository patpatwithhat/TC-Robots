package com.example.tc_robots.backend.network;

import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.backend.tinyDB.TinySingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class TCPClientSet {
    private static TCPClientSet instance;
    List<TCPClient> tcpClientList = new ArrayList<>();
    List<OnTCPClientSetChange> onChangeListeners = new ArrayList<>();
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

    public TCPClient createClient(Robot robot) {
        List<TCPClient> tcpClients = tcpClientList.stream().filter(client -> client.getRobot().equals(robot)).collect(Collectors.toList());
        if (tcpClients.size() > 0) return null;
        TCPClient client = new TCPClient(robot, executorService);
        tcpClientList.add(client);
        sendTCPClientAddEvent(client);
        return client;
    }

    public void loadSavedRobotsFromSharedPref() {
        List<Robot> robots = TinySingleton.getInstance().getSavedRobots();
        robots.forEach(robot -> {
            createClient(robot).startClient();
        });
    }

    public TCPClient getClientByRobot(Robot robot) {
        List<TCPClient> clients = tcpClientList.stream().filter(client -> client.getRobot().equals(robot)).collect(Collectors.toList());
        if (clients.size() != 1) return null;
        return clients.get(0);
    }

    public void removeClient(TCPClient client) {
        client.stopClient();
        tcpClientList.remove(client);
        sendTCPClientRemoveEvent(client);
    }

    public List<TCPClient> getTcpClientList() {
        return tcpClientList;
    }

    public void deleteRobot(Robot deletedRobot) {
        List<TCPClient> clients = tcpClientList.stream().filter(client -> client.getRobot().equals(deletedRobot)).collect(Collectors.toList());
        if (clients.size() == 0) return;
        clients.forEach(this::removeClient);
    }

    public void addOnChangeListener(OnTCPClientSetChange listener) {
        onChangeListeners.add(listener);
    }
    public void removeOnChangeListener(OnTCPClientSetChange listener) {
        onChangeListeners.remove(listener);
    }
    private void sendTCPClientAddEvent(TCPClient client) {
        onChangeListeners.forEach(listener -> listener.clientAdded(client));
    }
    private void sendTCPClientRemoveEvent(TCPClient client) {
        onChangeListeners.forEach(listener -> listener.clientRemoved(client));
    }

    public interface OnTCPClientSetChange {
        void clientAdded(TCPClient client);
        void clientRemoved(TCPClient client);
    }
}
