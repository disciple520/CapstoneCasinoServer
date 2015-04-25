package blackjack.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import blackjack.core.Blackjack;
import blackjack.core.Hand;
import java.util.logging.Level;
import java.util.logging.Logger;

    /**
     * The class for the helper threads in this multithreaded server
     * application. 
     */
public class Session {
    
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
    Session currentPlayer;
    

    public class Player extends Thread {
        Socket socket;
        BufferedReader inputFromClient;
        PrintWriter outputToClient;
        int playerNumber;
        int currentPlayer;
        boolean activeTurn = false;
        public boolean isReadyForDeal;
        public boolean busted;
        public boolean dealerHasBlackjack;
        public Hand hand;
        public int bet;
        public int action;
        public int handValue;
        public int dealersHandValue;
     

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields
         */
        public Player(Socket socket, int player) {
            this.socket = socket;
            this.playerNumber = player;
            System.out.println("We created a player!");
            try {
                inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputToClient = new PrintWriter(socket.getOutputStream(), true);
                outputToClient.println("Welcome Player " + player);
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }
      
        /**
         * The run method of this thread.
         */
        public void run() {
            try {
                outputToClient.println("Connected to Server");
                if(playerNumber == 1){
                    System.out.println("Player 1 goes first");
                    currentPlayer = PLAYER_ONE;
                }
                outputToClient.println("SET_PLAYER_" + playerNumber);
                
                // Repeatedly get commands from the client and processes them.
                while (true) {
                    if (inputFromClient.ready()) {
                        String command = inputFromClient.readLine();
                        System.out.println("This just in: " + command);
                        if (command != null && command.startsWith("PLAY_FOR_")) {
                            bet = Integer.parseInt(command.substring(9));
                            outputToClient.println("DEAL");
                            isReadyForDeal = true;
                            System.out.println("command = PLAY from Player " + playerNumber + " for $" + bet);
                        }
                        else if (command.equals("HIT")) {
                            System.out.println("Action being set to HIT");
                            action = HIT;
                        }
                        else if (command.equals("STAND")) {
                            action = STAND;
                            System.out.println("Action being set to STAND");
                        }
                        else if (command.startsWith("DEALERS_HAND_VALUE_IS_")) {
                            dealersHandValue = Integer.parseInt(command.substring(22));
                        }
                        else if (command.startsWith("PLAYER")) {
                            handValue = Integer.parseInt(command.substring(22));
                        }
                        else if (command.equals("BLACKJACK_FOR_DEALER")) {
                            dealerHasBlackjack = true;
                        }
                    } else {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
       
        public boolean activeTurn() {
            return activeTurn;
        }
        public void setActive(boolean b){
            activeTurn = b;
        }
        public void enableButton(){
            outputToClient.println("ENABLE");
        }
                
        
        public void sendMessageToClient(String messageToClient) {
            System.out.println("sendMessageToClient Fired! (" + messageToClient + ")");
            outputToClient.println(messageToClient);
        }
    }
}
