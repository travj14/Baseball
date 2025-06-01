import java.util.ArrayList;

public class Transaction extends Message {

    private ClientHandler sender;
    private static ArrayList<ClientHandler> reciever;
    private ArrayList<Player> senderList;
    private ArrayList<Player> recieverList;

    public Transaction(ClientHandler sender, ArrayList<ClientHandler> reciever) {
        super(sender, reciever);
        senderList = null;
        recieverList = null;
    } 

    public Transaction(ClientHandler sender, ArrayList<ClientHandler> reciever, 
                        ArrayList<Player> senderList, ArrayList<Player> recieverList) {
        super(sender, reciever);
        this.senderList = senderList;
        this.recieverList = recieverList;
    }

    public void setSenderList(ArrayList<Player> senderList) {
        this.senderList = senderList;
    }

    public void setRecieverList(ArrayList<Player> recieverList) {
        this.recieverList = recieverList;
    }

    public void addToSenderList(Player player) {
        senderList.add(player);
    }

    public void addToRecieverList(Player player) {
        recieverList.add(player);
    }

    public void printTrade() {
        String output = sender.getUsername() + " will send: ";
        int senderSize = senderList.size(); 
        int recieverSize = recieverList.size();

        for (int i = 0; i < senderSize - 1; i++) {
            output += senderList.get(i).getName() + ", ";
        }
        output += senderList.get(senderSize - 1).getName() + "\n";

        for (int i = 0; i < recieverSize - 1; i++) {
            output += recieverList.get(i).getName() + ", ";
        }
        output += recieverList.get(recieverSize - 1).getName() + "\n";

        System.out.println(output);
    }
}
