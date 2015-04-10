/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Jerry
 */
public class BlackjackController {
    public static void main(String[] args) throws IOException {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        Socket s = new Socket(serverAddress, 9090);
        BufferedReader inputFromServer =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
        String successMessage = inputFromServer.readLine();
        JOptionPane.showMessageDialog(null, successMessage);
        System.exit(0);
    }
}
