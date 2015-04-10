package practice.controller;

import practice.view.BlackjackGui;

import javax.swing.JOptionPane;

public class BlackjackController {

	public static final int MIN_BET = 10;
	
	public static void main(String[] args) {
		BlackjackGui blackjack = new BlackjackGui();
        while (true) {
        	if (blackjack.getPlayer4Panel().getMoney() < MIN_BET) {
                JOptionPane.showMessageDialog(blackjack, "Sorry, you have run out of money");
                System.exit(0);
        	}
        	blackjack.askBets(); 
        	blackjack.deal();            
        	blackjack.repaint();
        	blackjack.insurance();
        	blackjack.processPlayerBlackjack();
        	blackjack.setButtonState(true, true, true, false, true);
        	if (blackjack.getPlayer4Panel().getCurrentBet() > blackjack.getPlayer4Panel().getMoney()) 
                blackjack.getPlayerChoices().disableDouble();
        	while (blackjack.getTurnContinue()) { blackjack.repaint(); }           
        	blackjack.setButtonState(false, false, false, false, false);                 
        	blackjack.doAITurns();
        	blackjack.repaint();
        	blackjack.doDealerTurn();
        	blackjack.repaint();       
        	blackjack.doPayOuts();
        	blackjack.reset();           
        }        

	}
}	
