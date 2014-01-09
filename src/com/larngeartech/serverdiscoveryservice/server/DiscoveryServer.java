/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larngeartech.serverdiscoveryservice.server;

import com.larngeartech.serverdiscoveryservice.common.Configuration;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ping
 */
public class DiscoveryServer implements Runnable {
   
    protected DatagramSocket discoverySocket = null;
    //protected MulticastSocket discoverySocket = null;
    protected int discoveryPort;
    protected String token;
    protected int interval;

    public DiscoveryServer() {
        this.discoveryPort = Configuration.DEFAULT_DISCOVERY_PORT;
        this.token = Configuration.DEFAULT_TOKEN;
        this.interval = Configuration.DEFAULT_DISCOVERY_INTERVAL;
    }
    
    public DiscoveryServer(int discoveryPort, String token, int interval) {
        this.discoveryPort = discoveryPort;
        this.token = token;
        this.interval = interval;
    }
    
    public int getDiscoveryPort() {
        return this.discoveryPort;
    }
    
    public void setDiscoveryPort(int port) {
        this.discoveryPort = port;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public int getInterval() {
        return this.interval;
    }
    
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        try {
            byte[] bcastData = token.getBytes();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (discoverySocket == null) {
                        discoverySocket = new DatagramSocket();
                    }
                    DatagramPacket bcastPacket = new DatagramPacket(bcastData, bcastData.length, InetAddress.getByName(Configuration.DEFAULT_BROADCAST_ADDR), this.discoveryPort);
                    discoverySocket.send(bcastPacket);
                    Thread.sleep(interval);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(DiscoveryServer.class.getName()).log(Level.SEVERE, null, ex); 
                    if (discoverySocket != null) {
                       discoverySocket.close();
                    }
                    Thread.sleep(interval);                  
                } 
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(DiscoveryServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
