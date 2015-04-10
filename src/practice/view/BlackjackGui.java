package practice.view;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

import practice.model.Card;
import practice.model.Shoe;

public class BlackjackGui extends JFrame implements ActionListener {
    
		public static final int MIN_BET = 10;
        public static final int START_MONEY = 1000;
        
        private static final long serialVersionUID = 1L; //Eclipse generated this to resolve a warning
				
        private ChoicesPanel playerChoices = new ChoicesPanel();
        private PlayerPanel player4Panel;
        private PlayerPanel player3Panel;
        private PlayerPanel player2Panel;
        private PlayerPanel player1Panel;
        private DealerPanel dealerPanel;
                
        private Shoe shoe;
        private boolean turnContinue;
                
        private Image cardImages;
                
                public BlackjackGui() {
                        super("Casino Server: Blackjack");
                        getContentPane().setBackground(new Color(80,135,85));
                        loadImages();
                        initComponents();
                        pack(); 
                        setLocationRelativeTo(null);
                        setDefaultCloseOperation(EXIT_ON_CLOSE);
                        setVisible(true);
                }
                
                /**
                 * Responds to button presses from the ChoicePanel. 
                 * @param a The event that will be responded to
                 */
                //Jerry Commented Out    @Override
                public void actionPerformed(ActionEvent a) {
                        String command = a.getActionCommand();
                        String bop = "That tickles!"; //Placeholder for executing actual command
                        if (command.equals("Hit")) {
                                giveCard(player4Panel);
                                boolean busted = player4Panel.getHand().isBusted();
                                turnContinue = !busted;
                                playerChoices.disableSurrender();
                                playerChoices.disableDouble();
                        } else if (command.equals("Stand")) {
                                turnContinue = false;
                        } else if (command.equals("Double")){
                                player4Panel.doubleDown();
                                giveCard(player4Panel);
                                turnContinue = false;
                        } else if (command.equals("Split")) {
                                JOptionPane.showMessageDialog(this, bop);
                        } else if (command.equals("Surrender")) {                               
                                JOptionPane.showMessageDialog(this, "Not feeling it? Fine,  " +
                                                "take back $" + player4Panel.getCurrentBet() / 2 + ".");
                                collectCards(player4Panel);
                                player4Panel.addWinnings(player4Panel.getCurrentBet() / 2);
                                turnContinue = false;
                        }
                }

