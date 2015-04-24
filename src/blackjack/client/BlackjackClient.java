/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.client;

import blackjack.core.Card;
import blackjack.gui.CapstoneCasinoBlackjackUI;
import blackjack.gui.CardPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class BlackjackClient {

    private static int PORT = 12345;
    private Socket socket;
    private BufferedReader inputFromServer;
    private PrintWriter outputToServer;
    private CapstoneCasinoBlackjackUI gui;
    private Image cardImages;

    /**
     * Constructs the client by connecting to a server and laying out the gui
     */
    public BlackjackClient(String serverAddress) throws Exception {

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
                response = inputFromServer.readLine();
                System.out.println("This just in: " + response);
                if (response.equals("DEAL")) {
                   System.out.println("response = DEAL");
                   gui.playButton.setEnabled(false);
                }
                if (response.equals("NOTURN")) {
                   gui.playButton.setEnabled(false); 
                }
                if(response.equals("ENABLE")){
                    gui.playButton.setEnabled(true);
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
        System.out.println("sendMessageToServer Fired! (" + messageFromGui + ")");
        outputToServer.println(messageFromGui);
    }

    public void sendBetToServer(int bet) {
        outputToServer.println(bet);
    }   
}
