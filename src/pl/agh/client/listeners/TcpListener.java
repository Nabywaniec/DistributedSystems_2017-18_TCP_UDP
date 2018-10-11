package pl.edu.agh.client.listeners;

import java.io.*;
import java.util.Scanner;

public class TcpListener extends Thread {
    private final Scanner scanner;

    public TcpListener(InputStream inputStream) {

        this.scanner = new Scanner(inputStream);
    }

    @Override
    public void run() {
        while (scanner.hasNextLine()) {
            System.out.println("TCP " + scanner.nextLine());
        }

    }
}
