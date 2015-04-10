package practice.model;

import java.util.ArrayList;

public class Hand {
    
    public final static int INITIAL_HAND_SIZE = 2;
    public final static int BLACKJACK_VALUE = 21;

    private ArrayList<Card> hand = new ArrayList<Card>();
    
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
    
    public int getValue() {
        int handValue = 0;
        for(Card card: hand) {
        	System.out.print("Card rank is " + card.getRank() + "\n");
        	handValue += card.getValue();
        }
        System.out.print("Final hand Value is " + handValue + "\n");
        return handValue;
    }
    
    public boolean isBusted(){
        return getValue() > 21;
    }
    
    public boolean isBlackjack(){
        boolean handHasAce = false;
        boolean handHasTen = false;
        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
                if (hand.get(i).getRank() == Card.ACE) // Can't use containsAce() because Ace must be in first two cards for Blackjack
                        handHasAce = true;
                if (hand.get(i).getValue() == 10)
                        handHasTen = true;
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