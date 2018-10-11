package pl.edu.agh.client.listeners;

import java.net.*;
import java.io.*;

public class UdpListener extends Thread {
    private final DatagramSocket datagramSocket;

    public UdpListener(DatagramSocket datagramSocket) {

        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        try {
            while (!datagramSocket.isClosed()) {
                byte[] buff = new byte[1000];
                DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
                datagramSocket.receive(datagramPacket);
                String message = new String(buff);
                System.out.println("UDP " + message);
            }
        } catch (IOException e) {
            System.out.println("IO Exception" + e.getMessage());
        } finally {
            datagramSocket.close();
        }
    }
}
