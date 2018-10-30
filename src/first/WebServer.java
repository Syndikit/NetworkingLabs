package first;

import java.net.*;
import java.io.*;
import java.util.*;

/*
https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd
*/

public final class WebServer {

    static final File WEB_ROOT= new File(".");

    // Will be passed to the Thread's constructor
    static final class HttpRequest implements Runnable {

        String content;

        // CRLF is the carriage return and line feed mandated by HTTP
        final static String CRLF = "\r\n";
        Socket socket;

        HttpRequest(Socket socket) throws Exception{

            this.socket = socket;
        }

        // Part of the Runnable interface
        @Override
        public void run() {

            try {
                processRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private String contentType(String fileName) {

            if (fileName.endsWith(".htm") || fileName.endsWith(".html")){
                content = "text/html";
                return content;
            }
            else if (fileName.endsWith(".txt")){
                content = "text/plain";
                return content;
            }

            else if (fileName.endsWith(".gif")){
                content = "image/gif";
                return content;
            }

            else if (fileName.endsWith(".png")){
                content = "image/png";
                return content;
            }
            else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")){
                content = "image/jpeg";
                return content;
            }

            return content;

        }

        private void processRequest() throws Exception{

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            DataOutputStream dos = new DataOutputStream(os);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String requestLine = br.readLine();
            System.out.println(CRLF);
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
                statusLine = "HTTP/1.1 200 OK";
                contentTypeLine = "Content-Type: " + contentType(fileName);
                entityBody = fis.toString();
                System.out.println(contentTypeLine);
            }else{
                statusLine = "404 Not Found";
                contentTypeLine = null;
                entityBody = "404.html";
            }
            // Provides a terminator called end, which is a blank line
            String end = " ";

            // Converts the content type and status line to byte arrays and writes to output stream
            dos.writeBytes(statusLine);
            dos.writeBytes(contentTypeLine);
            dos.writeBytes(entityBody);
            dos.writeBytes(end);

            if(fileExists){
                sendBytes(fis, dos);
                fis.close();
            }else{
                String errorMessage = "File Not Found";
                dos.writeBytes(errorMessage);
            }
            // Closes all of our streams, readers, and socket.
            os.close();
            dos.close();
            br.close();
            socket.close();
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
            // Test port
            int port = 6789;

            ServerSocket webServer = new ServerSocket(port);

            while (true) {
                Socket connectedSocket = webServer.accept();
                HttpRequest request = new HttpRequest(connectedSocket);

                Thread thread = new Thread(request);
                // Indirectly calls the run() of the HttpRequest object request in a new thread.
                thread.start();
            }

        } catch(
                IOException e)

        {
            e.printStackTrace();
        }

    }


}
