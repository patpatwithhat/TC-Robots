package com.example.tc_robots.backend.monitoring;

import com.example.tc_robots.backend.network.TCPClient;

import java.util.Comparator;

public class Robot implements Comparable<Robot> {
    private String name;
    private String ip;
    private String port;
    private TCPClient client;

    public Robot(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public TCPClient setTPClient(TCPClient client) {
        this.client = client;
        return client;
    }

    public TCPClient getTCPClient() {
        return this.client;
    }

    /**
     * Two Robots are the same, if ip and port match!
     */
    @Override
    public int compareTo(Robot robot) {
        return Comparator.comparing(Robot::getIp)
                .thenComparing(Robot::getPort)
                .compare(this, robot);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Robot) {
            Robot robot = (Robot) obj;
            return robot.getIp().equals(this.getIp()) && robot.getPort().equals(this.getPort());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return 17 * hash + (this.getIp() != null ? this.getIp().hashCode() : 0) + (this.getPort() != null ? this.getPort().hashCode() : 0);
    }
}
