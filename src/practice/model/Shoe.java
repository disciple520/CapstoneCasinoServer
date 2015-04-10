package practice.model;

import java.util.ArrayList;
import java.util.Random;

public class Shoe {
    
    private final static int NUM_DECKS = 3;
    private final static int RANKS_IN_DECK = 13;
    private final static int SUITS_IN_DECK = 4;
    private final static int CARDS_IN_DECK = RANKS_IN_DECK * SUITS_IN_DECK;
    
    private ArrayList<Card> cardsInShoe = new ArrayList<Card>();
    private int numCardsRemaining;
    
    public Shoe(){
        for(int i = 0; i < NUM_DECKS; i++){
            for(int j = 0; j < SUITS_IN_DECK; j++){
                for(int k = 1; k <= RANKS_IN_DECK; k++){
                    cardsInShoe.add(new Card(j, k));
                }
            }
        }
        numCardsRemaining = NUM_DECKS * CARDS_IN_DECK;
        System.out.print(cardsInShoe.size());
        
        shuffle();
    }

    public void shuffle(){ // I can find a better way to do this (JW)
        ArrayList<Card> tempDeck = new ArrayList<Card>();
        Random random = new Random();
        while(cardsInShoe.size() > 0){
            int cardToRemove = random.nextInt(cardsInShoe.size());
            Card tempCard = cardsInShoe.get(cardToRemove);
            cardsInShoe.remove(cardToRemove);
            tempDeck.add(tempCard);
        }
        while(tempDeck.size() > 0){
            Card tempCard = tempDeck.get(0);
            tempDeck.remove(0);
            cardsInShoe.add(tempCard);            
        }
    }
    
    public Card draw(){
        Card nextCardToDraw = cardsInShoe.get(0);
        cardsInShoe.remove(0);
        numCardsRemaining--;
        return nextCardToDraw;
    }
    
    public int getNumCardsRemaining() {
    	return numCardsRemaining;
    }
}

