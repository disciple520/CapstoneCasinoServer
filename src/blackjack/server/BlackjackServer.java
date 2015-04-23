package blackjack.server;

import blackjack.client.Session;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import blackjack.core.Blackjack;

public class BlackjackServer {

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        System.out.println("Blackjack Server is Running");
        List<Session> playerHelpers = new ArrayList<Session>();
        Blackjack blackjack = new Blackjack();
        int currentPlayer = 1;
        try {
            while (true) {
                Session session = new Session();
                Session.Player playerOne = session.new Player(listener.accept(), 1);
                System.out.println("player 1 accepted!");
                playerOne.start();
                System.out.println("player one helper thread started");
                Session.Player playerTwo = session.new Player(listener.accept(), 2);
                System.out.println("player 2 accepted!");
                playerTwo.start();
                System.out.println("player 2 helper thread started");
                Session.Player playerThree = session.new Player(listener.accept(), 3);
                System.out.println("player 3 accepted!");
                playerThree.start();
                System.out.println("player 3 helper thread started");
                Session.Player playerFour = session.new Player(listener.accept(), 4);
                System.out.println("player 4 accepted!");
                playerFour.start();
                System.out.println("player 4 helper thread started");
                
                while (true) {
                    Thread.sleep(3000);
                    System.out.print("wait  ");
                    if (currentPlayer == 1 && playerOne.activeTurn()) {
                        currentPlayer++;
                        System.out.println("Player 1's turn");
                        playerOne.sendMessageToClient("DEAL");
                        playerOne.setActive(false);
                        blackjack.dealCards(playerOne);
                        playerTwo.enableButton();
                    }
                    if (currentPlayer == 2 && playerTwo.activeTurn()) {
                        currentPlayer++;
                        System.out.println("Player "+currentPlayer+"'s turn");
                        playerTwo.sendMessageToClient("DEAL");
                        playerTwo.setActive(false);
                        blackjack.dealCards(playerTwo);
                        playerThree.enableButton();
                    }
                    if (currentPlayer == 3 && playerThree.activeTurn()) {
                        currentPlayer++;
                        System.out.println("Player "+currentPlayer+"'s turn");
                        playerThree.sendMessageToClient("DEAL");
                        playerThree.setActive(false);
                        blackjack.dealCards(playerThree);
                        playerFour.enableButton();
                    }
                    if (currentPlayer == 4 && playerFour.activeTurn()) {
                        currentPlayer = 1;
                        System.out.println("Player "+currentPlayer+"'s turn");
                        playerFour.sendMessageToClient("DEAL");
                        playerFour.setActive(false);
                        blackjack.dealCards(playerFour);
                        playerOne.enableButton();
                    }
                }
            }
        } finally {
            listener.close();
        }
    }
}


