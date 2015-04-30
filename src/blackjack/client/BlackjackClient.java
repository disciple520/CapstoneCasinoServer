/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.client;

import blackjack.core.Card;
import blackjack.core.Hand;
import blackjack.gui.CapstoneCasinoBlackjackUI;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.SwingUtilities;

public class BlackjackClient {

    private static final int PORT = 12345;
    private final Socket socket;
    private final BufferedReader inputFromServer;
    private final PrintWriter outputToServer;
    private CapstoneCasinoBlackjackUI gui;
    private Image cardImages;
    private Card card;
    public int playerNumber;
    Hand hand;
    Hand dealersHand;
    int stake;
    public int bet;
    boolean isDealersFirstCard = true;
    boolean isDealersTurn = false;

    /**
     * Constructs the client by connecting to a server and laying out the gui
     */
    public BlackjackClient(String serverAddress) throws Exception {

        hand = new Hand();
        dealersHand = new Hand();
        
        
        // Setup networking
        socket = new Socket(serverAddress, PORT);
        inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputToServer = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("In client constructor");
      

        // Layout GUI
        gui = new CapstoneCasinoBlackjackUI(this);
        gui.setLocation(200, 50);
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                gui.setVisible(true);
            }
            
        });
    }

    /**
     * The main thread of the client will listen for messages
     * from the server. 
     */
    public void play() throws Exception {
        String welcomeMsg;
        String response;
        try {
            welcomeMsg = inputFromServer.readLine();
            System.out.println(welcomeMsg);
            while (true) {
                if (inputFromServer.ready()) {
                    response = inputFromServer.readLine();
                    System.out.println("This just in: " + response);
                    
                    if (response.startsWith("SET_PLAYER_")) {
                        playerNumber = Integer.parseInt(response.substring(11));
                        System.out.println("playerNumber set to " + playerNumber);
                    }
                    else if (response.equals("DEAL")) {
                       gui.clearCardHolderPanels();
                       isDealersFirstCard = true;
                       bet = gui.betStakeUpdater.getBet();
                    } 
                    else if(response.equals("ENABLE_PLAY_AND_CLEAR")){
                        gui.playButton.setEnabled(true);
                        gui.clearButton.setEnabled(true);
                        gui.betCheck1.setEnabled(true);
                        gui.betCheck5.setEnabled(true);
                        gui.betCheck25.setEnabled(true);
                        gui.betCheck50.setEnabled(true);
                        gui.betCheck100.setEnabled(true);
                    } 
                    else if(response.equals("DISABLE_PLAY_AND_CLEAR")){
                        gui.playButton.setEnabled(false); 
                        gui.clearButton.setEnabled(false);
                        gui.betCheck1.setEnabled(false);
                        gui.betCheck5.setEnabled(false);
                        gui.betCheck25.setEnabled(false);
                        gui.betCheck50.setEnabled(false);
                        gui.betCheck100.setEnabled(false);
                        
                    }
                    else if(response.equals("DISABLE_ACTION_BUTTONS")) {
                        gui.hitButton.setEnabled(false);
                        gui.standButton.setEnabled(false);
                        gui.doubleButton.setEnabled(false);
                    }
                    else if(response.equals("ENABLE_ACTION_BUTTONS")) {
                        gui.hitButton.setEnabled(true);
                        gui.standButton.setEnabled(true);
                        gui.doubleButton.setEnabled(true);
                    }
                    else if(response.equals("DEALERS_TURN")) {
                        if (dealersHand.getCardAtIndex(0).isFaceUp() == false) {
                            dealersHand.getCardAtIndex(0).flip();
                            gui.cardHolderDealer.removeAll();
                            gui.swingWorkerCardDraw(dealersHand.getCardAtIndex(0), 5);
                            gui.swingWorkerCardDraw(dealersHand.getCardAtIndex(1), 5);
                            gui.swingWorkerTurn("Dealer");
                        }
                    }
                    else if(response.startsWith("UPDATE_")) {
                        String[] parameters = response.split("_");
                        int player = Integer.parseInt(parameters[1]);
                        int bet = Integer.parseInt(parameters[3]);
                        gui.swingWorkerPlayerUpdate(bet,player);
                    }
                    else if(response.startsWith("TURN_")) {
                        String[] parameters = response.split("_");
                        String player = parameters[1];
                        gui.swingWorkerTurn("Player "+player);
                        if(Integer.parseInt(player) == 5)
                            gui.swingWorkerTurn("Dealer");
                    }
                    else if(response.equals("CLOSE")){
                        //closes all attached clients
                        //found in http://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                        gui.dispatchEvent(new WindowEvent(gui,WindowEvent.WINDOW_CLOSING));
                    }
                    //This is a command of the form "DEALING_RANK_OF_SUIT_TO_PLAYER
                    //i.e. "DEALING_11_OF_3_TO_5" means dealing jack of spades to dealer
                    else if(response.startsWith("DEALING_")){ 
                        String[] parameters = response.split("_");
                        int rank = Integer.parseInt(parameters[1]);
                        int suit = Integer.parseInt(parameters[3]);
                        int placement = Integer.parseInt(parameters[5]);
                        card = new Card(rank, suit);
                        if (placement == playerNumber) {
                            hand.addCard(card);
//                            gui.cardHolderPlayer1.removeAll();
//                            for(int i=0; i<hand.getSizeOfHand(); i++){
//                                gui.swingWorkerCardDraw(hand.getCardAtIndex(i), placement);
//                            }
                            if (hand.isBusted()) {
                                sendMessageToServer("PLAYER_BUSTED");
                            } else {
                                sendMessageToServer("PLAYER" + playerNumber + "_HAND_VALUE_IS_" + hand.getValue());
                            }
                            if (hand.isBlackjack()) {
                                sendMessageToServer("PLAYER_HAS_BLACKJACK");
                            }
                        } else if (placement == 5) { // i.e. if card was dealt dealer instead of player
                            if (isDealersFirstCard) {
                                card.flip();
                                isDealersFirstCard = false;
                            }
                            dealersHand.addCard(card);
//                            gui.cardHolderDealer.removeAll();
//                            for(int i=dealersHand.getSizeOfHand()-1; i>=0; i--){
//                                gui.swingWorkerCardDraw(hand.getCardAtIndex(i), placement);
//                            }
                            sendMessageToServer("DEALERS_HAND_VALUE_IS_" + dealersHand.getValue());
                            if (dealersHand.isBlackjack()) {
                                sendMessageToServer("BLACKJACK_FOR_DEALER");
                            } else if (dealersHand.isBusted()) {
                                sendMessageToServer("DEALER_BUSTED");
                            }
                                
                        }
                        gui.swingWorkerCardDraw(card, placement);
                    } else if(response.equals("PAY") || response.equals("TAKE") || response.equals("PUSH") || response.equals("3:2")) {
                        bet = gui.betStakeUpdater.getBet();
                        stake = gui.betStakeUpdater.getStake();
                        if (response.equals("PAY")) {
                            stake += (2 * bet);
                        } else if (response.equals("PUSH")) {
                            stake += bet;
                        } else if (response.equals("3:2")) {
                            stake += (2.5 * bet);
                        } else if (response.equals("TAKE")) {
                            // No action needed since we reduce their stake at play time
                        }
                        gui.betStakeUpdater.setStake(stake);
                        gui.betStakeUpdater.setBet(0);
                        gui.updateBetAndStake();
                        hand.clearHand();
                        dealersHand.clearHand();
                        gui.swingWorkerTurn("Betting");
                    }
                }
               }
         
            }
        
        finally {
            socket.close();
        }
    } 
    /**
     * Runs the client as an application.
     */
    public static void main(String[] args) throws Exception {
        while (true) {
            String serverAddress = (args.length == 0) ? "97.80.81.158" : args[1];
            BlackjackClient client = new BlackjackClient(serverAddress);
            System.out.println("Client created");
            client.play();
        }
    }

    public void sendMessageToServer(String messageFromGui) {
        System.out.println("sending \"" + messageFromGui + "\"");
        outputToServer.println(messageFromGui);
    }
}
