package src;

import java.net.*;
import java.util.Scanner;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        InetAddress IPAddress = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);

        Console cons = new Console();
        DatagramSocket serverSocket = new DatagramSocket(port);
        OutputThread outputThread = new OutputThread(serverSocket, IPAddress, cons);  
        outputThread.start();

        System.out.print("Input ur name pls: ");
        try (Scanner scanner = new Scanner(System.in)) {
            String name = scanner.nextLine();

            while (outputThread.getPort() == 0) {
                Thread.sleep(2000);
            }
            
            System.out.println("To end the conversation, enter: '@quit'");
            System.out.println("To save the conversation, enter: '@dump filename'");
            InputThread inThread = new InputThread(serverSocket, IPAddress, outputThread.getPort(), name, cons);
            inThread.start();
        }
    }
}