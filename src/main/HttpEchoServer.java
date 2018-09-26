import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;


public class HttpEchoServer {

    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        new HttpEchoServer(8080);
    }

    public HttpEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        Thread thread = new Thread(() -> runServer());
        thread.start();
        System.out.println("Nå kjører serveren på port " + getPort()+ "!");
    }

    private void runServer(){
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                String uri = readLine(socket).split(" ")[1];

                if(!uri.equals("/favicon.ico")){


                    Map<String, String> parameters = readParameters(uri);

                    String statusCode = parameters.get("status");
                    if(statusCode == null) {
                        statusCode = "200";
                    }
                    String body = parameters.get("body");
                    if(body == null) {
                        body = "We did it!";
                    }
                    String location = parameters.get("Location");

                    socket.getOutputStream().write(("HTTP/1.1 " + statusCode + " OK\r\n").getBytes());
                    socket.getOutputStream().write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
                    if (location != null) {
                        socket.getOutputStream().write(("Location: " + location + "\r\n").getBytes());
                    }
                    socket.getOutputStream().write("Server: Kristiania Java Server!!\r\n".getBytes());
                    socket.getOutputStream().write(("Content-Length: " + body.length() + "\r\n").getBytes());
                    socket.getOutputStream().write("\r\n".getBytes());
                    socket.getOutputStream().write((body + "\r\n").getBytes());
                    socket.getOutputStream().flush();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> readParameters(String uri) {
        return new HttpPath(uri).getQuery().getParameters();
    }

    private String readLine(Socket socket) throws IOException {
        StringBuilder requestLine = new StringBuilder();

        int c;
        while ((c = socket.getInputStream().read()) != -1) {
            if(c == '\r') {
                break;
            }
            requestLine.append((char)c);
        }

        return requestLine.toString();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }
}