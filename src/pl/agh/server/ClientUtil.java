package pl.edu.agh.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientUtil extends Thread {

    private final Scanner scanner;
    private final PrintWriter printWriter;
    private Socket socket;

    public ClientUtil(Socket socket) throws IOException {
        this.socket = socket;

        scanner = new Scanner(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream(), true);

    }

    @Override
    public void run() {
        String login = scanner.nextLine();
        Server.registerUser(login, this);
        System.out.println("Rejestracja");
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            sendToAll(login, message);
        }
    }

    private void send(String message) {
        printWriter.write(message + '\n');
        printWriter.flush();
    }

    private void sendToAll(String login, String message) {
            synchronized (Server.clients) {
                Server.clients.entrySet().stream()
                        .filter(client -> !client.getKey().equals(login))
                        .forEach(client -> client.getValue().send(login + ":" + message));
            }
    }

    public Socket getSocket() {
        return socket;
    }
}
