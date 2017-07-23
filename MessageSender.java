import java.io.*;
import java.net.*;

//http://stackoverflow.com/questions/15797788/multicast-sockets-in-java
public class MessageSender implements Runnable {
  String selfNick;
  String groupIP;
  int port;

  public MessageSender(int aPort, String aGroup, String aNick) {
    groupIP = aGroup;
    port = aPort;
    selfNick = aNick;
  }

  @Override
  public void run() {
    try {
      MulticastSocket chat = new MulticastSocket(port);
      InetAddress group = InetAddress.getByName(groupIP);
      chat.joinGroup(group);
      String msg = "";
      System.out.println("Type a message then pressed enter to send...");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String helo = "/join " + selfNick;
      DatagramPacket initDgram = new DatagramPacket(helo.getBytes(), 0, helo.length(), group, port);
      chat.send(initDgram);
      while(true) {
        msg = br.readLine();
        String[] extractedMessage = msg.split("\\s+");

        if(extractedMessage[0].equals("/bye")) {
          System.out.println("Leaving.");
          chat.leaveGroup(group);
          break;
        } else if(extractedMessage[0].equals("/whisper")) {
          InetAddress privateIP = InetAddress.getByName(extractedMessage[1]);
          DatagramPacket data = new DatagramPacket(msg.getBytes(), 0, msg.length(), privateIP, port);
          chat.send(data);
        } else {
          DatagramPacket data = new DatagramPacket(msg.getBytes(), 0, msg.length(), group, port);
          chat.send(data);
        }
      }
      chat.close();
    } catch(IOException e) {
      System.out.println("IO Error.");
    }
  }
}
