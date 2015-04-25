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
    int playerNumber;
    Hand hand;
    Hand dealersHand;
    int stake;
    int bet;

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
                       gui.playButton.setEnabled(false);
                       gui.clearButton.setEnabled(false);
                       gui.clearCardHolderPanels();
                    } 
                    else if(response.equals("HANDS_DEALT")){
                        gui.hitButton.setEnabled(true);
                        gui.standButton.setEnabled(true);
                        gui.doubleButton.setEnabled(true);
                    }
                    else if(response.equals("ENABLE_PLAY_AND_CLEAR")){
                        gui.playButton.setEnabled(true);
                        gui.clearButton.setEnabled(true);
                    } 
                    else if(response.equals("DISABLE_PLAY_AND_CLEAR")){
                        gui.playButton.setEnabled(false); 
                        gui.clearButton.setEnabled(false);
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
                    else if(response.startsWith("DEALING_")){
                        String[] parameters = response.split("_");
                        int rank = Integer.parseInt(parameters[1]);
                        int suit = Integer.parseInt(parameters[3]);
                        int placement = Integer.parseInt(parameters[5]);
                        card = new Card(rank, suit);
                        gui.swingWorkerCardDraw(card, placement);
                        if (placement == playerNumber) {
                            hand.addCard(card);
                            sendMessageToServer("PLAYER" + playerNumber + "_HAND_VALUE_IS_" + hand.getValue());
                        }
                        if (placement == 5) {
                            dealersHand.addCard(card);
                            sendMessageToServer("DEALERS_HAND_VALUE_IS_" + dealersHand.getValue());
                            if (dealersHand.isBlackjack()) {
                                sendMessageToServer("BLACKJACK_FOR_DEALER");
                            }
                                
                        }
                    } else if(response.equals("PAY") || response.equals("TAKE") || response.equals("PUSH")) {
                        bet = gui.betStakeUpdater.getBet();
                        stake = gui.betStakeUpdater.getStake();
                        if (response.equals("PAY")) {
                            stake += (2 * bet);
                        } else if (response.equals("PUSH")) {
                            stake += bet;
                        }
                        gui.betStakeUpdater.setStake(stake);
                        gui.betStakeUpdater.setBet(0);
                        gui.updateBetAndStake();
                        hand.clearHand();
                        dealersHand.clearHand();
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
            String serverAddress = (args.length == 0) ? "localhost" : args[1];
            BlackjackClient client = new BlackjackClient(serverAddress);
            System.out.println("Client created");
            client.play();
        }
    }

    public void sendMessageToServer(String messageFromGui) {
        //System.out.println("sendMessageToServer Fired! (" + messageFromGui + ")");
        outputToServer.println(messageFromGui);
    }
}
