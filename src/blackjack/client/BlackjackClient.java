package blackjack.client;

import blackjack.gui.CapstoneCasinoBlackjackUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Jerry
 */
public class BlackjackClient {
    public static void main(String[] args) throws IOException {
        
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        Socket serverSocket = new Socket(serverAddress, 9090);
        
        BufferedReader readFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        PrintWriter writeToServer = new PrintWriter(serverSocket.getOutputStream(), true);
        String successMessage = readFromServer.readLine();
        JOptionPane.showMessageDialog(null, successMessage);
        
        CapstoneCasinoBlackjackUI blackjackUI = new CapstoneCasinoBlackjackUI();
        blackjackUI.setVisible(true);
        
        //System.exit(0);
    }
}
