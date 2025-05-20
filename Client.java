import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedWriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader (new InputStreamWriter(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine(); 
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine(); 
                bufferedWriter.flush();
            }
        } catch (IOException entered) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGC;

                while(socket.isConnected()) {
                    try {
                        msgFromGC = bufferedReader.nextLine();
                        System.out.println(msgFromGC);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    puiblic static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter team name");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}