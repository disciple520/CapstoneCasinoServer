package blackjack.server;

import blackjack.client.Session;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
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
    public static final int HIT = 1;
    public static final int STAND = 2;
    public static final int DOUBLE = 3;
    public static int maxPlayers = 4;
    private static Random rand = new Random();
    private static Session.Player playerOne;
    private static Session.Player playerTwo;
    private static Session.Player playerThree;
    private static Session.Player playerFour;
    private static int currentSeat;
    private static Session.Player currentPlayer;
    
    static List<Integer> playerOneRanks = new ArrayList<>();
    static List<Integer> playerOneSuits = new ArrayList<>();
    static List<Integer> playerTwoRanks = new ArrayList<>();
    static List<Integer> playerTwoSuits = new ArrayList<>();
    static List<Integer> playerThreeRanks = new ArrayList<>();
    static List<Integer> playerThreeSuits = new ArrayList<>();
    static List<Integer> playerFourRanks = new ArrayList<>();
    static List<Integer> playerFourSuits = new ArrayList<>();
    static List<Integer> dealerRanks = new ArrayList<>();
    static List<Integer> dealerSuits = new ArrayList<>();
    
    
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        System.out.println("Blackjack Server is Running");
        currentSeat = 1;
        
        try {
            while (true) {
                Session session = new Session();
                playerOne = session.new Player(listener.accept(), 1);
                System.out.println("player 1 accepted!");
                playerOne.start();
                System.out.println("player one helper thread started");
                playerOne.sendMessageToClient("DISABLE_ACTION_BUTTONS");
                if(maxPlayers != 1){
                    playerTwo = session.new Player(listener.accept(), 2);
                    System.out.println("player 2 accepted!");
                    playerTwo.start();
                    System.out.println("player 2 helper thread started");
                    playerTwo.sendMessageToClient("DISABLE_ACTION_BUTTONS");
                    playerThree = session.new Player(listener.accept(), 3);
                    System.out.println("player 3 accepted!");
                    playerThree.start();
                    System.out.println("player 3 helper thread started");
                    playerThree.sendMessageToClient("DISABLE_ACTION_BUTTONS");
                    playerFour = session.new Player(listener.accept(), 4);
                    System.out.println("player 4 accepted!");
                    playerFour.start();
                    System.out.println("player 4 helper thread started");
                    playerFour.sendMessageToClient("DISABLE_ACTION_BUTTONS");
                }
                while (true) { // this is where all the complexity lies
                    Thread.sleep(250);
                    System.out.print("wait  ");
                    buildFirstHands();
                    if (checkAllPlayersReadyStatus() == true) {
                        sendMessageAllClients("DEAL");
                        sendHandsToClients();                        
                        getPlayer(currentSeat).sendMessageToClient("ENABLE_ACTION_BUTTONS");
                        Thread.sleep(500);
                        //getPlayer(currentSeat).isReadyForDeal = false; // just resetting for next time
                        Thread.sleep(2000); //give dealer time to check for blackjack
                        if (getPlayer(currentSeat).dealerHasBlackjack == false) {
                            System.out.println("Dealer does not have blackjack");
                            while (currentSeat != DEALER) {
                                System.out.println("current seat can't be DEALER if we're in here!");
                                Thread.sleep(250);
                                if (getPlayer(currentSeat).hasBlackjack == false) {
                                    if (getPlayer(currentSeat).busted == false) {
                                        if (getPlayer(currentSeat).action == HIT){
                                            System.out.println("Player "+ currentSeat+" hit!");
                                            addCard();
                                            sendMessageAllClients("DEALING_" + getPlayerRanksArray().get(getPlayerRanksArray().size()-1) + "_OF_" + getPlayerSuitArray().get(getPlayerSuitArray().size()-1) + "_TO" + "_"+currentSeat);
                                            getPlayer(currentSeat).action = NO_ACTION;
                                        }
                                        else if (getPlayer(currentSeat).action == STAND){
                                            System.out.println("Player "+ currentSeat+" stands!");
                                            getPlayer(currentSeat).action = NO_ACTION;
                                            getPlayer(currentSeat).sendMessageToClient("DISABLE_ACTION_BUTTONS");
                                            if(maxPlayers == 1){
                                                System.out.println("Setting currentSeat to DEALER here too");
                                                currentSeat = DEALER;
                                            } else {
                                                currentSeat++;
                                                if(currentSeat < 5)
                                                    getPlayer(currentSeat).sendMessageToClient("ENABLE_ACTION_BUTTONS");
                                                else
                                                    currentSeat = DEALER;
                                            }
                                        }
                                    } else {
                                        moveToNextPlayer(); 
                                    }
                                } else {
                                    System.out.println("Player "+ currentSeat +" has blackjack");
                                    getPlayer(currentSeat).action = NO_ACTION;
                                    getPlayer(currentSeat).sendMessageToClient("DISABLE_ACTION_BUTTONS");
                                    if(maxPlayers == 1){
                                        currentSeat = PLAYER_ONE;
                                    } else {
                                        currentSeat++;
                                        if(currentSeat < 5)
                                           getPlayer(currentSeat).sendMessageToClient("ENABLE_ACTION_BUTTONS");
                                        else
                                            currentSeat = DEALER;
                                    }
                                    
                                        
                                }
                            }
                            while (currentSeat == DEALER) {
                                if(!playerOne.busted){
                                    
                                    Thread.sleep(1000);
                                    if (playerOne.dealersHandValue < 17) {
                                        System.out.println("Dealer hits");
                                        addCard();
                                        sendMessageAllClients("DEALING_" + getPlayerRanksArray().get(getPlayerRanksArray().size()-1) + "_OF_" + getPlayerSuitArray().get(getPlayerSuitArray().size()-1) + "_TO" + "_5");
                                    } 
                                    else if (playerOne.dealerBusted) {
                                        currentSeat = PLAYER_ONE;
                                    }
                                    else {
                                        System.out.println("Dealer stands with " + playerOne.dealersHandValue);
                                        currentSeat = PLAYER_ONE;
                                    }
                                } else {
                                    moveToNextPlayer();
                                }
                            }
                            payOrTakeBets();
                            getPlayer(currentSeat).dealersHandValue = 0;
                            //playerOne.sendMessageToClient("ENABLE_PLAY_AND_CLEAR");
                            getPlayer(currentSeat).sendMessageToClient("DISABLE_ACTION_BUTTONS");
                        } else {
                            System.out.println("Dealer has blackjack");
                            currentSeat = PLAYER_ONE;
                            getPlayer(currentSeat).action = NO_ACTION;
                            payOrTakeBets();
                            getPlayer(currentSeat).dealersHandValue = 0;
                            getPlayer(currentSeat).dealerHasBlackjack = false;
                            //playerOne.sendMessageToClient("ENABLE_PLAY_AND_CLEAR");
                            getPlayer(currentSeat).sendMessageToClient("DISABLE_ACTION_BUTTONS");
                        }
                        sendMessageAllClients("ENABLE_PLAY_AND_CLEAR");
                        for(int i=1;i<=maxPlayers;i++){
                            getPlayer(i).isReadyForDeal = false;
                        }
                    }
                    clearArrays();
                }
            }
        } finally {
            listener.close();
        }
    }
    private static boolean checkAllPlayersReadyStatus() {
        if(maxPlayers == 1) {
            return playerOne.isReadyForDeal;
        }
        else {
            if(playerOne.isReadyForDeal && playerTwo.isReadyForDeal && playerThree.isReadyForDeal && playerFour.isReadyForDeal){
                return true;
            }
            else return false;
        }        
    }
    private static void sendHandsToClients() throws InterruptedException {
        
        sendMessageAllClients("DEALING_" + playerOneRanks.get(0) + "_OF_" + playerOneSuits.get(0) + "_TO" + "_1");
        Thread.sleep(300);
        sendMessageAllClients("DEALING_" + dealerRanks.get(0) + "_OF_" + dealerSuits.get(0) + "_TO" + "_5");
        Thread.sleep(300);
        sendMessageAllClients("DEALING_" + playerOneRanks.get(1) + "_OF_" + playerOneSuits.get(1) + "_TO" + "_1");
        Thread.sleep(300);
        sendMessageAllClients("DEALING_" + dealerRanks.get(1) + "_OF_" + dealerSuits.get(1) + "_TO" + "_5");
        Thread.sleep(300);
        if(maxPlayers >1) {

        sendMessageAllClients("DEALING_" + playerOneRanks.get(0) + "_OF_" + playerOneSuits.get(0) + "_TO" + "_1");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + playerTwoRanks.get(0) + "_OF_" + playerTwoSuits.get(0) + "_TO" + "_2");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + playerThreeRanks.get(0) + "_OF_" + playerThreeSuits.get(0) + "_TO" + "_3");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + playerFourRanks.get(0) + "_OF_" + playerFourSuits.get(0) + "_TO" + "_4");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + dealerRanks.get(0) + "_OF_" + dealerSuits.get(0) + "_TO" + "_5");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + playerOneRanks.get(1) + "_OF_" + playerOneSuits.get(1) + "_TO" + "_1");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + playerTwoRanks.get(1) + "_OF_" + playerTwoSuits.get(1) + "_TO" + "_2");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + playerThreeRanks.get(1) + "_OF_" + playerThreeSuits.get(1) + "_TO" + "_3");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + playerFourRanks.get(1) + "_OF_" + playerFourSuits.get(1) + "_TO" + "_4");
        Thread.sleep(200);
        sendMessageAllClients("DEALING_" + dealerRanks.get(1) + "_OF_" + dealerSuits.get(1) + "_TO" + "_5");
        Thread.sleep(200);
        }
