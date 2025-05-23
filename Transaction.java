import java.util.ArrayList;

public class Transaction extends Message {
    public Transaction(ClientHandler sender, ArrayList<ClientHandler> reciever) {
        super(sender, reciever);
    }
}
