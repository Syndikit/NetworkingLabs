package second;
import java.io.*;
import java.net.*;   
import java.util.*;   

public class PingClient {   
  
   
  public static void main(String[] args)throws Exception {   
    
    long totalrtt = 0;
    long maxrtt = -9999;
    long minrtt = 9999;
    int drops = 0;
    int retPacket;
    
    if (?) {  // check if number of arguments are correct
      System.out.println("Required arguments: host port");   
      return;   
    }   
    String server = ?;   // Read first argument from user 
    String serport = ? // Read second argument from user
    int serverPort = Integer.parseInt(serport);   
    
    DatagramSocket socket = ?; // Create new datagram socket
    ?; // Set socket timeout value. Read API for DatagramSocket to do this

    InetAddress serverAddress = ?; //Convert server to InetAddress format; Check InetAddress API for this
    byte[] sendData = new byte[1024];   
    byte[] receiveData = new byte[1024];   
    
    for(int i = 0; i < 10; i++) {  
      Long time = new Long(System.currentTimeMillis());
      String payload = ?; // Construct data payload for PING as per the instructions
      sendData = ?; // Convert payload into bytes
      DatagramPacket packet = new DatagramPacket(?);    // Create new datagram packet
      ?; // send packet
      
      DatagramPacket reply = ?; // Create datagram packet for reply
      
      try {
        ?; wait for incoming packet reply
        byte[] buf = reply.getData();          
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);   
        InputStreamReader isr = new InputStreamReader(bais);   
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine();   
        
	  // Parse incoming string "line"

	  // extract packet sequence number into the variable retPacket
        retPacket = ?
        
        if (retPacket != i) {   
          System.out.print("Received out of order packet");
          System.out.println();
        }
	  else {
	    System.out.println("Received from " + request.getAddress().getHostAddress() + " ," + new String(line)); 
          System.out.println();
          long rtt = ?;  // calculate roundtrip time
          // calculate total, max and min rtt
        }        
      } 
      catch(SocketTimeoutException e) {
        System.out.println("Error: Request timed out");
        drops++;
      }   
    } 
    long avgrtt = ?; calculate average rtt
    
    // print and store average, max, min rtt and drops
  }    
}   