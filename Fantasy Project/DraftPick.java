public class DraftPick {

    private static int picksCreated = 0;

    private int pickID;
    private int draftRound;
    private Team originalDraftTeam;
    private int draftYear;
    private int pickInRound;

    private Team currentTeam;

    public DraftPick(int round, Team team, int year) {
        picksCreated++;

        pickID = picksCreated;

        draftRound = round;
        originalDraftTeam = team;
        draftYear = year;
    }

    public int getPickID() {
        return pickID;
    }
    public Team getCurrentTeam() {
        return currentTeam;
    }

    public void setPick(int pickNo) {
        pickInRound = pickNo;
    }

    public Team getOriginalTeam() {
        return originalDraftTeam;
    }

    public void setNewTeam(Team team) {
        currentTeam = team;
    }

    public String printPick() {
        if (pickInRound == 0) {
            String output = "" + draftYear + " Round " + draftRound;

            if (!currentTeam.equals(originalDraftTeam)) {
                output = output + " (" + originalDraftTeam + ")";
            }

            return output;
        }
        return "" + draftYear + " " + draftRound + "." + pickInRound;
    }
}
