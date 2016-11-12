import java.io.*;
import java.net.*;

/*
  LANChat: Allows for users on a network who join in a
  public chat and enables private whispers between users.
  Uses TCP for private whipsers and a UDP Multicast for
  public chats.
  Version: 0.901
*/

class LANChat {
  //Chat variables
  String userName;
  InetAddress chatKey;
  int chatPort;

  /* Empty Initialiser so we can set the chat variables as we go. */
  public LANChat() {};

  public static void askForUserDetails(LANChat aClient) {
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    String IUsername = new String();
    InetAddress IChatKey;
    int IChatPort = 0;

    try {
      IUsername = new String(inputReader.readLine());
      IChatKey = InetAddress.getByName(new String(inputReader.readLine()));
      IChatPort = Integer.parseInt(new String(inputReader.readLine()));
      aClient.userName = IUsername;
      aClient.chatKey = IChatKey;
      aClient.chatPort = IChatPort;
    } catch(NumberFormatException e) {
      System.out.println("Error: Port Number is Invalid. Required: Port Number (Integer). Exitting...");
      System.exit(0);
    } catch(UnknownHostException e) {
      System.out.println("Error: Invalid IP Address. Required: IP Address. Exitting...");
      System.exit(0);
    } catch(IOException e) {
      System.out.println("Could not handle the IO. Exitting...");
      System.exit(0);
    }
  }

  public static void main(String[] args) {
    LANChat userChatClient = new LANChat();
    askForUserDetails(userChatClient);
  }
}
