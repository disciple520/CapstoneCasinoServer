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
        betTracker = addedBet + betTracker;
        return betTracker;
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
       return stakeTracker;
    }
    public int getStake() {
        return stakeTracker;
    }
}
