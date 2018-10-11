package pl.edu.agh.server;

import pl.edu.agh.server.listeners.TcpListener;
import pl.edu.agh.server.listeners.UdpListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final int PORT = 12345;
    public static final ExecutorService executor = Executors.newFixedThreadPool(100);
    public static final HashMap<String, ClientUtil> clients = new HashMap<>();
    public static int max_connections =100;
    public static int connections = 0;

    public static void main(String[] args) throws IOException {
        new UdpListener().start();
        new TcpListener().start();
    }

    public synchronized static boolean registerUser(String name, ClientUtil clientHandler) {


        if(connections < max_connections) {
            clients.put(name, clientHandler);
            System.out.println("Registered " + name);
            connections +=1;
            return true;
        }
        System.out.println("Zbyt duża ilośc klientów");
        return false;
    }

    public synchronized static void deleteUser(String name){

        clients.remove(name);
        connections -=1;
        System.out.println("User removed " + name);

    }

}
