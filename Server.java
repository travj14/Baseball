import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {

        try {
            while (!serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();
                System.out.println("A new player has entered the game");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        } catch (IOException e) {

        }
    }

    public void closerServerSocket() {

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int roomNum;
        ServerSocket serverSocket;

        while (true) {
            System.out.println("Enter room number");
            try {
                roomNum = Integer.parseInt(scanner.nextLine());
                serverSocket = new ServerSocket(roomNum);
                break;
            } catch (Exception e) {
                System.out.println("Room number is either invalid or already exists");
            }
        }

        Server server = new Server(serverSocket);
        server.startServer();
        scanner.close();
    }

}