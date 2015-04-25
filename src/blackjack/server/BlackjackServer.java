package blackjack.server;

import blackjack.client.Session;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import blackjack.core.Blackjack;
import java.util.Random;

public class BlackjackServer {

    // Some constants to make the code more readable
    private static final int NO_PLAYER = 0;
    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;
    private static final int PLAYER_THREE = 3;
    private static final int PLAYER_FOUR = 4;
    private static final int DEALER = 5;
    
    public static final int NO_ACTION = 0;
    // maybe add ACTION_FINISHED to differentiate from this one
    // I have some logic errors where the dealer acts before he should sometimes
    public static final int HIT = 1;
    public static final int STAND = 2;
    public static final int DOUBLE = 3;
    
    private static Random rand = new Random();
    private static Session.Player playerOne;
    private static int currentPlayer;
    
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
                
                while (true) { // this is where all the complexity lies
                    Thread.sleep(250);
                    System.out.print("wait  ");
                    playerOne.setActive(true);
                    if (playerOne.isReadyForDeal == true) {
                        sendHandsToClients();
                        playerOne.isReadyForDeal = false; // just resetting for next time
                        currentPlayer = PLAYER_ONE;
                        Thread.sleep(2000); //give dealer time to check for blackjack
                        if (playerOne.dealerHasBlackjack == false) {
                            System.out.println("Dealer does not have blackjack");
                            while (currentPlayer == PLAYER_ONE) {
                                Thread.sleep(250);
                                if (playerOne.hasBlackjack == false) {
                                    if (playerOne.busted == false) {
                                        if (playerOne.action == HIT){
                                            System.out.println("Player 1 hit!");
                                            playerOne.sendMessageToClient("DEALING_" + addAces(rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_1");
                                            playerOne.action = NO_ACTION;
                                        }
                                        if (playerOne.action == STAND){
                                            System.out.println("Player 1 stands!");
                                            playerOne.action = NO_ACTION; 
                                            currentPlayer = DEALER;
                                        }
                                    } else {
                                        playerOne.action = NO_ACTION;
                                        currentPlayer = DEALER;
                                    }
                                } else {
                                    System.out.println("Player1 has blackjack");
                                    playerOne.action = NO_ACTION;
                                    currentPlayer = NO_PLAYER;
                                }
                            }
                            while (currentPlayer == DEALER) {
                                Thread.sleep(2000);
                                if (playerOne.dealersHandValue < 17) {
                                    System.out.println("Dealer hits");
                                    playerOne.sendMessageToClient("DEALING_" + addAces(rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_5");
                                } 
                                else if (playerOne.dealerBusted) {
                                    currentPlayer = NO_PLAYER;
                                }
                                else {
                                    System.out.println("Dealer stands with " + playerOne.dealersHandValue);
                                    currentPlayer = NO_PLAYER;
                                }
                            }
                            payOrTakeBets();
                            playerOne.dealersHandValue = 0;
                            playerOne.sendMessageToClient("ENABLE_PLAY_AND_CLEAR");
                            playerOne.sendMessageToClient("DISABLE_ACTION_BUTTONS");
                        } else {
                            System.out.println("Dealer has blackjack");
                            currentPlayer = NO_PLAYER;
                            playerOne.action = NO_ACTION;
                            payOrTakeBets();
                            playerOne.dealersHandValue = 0;
                            playerOne.dealerHasBlackjack = false;
                            playerOne.sendMessageToClient("ENABLE_PLAY_AND_CLEAR");
                            playerOne.sendMessageToClient("DISABLE_ACTION_BUTTONS");
                        }
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
    
    private static void sendHandsToClients() throws InterruptedException {
        
       
        playerOne.sendMessageToClient("DEALING_" + addAces(rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_1");
        Thread.sleep(300); // to create a visual effect and give client time to respond
        playerOne.sendMessageToClient("DEALING_" + addAces(rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_5");
        Thread.sleep(300);
        playerOne.sendMessageToClient("DEALING_" + addAces(rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_1");
        Thread.sleep(300);
        playerOne.sendMessageToClient("DEALING_" + addAces(rand.nextInt(13)+1) + "_OF_" + rand.nextInt(4) + "_TO" + "_5");
        Thread.sleep(300);
        playerOne.sendMessageToClient("ENABLE_ACTION_BUTTONS");Thread.sleep(500);
    }
    
    // Changes 4,5,6,7,8, and 9 and be Aces to make testing blackjack and Ace logic easier 
    // (You don't have to wait as long to get them)
    private static int addAces(int number) {
        if (number >= 2 && number <= 4) {
            return 1;
        } else if (number == 9 || number == 8) {
            return 10;
        }{
            return number;
        }
    }
    private static void payOrTakeBets() {
        String msg = "problem in payOrTakeBets()";
        System.out.println("Dealer: " + playerOne.dealersHandValue);
        System.out.println("Player1: " + playerOne.handValue);
        if (playerOne.dealerHasBlackjack) {
            if (playerOne.hasBlackjack) {
                System.out.print("player has blackjack");
                playerOne.hasBlackjack = false;
                msg = "PUSH";
            } else {
                msg = "TAKE";
            }
        } else if (playerOne.dealerHasBlackjack == false) {
            if (playerOne.hasBlackjack) {
                playerOne.hasBlackjack = false;
                msg = "3:2";
            } else if (playerOne.dealersHandValue >=22) {
                System.out.println("Dealer busted!");
                if (playerOne.busted == false) {
                    msg = "PAY";
                } else {
                    msg = "TAKE";
                }
            } else if (playerOne.handValue > playerOne.dealersHandValue) {
                msg = "PAY";
            } else if (playerOne.handValue < playerOne.dealersHandValue) {
                msg = "TAKE";
            } else {
                msg = "PUSH";
            }
        }    
        playerOne.sendMessageToClient(msg);
    }
}


