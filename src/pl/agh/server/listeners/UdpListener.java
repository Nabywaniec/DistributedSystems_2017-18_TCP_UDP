package pl.edu.agh.server.listeners;

import pl.edu.agh.server.Server;

import java.io.*;
import java.net.*;


public class UdpListener extends Thread {

    @Override
    public void run() {
        try {

            DatagramSocket datagramSocket = new DatagramSocket(Server.PORT);

            while (!datagramSocket.isClosed()) {

                byte[] bytes = new byte[500];
                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
                datagramSocket.receive(datagramPacket);
                String message = new String(bytes);
                String[] split = message.split(":", 2);
                String nick = split[0];
                System.out.println("////" + message);

                Server.clients.entrySet().stream().filter(client -> !client.getKey().equals(nick))
                        .forEach(client -> {
                            try {
                                Socket s = client.getValue().getSocket();
                                int port = s.getPort();
                                InetAddress IPAddress = InetAddress.getByName("localhost");
                                byte[] newBytes = (message).getBytes();
                                datagramSocket.send(new DatagramPacket(newBytes, newBytes.length, IPAddress, port));
                                System.out.println("UDP - datagram");
                            }catch (SocketException s){
                                System.out.println("UDP socket exception" + s.getMessage());
                            }
                            catch (IOException e) {
                                System.out.println("IO Exception" + e.getMessage());
                            }
                            catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                                finally
                             {
                                datagramSocket.close();
                            }
                        });
                }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
