public class Bid implements Comparable<Bid>{
    private Player player;
    private Team team;

    private int timeLeft;

    private int contractLength;
    private int contractTotal;

    public Bid(Player player, Team team, int contractLength, int contractTotal) {
        this.player = player;
        this.team = team;
        this.contractLength = contractLength;
        this.contractTotal = contractTotal;

        timeLeft = 4;
    }

    public Bid() {
        timeLeft = 4;
    }

    public Team getTeam() { return team; }
    public int getTimeLeft() { return timeLeft; }

    /*
     * Returns true if the time to bid on a player has ran out
     */

    public boolean updateDay() {
        timeLeft -= 1;
        return (timeLeft == 0);
    }

    public Player getPlayer() { return player;} 

    public double contractScore() {
        double offer = contractTotal;

        if (team == null) {
            return 0;
        }

        if (player.getPreviousTeam().equals(team.getName())) {
            offer = offer * 1.2;
        }

        if (contractLength == 1) {
            return contractTotal;
        }
        if (contractLength == 2) {
            return contractTotal / 1.75;
        }
        if (contractLength == 3) {
            return contractTotal / 2.25;
        }
        return contractTotal / 2.75;
    }

    /*
     * This method is under the impression that this bid is the oldest bid
     * Bid o is assumed to be submitted after this bid
     */

    @Override
    public int compareTo(Bid newBid) {
        Double scoreComp = newBid.contractScore() - contractScore();
        if (scoreComp > 0) {
            return 1;
        } 
        return -1;
    }
}
