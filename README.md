# LANChat
LANChat is a pure Java-made console-based chat application that allows users to chat with other users on the same local area network (LAN). The LANChat uses a UDP Multicast Socket to ensure that messages are private, but only for those who join the chat using the Chat Key (a Multicast IP address from a range 224.0.0.0 to 239.255.255.255). Users are also able to send private messages between users. This chat will also allow for encryption of messages which enables confidentially between the users - if users know thhe key they will be able to read the messages.<br /><br />


**How to start LANChat**
```
java LANChat
```

You'll be asked for a few bits of details. These parameters will be used for starting the chat and letting others identify you.
```
Username (String), Group Chat Key (Multicast Address), Port Number (Integer).
```

Once details have been retrieved, the chat program will start and send a /join request to any existing peers to let them know that you've joined. If an unknown peer sends you a message, the software will automatically send a /request command to get the name of that peer, adds them to your list of known users and eventually print out the message and identifies the sender.<br /><br />

**How to send a private message**
```
/whisper IP-address message
OR
/whisper peer-name message
```

**Still to do**
- [X] ~~Add user details to chat~~
- [ ] Initialise the chat
- [ ] Implement the /join and /request commands
- [ ] Implement the /whisper command
- [ ] Encrypt the chat
