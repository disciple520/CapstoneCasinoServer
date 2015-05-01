package blackjack.server;

import blackjack.client.Session;
import blackjack.server.gui.ServerUI;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;

public class BlackjackServer {
    //variable declarations
    private ServerUI serverUI;
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
    private static boolean dealerShouldPlay;
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
    
    //set up basic constructor for UI implementation and restart functionality
    public BlackjackServer() throws Exception{
        serverUI = new ServerUI(this);
        serverUI.setLocation(200, 50);
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                serverUI.setVisible(true);
            }
        });
    }
    //creates and runs the new server object
    public static void main(String[] args) throws Exception {
        BlackjackServer server = new BlackjackServer();
        server.play();
    }
    //logic for what what is happening while server is runn
    //contains all the logic for the game itself
    public void play() throws Exception {
        //sets up listener for clients to connect to
        ServerSocket listener = new ServerSocket(12345);
        System.out.println("Blackjack Server is Running");
        currentSeat = 1;
        

        //sets up listeners for new players to connect to the session, if one
        //player only only creates stuff for single player
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
                //game logic
                while (true) { // this is where all the complexity lies
                    Thread.sleep(250);
                    System.out.print("wait  ");
                    buildFirstHands();
                    //once everyone has their ebts in, sets the condition to true
                    if (checkAllPlayersReadyStatus() == true) {
                        playerOne.dealerBusted = false;
                        sendMessageAllClients("DEAL");
                        //updates label to show what everyones bets are
                        ShowPlayerBets();
                        //draws the cards on the gui
                        sendHandsToClients(); 
                        //text based commands to turn on and off buttons
                        getPlayer(currentSeat).sendMessageToClient("ENABLE_ACTION_BUTTONS");
                        //updates current turn slot
                        sendMessageAllClients("TURN_"+currentSeat);
                        Thread.sleep(1000); //give dealer time to check for blackjack
                        if (getPlayer(currentSeat).dealerHasBlackjack == false) {
                            System.out.println("Dealer does not have blackjack");
                            //while not dealers turn does logic for players
                            while (currentSeat != DEALER) {
                                Thread.sleep(250);
                                if (getPlayer(currentSeat).hasBlackjack == false) {
                                    if (getPlayer(currentSeat).busted == false) {
                                        //what happens when player hits
                                        if (getPlayer(currentSeat).action == HIT){
                                            System.out.println("Player "+ currentSeat+" hit!");
                                            addCard();
                                            //tells all clients what card to draw and where
                                            sendMessageAllClients("DEALING_" + getPlayerRanksArray().get(getPlayerRanksArray().size()-1) + "_OF_" + getPlayerSuitArray().get(getPlayerSuitArray().size()-1) + "_TO" + "_"+currentSeat);
                                            getPlayer(currentSeat).action = NO_ACTION;
                                        }
                                        //when double button is pressed it comes to here
                                        else if (getPlayer(currentSeat).action == DOUBLE) {
                                            System.out.println("Player "+ currentSeat+" doubles!");
                                            sendMessageAllClients("DEALING_" + (rand.nextInt(13)+1) + "_OF_" + (rand.nextInt(4)) + "_TO_" + currentSeat);
                                            moveToNextPlayer();
                                        }
                                        //when they press stand
                                        else if (getPlayer(currentSeat).action == STAND){
                                            System.out.println("Player "+ currentSeat+" stands!");
                                            moveToNextPlayer();
                                            
                                        }
                                        //if they bust end turn
                                    } else {
                                        moveToNextPlayer();
                                    }
                                    //if they have blackjack
                                } else {
                                    System.out.println("Player "+ currentSeat +" has blackjack");
                                    moveToNextPlayer();
                                }
                            }//dealers turn
                            while (currentSeat == DEALER) {
                                //if all players are busted, dont play
                                if (maxPlayers == 1) {
                                    dealerShouldPlay = !playerOne.busted;
                                } else if (maxPlayers == 4) {
                                    dealerShouldPlay = !(playerOne.busted && playerTwo.busted && playerThree.busted && playerFour.busted);
                                }
                                //if all players arent busted, play
                                if(dealerShouldPlay){
                                    
                                    Thread.sleep(1000);
                                    if (playerOne.dealersHandValue < 17) {
                                        System.out.println("Dealer hits");
                                        addCard();
                                        sendMessageAllClients("DEALING_" + getPlayerRanksArray().get(getPlayerRanksArray().size()-1) + "_OF_" + getPlayerSuitArray().get(getPlayerSuitArray().size()-1) + "_TO" + "_5");
                                    }
                                    //reset the current player to player 1
                                    else if (playerOne.dealerBusted) {
                                        currentSeat = PLAYER_ONE;
                                    }
                                    else {
                                        System.out.println("Dealer stands with " + playerOne.dealersHandValue);
                                        currentSeat = PLAYER_ONE;
                                    }
                                } else {
                                    currentSeat = PLAYER_ONE;
                                    sendMessageAllClients("DEALERS_TURN");
                                }
                            }
                            //when 
                            payOrTakeBets();
                            getPlayer(currentSeat).dealersHandValue = 0;
                            getPlayer(currentSeat).sendMessageToClient("DISABLE_ACTION_BUTTONS");
                        } else {
                            //if the dealer has blackjack
                            System.out.println("Dealer has blackjack");
                            currentSeat = PLAYER_ONE;
                            getPlayer(currentSeat).action = NO_ACTION;
                            sendMessageAllClients("DEALERS_TURN");
                            payOrTakeBets();
                            getPlayer(currentSeat).dealersHandValue = 0;
                            getPlayer(currentSeat).dealerHasBlackjack = false;
                            getPlayer(currentSeat).sendMessageToClient("DISABLE_ACTION_BUTTONS");
                        }
                        //resets so everyone can set bets
                        sendMessageAllClients("ENABLE_PLAY_AND_CLEAR");
                        for(int i=1;i<=maxPlayers;i++){
                            getPlayer(i).isReadyForDeal = false;
                        }
                    }
                    //clears card arrays for a fresh set
                    clearArrays();
                }
            }
        } finally {
            listener.close();
        }
    }
    //if false until all bets are placed
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
    //sends the initial 2 cards to every player, with some delay for effect
    private static void sendHandsToClients() throws InterruptedException {
        
        if(maxPlayers == 1) {
        sendMessageAllClients("DEALING_" + playerOneRanks.get(0) + "_OF_" + playerOneSuits.get(0) + "_TO" + "_1");
        Thread.sleep(300);
        sendMessageAllClients("DEALING_" + dealerRanks.get(0) + "_OF_" + dealerSuits.get(0) + "_TO" + "_5");
        Thread.sleep(300);
        sendMessageAllClients("DEALING_" + playerOneRanks.get(1) + "_OF_" + playerOneSuits.get(1) + "_TO" + "_1");
        Thread.sleep(300);
        sendMessageAllClients("DEALING_" + dealerRanks.get(1) + "_OF_" + dealerSuits.get(1) + "_TO" + "_5");
        Thread.sleep(300);
        }
        
        else if(maxPlayers == 4) {

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
    }
    //used when a message is needed to be sent to all clients
    private static void sendMessageAllClients(String message) {
        playerOne.sendMessageToClient(message);
        if(maxPlayers != 1){
            playerTwo.sendMessageToClient(message);
            playerThree.sendMessageToClient(message);
            playerFour.sendMessageToClient(message);
        }
    }
    //gets random variables for suits and ranks array for every player
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
    //when hit is called, this adds a card to proper array
    private static void addCard() {
        getPlayerSuitArray().add(rand.nextInt(4));
        getPlayerRanksArray().add(rand.nextInt(13)+1);
    }
    //gets the suit array for correct player based on their seatNumber
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
    //same as above for ranks of the cards
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
    //used to get the current player based on seatNumber or passed in int
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
    //logic to make sure the next person is set up correctly
    private static void moveToNextPlayer() {
        getPlayer(currentSeat).action = NO_ACTION;
        getPlayer(currentSeat).sendMessageToClient("DISABLE_ACTION_BUTTONS");
        if(maxPlayers == 1){
            moveToDealer();
        } else {
            currentSeat++;
            if(currentSeat < 5)
                getPlayer(currentSeat).sendMessageToClient("ENABLE_ACTION_BUTTONS");
            else
               moveToDealer();
        }
        sendMessageAllClients("TURN_"+currentSeat);
    }
    //when all players done with nothing else to do, dealers turn
    private static void moveToDealer() {
        currentSeat = DEALER;
        sendMessageAllClients("DEALERS_TURN");
    }
    //logic compares your hand to dealers to see who wins, payouts for Blackjack is 3:2
    private static void payOrTakeBets() {
        String msg = "problem in payOrTakeBets()";
        System.out.println("Dealer: " + playerOne.dealersHandValue);
        for(int i =1;i<=maxPlayers;i++){
            System.out.println("Player "+i+": " + getPlayer(i).handValue);
            if (!getPlayer(i).busted) { 
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
                        msg = "PAY";
                    } else if (getPlayer(i).handValue > playerOne.dealersHandValue) {
                        msg = "PAY";
                    } else if (getPlayer(i).handValue < playerOne.dealersHandValue) {
                        msg = "TAKE";
                    } else {
                        msg = "PUSH";
                    }
                }
            } else {
                msg = "TAKE";
                getPlayer(i).busted = false;
            }
            getPlayer(i).sendMessageToClient(msg);
        }
    }
    //clears all arrays at the end of the hand
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
    //updates player labels to include bet amount
    private static void ShowPlayerBets() {
        sendMessageAllClients("UPDATE_1_BET_"+playerOne.bet);
        if(maxPlayers != 1){
            sendMessageAllClients("UPDATE_2_BET_"+playerTwo.bet);
            sendMessageAllClients("UPDATE_3_BET_"+playerThree.bet);
            sendMessageAllClients("UPDATE_4_BET_"+playerFour.bet);
        }
    }
