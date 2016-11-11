import java.io.*;
import java.net.*;

/*
  LANChat: Allows for users on a network who join in a
  public chat and enables private whispers between users.
  Uses TCP for private whipsers and a UDP Multicast for
  public chats.
  Version: 0.9 (11 November 2016)
*/

public class LANChat {
  //Chat variables
  String userName;
  InetAddress chatKey;
  int chatPort;

  /* Empty Initialiser so we can set the chat variables as we go. */
  public LANChat() {};

  public static void askForUserDetails(LANChat aClient) {
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    //Get the input
    String IUsername = new String(inputReader.readLine());
    String IChatKey = new String(inputReader.readLine());
    String IChatPort = new String(inputReader.readLine());
    //Conver the required inputs - Type Safing
    InetAddress CChatKey = InetAddress.getByName(IUsername);
    int CChatPort = Integer.parseInt(IChatPort);
    aClient.userName = IUsername;
    aClient.chatKey = CChatKey;
    aClient.chatPort = CChatPort;
  }

  public static void main(String[] args) {
    LANChat userChatClient = new LANChat();
    askForUserDetails(userChatClient);
  }
}
