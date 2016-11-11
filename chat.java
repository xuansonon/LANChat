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
  public LANChat(String aUsername, InetAddress aChatKey, int aChatPort) {
    userName = aUsername;
    chatKey = aChatKey;
    chatPort = aChatPort;
  };

  public static void askForUserDetails(LANChat aClient) {
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    String IUsername = new String();
    InetAddress IChatKey;
    int IChatPort = new Integer();

    try {
      IUsername = new String(inputReader.readLine());
      IChatKey = InetAddress.getByName(new String(inputReader.readLine()));
      IChatPort = Integer.parseInt(new String(inputReader.readLine()));
    } catch(NumberFormatterException e) {
      System.out.println("Error: Port Number is Invalid. Required: Port Number (Integer). Exitting...")
      System.exit(0);
    } catch(UnknownHostException e) {
      System.out.println("Error: Invalid IP Address. Required: IP Address.");
      System.exit(0);
    }

    aClient = new LANChat(IUsername, IChatKey, IChatPort);

  }

  public static void main(String[] args) {
    LANChat userChatClient;
    askForUserDetails(userChatClient);
  }
}
