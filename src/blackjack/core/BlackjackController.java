/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.core;

import javax.swing.SwingUtilities;
import blackjack.gui.CapstoneCasinoBlackjackUI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Hellz
 */
public class BlackjackController {
    
    public static void main(String[] args) {
        CapstoneCasinoBlackjackUI gui = new CapstoneCasinoBlackjackUI();
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                gui.setVisible(true);
            }
            
        });
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    