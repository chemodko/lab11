package src;

import java.net.*;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) throws Exception {
        InetAddress IPAddress = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        DatagramSocket clientSocket = new DatagramSocket();
        Console cons = new Console();
        OutputThread outputThread = new OutputThread(clientSocket, IPAddress, cons);
        outputThread.start();

        System.out.print("Input ur name pls: ");

        try(Scanner scanner = new Scanner(System.in)) {
            String name = scanner.nextLine();

            InputThread inThread = new InputThread(clientSocket, IPAddress, port, name, cons);
            
            System.out.println("To end the conversation, enter: '@quit'");
            System.out.println("To save the conversation, enter: '@dump filename'");
            inThread.start();
        }
    }
}