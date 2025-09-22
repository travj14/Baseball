public class Contract {

    private Player player;
    private int initialLength;
    private int initialTotal;
    private int yearOfTotal;
    private int[] payoutStructure;
    private int extensionYears;

    /*
     * Returns true if the contract has been paid out in full
     */

    public boolean advanceYear() {
        yearOfTotal++;
        return (yearOfTotal > initialLength);
    }

    public int getYearlyOwed() {
        return payoutStructure[yearOfTotal - 1];
    }

    public Player getPlayer() {
        return player;
    }

    public int getLength() { return initialLength; }
    public int getYear() { return yearOfTotal; }

    public boolean extensionEligible() {
        return (yearOfTotal == initialLength);
    }

    public void extend() {

        int extensionAmount;

        if (extensionYears == 0) {
            extensionAmount = Math.ceilDiv(initialTotal, initialLength) + 6;
        } else {
            extensionAmount = payoutStructure[initialLength - 1] + 6;
        }

        extensionYears++;
        initialLength++;

        int[] temp = payoutStructure;
        payoutStructure = new int[initialLength];

        for (int i = 0; i < initialLength - 1; i++) {
            payoutStructure[i] = temp[i];
        }
        payoutStructure[initialLength - 1] = extensionAmount;
    }

    public Contract(Player player, int years, int total) {
        this.player = player;
        this.initialLength = years;
        this.initialTotal = total;
        yearOfTotal = 1;

        payoutStructure = new int[years];

        for (int i = 0; i < years; i++) {
            payoutStructure[i] = years / total;

            /* 
             * In the case that years / total is not an integer, 
             * we must slightly front load the contract by adding 1 to salary for the
             * first years % total of the contract.
             * 
             * We achieve this by the equation
             */

            if (i < years % total) {
                payoutStructure[i]++;
            }
        }
    }

    public void setPayout(int[] payout) throws IllegalArgumentException {

        for (int i = 0; i < yearOfTotal - 1; i++) {
            if (payoutStructure != payout) {
                throw new IllegalArgumentException();
            }
        }

        payoutStructure = payout;

    }


    
}
