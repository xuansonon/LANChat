import java.io.*;
import java.net.*;

class PrivateChat {

  String chatNickname;
  String chatGroupKey;
  int chatPortNumber;

  public PrivateChat() {
    chatNickname = "";
    chatGroupKey = "";
    chatPortNumber = 0;
  }

  //http://stackoverflow.com/questions/1992970/check-if-int-is-between-two-numbers- Modifed to be inclusive
  public static boolean isBetween(int a, int b, int c) {
      return b >= a ? c >= a && c <= b : c >= b && c <= a;
  }

  //Self Learning Methods
  public static String learnIP() {
    String myIP = "";
    try {
      InetAddress addr = InetAddress.getLocalHost();
      myIP = addr.getHostAddress();
    } catch(UnknownHostException e) {
      System.out.println("Unknown host error: " + e);
    }
    return myIP;
  }
  public static String learnSubnetInBits() {
    String mySubnet = "";
    try {
      //http://stackoverflow.com/questions/1221517/how-to-get-subnet-mask-of-local-system-using-java
      InetAddress localHost = Inet4Address.getLocalHost();
      NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
      //Can change 0 for AMDC - 3 for EN Labs
      int hostBits = (networkInterface.getInterfaceAddresses().get(3).getNetworkPrefixLength());
      for(int i = 1; i <= 32; i++) { //32 bits in IP
        if(i <= hostBits) {
          mySubnet += "1";
        } else {
          mySubnet += "0";
        }
        if(((i % 8) == 0) && (i != 32)){
          mySubnet += ".";
        }
      }
    } catch(SocketException e) {
      System.out.println("Unknown socket error: " + e);
    } catch(UnknownHostException e) {
      System.out.println("Unknwon host error: " + e);
    }
    return mySubnet;
  }
  public static String learnSubnet() {
    String subnetMask = "";
    String subnet = learnSubnetInBits();
    String[] subnetSections = subnet.split("\\.");

    for(int i = 0; i < subnetSections.length; i++) {
      //http://stackoverflow.com/questions/7437987/how-to-convert-binary-string-value-to-decimal
      subnetMask += String.valueOf(Integer.parseInt(subnetSections[i], 2));
      if(i != subnetSections.length - 1) {
        subnetMask += ".";
      }
    }
    return subnetMask;
  }
  public static String learnNetworkAddres() {
    String networkAddr = "";
    String subnet[] = learnSubnet().split("\\.");
    String myIP[] = learnIP().split("\\.");
    int networkCount = 0;
    for(int i = 0; i < subnet.length; i++) {
      if(Integer.parseInt(subnet[i]) != 0) { networkCount++; }
    }
    for(int i = 0; i < networkCount; i++) {
      networkAddr += myIP[i];
      if(i != (networkCount - 1)) { networkAddr += "."; }
    }
    return networkAddr;
  }
  public static String learnHostNumber() {
    String hostNumber = "";
    String subnet[] = learnSubnet().split("\\.");
    String myIP[] = learnIP().split("\\.");
    int networkCount = 0;
    for(int i = 0; i < subnet.length; i++) {
      if(Integer.parseInt(subnet[i]) != 0) { networkCount++; }
    }
    for(int i = networkCount; i < myIP.length; i++) {
      hostNumber += myIP[i];
    }
    return hostNumber;
  }
  public static void learnSelf() {
    System.out.println("Current IP: " + learnIP());
    //System.out.println("Subnet Mask in Bits: " + learnSubnetInBits()); //Optional If required in bits
    System.out.println("Subnet Mask: " + learnSubnet());
    System.out.println("Network Address: " + learnNetworkAddres());
    System.out.println("Host number: " + learnHostNumber() + "\n");
  }

  //Initiate the chat methods
  public static Boolean checkChatKey(String[] input) {
    int[] high = {224, 0, 0 , 0};
    int[] low = {239, 255, 255, 255};
    Boolean check = false;
    for(int i = 0; i < input.length; i++) {
      if(isBetween(low[i], high[i], Integer.parseInt(input[i]))) {
        check = true;
      } else {
        check = false;
        break;
      }
    }
    return check;
  }
  public static void askForDetails(PrivateChat aChat) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String nicknameInput = "";
    String chatKeyInput = "";
    int chatPortInput = 0;

    System.out.print("Please enter a nickname: ");
    try {
      nicknameInput = reader.readLine();
      aChat.chatNickname = nicknameInput;
      System.out.println("Nickname is set to " + nicknameInput + "\n");
    } catch(IOException e) {
      System.out.println("There was an error reading the nickname. Exiting...");
      System.exit(0);
    }

