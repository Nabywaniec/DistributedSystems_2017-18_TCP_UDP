package pl.edu.agh.client;

import pl.edu.agh.client.listeners.MulticastListener;
import pl.edu.agh.client.listeners.TcpListener;
import pl.edu.agh.client.listeners.UdpListener;
import pl.edu.agh.server.Server;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {


    public static final String MULTICAST_IP = "230.1.1.1";
    public static final int MULTICAST_PORT = 12344;
    public static String login;
    private static byte[] asciiBytes;

    public static void main(String[] args) throws IOException {

        System.out.println("Enter nick:");
        Scanner scanner = new Scanner(System.in);
        login = scanner.nextLine();

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(Server.PORT));

        DatagramSocket datagramSocket = new DatagramSocket(socket.getLocalPort());
        asciiBytes = (login + ":                    \n" +
                "                 _.-;;-._\n" +
                "          '-..-'|   ||   |\n" +
                "          '-..-'|_.-;;-._|\n" +
                "          '-..-'|   ||   |\n" +
                "    jgs   '-..-'|_.-''-._|").getBytes();
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.write(login + '\n');
        printWriter.flush();

        ExecutorService executorService = Executors.newScheduledThreadPool(10);
        executorService.submit(new TcpListener(socket.getInputStream()));
        executorService.submit(new UdpListener(datagramSocket));
        executorService.submit(new MulticastListener(login));


        boolean works = true;

        while (scanner.hasNextLine() && works ) {
            String message = scanner.nextLine();

                if(message.equals("U")) {
                    InetAddress IPAddress = InetAddress.getByName("localhost");
                    datagramSocket.send(new DatagramPacket(asciiBytes, asciiBytes.length, IPAddress, Server.PORT));

                }
                 if(message.equals("M")){
                    InetAddress IPAddress = InetAddress.getByName(MULTICAST_IP);
                    datagramSocket.send(new DatagramPacket(asciiBytes, asciiBytes.length, IPAddress, MULTICAST_PORT));

                }
                if(message.equals("D")){
                     Server.deleteUser(login);
                     socket.close();
                     datagramSocket.close();
                     works = false;
                     executorService.shutdown();
                     System.exit(-1);
                }
                else {
                     printWriter.write(message + '\n');
                     printWriter.flush();
                 }
        }

        socket.close();
        datagramSocket.close();
    }



}
