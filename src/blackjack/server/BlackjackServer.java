package blackjack.server;

import blackjack.client.PlayerHelper;
import java.net.ServerSocket;

public class BlackjackServer {

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        System.out.println("Blackjack Server is Running");
        try {
            while (true) {
                PlayerHelper playerOne = new PlayerHelper(listener.accept(), 1);
                System.out.println("player 1 accepted!");
                playerOne.start();
                System.out.println("player one helper thread started");
                while (true) {
                    //System.out.println("This is an infinite loop");
                    if (playerOne.isReadyForDeal()) {
                        System.out.println("in the While loop for isReadyToDeal()");
                        playerOne.sendMessageToClient("DEAL");
                        break;
                    }
                }
            }
        } finally {
            listener.close();
        }
    }
}