    System.out.print("Please enter a chat key IP: ");
    try {
      chatKeyInput = reader.readLine();
      //Make sure chat key is an IP first then check
      InetAddress addr = InetAddress.getByName(chatKeyInput);
      //Check if chat key is in range
      String[] extractedChat = chatKeyInput.split("\\.");
      Boolean chatKeyStatus = checkChatKey(extractedChat);
      if(chatKeyStatus == true) {
        aChat.chatGroupKey = chatKeyInput;
        System.out.println("Chat key entered was: " + chatKeyInput + "\n");
      } else {
        System.out.println("Not a valid range. Must be between 239.255.255.255 and 244.0.0.0. Exiting...");
        System.exit(0);
      }
    } catch(IOException e) {
      System.out.println("Chat key is invalid. Please check: Chat key (IP Address).");
      System.exit(0);
    }

    System.out.print("Please enter a chat port number: ");
    try {
      chatPortInput = Integer.parseInt(reader.readLine());
      if((chatPortInput >= 4000) && (chatPortInput <= 4010)) {
        aChat.chatPortNumber = chatPortInput;
        System.out.println("Port number is set to " + String.valueOf(chatPortInput) + "\n");
      } else {
        System.out.println("Port number must be in the range of 4000 - 4010. Exiting...");
        System.exit(0);
      }
    } catch(IOException e) {
      System.out.println("I/O Error: Could not handle the input. Exiting...");
      System.exit(0);
    } catch(NumberFormatException e) {
      System.out.println("Port number error: Port number needs to be an Integer. Exiting...");
      System.exit(0);
    }
  }
  public static void findOthersOnline() {
    int hostCount = 0;
    String selfNetwork = learnNetworkAddres();
    int hostNumber = Integer.parseInt(learnHostNumber()); //Changeable
    //To the left
    System.out.println("Looking for connectable peers to hosts left side.");
    for(int i = hostNumber - 1; i >= hostNumber - 11; i--) { // -1: Cannot ping for self
      //Find reachable hosts on this IP
      //If reachable add to list
      String tempIP = learnNetworkAddres();
      tempIP += "." + i;
      try {
        InetAddress searchIP = InetAddress.getByName(tempIP);
        if(searchIP.isReachable(500)) { //Give a good timeout period
          //If is part of group
            System.out.println(tempIP + " is reachable. Can add to list.");
            hostCount++;
            //Add to a list
          //else
            System.out.println(tempIP + " is reachable, but incorrect group key.");
        } else {
          System.out.println(tempIP + " is not reachable.");
        }
      } catch(SocketException e) {
        System.out.println("Socket error: Cannot connect to host");
      } catch(IOException e) {
        System.out.println("IO Error: Could not handle I/O");
      }
    }

    //Break line to make it look nice - Not really required
    System.out.println("");

    //To the right
    System.out.println("Looking for connectable peers to hosts right side.");
    for(int i = hostNumber + 1; i <= hostNumber + 11; i++) { //+1: Cannot ping for self
      //Find reachable hosts on this IP
      //If reachable add to list
      String tempIP = learnNetworkAddres();
      tempIP += "." + i;
      try {
        InetAddress searchIP = InetAddress.getByName(tempIP);
        if(searchIP.isReachable(500)) {
          System.out.println(tempIP + " is reachable. Can add to list.");
          hostCount++;
          //Add to list?
        } else {
          System.out.println(tempIP + " is not reachable.");
        }
      } catch(SocketException e) {
        System.out.println("Socket error: " + "Cannot connect.");
      } catch(IOException e) {
        System.out.println("IO Error.");
      }
    }

    System.out.println(String.valueOf(hostCount) + " has been found and can chat.");
    //Break line to make it look nice - Not really required
    System.out.println("");
  }

  public static void main(String[] args) {
    PrivateChat selfChat = new PrivateChat();

    //This is used to save time instead of actually asking for details
    selfChat.chatPortNumber = 4000;
    selfChat.chatGroupKey = "225.225.225.225";
    selfChat.chatNickname = "X-MBP(UP)";

    System.out.println("Chat program loaded. Learning about self...");
    learnSelf();
    System.out.println("Starting up the chat...");
    //askForDetails(selfChat);
    findOthersOnline();

    //Start chat
    Thread recieverThread = new Thread(new MessageReciever(selfChat.chatPortNumber, selfChat.chatGroupKey, selfChat.chatNickname));
    Thread senderThread = new Thread(new MessageSender(selfChat.chatPortNumber, selfChat.chatGroupKey, selfChat.chatNickname));
    recieverThread.start();
    senderThread.start();
    //Start fileserver
  }
}
