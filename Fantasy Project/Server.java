import java.io.*;
import java.net.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Server {

    private ArrayList<Team> teams;
    private HashMap<Integer, Team> teamMap;

    private ArrayList<Player> players;
    private HashMap<Integer, Player> playerMap;

    private ArrayList<DraftPick> picks;
    private HashMap<Integer, DraftPick> pickMap;

    private HashMap<Integer, ArrayList<Bid>> bidMap = new HashMap<>();
    private ArrayList<Bid> bids = new ArrayList<>();

    public Server() {
        teams = new ArrayList<>();
        teamMap = new HashMap<>();


        for (int i = 0; i < 12; i++) {
            Team team = new Team(50, 200);

            teams.add(team);
            teamMap.put(team.getTeamID(), team);
        }

        players = new ArrayList<>();
        playerMap = new HashMap<>();

        fillPlayers();
    }

    public static void main(String[] args) {    
        
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                new Thread(() -> handleClient(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {

        Server server = new Server();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);

                server.interpretInputs(line);
            }

            System.out.println("Client disconnected: " + socket.getInetAddress());

        } catch (IOException e) {
            System.out.println("Client disconnected: " + socket.getInetAddress());
        }
    }

    /***
     * private void handleInputs(String line) {
        
        while (true) {
            if (line.hasNextLine()) {

                String nextLine = line.nextLine();

                if (nextLine.equals("quit")) {
                    break;
                }

                try {
                    line.interpretInputs(nextLine);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Input not recognized");
                }
            }
        }
    }
    ***/

    /*
     * There are serveral types of inputs the system could be recieving
     *  1. Roster updates for a team (*)
     *  2. Adds/drops for a team
     *  3. Trades
     *  4. Trade requests
     *  5. Incoming Bids
     *  6. Daily Bid updates
     *  7. Draft picks
     *  8. Extensions
     *  9. Routine Progress Save (*)
     *  10. Contract restructure
     * 
     * This list will most likely never be complete, and will continue to grow over time as new features are added
     * 
     * ALL INPUTS SHOULD BE VALID BEFORE BEING SENT
     * THIS WILL NOT HANDLE INVALID INPUTS
     * 
     * (*) indicates that a feature has not been implemented yet
     */

    private String interpretInputs(String input) {
        String[] inputs = input.split(";");

        String inputType = inputs[0];

        if (inputType == "Roster") {
            teams.get(0).printRoster();
        }

        if (inputType == "6") {
            updateDailyBids();
            return null;
        }
        if (inputType == "9") { // Infrastructure not complete yet, will do nothing for now
            return null;
        }

        int actingTeamID = Integer.parseInt(inputs[1]);
        Team actingTeam = teamMap.get(actingTeamID);

        if (inputType != "3" || inputType != "4") {
            int playerID = Integer.parseInt(inputs[2]);
            Player player = playerMap.get(playerID);

            if (inputType == "2") {
                if (actingTeam.getRoster().contains(player)) {
                    actingTeam.remove(player); // need to add a feature to add dead money into budget
                    System.out.println("Successfully dropped player");
                } else if (player.getTeam() == null) {
                    actingTeam.add(player);
                    System.out.println("Successfully added player");
                } else {
                    System.out.println("Player was attempted to be added incorrectly");
                }
            } else if (inputType == "5") {
                int years = Integer.parseInt(inputs[3]);
                int total = Integer.parseInt(inputs[4]);

                createBid(player, actingTeam, years, total);
            } else if (inputType == "7") {
                int years = Integer.parseInt(inputs[3]);
                int total = Integer.parseInt(inputs[4]);

                actingTeam.draft(player, years, total);
            } else if (inputType == "8") {
                actingTeam.extend(player);
            } else if (inputType == "10") {

                int years = player.getContract().getLength();
                int[] newContract = new int[years];
                
                for (int i = 0; i < years; i++) {
                    newContract[i] = Integer.parseInt(inputs[3 + i]);
                }

                actingTeam.restructureContract(player, newContract);
            }
        } 
        else if (inputType == "4") { // Needs to be fixed anyway, right now it is a copy of completed trades

            int recievingTeamID = Integer.parseInt(inputs[3]);
            Team recievingTeam = teamMap.get(recievingTeamID);

            int i = 4;
            int playerID; 
            Player player;

            while (true) {
                try {
                    playerID = Integer.parseInt(inputs[i]);
                    player = playerMap.get(playerID);
                    
                    if (player.getTeamID() == actingTeamID) {
                        recievingTeam.tradeAway(player, actingTeam);
                    } else if (player.getTeamID() == recievingTeamID) {
                        actingTeam.tradeAway(player, recievingTeam);
                    } else {
                        System.out.println("Invalid trade executed");
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }

        } else if (inputType == "3") {
            int recievingTeamID = Integer.parseInt(inputs[3]);
            Team recievingTeam = teamMap.get(recievingTeamID);

            int i = 4;
            int playerID; 
            Player player;
            DraftPick pick;

            while (true) {
                try {
                    playerID = Integer.parseInt(inputs[i]);

                    if (playerID < 0) {
                        pick = pickMap.get(playerID * -1);

                        if (pick.getCurrentTeam().equals(actingTeam)) {
                            actingTeam.sendPick(pick, recievingTeam);
                        } else if (pick.getCurrentTeam().equals(recievingTeam)) {
                            recievingTeam.sendPick(pick, actingTeam);
                        } else {
                            System.out.println("Invalid trade executed");
                        }
                    } else {
                        player = playerMap.get(playerID);
                    
                        if (player.getTeamID() == recievingTeamID) {
                            recievingTeam.tradeAway(player, actingTeam);
                        } else if (player.getTeamID() == actingTeamID) {
                            actingTeam.tradeAway(player, recievingTeam);
                        } else {
                            System.out.println("Invalid trade executed");
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        return null;
    }

    private void createBid(Player player, Team team, int length, int total) {

        Bid newBid = new Bid(player, team, length, total);

        for (Bid bid : bids) {
            if (bid.getPlayer().equals(player)) {
                if (bid.compareTo(newBid) == 1) {
                    if (bid.getPlayer().addBid(team) == -1) {
                        System.out.println("Team has already submitted two many bids to this player.\n" + 
                                            "They are fed up with an inability to submit a proper offer sooner.");
                        return;
                    }

                    // getting rid of all of the references of the bid in both the list and map
                    bids.remove(bid);
                    bidMap.get(bid.getTimeLeft()).remove(bid);

                    // adding new bids to both the list and the map
                    bids.add(newBid);
                    bidMap.get(4).add(newBid);

                } else {
                    System.out.println("The player has a better offer that has already been submitted");
                }
                return;
            }
        }

        bids.add(newBid);
        bidMap.get(4).add(newBid);
    }

    private void updateDailyBids() {

        for (Bid bid : bidMap.get(1)) {
            bid.getTeam().add(bid.getPlayer());
            bids.remove(bid); // this should remove all old bids
        }
        bidMap.put(1, bidMap.get(2));
        bidMap.put(2, bidMap.get(3));
        bidMap.put(3, bidMap.get(4));
        bidMap.put(4, new ArrayList<>());
    }

    public void addPicks(int year, int rounds) {

        DraftPick newPick;
        for (Team team : teams) {
            for (int i = 1; i < rounds + 1; i++) {
                newPick = new DraftPick(i, team, year);

                team.addDraftPicks(newPick);
            }
        }
    }

    public void fillPlayers() {
        String filepath = "sample_players.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean firstLine = true; // to skip header
            while ((line = br.readLine()) != null) {
                if (firstLine) { 
                    firstLine = false; 
                    continue; // skip header row
                }
                String[] values = line.split(",");

                Player player = new Player(values[0], values[1], values[2], values[3], values[4], values[5], Integer.parseInt(values[6]), values[7]);

                players.add(player);
                playerMap.put(player.getPlayerID(), player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
