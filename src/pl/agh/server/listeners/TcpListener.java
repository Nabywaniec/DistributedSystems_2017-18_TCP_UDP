package pl.edu.agh.server.listeners;

import pl.edu.agh.server.ClientUtil;
import pl.edu.agh.server.Server;

import java.io.*;
import java.net.*;

public class TcpListener extends Thread {

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Server.PORT)) {
            while (!serverSocket.isClosed()) {
                ClientUtil clientUtil = new ClientUtil(serverSocket.accept());
                Server.executor.submit(clientUtil);
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException ie){
            System.out.println(ie.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
