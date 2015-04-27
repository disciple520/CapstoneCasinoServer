/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.core.Card;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Hellz
 */
public class CardHolder extends JPanel {
    private int cardSpot;
    private int xCoor;
    private int yCoor;
    
    public CardHolder() {
        setOpaque(false);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     
    }
}
