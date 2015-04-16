/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.core;

/**
 *
 * @author Hellz
 */
public class Blackjack {
        
    int betTracker =0;
    int stakeTracker = 1500;
    
    
    
    
    public int updateBet(int addedBet) {
        betTracker = addedBet + betTracker;
        updateStake(addedBet);
        return betTracker;
    }
    public int resetBet() {
        betTracker = 0;
        return betTracker;
    }
    public int getBet() {
        return betTracker;
    }
    private void updateStake(int stakeChange) {
       stakeTracker =  stakeTracker - stakeChange;
    }
    public int getStake() {
        return stakeTracker;
    }
}
