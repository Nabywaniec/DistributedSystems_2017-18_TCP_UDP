package pl.edu.agh.client.listeners;

import pl.edu.agh.client.Client;

import java.io.*;
import java.net.*;


public class MulticastListener extends Thread {
    private String login;

    public MulticastListener(String login)
    {
        this.login = login;
    }

    @Override
    public void run() {

        try {
            MulticastSocket multicastSocket = new MulticastSocket(Client.MULTICAST_PORT);
            multicastSocket.joinGroup(InetAddress.getByName(Client.MULTICAST_IP));

            while (!multicastSocket.isClosed()) {
                byte[] buff = new byte[1000];
                DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
                multicastSocket.receive(datagramPacket);
                String message = new String(buff);
                if (!message.startsWith(Client.login)) {
                    System.out.println("MultiCast UDP " + message);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
