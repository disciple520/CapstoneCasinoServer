// Pulled from https://code.google.com/p/blackjack-school-project/

package blackjack.core;

import java.util.ArrayList;

public class Hand {
    
    public final static int INITIAL_HAND_SIZE = 2;
    public final static int BLACKJACK_VALUE = 21;

    private ArrayList<Card> hand = new ArrayList<Card>();
    
    public Hand(){
        
    }
    
    public Hand(Card card1) {
        hand.add(card1);
    }
    
    public Hand(Card card1, Card card2){
        hand.add(card1);
        hand.add(card2);
    }

    public void addCard(Card cardToAdd){
        hand.add(cardToAdd);
    }
    
    public Card getCardAtIndex(int index) {
        return hand.get(index);
    }
    
    public int getSizeOfHand(){
        return hand.size();
    }
    
    
    public boolean containsAce() {
    	for(Card card: hand) {
        	if (card.isAce()) {
        		return true; 
        	}
        }
    	return false;
    }
    
    public int numberOfAces() {
        int aceCount = 0;
        for(Card card: hand) {
        	if (card.isAce()) {
                    aceCount++;
        	}
        }
    	return aceCount;
    }
    
    public int getValue() {
        int handValue = 0;
        for(Card card: hand) {
        	handValue += card.getValue();
        }
        //System.out.println("Unadjusted hand value is " + handValue);
        if (handValue > 21) {
            //System.out.println("Hand value is greater than 21, checking for an Ace");
            if (containsAce()){
                //System.out.println("Hand contains an ace. Value being adjusted -10");
                handValue -= 10;
                if (handValue > 21) {
                    if (numberOfAces() > 1) {
                        handValue -= 10;
                        if (handValue > 21) {
                            if (numberOfAces() > 2) {
                                handValue -= 10;
                                if (handValue > 21) {
                                    if (numberOfAces() > 3) {
                                        handValue -= 10;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                //System.out.println("No aces were found. Hand is busted.");
            }
        }
                                
        return handValue;
    }
    
    public boolean isBusted(){
        return getValue() > 21;
    }
    
    public boolean isBlackjack(){
        boolean handHasAce = false;
        boolean handHasTen = false;
        if (hand.size()>1) {
            for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
                    if (hand.get(i).getRank() == Card.ACE) // Can't use containsAce() because Ace must be in first two cards for Blackjack
                            handHasAce = true;
                    if (hand.get(i).getValue() == 10)
                            handHasTen = true;
            }
        }
        return handHasAce && handHasTen;
    }
    /**
     * Removes the cards from the hand and returns them in an ArrayList
     * @return 
     * @return ArrayList containing the removed cards
     */
    public void clearHand(){
        hand.clear(); // this method used to do more (JW)
    }
}