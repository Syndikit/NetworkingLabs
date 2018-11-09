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
    final String CRLF = "\r\n";
    long[] timeArray = new long[10];

    if (args.length == 2) {  // check if number of arguments are correct
      System.out.println("Required arguments: host port");   
      return;   
    }   
    String server = args[0].toString();   // Read first argument from user args[0].toString();
    String serport = args[1].toString(); // Read second argument from user args[1].toString()
    int serverPort = Integer.parseInt(serport);

    InetAddress serverAddress = InetAddress.getByName(server); //Convert server to InetAddress format; Check InetAddress API for this

    DatagramSocket socket = new DatagramSocket();
    socket.connect(serverAddress,serverPort);// Create new datagram socket

    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];   
    
    for(int i = 0; i < 10; i++) {
      Long time = new Long(System.currentTimeMillis());
      // Construct data payload for PING as per the instructions
      String payload = "PING "+ i + " " + time + CRLF;
      // Convert payload into bytes
      sendData = payload.getBytes();
      // Create new datagram packet
      DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
       // send packet
      socket.send(packet);

      try {
        socket.setSoTimeout(1000);
        // Create datagram packet for reply
        DatagramPacket reply = new DatagramPacket(receiveData, receiveData.length, serverAddress, serverPort);
        socket.receive(reply);
        Long time2 = new Long(System.currentTimeMillis());

        //?; wait for incoming packet reply
        byte[] buf = reply.getData();          
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);   
        InputStreamReader isr = new InputStreamReader(bais);   
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine();
        System.out.println(line);
        
	  // Parse incoming string "line"

	  // extract packet sequence number into the variable retPacket
        retPacket = Integer.parseInt(String.valueOf(line.charAt(5)));
        
        if (retPacket != i) {
          System.out.print("Received out of order packet");
          System.out.println();
        }
	  else {
	    System.out.println("Received from " + reply.getAddress().getHostAddress() + " ," + new String(line));
          System.out.println();
          long rtt = time2-time;
          timeArray[i] = rtt;
          Arrays.sort(timeArray);
          maxrtt = timeArray[9];
          minrtt = timeArray[0];
          totalrtt += rtt; // calculate roundtrip time
          System.out.println("Total RTT: "+totalrtt + "ms");
          System.out.println("Max RTT: "+maxrtt + "ms");
          System.out.println("Min RTT: " + minrtt+ "ms");
          // calculate total, max and min rtt
        }        
      } 
      catch(SocketTimeoutException e) {
        System.out.println("Error: Request timed out");
        drops++;
      }   
    } 
    long avgrtt = totalrtt/10; //calculate average rtt
    
    // print and store average, max, min rtt and drops
  }    
}   