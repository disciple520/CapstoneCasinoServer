package blackjack.gui;

/**
 *
 * @author Spencer Malovrh
 */
public class BetStakeUpdater {
        
    int betTracker = 0;
    int stakeTracker = 1500;
    
    //this add the new button presses to the existing bet
    //also makes sure you have the funded to do the bet
    public int updateBet(int addedBet) {
        if(betTracker < stakeTracker)
        {
            betTracker = addedBet + betTracker;
            return betTracker;
        }
        else {
            betTracker = stakeTracker;
            return betTracker;
        }
    }
    //sets the bet to 0
    public int resetBet() {
        betTracker = 0;
        return betTracker;
    }
    //bet getter
    public int getBet() {
        return betTracker;
    }
    //when placing bet, subtracts it from you stake
    public int updateStake() {
       stakeTracker =  stakeTracker - getBet();
       if(stakeTracker <= 0){
           stakeTracker = 1499;
       }
       return stakeTracker;
    }
    //stake getter
    public int getStake() {
        return stakeTracker;
    }
    //stake setter
    public void setStake(int stake) {
        stakeTracker = stake;
    }
    //bet setter
    public void setBet(int bet) {
        betTracker = bet;
    }
    //checks to makes sure you dont have a 0 bet
    public void nonZeroBet(){
        if (getBet() ==0)
        {
            setBet(1);
            System.out.println("Can't have 0 bet, defaulting to 1");
        }
    }
}
