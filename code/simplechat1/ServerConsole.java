
import java.io.*;
import common.*;
import java.util.*;
import ocsf.server.*;

/**
 * This class constructs the UI for a server client. It implements the chat
 * interface in order to activate the display() method.
 *
 * @author Hridyansh Sharma
 * @version June 2020
 */
public class ServerConsole implements ChatIF {

  // Deafult port to connect on.
  final public static int DEFAULT_PORT = 5555;

  /**
   * The instance of the server that created this ConsoleChat.
   */
  EchoServer server;

  /**
   * Constructs an instance of the ServerConsole UI.
   * 
   * @param port The port to connect on.
   */
  public ServerConsole(int port) {
    try {
      server = new EchoServer(port);
    } 
    catch (Exception e) {
      System.out.println("Error: Can't setup connection!" + " Terminating server.");
      System.exit(1);
    }

    try{
      server.listen();
    } catch (IOException ex){
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  /**
   * This method waits for input from the console. Once it is received, it sends
   * it to the client's message handler.
   */
  public void accept() {
    try {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) {
        message = fromConsole.readLine();
        server.handleMessageFromServerUI(message);
      }
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from ServerConsole!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface. It displays a
   * message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
    System.out.println("SERVER MSG > " + message);
  }

  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    ServerConsole sv = new ServerConsole(port);
    System.out.println("Server is online at port: " + port);
    sv.accept();    
  }
}