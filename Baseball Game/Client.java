import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.*;

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
            this.bufferedReader = new BufferedReader (new InputStreamReader(socket.getInputStream()));
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
            while (socket.isConnected()) {
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
                        msgFromGC = bufferedReader.readLine();
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        AccountManager creator = new AccountManager();

        String account;
        while (true) {
            System.out.println("Login (1) or Sign up (2)?");
            account = scanner.nextLine();
            if (account.equals("1") || account.equals("2")) {
                break;
            }
            System.out.println("Invalid choice");
        }

        String username;
        while (true) {
            System.out.println("Enter username");
            username = scanner.nextLine();
            if (username.length() > 12) {
                System.out.println("Username must be 12 characters or less");
            } else {
                if (account.equals("1")) {
                    if (creator.usernameExists(username)) {
                        break;
                    }
                    System.out.println("Username not in database");
                } else {
                    if (creator.usernameExists(username)) {
                        System.out.println("Username already in use");
                    } else { 
                        break; 
                    }
                }
            }
        }

        String password;
        while (true) {
            System.out.println("Enter password");
            password = scanner.nextLine();

            if (account.equals("1")) {
                if (creator.passwordCorrect(username, password)) {
                    System.out.println("Login successful");
                    break;
                } else {
                    System.out.println("Password doesn't not match");
                }
            } else {
                if (creator.accountCreator(username, password)) {
                    System.out.println("Account created successfully");
                    break;
                }
                System.out.println("Error occured");
            }
        }

        int portNum;
        Socket socket;

        while (true) {
            System.out.println("Enter room number");
            try {
                portNum = Integer.parseInt(scanner.nextLine());
                socket = new Socket("192.168.0.249", portNum);
                break;
            } catch (Exception e) {
                System.out.println("Room number is either invalid or doesn't exist");
            }
        }

        System.out.println("Successfully entered game");
        
        
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
} 