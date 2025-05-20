import java.util.ArrayList;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.*;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();

            clientHandler.add(this);
            broadCastMessage("SERVER: " + clientUsername + " has entered game");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadCastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadCastMessage(String messageToSend) {
        for (ClientHandler clientHandler: clientHandler) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandler.remove(this);
        broadCastMessage("SERVER: " + clientUsername + " has left game");
    }

    public void closeEverything(Socket sockter, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}