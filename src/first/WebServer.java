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

            // Initializes header line
            String headerLine = null;

            // Loops through headerline until end of file. Loop terminates when the headerline reaches the empty line at file end.
            while((headerLine = br.readLine()).length() != 0){
                System.out.println(headerLine);
            }

            StringTokenizer tokens = new StringTokenizer(requestLine);
            tokens.nextToken();
            String fileName = tokens.nextToken();
            fileName = "." + fileName;

            FileInputStream fis = null;
            boolean fileExists = true;

            try{
                fis = new FileInputStream(fileName);
            }catch (FileNotFoundException e){
                e.printStackTrace();
                fileExists = false;
            }

            String statusLine = null;
            String contentTypeLine = null;
            String entityBody = null;

            if (fileExists){
                statusLine = "100";
                contentTypeLine = "Content Type: "+ contentType(contentTypeLine);
            }else{
                statusLine = "404 Not Found";
                entityBody = "404.html";
            }
            // Provides a terminator called end, which is a blank line
            String end = " ";

            // Converts the content type and status line to byte arrays and writes to output stream
            os.write(statusLine.getBytes());
            os.write(contentTypeLine.getBytes());
            os.write(end.getBytes());

            if(fileExists){
                sendBytes(fis, os);
                fis.close();
            }else{
                String errorMessage = "404.html";
                os.write(errorMessage.getBytes());
            }
            // Closes all of our streams, readers, and socket.
            os.close();
            br.close();
            socket.close();
        }

        private String contentType(String fileName) {

            String content = null;

            if (fileName.endsWith(".htm") || fileName.endsWith(".html")){
                content = "text/html";
            }
            if (fileName.endsWith(".txt")){
                content = "text/plain";
            }

            if (fileName.endsWith(".gif")){
                content = "image/gif";
            }

            if (fileName.endsWith(".png")){
                content = "text/plain";
            }
            if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")){
                content = "image/jpeg";
            }

            return content;

        }

        private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception{

            // Creates a 1KB window-size
            byte[] buffer = new byte[1024];

            int bytes = 0;

            // Copy the requested file in to the output stream
            while((bytes = fis.read(buffer)) != -1){
                os.write(buffer, 0, bytes);
            }
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
