/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.core;

import javax.swing.SwingUtilities;
import blackjack.gui.CapstoneCasinoBlackjackUI;

/**
 *
 * @author Hellz
 */
public class BlackjackController {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new CapstoneCasinoBlackjackUI().setVisible(true);
            }
            
        });
    }
}

