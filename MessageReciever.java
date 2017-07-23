import java.io.*;
import java.net.*;
import java.util.*;

//http://stackoverflow.com/questions/15797788/multicast-sockets-in-java
public class MessageReciever implements Runnable {
  String selfNick;
  String groupIP;
  int port;
  HashMap<String, String> peers;

  public MessageReciever(int aPort, String aGroup, String aNick) {
    selfNick = aNick;
    groupIP = aGroup;
    port = aPort;
    peers = new HashMap<String, String>();
  }

  @Override
  public void run() {
    try {
      InetAddress myAddr = InetAddress.getLocalHost();
      MulticastSocket server = new MulticastSocket(port);
      InetAddress group = InetAddress.getByName(groupIP);
      System.out.println("MessageReciever: " + groupIP + String.valueOf(port));
      //getByName - returns IP address of the given host
      server.joinGroup(group);
      System.out.println("Chat initalised. Group joined.");
      /* Server continually receives data and prints them */
      while(true) {
       byte buf[] = new byte[1024];
       DatagramPacket data = new DatagramPacket(buf, buf.length);
       server.receive(data);
       //Handle the message that has been recieved
       //If joined, public or private or file (or if self)
       String msg = new String(data.getData(), data.getOffset(), data.getLength());
       String selfAddr = InetAddress.getLocalHost().toString();
       //If self, don't recieve self - send message (STILL A BUUG)
       if(!(data.getAddress().toString().equals(selfAddr.toString()))) {
         String[] extractedMessage = msg.split("\\s+");
         if(extractedMessage[0].equals("/whisper")) {
           String actualMessage = "";
           for(int i = 1; i <= extractedMessage.length; i++) {
             actualMessage += (" " + extractedMessage[i]);
           }
           System.out.println("Whisper from PRIVATE:" + actualMessage);
         } else if(extractedMessage[0].equals("/join")) {
           String peerName = extractedMessage[extractedMessage.length - 1];
           String peerAddr = data.getAddress().getHostAddress();
           peers.put(peerAddr, peerName);
           System.out.println("A user has joined the chat. Please welcome, " + extractedMessage[1]);
         } else {
           //Check which ip message is from and convert to a name (from list)
           System.out.println("Message recieved: " + msg);
         }
       }
     }
   } catch(IOException e) {
       System.out.println(e);
     }
    }
  }
