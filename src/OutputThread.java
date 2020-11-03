package src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class OutputThread extends Thread {
    DatagramSocket Socket;
    InetAddress IPAddress;
    int portToSend = 0;
    Console cons;

    public OutputThread(DatagramSocket Socket, InetAddress IPAddress, Console cons) {
        this.Socket = Socket;
        this.IPAddress = IPAddress;
        this.cons = cons;
    }

    @Override
    public void run() {
        byte[] otherName = new byte[100];
        DatagramPacket namePacket = new DatagramPacket(otherName, otherName.length);
        try {
            Socket.receive(namePacket);
            portToSend = namePacket.getPort();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String name = new String(namePacket.getData());
        
        while (true) {
            synchronized (this) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    Socket.receive(receivePacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String receivedSentence = new String(receivePacket.getData());
                if (receivedSentence.startsWith("@dump ")) {
                    continue;
                }
                if(receivedSentence.trim().equals("@quit")) {
                    break;
                }
                cons.saveToHistory("From " + name.trim() + ": " + receivedSentence.trim());
                System.out.println("From " + name.trim() + ": " + receivedSentence.trim());
            }
        }
    }

    public int getPort() {
        return portToSend;
    }
}