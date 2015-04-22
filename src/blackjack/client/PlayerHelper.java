package blackjack.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

    /**
     * The class for the helper threads in this multithreaded server
     * application. 
     */
    public class PlayerHelper extends Thread {
        Socket socket;
        BufferedReader inputFromClient;
        PrintWriter outputToClient;
        int playerNumber;
        boolean isReadyForDeal = false;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields
         */
        public PlayerHelper(Socket socket, int player) {
            this.socket = socket;
            this.playerNumber = player;
            System.out.println("We created a player helper!");
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

                // Repeatedly get commands from the client and processes them.
                while (true) {
                    String command = inputFromClient.readLine();
                    System.out.println("This just in: " + command);
                    if (command.equals("PLAY")) {
                        System.out.println("command = PLAY");
                        isReadyForDeal = true;
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
       
        public boolean isReadyForDeal() {
            return isReadyForDeal;
        }
        
        public void sendMessageToClient(String messageToClient) {
            System.out.println("sendMessageToClient Fired! (" + messageToClient + ")");
            outputToClient.println(messageToClient);
        }
    }
