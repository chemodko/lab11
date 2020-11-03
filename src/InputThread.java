package src;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class InputThread extends Thread {
    DatagramSocket Socket;
    InetAddress IPAddress;
    int port;
    String username;
    Console cons;

    public InputThread(DatagramSocket Socket, InetAddress IPAddress, int port, String username, Console cons) {
        this.Socket = Socket;
        this.IPAddress = IPAddress;
        this.port = port;
        this.username = username;
        this.cons = cons;
    }

    @Override
    public void run() {
        byte[] name = new byte[100];
        name = username.getBytes();
        DatagramPacket sendName = new DatagramPacket(name, name.length, IPAddress, port);
        try {
            Socket.send(sendName);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while (true) {
            synchronized (this) {
                byte[] sendData = new byte[1024];
                try {
                    String sentenceToSend = cons.read();
                    sendData = sentenceToSend.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    Socket.send(sendPacket);

                    if (sentenceToSend.startsWith("@dump ")) {
                        try {
                            String filename = sentenceToSend.substring(6);
                            FileWriter writer = new FileWriter(filename + ".txt");
                            for (int i = 0; i < cons.dump().size(); i++) {
                                writer.write(cons.dump().get(i) + "\n");
                            }
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    if (sentenceToSend.equals("@quit")) {
                        break;
                    }
                    cons.saveToHistory("From " + username + ": " + sentenceToSend);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
