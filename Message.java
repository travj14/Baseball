import java.util.ArrayList;

public class Message {
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

    public void addMessage(String message) {
        this.message = message;
    }
}