/*       for(int i =1;i<=maxPlayers;i++) {
           getPlayer(i).sendMessageToClient("DEALING_" + playerOneRanks.get(0) + "_OF_" + playerOneSuits.get(0) + "_TO" + "_1");
           getPlayer(i).sendMessageToClient("DEALING_" + dealerRanks.get(0) + "_OF_" + dealerSuits.get(0) + "_TO" + "_5");
           Thread.sleep(300);
           getPlayer(i).sendMessageToClient("DEALING_" + playerOneRanks.get(1) + "_OF_" + playerOneSuits.get(1) + "_TO" + "_1");
           getPlayer(i).sendMessageToClient("DEALING_" + dealerRanks.get(1) + "_OF_" + dealerSuits.get(1) + "_TO" + "_5");
           Thread.sleep(300);
           
           if(maxPlayers > 1){
                getPlayer(i).sendMessageToClient("DEALING_" + playerTwoRanks.get(0) + "_OF_" + playerTwoSuits.get(0) + "_TO" + "_2");
                getPlayer(i).sendMessageToClient("DEALING_" + playerThreeRanks.get(0) + "_OF_" + playerThreeSuits.get(0) + "_TO" + "_3");
                getPlayer(i).sendMessageToClient("DEALING_" + playerFourRanks.get(0) + "_OF_" + playerFourSuits.get(0) + "_TO" + "_4");
                Thread.sleep(300);
                getPlayer(i).sendMessageToClient("DEALING_" + playerTwoRanks.get(1) + "_OF_" + playerTwoSuits.get(1) + "_TO" + "_2");
                getPlayer(i).sendMessageToClient("DEALING_" + playerThreeRanks.get(1) + "_OF_" + playerThreeSuits.get(1) + "_TO" + "_3");
                getPlayer(i).sendMessageToClient("DEALING_" + playerFourRanks.get(1) + "_OF_" + playerFourSuits.get(1) + "_TO" + "_4");

                
           }
       }
*/        
    }
    private static void sendMessageAllClients(String message) {
        playerOne.sendMessageToClient(message);
        if(maxPlayers != 1){
            playerTwo.sendMessageToClient(message);
            playerThree.sendMessageToClient(message);
            playerFour.sendMessageToClient(message);
        }
    }

    private static void buildFirstHands() {
        playerOneRanks.add(rand.nextInt(13)+1);
        playerOneRanks.add(rand.nextInt(13)+1);
        playerOneSuits.add(rand.nextInt(4));
        playerOneSuits.add(rand.nextInt(4));
        
        dealerRanks.add(rand.nextInt(13)+1);
        dealerRanks.add(rand.nextInt(13)+1);
        dealerSuits.add(rand.nextInt(4));
        dealerSuits.add(rand.nextInt(4));
        
        if(maxPlayers != 1) {
            
            playerTwoRanks.add(rand.nextInt(13)+1);
            playerTwoRanks.add(rand.nextInt(13)+1);
            playerTwoSuits.add(rand.nextInt(4));
            playerTwoSuits.add(rand.nextInt(4));

            playerThreeRanks.add(rand.nextInt(13)+1);
            playerThreeRanks.add(rand.nextInt(13)+1);
            playerThreeSuits.add(rand.nextInt(4));
            playerThreeSuits.add(rand.nextInt(4));

            playerFourRanks.add(rand.nextInt(13)+1);
            playerFourRanks.add(rand.nextInt(13)+1);
            playerFourSuits.add(rand.nextInt(4));
            playerFourSuits.add(rand.nextInt(4));
        }
    }
    private static void addCard() {
        getPlayerSuitArray().add(rand.nextInt(4));
        getPlayerRanksArray().add(rand.nextInt(13)+1);
    }
    private static List<Integer> getPlayerSuitArray(){
        
        if(currentSeat == 1)
            return playerOneSuits;
        if(currentSeat == 2)
           return playerTwoSuits;
        if(currentSeat == 3)
           return playerThreeSuits;
        if(currentSeat == 4)
           return playerFourSuits;
        else
           return dealerSuits;
    }
    private static List<Integer> getPlayerRanksArray(){
        
        if(currentSeat == 1)
            return playerOneRanks;
        if(currentSeat == 2)
           return playerTwoRanks;
        if(currentSeat == 3)
           return playerThreeRanks;
        if(currentSeat == 4)
           return playerFourRanks;
        else
           return dealerRanks;
    }
    private static Session.Player getPlayer(int playersTurn) {
        if(playersTurn == 1)
            return playerOne;
        if(playersTurn == 2)
            return playerTwo;
        if(playersTurn == 3)
            return playerThree;
        if(playersTurn == 4)
            return playerFour;
        else
            return currentPlayer;
    }
    
    private static void moveToNextPlayer() {
        getPlayer(currentSeat).action = NO_ACTION;
        getPlayer(currentSeat).sendMessageToClient("DISABLE_ACTION_BUTTONS");
        if(maxPlayers == 1){
            System.out.println("Setting currentSeat to DEALER");
            currentSeat = DEALER;
        } else {
            currentSeat++;
            if(currentSeat < 5)
                getPlayer(currentSeat).sendMessageToClient("ENABLE_ACTION_BUTTONS");
            else
                currentSeat = DEALER;
        }
    }
    private static void payOrTakeBets() {
        String msg = "problem in payOrTakeBets()";
        System.out.println("Dealer: " + playerOne.dealersHandValue);
        System.out.println("Player1: " + playerOne.handValue);
        System.out.println("Dealer: " + playerOne.dealersHandValue);
        for(int i =1;i<=maxPlayers;i++){
            System.out.println("Player "+i+": " + getPlayer(i).handValue);
            if (playerOne.dealerHasBlackjack) {

                if (getPlayer(i).hasBlackjack) {
                    System.out.print("player "+ currentSeat +"has blackjack");
                    getPlayer(i).hasBlackjack = false;
                    msg = "PUSH";
                } else {
                    msg = "TAKE";
                }
            } else if (playerOne.dealerHasBlackjack == false) {
                if (getPlayer(i).hasBlackjack) {
                    getPlayer(i).hasBlackjack = false;
                    msg = "3:2";
                } else if (playerOne.dealerBusted) {
                    System.out.println("Dealer busted!");
                    if (getPlayer(i).busted == false) {
                        msg = "PAY";
                    } else {
                        msg = "TAKE";
                    }
                } else if (getPlayer(i).handValue > playerOne.dealersHandValue) {
                    msg = "PAY";
                } else if (getPlayer(i).handValue < playerOne.dealersHandValue) {
                    msg = "TAKE";
                } else {
                    msg = "PUSH";
                }
            }    
            getPlayer(i).sendMessageToClient(msg);
        }
    }

    private static void clearArrays() {
        playerOneSuits.clear();
        playerTwoSuits.clear();
        playerThreeSuits.clear();
        playerFourSuits.clear();
        dealerSuits.clear();
        playerOneRanks.clear();
        playerTwoRanks.clear();
        playerThreeRanks.clear();
        playerFourRanks.clear();
        dealerRanks.clear();
    }
}


