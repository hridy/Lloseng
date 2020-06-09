// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {

  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private boolean serverOpen;
  private boolean serverRunning;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) {
    super(port);
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {
    String message = client.getInfo("Login ID") + " has joined at " + getPort();
    this.sendToAllClients(message);
    System.out.println(message);
  }

  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    String message = client.getInfo("Login ID") + " has left at " + getPort();
    this.sendToAllClients(message);
    System.out.println(message);
  }

  @Override
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
    String message = client.getInfo("Login ID") + " has left at " + getPort();
    this.sendToAllClients(message);
    System.out.println(message);
  }
  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    if (msg.toString().startsWith("#login")){
      String[] index = msg.toString().split(" ");
      client.setInfo("Login ID", index[1]); 
      System.out.println(msg);
      this.sendToAllClients(client.getInfo("Login ID") + " has connected!");
    }
    else{
      System.out.println(client.getInfo("Login ID") + ": " + msg);
      this.sendToAllClients(client.getInfo("Login ID") + ": " + msg);
    }
  }

  public void handleMessageFromServerUI(String message) {

    //System.out.println("SERVER MSG > " + message);
    if (message.startsWith("#")) {
      String[] command = message.split(" ");
      String input = command[0];
      switch (input) {
        case "#quit":
          System.out.print("Server is quitting...");
          System.exit(0);
          break;

        case "#stop":
          this.stopListening();
          System.out.print("Server has stopped listening...");
          break;

        case "#close":
          try {
            this.sendToAllClients("Server is disconnecting...");
            this.close();
          } 
          catch (IOException e) {}
          break;

        case "#setport":
          if (!serverOpen)
            this.setPort(Integer.parseInt(command[1]));
          else
            System.out.println("Only allowed is server is closed...");
          break;

        case "#start":
          if (!serverRunning) {
            try {
              this.listen();
            } catch (IOException e) {
            }
          } else
            System.out.println("Only valid if server is stopped...");
          break;

        case "#getport":
          System.out.println("Port: " + this.getPort());
          break;
      }
    } else {
      this.sendToAllClients("SERVER MSG> " + message);
    }
  }

  /**
   * This method overrides the one in the superclass. Called when the server
   * starts listening for connections.
   */
  protected void serverStarted() {
    serverOpen = true;
    serverRunning = true;
    System.out.println("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called when the server stops
   * listening for connections.
   */
  protected void serverStopped() {
    serverRunning = false;
    System.out.println("Server has stopped listening for connections.");
  }

  /**
   * Hook method called when the server is clased. The default implementation does
   * nothing. This method may be overriden by subclasses. When the server is
   * closed while still listening, serverStopped() will also be called.
   */
  protected void serverClosed() {
    serverOpen = false;
  }
  // Class methods ***************************************************
}
// End of EchoServer class