//grabbed from http://java.dzone.com/articles/programmatically-restart-java
//took out the runnable since it wasnt needed and closes clients that are open
//and connected to the server, restart server
public static final String SUN_JAVA_COMMAND = "sun.java.command";
/**
 * Restart the current Java application
 * @throws IOException
 */
    public static void restartApplication() throws IOException {
        sendMessageAllClients("CLOSE");
	try {
            // java binary
            String java = System.getProperty("java.home") + "/bin/java";
            // vm arguments
            List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            StringBuffer vmArgsOneLine = new StringBuffer();
            for (String arg : vmArguments) {
                // if it's the agent argument : we ignore it otherwise the
                // address of the old application and the new one will be in conflict
                if (!arg.contains("-agentlib")) {
                    vmArgsOneLine.append(arg);
                    vmArgsOneLine.append(" ");
                }
            }
            // init the command to execute, add the vm args
            final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

            // program main and program arguments
            String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
            // program main is a jar
            if (mainCommand[0].endsWith(".jar")) {
                // if it's a jar, add -jar mainJar
                cmd.append("-jar " + new File(mainCommand[0]).getPath());
            } else {
                // else it's a .class, add the classpath and mainClass
                cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
            }
            // finally add program arguments
            for (int i = 1; i < mainCommand.length; i++) {
                cmd.append(" ");
                cmd.append(mainCommand[i]);
            }
            // execute the command in a shutdown hook, to be sure that all the
            // resources have been disposed before restarting the application
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Runtime.getRuntime().exec(cmd.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            // exit

            System.exit(0);
	} catch (Exception e) {
            // something went wrong
            throw new IOException("Error while trying to restart the application", e);
	}
    }

    public void setPlayers(int i) {
        maxPlayers = i;
    }
}

