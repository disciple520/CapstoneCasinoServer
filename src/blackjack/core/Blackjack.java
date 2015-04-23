/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.core;

import blackjack.client.Session;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/**
 *
 * @author Hellz
 */
public class Blackjack {
    private Shoe shoe = new Shoe();
    private Hand hand;
    

    //need to pass in current player then uncomment
    public void dealCards(Session.Player playerOne){
                    Card c1 = shoe.draw();
                    Card c2 = shoe.draw();
                    startHand(c1, c2);
                }
    
    public void startHand(Card c1, Card c2){
        hand = new Hand(c1, c2);
    }
    public void payOut() {
        
    }

}
