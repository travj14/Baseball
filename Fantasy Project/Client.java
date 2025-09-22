import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String host = "147.93.190.250"; // Contabo server IP
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server");
            String userInput;
            while ((userInput = console.readLine()) != null) {

                out.println(userInput);
                System.out.println("Server: " + in.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
