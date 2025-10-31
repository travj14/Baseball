import java.util.ArrayList;
import java.util.HashMap;

public class Trade {
    private Team[] teams;

    private int numTeams;

    private Team[] teamsAccepted;
    private int numAccepted;

    private HashMap<Team, ArrayList<Player>> playerMap;
    private HashMap<Team, ArrayList<DraftPick>> pickMap;

    private String status;
    private Team declinedBy;

    public Trade(int numTeams, Team[] teams, HashMap<Team, ArrayList<Player>> playerMap) {
        this.numTeams = numTeams;
        this.playerMap = playerMap;
        teams = new Team[numTeams];
        teamsAccepted = new Team[numTeams];
        numAccepted = 1;

        this.teams = teams;

        teamsAccepted[0] = teams[0];

        status = "Outgoing";
    }

    public String status() {
        return status;
    }

    public void withdraw() {
        status = "Withdrawn";
    }

    public void decline(Team team) {
        declinedBy = team;
        status = "Declined by " + team.toString();
    }

    public boolean accepted(Team team) {
        for (int i = 0; i < numAccepted; i++) {
            if (team.getTeamID() == teamsAccepted[i].getTeamID()) {
                return true;
            }
        }
        return false;
    }

    public void accept(Team team) {
        if (!accepted(team)) {
            teamsAccepted[numAccepted] = team;
            numAccepted++;
        }

        if (numAccepted == numTeams) {
            completeTrade();
        }
    }

    private void completeTrade() {
        for (int i = 0; i < numTeams; i++) {
            Team team = teams[numTeams];
            ArrayList<Player> teamPlayers = playerMap.get(team);
            for (Player player: teamPlayers) {
            }
        }
    }
}
