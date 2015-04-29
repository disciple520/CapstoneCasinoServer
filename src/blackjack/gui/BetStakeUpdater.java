/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

/**
 *
 * @author Hellz
 */
public class BetStakeUpdater {
        
    int betTracker = 0;
    int stakeTracker = 1500;
    
    
    
    
    public int updateBet(int addedBet) {
        if(betTracker <= stakeTracker)
        {
            betTracker = addedBet + betTracker;
            return betTracker;
        }
        else {
            betTracker = stakeTracker;
            return betTracker;
        }
    }
    public int resetBet() {
        betTracker = 0;
        return betTracker;
    }
    public int getBet() {
        return betTracker;
    }
    public int updateStake() {
       int tempBet = getBet();
       stakeTracker =  stakeTracker - tempBet;
       if(stakeTracker <= 0){
           stakeTracker = 1499;
       }
       return stakeTracker;
    }
    public int getStake() {
        return stakeTracker;
    }
    public void setStake(int stake) {
        stakeTracker = stake;
    }
    public void setBet(int bet) {
        betTracker = bet;
    }
    public void nonZeroBet(){
        if (getBet() ==0)
        {
            setBet(1);
            System.out.println("Can't have 0 bet, defaulting to 1");
        }
    }
}
