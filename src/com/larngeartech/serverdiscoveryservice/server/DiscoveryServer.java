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

    public DiscoveryServer(int discoveryPort, String token) {
        this.discoveryPort = discoveryPort;
        this.token = token;
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        try {
            discoverySocket = new DatagramSocket(discoveryPort);
            System.out.println("Open discovery port " + discoveryPort);
            byte[] bcastData = Configuration.DEFAULT_TOKEN.getBytes();
            while (!Thread.currentThread().isInterrupted()) {
                DatagramPacket bcastPacket = new DatagramPacket(bcastData, bcastData.length, InetAddress.getByName(Configuration.DEFAULT_BROADCAST_ADDR), Configuration.DEFAULT_DISCOVERY_PORT);
                discoverySocket.send(bcastPacket);
                //System.out.println("Broadcast discovery message to " + TabConConfiguration.DEFAULT_MULTICAST_ADDR);
                Thread.sleep(Configuration.DEFAULT_DISCOVERY_INTERVAL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(DiscoveryServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (discoverySocket != null) {
                discoverySocket.close();
            }
        }
    }
}
