/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.client.BlackjackClient;
import java.awt.Graphics;
import javax.swing.JPanel;
import blackjack.core.Card;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

/**
 *
 * @author Jerry
 */
public class CardPanel extends JPanel {
    
    private Card card;
    private static Image cardImages = null;
    
    public CardPanel(Card card) {
        super();
        if(cardImages == null){
            loadImages();
        }
        setPreferredSize(new Dimension(79, 123));
        setSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        setOpaque(false);
        
        
        this.card = card;
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawCard(g);   
    }
    /**
         * Paints a card image onto (x,y) of the container. A facedown card will
         * be drawn accordingly.
         * @param g the graphics context
         * @param card the card to be printed
         * @param x the x-position of the printed card in this container
         * @param y the y-position of the printed card in this container
         */
    private void drawCard(Graphics g) {
        int cx; // top-left x of cardsImage
        int cy; // top-left y of cardsImage
        boolean faceUp = true;
        if (card.isFaceUp() != faceUp) {
                cx = 2*79;
                cy = 4*123;
        }
        else {
           cx = (card.getRank())*79-79;
           switch (card.getSuit()) {
                   case Card.DIAMONDS: cy = 123;       break;
                   case Card.CLUBS:    cy = 0;         break;              
                   case Card.HEARTS:   cy = 2*123;     break;
                   default:            cy = 3*123;     break; //Spades
           }
        }
          g.drawImage(cardImages,0,0,79,123,cx,cy,cx+79,cy+123,this);


       }
       
    private void loadImages() {
           URL imageURL = null;
           try {
               imageURL = BlackjackClient.class.getResource("cards.png");
           } catch (Exception e) {
               System.out.println("whoops");
           }

           if (imageURL != null)
                cardImages = Toolkit.getDefaultToolkit().createImage(imageURL);
           else {
                String errorMsg = "Card image file loading failed.";
                System.out.println(errorMsg);
                System.out.println(imageURL.toString());
                System.exit(1);
           }                   
       }
    
}
