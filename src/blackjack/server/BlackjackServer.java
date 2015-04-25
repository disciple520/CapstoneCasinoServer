package blackjack.server;

import blackjack.client.Session;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import blackjack.core.Blackjack;
import blackjack.core.Card;
import blackjack.core.Hand;
import java.util.Random;

public class BlackjackServer {

    private static final int NO_PLAYER = 1;
    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;
    private static final int PLAYER_THREE = 3;
    private static final int PLAYER_FOUR = 4;
    private static final int DEALER = 5;
    
    public static final int NO_ACTION = 0;
    public static final int HIT = 1;
    public static final int STAND = 2;
    public static final int DOUBLE = 3;
    
    private static Random rand = new Random();
    private static Session.Player playerOne;
    private static int currentPlayer;
    private static int dealersHandValue;
    
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        System.out.println("Blackjack Server is Running");
        List<Session> playerHelpers = new ArrayList<Session>();
        Blackjack blackjack = new Blackjack();
        int currentPlayer = 1;
        try {
            while (true) {
                Session session = new Session();
                playerOne = session.new Player(listener.accept(), 1);
                System.out.println("player 1 accepted!");
                playerOne.start();
                System.out.println("player one helper thread started");
//                Session.Player playerTwo = session.new Player(listener.accept(), 2);
//                System.out.println("player 2 accepted!");
//                playerTwo.start();
//                System.out.println("player 2 helper thread started");
//                Session.Player playerThree = session.new Player(listener.accept(), 3);
//                System.out.println("player 3 accepted!");
//                playerThree.start();
//                System.out.println("player 3 helper thread started");
//                Session.Player playerFour = session.new Player(listener.accept(), 4);
//                System.out.println("player 4 accepted!");
//                playerFour.start();
//                System.out.println("player 4 helper thread started");
                
                while (true) {
                    Thread.sleep(250);
                    System.out.print("wait  ");
                    playerOne.setActive(true);
                    System.out.println("The Server things that PlayerOne is ready for deal: " + playerOne.isReadyForDeal);
                    if (playerOne.isReadyForDeal == true) {
                        sendHandsToClients();
                        playerOne.isReadyForDeal = false;
                        currentPlayer = PLAYER_ONE;
                        while (currentPlayer == PLAYER_ONE) {
                            Thread.sleep(250);
                            if (playerOne.action == HIT){
                                System.out.println("Player 1 hit!");
                                playerOne.sendMessageToClient("DEALING_" + (rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_1");
                                playerOne.action = NO_ACTION;
                            }
                            if (playerOne.action == STAND){
                                System.out.println("Player 1 stands!");
                                playerOne.action = NO_ACTION;
                                currentPlayer = DEALER;
                            }
                        }
                        while (currentPlayer == DEALER) {
                            Thread.sleep(2000);
                            dealersHandValue = playerOne.dealersHandValue;
                            System.out.println("The server thinks the dealer has " + dealersHandValue);
                            if (dealersHandValue <=17) {
                                System.out.println("Dealer hits");
                                playerOne.sendMessageToClient("DEALING_" + (rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_5");
                            } 
                            else {
                                System.out.println("Dealer stands with " + dealersHandValue);
                                currentPlayer = NO_PLAYER;
                            }
                        }
                        payOrTakeBets(dealersHandValue);
                        playerOne.dealersHandValue = 0;
                        dealersHandValue = 0;
                        playerOne.sendMessageToClient("ENABLE_PLAY_AND_CLEAR");
                        playerOne.sendMessageToClient("DISABLE_ACTION_BUTTONS");
                    }
//                    if (currentPlayer == 1 && playerOne.activeTurn()) {
//                        currentPlayer++;
//                        System.out.println("Player 1's turn");
//                        playerOne.sendMessageToClient("DEAL");
//                        playerOne.setActive(false);
//                        blackjack.dealCards(playerOne);
//                        playerTwo.enableButton();
//                    }
//                    if (currentPlayer == 2 && playerTwo.activeTurn()) {
//                        currentPlayer++;
//                        System.out.println("Player "+currentPlayer+"'s turn");
//                        playerTwo.sendMessageToClient("DEAL");
//                        playerTwo.setActive(false);
//                        blackjack.dealCards(playerTwo);
//                        playerThree.enableButton();
//                    }
//                    if (currentPlayer == 3 && playerThree.activeTurn()) {
//                        currentPlayer++;
//                        System.out.println("Player "+currentPlayer+"'s turn");
//                        playerThree.sendMessageToClient("DEAL");
//                        playerThree.setActive(false);
//                        blackjack.dealCards(playerThree);
//                        playerFour.enableButton();
//                    }
//                    if (currentPlayer == 4 && playerFour.activeTurn()) {
//                        currentPlayer = 1;
//                        System.out.println("Player "+currentPlayer+"'s turn");
//                        playerFour.sendMessageToClient("DEAL");
//                        playerFour.setActive(false);
//                        blackjack.dealCards(playerFour);
//                        playerOne.enableButton();
//                    }
                }
            }
        } finally {
            listener.close();
        }
    }
    
    private static void sendHandsToClients() {
        playerOne.sendMessageToClient("DEALING_" + (rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_1");
        playerOne.sendMessageToClient("DEALING_" + (rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_1");
        playerOne.sendMessageToClient("DEALING_" + (rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_5");
        playerOne.sendMessageToClient("DEALING_" + (rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_5");
        playerOne.sendMessageToClient("HANDS_DEALT");
    }
    
    private static void payOrTakeBets(int dealersHandValue) {
        if (dealersHandValue >=22) {
            System.out.println("Dealer busted!");
            if (playerOne.busted == false) {
                playerOne.sendMessageToClient("PAY");
            } else {
                playerOne.sendMessageToClient("TAKE");
            }
        } else if (playerOne.handValue > dealersHandValue) {
                playerOne.sendMessageToClient("PAY");
            } else if (playerOne.handValue < dealersHandValue) {
                playerOne.sendMessageToClient("TAKE");
            } else {
                playerOne.sendMessageToClient("PUSH");
            }
    }
}


