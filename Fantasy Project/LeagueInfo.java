public class LeagueInfo {
    
    private String leagueName;
    private int numTeams;
    private int budget;
    private int rosterSizes;
    private int startN;

    public LeagueInfo(String leagueName, int numTeams, int budget, int rosterSizes, int startN) {
        this.leagueName = leagueName;
        this.numTeams = numTeams;
        this.budget = budget;
        this.rosterSizes = rosterSizes;
        this.startN = startN;
    }

    public LeagueInfo(String leagueName, String numTeams, String budget, String rosterSizes, String startN) {
        this(leagueName, Integer.parseInt(numTeams), Integer.parseInt(budget), 
                        Integer.parseInt(rosterSizes), Integer.parseInt(startN));
    }

    public String getLeagueName() { return leagueName; }
    public int getNumTeams() { return numTeams; }
    public int getBudget() { return budget; }
    public int getRosterSizes() { return rosterSizes; }
    public int getStartSize() { return startN; }
}
