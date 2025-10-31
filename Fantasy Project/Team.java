import java.util.ArrayList;

public class Team {

    private static int teamsCreated = 0;

    private String teamName;
    private int teamID;
    private String ownerUserName;
    private String ownerPassword;

    private ArrayList<ArrayList<Player>> incomingTrades;
    private ArrayList<ArrayList<Player>> outgoingTrades;

    public int getTeamID() { return teamID; }
    public void OverrideTeamID(int ID) { teamID = ID; }
    public String getUserName() { return ownerUserName; }
    public String getPassWord() { return ownerPassword; }
    public void setUserName(String ownerUserName) { this.ownerUserName = ownerUserName; }
    public void setPassWord(String ownerPassword) { this.ownerPassword = ownerPassword; }

    private ArrayList<Player> roster;
    private int rosterSize;
    private int budget;

    private ArrayList<DraftPick> picks;

    private ArrayList<Contract> contracts;
    private ArrayList<Bid> outgoingBids;

    private Player[] startingRoster;
    private String[] startingPositions;
    private int maxRosterSize;
    
    public String getName() { return teamName; }
    public void setName(String name) { this.teamName = name; }

    public int getYearlyBudgetRemaining() {
        int spent = 0;
        for (int i = 0; i < rosterSize; i++) {
            spent += contracts.get(i).getYearlyOwed();
        }
        return budget - spent;
    }
    public int getFullBudget() { return budget; }

    public ArrayList<Player> getRoster() { return roster; }

    public Team(int startingRosterSize, int budget) {
        this();

        this.budget = budget;

        startingRoster = new Player[startingRosterSize];
        startingPositions = new String[startingRosterSize];
    }

    public Team() {
        roster = new ArrayList<>();
        teamsCreated++;

        teamID = teamsCreated;
    }

    public void setStartingPositions(int size, String[] positions) {
        startingPositions = positions;
        startingRoster = new Player[size];
    }
    public void setMaximumRosterSize(int maxRosterSize) {
        this.maxRosterSize = maxRosterSize;
    }

    public void draft(Player player, int years, int total) {
        roster.add(player);
        rosterSize--;
        contracts.add(new Contract(player, years, total));
    }

    /* 
     * Returns 1 if an extension is made,
     * -1 if the extension is not possible
     */

    public int extend(Player player) {
        if (!roster.contains(player) || !player.extensionEligible()) {
            return -1;
        }

        player.giveExtension();

        return 1;
    }

    public void restructureContract(Player player, int[] yearlySalary) {
        Contract contract = player.getContract();

        contracts.remove(contract);

        contract.setPayout(yearlySalary);
        
        contracts.add(contract);
        player.newContract(contract);
        
    }

    public void add(Player player) throws IndexOutOfBoundsException {
        if (rosterSize++ < maxRosterSize || maxRosterSize == 0) {
            roster.add(player);
            rosterSize++;

            return;
        }
        throw new IndexOutOfBoundsException();
    }
    public void add(Player player, Contract contract) {
        roster.add(player);
        rosterSize++;
        contracts.add(contract);
    }

    public void remove(Player player) {
        roster.remove(player);
        rosterSize--;
    }

    public void tradeAway(Player player, Team team) {
        roster.remove(player);
        contracts.remove(player.getContract());
        rosterSize--;

        team.add(player, player.getContract());
    }

    public void tradeFor(Player player, Team team) {
        roster.add(player);
        contracts.remove(player.getContract());
        rosterSize++;

        team.remove(player);
    }

    public void sendPick(DraftPick pick, Team team) {
        picks.remove(pick);

        team.addDraftPicks(pick);
    }

    public void printRoster() {
        String output = "";

        for (int i = 0; i < rosterSize; i++) {
            output += roster.get(i).getName() + "\n";
        }

        System.out.println(output);
    }

    public void addDraftPicks(DraftPick pick) {
        picks.add(pick);
    }
    

}
