/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larngeartech.serverdiscoveryservice.client;

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
public class DiscoveryClient implements Runnable {
    protected DatagramSocket socket = null;
    protected int port;
    protected String serverIp;
    
    public DiscoveryClient() {
        this.port = Configuration.DEFAULT_DISCOVERY_PORT;
    }
    
    public String getServerIp() {
        return this.serverIp;
    }
    
    @Override
    public void run() {
        try {
            socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            
            while (true) {
                // Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);
                
                // Packet received, check that it is the right discovery packet
                String recvToken = new String(packet.getData()).trim();
                if (recvToken.equals(Configuration.DEFAULT_TOKEN)) {
                    // Get server's IP address
                    this.serverIp = packet.getAddress().getHostAddress();
                    break;
                }
            }
            postProcess();
        } catch (IOException ex) {
            Logger.getLogger(DiscoveryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // do something after receiving server's IP address 
    protected void postProcess() {
        System.out.println("Server IP is " + this.getServerIp());
        
        // Open web browser with the server ip address
        WebBrowser browser = new WebBrowser();
        browser.openUrl("http://" + this.getServerIp());
    }
    
    public static void main(String args[]) {
        new Thread(new DiscoveryClient()).start();
    }
    
}
