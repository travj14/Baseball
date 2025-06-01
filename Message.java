import java.util.ArrayList;

public class Message {
    private boolean read;

    private boolean starred;

    private ClientHandler sender;
    private static ArrayList<ClientHandler> reciever;
    private String message;

    public Message(ClientHandler sender, ArrayList<ClientHandler> reciever) {
        this.sender = sender;
        this.reciever = new ArrayList<>();

        for (ClientHandler client: reciever) {
            if (!sender.equals(client)) {
                this.reciever.add(client);
            }
        }
    }

    public Message(ClientHandler sender, ArrayList<ClientHandler> reciever, String message) {
        this(sender, reciever);
        setMessage(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void print() {
        System.out.println(message);
    }

    public String getMessage() {
        return message;
    }
}