                /**
                 * Adds components to the frame.
                 */
                private void initComponents() {
                		shoe = new Shoe();
                		turnContinue = true;
                        
                        setLayout(new BorderLayout(10, 10));                            
                        
                        dealerPanel = new DealerPanel(MIN_BET, cardImages);
                        add(dealerPanel, BorderLayout.LINE_START);
                        
                        JPanel players = new JPanel();
                        players.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY), "PLAYERS"));
                        player4Panel = new PlayerPanel("Human - You", true, START_MONEY, MIN_BET, cardImages);
                        player3Panel = new PlayerPanel("Computer 1", false, START_MONEY, MIN_BET, cardImages);
                        player2Panel = new PlayerPanel("Computer 2", false, START_MONEY, MIN_BET, cardImages);
                        player1Panel = new PlayerPanel("Computer 3", false, START_MONEY, MIN_BET, cardImages);         
                        players.add(player4Panel);
                        players.add(player3Panel);
                        players.add(player2Panel);
                        players.add(player1Panel);
                        players.setOpaque(false);
                        add(players, BorderLayout.CENTER);      
                        playerChoices.addListener(this);
                        add(playerChoices, BorderLayout.PAGE_END);              
                }

                /**
                 * Gives or takes money from each player
                 * @param player 
                 */
                public void payOut(PlayerPanel player) {       
                        // surrender check
                        if (player.getHand().getSizeOfHand() == 0) 
                                return;
                        
                        // blackjack hands
                        boolean playerHasBJ = player.getHand().isBlackjack();
                        boolean dealerHasBJ = dealerPanel.getHand().isBlackjack();
                        if (playerHasBJ && dealerHasBJ) {
                                player.addWinnings(player.getCurrentBet());
                                if (player.isHuman())
                                        JOptionPane.showMessageDialog(this, "We both have Blackjack," +
                                                        " a push. Your $" + player.getCurrentBet() + "bet is returned.");
                                return;
                        } else if (playerHasBJ && !dealerHasBJ) {
                                player.addWinnings(player.getCurrentBet() * 5 / 2);
                                return;
                        } else if (!playerHasBJ && dealerHasBJ) {
                                player.addWinnings(0);
                                if (player.isHuman())
                                        JOptionPane.showMessageDialog(this, "I have Blackjack. " +
                                                        "Sorry, you lose.");
                                
                                return;
                        }
                        
                        // busting check
                        boolean playerHasBusted = player.getHand().isBusted();
                        boolean dealerHasBusted = dealerPanel.getHand().isBusted();
                        if (playerHasBusted) {
                                player.addWinnings(0);
                                if (player.isHuman())
                                        JOptionPane.showMessageDialog(this, "You have busted. " +
                                                        "Sorry, you lose.");
                                return;
                        } else if (dealerHasBusted) {
                                player.addWinnings(player.getCurrentBet() * 2);
                                if (player.isHuman())
                                        JOptionPane.showMessageDialog(this, "Damn, I've busted. " +
                                                        "You get $" + player.getCurrentBet() * 2 + ".");
                                return;
                        }

                        // normal hands
                        int playerValue = player.getHand().getValue();
                        int dealerValue = dealerPanel.getHand().getValue();      
                        if (playerValue > dealerValue) {
                                player.addWinnings(player.getCurrentBet() * 2);
                                if (player.isHuman())
                                        JOptionPane.showMessageDialog(this, "Looks like you've won. " +
                                                        "Take your $" + player.getCurrentBet() * 2 + ".");
                                return;
                        } else if (playerValue == dealerValue){
                                player.addWinnings(player.getCurrentBet());
                                if (player.isHuman())
                                        JOptionPane.showMessageDialog(this, "A push. Your $" + 
                                                        player.getCurrentBet() + "bet is returned.");
                                return;
                        } else {
                                player.addWinnings(0);
                                if (player.isHuman())
                                        JOptionPane.showMessageDialog(this, "My hand wins. Better luck" +
                                                        "next time around.");
                                return;
                        }
                }
                
                /**
                 * Asks for insurance bets from each player
                 * @param player 
                 */
                public void doInsurance(PlayerPanel player) {
                    int insuranceBetAmount = player.askInsurance();
                    if (insuranceBetAmount == 0) {
                        return;
                    }
                    if (dealerPanel.getHand().isBlackjack()) {
                        player.addWinnings(insuranceBetAmount * 3);
                        turnContinue = false;
                    } else {
                        JOptionPane.showMessageDialog(this, "Dealer does not have blackjack");
                    }
                }
                
                public void loadImages() {
                    URL imageURL = BlackjackGui.class.getResource("cards.png");
                    if (imageURL != null)
                         cardImages = Toolkit.getDefaultToolkit().createImage(imageURL);
                    else {
                         String errorMsg = "Card image file loading failed.";
                         JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE); 
                         System.exit(1);
                    }                   
                }
                
                /**
                 * Deals cards to the dealer
                 * @param dealer The dealer to deal cards to
                 */
                public void dealerCards(DealerPanel dealer){
                    Card c1 = shoe.draw();
                    System.out.print(c1.toString());
                    Card c2 = shoe.draw();
                    dealer.startHand(c1, c2);
                    dealer.flipSecond();                    
                }
                
                /**
                 * Deals cards to the player
                 * @param player The player to deal cards to
                 */
                public void dealCards(PlayerPanel player){
                    Card c1 = shoe.draw();
                    Card c2 = shoe.draw();
                    player.startHand(c1, c2);
                }
                
                /**
                 * Collects cards from the dealer
                 */
                public void collectDealerCards(){
                    dealerPanel.clearHand(); // this method used to do more (JW)
                }
                
                /**
                 * Collects cards from the player
                 * @param player The player to collect cards from
                 */
                public void collectCards(PlayerPanel player){
                    player.clearHand(); // this method used to do more (JW)
                }
                
                /**
                 * Gives a card to the player
                 * @param player 
                 */
                private void giveCard(PlayerPanel player){
                    player.getHand().addCard(shoe.draw());
                }
                
                /**
                 * Enables and disables some buttons 
                 * @param hitState The hit button
                 * @param standState The stand button
                 * @param doubleState The double button
                 * @param splitState The split button
                 * @param surrenderState The surrender button
                 */
                public void setButtonState(boolean hitState, boolean standState, 
                                boolean doubleState, boolean splitState, boolean surrenderState) {
                        if (hitState) playerChoices.enableHit(); else playerChoices.disableHit();
                        if (standState) playerChoices.enableStand(); else playerChoices.disableStand();
                        if (doubleState) playerChoices.enableDouble(); else playerChoices.disableDouble();
                        if (splitState) playerChoices.enableSplit(); else playerChoices.disableSplit();
                        if (surrenderState) playerChoices.enableSurrender(); else playerChoices.disableSurrender();
                }
                
                /**
                 * Asks for bets from players
                 */
                public void askBets() {
                        player4Panel.askBet();
                        player3Panel.askBet();
                        player2Panel.askBet();
                        player1Panel.askBet();
                }     
                
                /**
                 * Deals out cards to players and dealer
                 */
                public void deal() {
                        dealerCards(dealerPanel);
                        dealCards(player4Panel);
                        dealCards(player3Panel);
                        dealCards(player2Panel);
                        dealCards(player1Panel);
                }                
                
                /**
                 * Asks for insurance bets from players
                 */
                public void insurance() {
                                if (dealerPanel.checkAce()) {
                                        doInsurance(player4Panel);
                                        //doInsurance(player3Panel);
                                        //doInsurance(player2Panel);
                                        //doInsurance(player1Panel);
                                }
                }
                
                public void processPlayerBlackjack() {
                	if (player4Panel.getHand().isBlackjack()) {
                		turnContinue = false;
                	}
                }
                
                /**
                 * Asks for AI to do their turns
                 */
                public void doAITurns() {
                        int aiAction;
                        do {
                                aiAction = player3Panel.askComputerAction(dealerPanel.getHand().getCardAtIndex(0));
                        } while (parseAIActions(player3Panel, aiAction) == true);
                        do {
                                aiAction = player2Panel.askComputerAction(dealerPanel.getHand().getCardAtIndex(0));
                        } while (parseAIActions(player2Panel, aiAction) == true);
                        do {
                                aiAction = player1Panel.askComputerAction(dealerPanel.getHand().getCardAtIndex(0));
                        } while (parseAIActions(player1Panel, aiAction) == true);
                }
                /**
                 * Processes the AI's actions
                 * @param ai The AI to parse actions for
                 * @param action The action to do
                 * @return boolean representing whether the AI will continue to play
                 */
                private boolean parseAIActions(PlayerPanel ai, int action) {
                        switch(action) {
                                case 0: return false;
                                case 1: giveCard(ai); return true;
                                case 2: return false; //AI never surrenders anyway
                                case 3: ai.doubleDown(); giveCard(ai); return false;
                                default: return false;
                        }
                }
                
                /**
                 * Does the dealer's turn
                 */
                public void doDealerTurn() {
                        dealerPanel.flipSecond();
                        while (dealerPanel.getHand().getValue() < 17) {
                                dealerPanel.getHand().addCard(shoe.draw());
                        }
                }
                
                /**
                 * Gives out the money
                 */
                public void doPayOuts() {
                        payOut(player3Panel);
                        payOut(player2Panel);
                        payOut(player1Panel);
                        payOut(player4Panel);
                }
                
                /**
                 * Clears the cards on the table
                 */
                public void reset() {
                        collectCards(player4Panel);
                        collectCards(player3Panel);
                        collectCards(player2Panel);
                        collectCards(player1Panel);
                        collectDealerCards();  
                        turnContinue = true;
                }
                
                public boolean getTurnContinue() {
                	return turnContinue;
                }
                
                public PlayerPanel getPlayer4Panel() {
                	return player4Panel;
                }
                
                public ChoicesPanel getPlayerChoices() {
                	return playerChoices;
                }
            
}
