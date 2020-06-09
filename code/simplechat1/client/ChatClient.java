// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;
import common.*;
import ocsf.client.*;
import ocsf.server.ConnectionToClient;

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param clientID The ID of the chat client
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String clientID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
    sendToServer("#login " + "<" + clientID + ">");
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if (message.startsWith("#")){
      String[] command = message.split(" ");
      String input = command[0];
      switch (input){
        case "#quit":
          System.exit(0);
          break;
        
        case "#logoff":
          try {
            closeConnection();
          } 
          catch (IOException e) { // required to catch the exception as it is thrown in AbstractClient
            System.out.println("Cannot close client connection, try again." );
          }
          break;
        
        case "#sethost":
          if (this.isConnected())
            System.out.println("#sethost: Client is already connected");
          else
            this.setHost(command[1]);
          break;
        
        case "#setport":
          if (this.isConnected())
            System.out.println("#setport: Client is already connected");
          else
            this.setPort(Integer.parseInt(command[1]));
          break;
        
        case "#login":
          if (this.isConnected())
            System.out.println("#login: Client is already connected");
          else
            try{
              this.openConnection();
            } catch(IOException e){}
          break;

        case "#gethost":
            System.out.println("Host: " + this.getHost());
            break;
          
        case "#getport":
            System.out.println("Port: " + this.getPort());
            break;
      }
    }
    else{
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display ("Could not send message to server.  Terminating client.");
        quit();
      }
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  public void connectionClosed(){
    System.out.println("E5a: Connection to Server lost");
  }
}
//End of ChatClient class
