import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Comparable<Player> {

    private static int playersCreated = 0;

    private int playerID;
    private String firstName;
    private String lastName;
    private String suffix = null;

    public int getPlayerID() { return playerID; }
    public String getName() {
        if (suffix == null) {
            return (firstName + " " + lastName);
        }
        return (firstName + " " + lastName + " " + suffix);
    }
    public void setName(String firstName, String lastName, String suffix) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.suffix = suffix;
    }
    public void setName(String firstName, String lastName) {
        setName(firstName, lastName, null);
    }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getSuffix() { return suffix; }

    private int age;
    private String height;
    private String weight;
    private String position;

    private String NFLTeam;
    private Team team;
    private String previousTeam;

    private int draftRound;
    private Team originalDraftTeam;
    private int draftYear;
    private int pickInRound;


    public int getAge() { return age; }
    public String getHeight() { return height; }
    public String getWeight() { return weight; }
    public String getPos() { return position; }
    public String getNFLTeam() { return NFLTeam; }
    public Team getTeam() { return team;}
    public int getTeamID() { return team.getTeamID(); }
    public String getPreviousTeam() { return previousTeam; }

    public void setAge(int age) { this.age = age; }
    public void setHeight(String height) { this.height = height; }
    public void setWeight(String weight) { this.height =  weight; }
    public void setPos(String pos) { this.position = pos; }
    public void setNFLTeam(String NFLTeam) { this.NFLTeam = NFLTeam; }
    public void setTeam(Team team) { this.team = team;}
    public void setPreviousTeam(String previousTeam) { this.previousTeam = previousTeam; }

    public void setDraftRound(int draftRound) { this.draftRound = draftRound; }
    public void setOriginalDraftTeam(Team originalDraftTeam) { this.originalDraftTeam = originalDraftTeam; }
    public void setDraftYear(int draftYear) { this.draftYear = draftYear; }
    public void setPickInRound(int pickInRound) { this.pickInRound = pickInRound; }

    public int getDraftRound() { return draftRound; }
    public Team getOriginalDraftTeam() { return originalDraftTeam; }
    public int getDraftYear() { return draftYear; }
    public int getPickInRound() { return pickInRound; }

    public Player(ArrayList<Object> profile) {
        this();
    }

    public Player() {
        playersCreated++;
        playerID = playersCreated;
    }

    public Player(String first, String last, String suf, String pos, String height, String weight, int age, String team) {
        firstName = first;
        lastName = last;
        suffix = suf;
        position = pos;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.NFLTeam = team;


    }
    private int experience;
    private int contractLength;
    private int yearsLeft;
    private int moneyOwed;
    private int[] payoutStructure;

    private Contract contract;

    public Contract getContract() { return contract; }
    public boolean extensionEligible() { return contract.extensionEligible(); }
    public void giveExtension() { contract.extend(); }

    public void newContract(Contract contract) {
        
    }
    
    public int getExperience() {
        return experience;
    }
    public int getContractLength() {
        return contractLength;
    }
    public int getYearsLeft() {
        return yearsLeft;
    }
    public int getMoneyOwed() {
        return moneyOwed;
    }
    public int[] getPayoutStructure() {
        return payoutStructure;
    }
    public int getCurrentSalary() {
        int relativeYear = contractLength - yearsLeft - 1;
        return payoutStructure[relativeYear];
    }

    private int currentOfferTotal;
    private int currentOfferYears;
    private String currentOfferTeam;

    private HashMap<String, Integer> teamBids = new HashMap<>();

    /*
     * Returns 1 if a team is eligible to put a bid on said player
     * Returns -1 if a team has put too many bids on a player
     */

    public int addBid(Team team) {
        if (!teamBids.containsKey(team.getName())) {
            teamBids.put(team.getName(), 1);
            return 1;
        }
        if (teamBids.get(team.getName()) == 3) {
            return -1;
        }
        teamBids.put(team.getName(), teamBids.get(team.getName()) + 1);
        return 1;
    }

    public String getCurrentOffer()  {
        return ("$" + currentOfferTotal + " over " + currentOfferYears + " years.");
    }

    public double getCurrentOfferScore() {
        double offer = currentOfferTotal;

        if (currentOfferTeam == null) {
            return 0;
        }

        if (previousTeam.equals(currentOfferTeam)) {
            offer = offer * 1.2;
        }

        if (currentOfferYears == 1) {
            return currentOfferTotal;
        }
        if (currentOfferYears == 2) {
            return currentOfferTotal / 1.75;
        }
        if (currentOfferYears == 3) {
            return currentOfferTotal / 2.25;
        }
        return currentOfferTotal / 2.75;
    }
    @Override
    public int compareTo(Player o) {
        if (playerID == ((Player) o).getPlayerID()) {
            return 0;
        }
        return 1;
    }
}
