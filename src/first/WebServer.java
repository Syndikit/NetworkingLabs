package first;

import java.net.*;
import java.io.*;
import java.util.*;

/*
https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd
*/

public final class WebServer {

    Boolean infinite = true;


    // Will be passed to the Thread's constructor
    static final class HttpRequest implements Runnable {

        // CRLF is the carriage return and line feed mandated by HTTP
        final static String CRLF = "\r\n";
        Socket socket;

        HttpRequest(Socket socket) throws Exception{

            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                processRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void processRequest() throws Exception{

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String requestLine = br.readLine();
            System.out.println();
            System.out.println(requestLine);

        }
    }


    public static void main(String[] args) throws Exception {
        try{
            int port = 6789;

            ServerSocket webServer;
            webServer = new ServerSocket(port);
            while (true) {
                Socket connectedSocket = null;
                connectedSocket = webServer.accept();
                HttpRequest request = new HttpRequest(connectedSocket);

                Thread thread = new Thread(request);
                thread.start();
            }

        } catch(
                IOException e)

        {
            e.printStackTrace();
        }

    }


}
