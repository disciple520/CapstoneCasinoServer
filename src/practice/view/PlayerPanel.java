package practice.view;

import java.awt.*;
import java.util.Random;

import javax.swing.*;

import practice.model.Card;
import practice.model.Hand;

// Split Functionality is not included (JW)

public class PlayerPanel extends JPanel {
        
		private static final long serialVersionUID = 1L; // Eclipse added to line to resolve an error
	
		private String name;
        private boolean isHuman;
        private int money;
        private int bet;
        private int minBet;
        private Hand hand;
        
        private Image cardImgs;
        
        private JLabel moneyDisp;
        private JLabel betDisp;
        
        public static final int WIN = 1;
        public static final int PUSH = 0;
        public static final int LOSS = -1;

		private static final int NO_INSURANCE = 0;
        
        private static Random rnd = new Random();
        
        private int previousBet;
        private int previousOutcome;
        
        public PlayerPanel(String pName, boolean isHumanPlayer, int startMoney, int minimumBet, Image cardImages) {
                super();        
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                setPreferredSize(new Dimension(99, 325));
                setOpaque(false); 
                Color c = Color.DARK_GRAY;
                if (isHumanPlayer) c = Color.LIGHT_GRAY;
                setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(c), name));
                name = pName;
                isHuman = isHumanPlayer;
                money = startMoney;
                bet = 0;
                minBet = minimumBet;
                hand = null;
                cardImgs = cardImages;          
                moneyDisp = new JLabel("$" + Integer.toString(money));
                moneyDisp.setForeground(new Color(87, 233, 100));
                moneyDisp.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
                betDisp = new JLabel("$" + Integer.toString(0));
                betDisp.setForeground(Color.RED);
                betDisp.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
                add(moneyDisp);
                add(betDisp);   
                
                previousBet = 0;
                previousOutcome = 0;
        }
        
        public Hand getHand() { return hand; }
        
        public void clearHand() {
                hand.clearHand();
        }       
        
        /**
         * For computer players only. Causes player to play Blackjack accordingly.
         * @param dealerCard the dealer's visible card
         * @return -1 means not a computer, 0 means stand, 1 means hit, 2 means surrender, 3 means double
         */
        public int askComputerAction(Card dealerCard) {
                if (!isHuman) {
                    if(hand.isBlackjack()){
                        return 0;
                    }
                    
                    if(hand.isBusted()) {
                        return 0;
                    }
                        
                    if(hand.getValue() == 11){
                        if (rnd.nextInt(101) < (4/13) * 100){
                            return 3;
                        }
                    } else {
                        int nowValue = hand.getValue();                            
                        if (nowValue <= 11) return 1;
                        double bustingChance = (nowValue - 8) / (double) 13;
                        double successChance = 1 - bustingChance;                            
                        if(rnd.nextInt(120) < successChance*100 - 16) 
                            return 1;
                        else
                            return 0;
                    }
                }
                return -1;
        }
        
        public void startHand(Card c1, Card c2){
            hand = new Hand(c1, c2);
        }
        
        public int askBet() {
                int normalBet;
                if (isHuman) {
                        normalBet = 10; //askHumanBet("Remember, the minimum wager is $" + minBet +
                                        //". How much will are you betting?", minBet, money);
                } else {
                        normalBet = previousBet;
                       
                        int rand = rnd.nextInt(3);                              
                        if (previousOutcome == LOSS) 
                                normalBet -= minBet * rand;                             
                        else if (previousOutcome == PUSH);
                        else if (previousOutcome == WIN)
                                normalBet += minBet * rand;                             
                        
                        if (normalBet > money / 10)
                                normalBet = money/10;                   
                        if (normalBet < minBet)
                                normalBet = minBet;
                        
                        previousBet = normalBet;
                }
                money -= normalBet;
                bet = normalBet;
                updateText();
                return normalBet;
        }
        
        public int askInsurance() {
        	int insuranceDecision;
        	int insuranceBet = NO_INSURANCE;
            if (hand.isBlackjack()) {
                insuranceDecision = JOptionPane.showConfirmDialog(this, "Dealer is showing an ace. "
                		+ "Would you like even money on your blackjack?", "Even Money?", JOptionPane.YES_NO_OPTION);
                if (insuranceDecision == JOptionPane.YES_OPTION) {
                	money =+ (bet*2);
                	updateText();
                }
            } else {
                insuranceDecision = JOptionPane.showConfirmDialog(this, "Dealer is showing an Ace. "
                		+ "Would you like Insurance?", "Insurance?", JOptionPane.YES_NO_OPTION);
                if (insuranceDecision == JOptionPane.YES_OPTION) {
                	insuranceBet = 5; // Add option to change amount later
                	money -= insuranceBet;
                	updateText(); 
                }
            } 
            return insuranceBet;
        }
        
        public void doubleDown() {
                money -= bet;
                bet *= 2;
        }
        
        public void addWinnings(int moneyWon) {
                money += moneyWon;
                updateText();
                                
                if (moneyWon > bet) {
                        previousOutcome = WIN;
                        moneyDisp.setText(moneyDisp.getText() + "  :)");
                } else if (moneyWon == bet) {                   
                        previousOutcome = PUSH;
                        moneyDisp.setText(moneyDisp.getText() + "  :|");
                } else {
                        previousOutcome = LOSS;
                        moneyDisp.setText(moneyDisp.getText() + "  :(");
                }
        }
        
        public void newRound(Card c1, Card c2) {
                bet = 0;
                updateText();
                startHand(c1, c2);              
        }
        
        public int getCurrentBet() {
                return bet;
        }
        
        /**
         * Gets the amount of money player has left.
         * @return player's remaining cash
         */
        public int getMoney() {
                return money;
        }
        
        /**
         * Paints the cards stacked top-down in addition to the rest of the 
         * components. The cards are arranged so the user can still see all of
         * the cards' values.
         */
        @Override
        public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (hand == null) return;
                for (int i = 0; i < hand.getSizeOfHand(); i++) {
                        drawCard(g, hand.getCardAtIndex(i), 10, 52 + 33*i);
                }
        }
        
        /**
         * Pops up an input dialog asking a question for amount to bet. Non-numbers
         * and clicking on cancel/X result in getting "kicked out", but on merely
         * illegal number values a new dialog asks for a correct input.
         * @param msg question to ask to player
         * @param min minimum player can bet
         * @param max maximum player can bet
         * @return
         */
        private int askHumanBet (String msg, int min, int max) {
                int hBet = 0;
                String sBet = JOptionPane.showInputDialog(msg);
                try {
                        hBet = Integer.valueOf(sBet);
                        while (hBet < 0 || hBet < min || hBet > max) {
                                String errReply;
                                if (hBet < 0) {
                                        errReply = "Huh? What did you say?";
                                } else if (hBet < min) {
                                        errReply = "At least $" + min + " needed:";
                                } else {
                                        errReply = "You can't bet that much:";                                                          
                                }
                                sBet = JOptionPane.showInputDialog(errReply);
                                hBet = Integer.valueOf(sBet);   
                        }
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Trying to be funny, eh? " + 
                                        "Don't wanna play? - GET OUT.");
                        System.exit(0);
                }
                return hBet;
        }
        
        /**
         * Updates the displays of player's remaining money and current bet.
         */
        private void updateText() {
                moneyDisp.setText("$" + Integer.toString(money));
                betDisp.setText("$" + Integer.toString(bet));
        }
        
        /**
         * Paints a card image onto (x,y) of the container. A facedown card will
         * be drawn accordingly.
         * @param g the graphics context
         * @param card the card to be printed
         * @param x the x-position of the printed card in this container
         * @param y the y-position of the printed card in this container
         */
        private void drawCard(Graphics g, Card card, int x, int y) {
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
         g.drawImage(cardImgs,x,y,x+79,y+123,cx,cy,cx+79,cy+123,this);
        }

        public boolean isHuman() {
                return isHuman;
        }
}