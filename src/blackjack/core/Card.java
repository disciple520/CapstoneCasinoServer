// Pulled from https://code.google.com/p/blackjack-school-project/

package blackjack.core;

/**
 * A class that can be used to create poker cards in a standard 52 card French
 * deck. Cards can be one of 13 faces, one of four suits, and one of two colors.
 * Methods will get the names, faces, values, suits, or colors of the cards and
 * can flip the card face up or face down
 * @author Brian
 */
public class Card {
    
    public static final int ACE = 1; 
    public static final int TWO = 2; 
    public static final int THREE = 3; 
    public static final int FOUR = 4; 
    public static final int FIVE = 5;
    public static final int SIX = 6; 
    public static final int SEVEN = 7;
    public static final int EIGHT = 8; 
    public static final int NINE = 9; 
    public static final int TEN = 10; 
    public static final int JACK = 11; 
    public static final int QUEEN = 12; 
    public static final int KING = 13; 
    
    public static final int DIAMONDS = 0; 
    public static final int CLUBS = 1; 
    public static final int HEARTS = 2; 
    public static final int SPADES = 3;
    
    private int suit; 
    private int rank;
    private boolean isFaceUp;

    public Card(int rank, int suit, boolean isCardFaceUp){
        this.suit = suit;
        this.rank = rank;
        this.isFaceUp = isCardFaceUp;
    }

    public Card(int rank, int suit){
        this.suit = suit;
        this.rank = rank;
        isFaceUp = true;
    }
    
    public int getSuit(){
        return suit;
    }
    
    public int getRank(){
        return rank;
    }
    
    public int getValue(){
        if(rank >= TWO && rank <= TEN)
            return rank;
        else if(rank > TEN)
            return 10;
        else
            return 11;
    }
    
    public boolean isAce() {
    	if (this.getRank() == ACE) 
    		return true;
    	else
    		return false;
    }
    
    public void flip(){
        isFaceUp = !isFaceUp;
    }
    
    public boolean isFaceUp(){
        return isFaceUp;        
    }
    
    public String toString() {
    	return "Card: "+ rank;
    }
}

