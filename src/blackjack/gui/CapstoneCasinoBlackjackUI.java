package blackjack.gui;

import blackjack.client.BlackjackClient;
import blackjack.core.Card;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import blackjack.server.BlackjackServer;
import java.awt.Image;

import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingWorker;


/**
 *
 * @author Patrick
 */
public class CapstoneCasinoBlackjackUI extends javax.swing.JFrame implements ActionListener{
    
    private static final int PLAYER_1_CARD_1 = 1;
    
    
    
    private Image cardImages;
    Card card;
    BlackjackClient blackjackClient;
    BetStakeUpdater betStakeUpdate = new BetStakeUpdater();
    
    /**
     * Creates new form CapstoneCasinoBlackjackUI
     */
    public CapstoneCasinoBlackjackUI(BlackjackClient blackjackClient) {
        this.blackjackClient = blackjackClient;
             PanelHolderSetup();
        initComponents();
   
    }

    /**
     *
     * @param card
     * @param cardHolder
     */
    public void swingWorkerCardDraw(Card card, CardHolder cardHolder) {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            
            @Override
            protected Void doInBackground() throws Exception {               
                return null;
                
            }
            @Override
            protected void done() {
                CardPanel newCard = new CardPanel(card);
                cardHolder.add(newCard);
                newCard.setLocation(cardHolder.getComponentCount()*20,0);
                System.out.println(cardHolder.getComponentCount());
            }


        };
        worker.execute();
    }
    private void swingWorkerBet(int value) {
        int passed = value;
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            
            @Override
            protected Void doInBackground() throws Exception {
                return null;
                
            }
            @Override
            protected void done() {
                System.out.println("You have finished the swing worker for betting");
                betLabel.setText(Integer.toString(passed));
            }


        };
        worker.execute();
    }
    private void swingWorkerStake(int value) {
        int passed = value;
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            
            @Override
            protected Boolean doInBackground() throws Exception {
                return false;
                
            }
            @Override
            protected void done() {
                System.out.println("You have finished the swing worker for stake update");
                stakeLabel.setText(Integer.toString(passed));
            }


        };
        worker.execute();
    }
    private void PanelHolderSetup() {
        CardHolder CardHolderPlayer1 = new CardHolder();
        CardHolderPlayer1.setLayout(null);
        CardHolderPlayer1.setBounds(70 ,160 ,205 ,125);
        
        CardHolder CardHolderPlayer2 = new CardHolder();
        CardHolderPlayer2.setLayout(null);
        CardHolderPlayer2.setBounds(300, 330,205 ,125);
        
        CardHolder CardHolderPlayer3 = new CardHolder();
        CardHolderPlayer3.setLayout(null);
        CardHolderPlayer3.setBounds(590,330,205,125);
        
        CardHolder CardHolderPlayer4 = new CardHolder();
        CardHolderPlayer4.setLayout(null);
        CardHolderPlayer4.setBounds(790,160,205,125);
        
        CardHolder CardHolderDealer = new CardHolder();
        CardHolderDealer.setLayout(null);
        CardHolderDealer.setBounds(400,100,205,125);
        
       
        getContentPane().add(CardHolderPlayer1);
        getContentPane().add(CardHolderPlayer2);
        getContentPane().add(CardHolderPlayer3);
        getContentPane().add(CardHolderPlayer4);
        getContentPane().add(CardHolderDealer);
    
        
        
    //All this code is used for testing, but is set to be put and created from
    // swingWorkerCardDraw(Card card, CardHolder cardHolder)
        Card aceOfSpades = new Card(3,1,true);
        Card playertest1 = new Card(3,2,true);
        Card playertest2 = new Card(3,3,true);
        Card playertest3 = new Card(3,4,true);
        Card playertest4 = new Card(3,11,true);
        
        CardPanel testCardPanel = new CardPanel(aceOfSpades);
        CardPanel player1testPanel = new CardPanel(playertest1);
        CardPanel player2testPanel = new CardPanel(playertest2);
        CardPanel player3testPanel = new CardPanel(playertest3);
        CardPanel player4testPanel = new CardPanel(playertest4);
        
        CardHolderPlayer3.add(testCardPanel);
        testCardPanel.setLocation(CardHolderPlayer3.getComponentCount()*20,0);
        
        CardHolderPlayer3.add(player1testPanel);
        player1testPanel.setLocation(CardHolderPlayer3.getComponentCount()*20,0);
        
        CardHolderPlayer3.add(player2testPanel);
        player2testPanel.setLocation(CardHolderPlayer3.getComponentCount()*20,0);
        
        CardHolderPlayer3.add(player3testPanel);
        player3testPanel.setLocation(CardHolderPlayer3.getComponentCount()*20,0);
        
        CardHolderPlayer3.add(player4testPanel);
        player4testPanel.setLocation(CardHolderPlayer3.getComponentCount()*20,0);
        System.out.println(CardHolderPlayer3.getComponentCount());
/*        
        Card playertest2 = new Card(3,3,true);
        Card playertest3 = new Card(3,4,true);
        Card playertest4 = new Card(3,11,true);
        CardPanel testCardPanel = new CardPanel(aceOfSpades);
        CardPanel player1testPanel = new CardPanel(playertest1);
        CardPanel player2testPanel = new CardPanel(playertest2);
        CardPanel player3testPanel = new CardPanel(playertest3);
        CardPanel player4testPanel = new CardPanel(playertest4);
                
        CardHolderPlayer4.add(player1testPanel);
        CardHolderPlayer3.add(player2testPanel);
        CardHolderPlayer2.add(player3testPanel);
        CardHolderPlayer1.add(player4testPanel);
        System.out.println(testCardPanel.getLocation().toString());
*/        
        pack();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        betCheck1 = new javax.swing.JButton();
        betCheck5 = new javax.swing.JButton();
        betCheck25 = new javax.swing.JButton();
        betCheck50 = new javax.swing.JButton();
        betCheck100 = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        hitButton = new javax.swing.JButton();
        doubleButton = new javax.swing.JButton();
        standButton = new javax.swing.JButton();
        betLabel = new javax.swing.JLabel();
        stakeLabel = new javax.swing.JLabel();
        timerLabel = new javax.swing.JLabel();
        player4Label = new javax.swing.JLabel();
        player3Label = new javax.swing.JLabel();
        player1Label = new javax.swing.JLabel();
        player2Label = new javax.swing.JLabel();
        backGroundGraphic = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Capstone Casino Blackjack");
        setBackground(new java.awt.Color(0, 0, 0));
        setBounds(new java.awt.Rectangle(0, 0, 1000, 650));
        setMinimumSize(new java.awt.Dimension(1015, 690));
        setPreferredSize(new java.awt.Dimension(1015, 690));
        getContentPane().setLayout(null);

        betCheck1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/oneDChipNormal.png"))); // NOI18N
        betCheck1.setBorderPainted(false);
        betCheck1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        betCheck1.setFocusPainted(false);
        betCheck1.setMultiClickThreshhold(200L);
        betCheck1.setName("betCheckOneDollar"); // NOI18N
        betCheck1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/oneDChipClick.png"))); // NOI18N
        betCheck1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneDChipClick(evt);
            }
        });
        getContentPane().add(betCheck1);
        betCheck1.setBounds(10, 533, 90, 90);

        betCheck5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/fiveDChipNormal.png"))); // NOI18N
        betCheck5.setBorderPainted(false);
        betCheck5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        betCheck5.setFocusPainted(false);
        betCheck5.setMultiClickThreshhold(200L);
        betCheck5.setName("betCheckFiveDollars"); // NOI18N
        betCheck5.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/fiveDChipClick.png"))); // NOI18N
        betCheck5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiveDChipClick(evt);
            }
        });
        getContentPane().add(betCheck5);
        betCheck5.setBounds(100, 533, 90, 90);

        betCheck25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/twentyfiveDChipNormal.png"))); // NOI18N
        betCheck25.setBorderPainted(false);
        betCheck25.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        betCheck25.setFocusPainted(false);
        betCheck25.setMultiClickThreshhold(200L);
        betCheck25.setName("betCheckTwentyFiveDollars"); // NOI18N
        betCheck25.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/twentyfiveDChipClick.png"))); // NOI18N
        betCheck25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twentyfiveDChipClick(evt);
            }
        });
        getContentPane().add(betCheck25);
        betCheck25.setBounds(190, 533, 90, 90);

        betCheck50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/fiftyDChipNormal.png"))); // NOI18N
        betCheck50.setBorderPainted(false);
        betCheck50.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        betCheck50.setFocusPainted(false);
        betCheck50.setMultiClickThreshhold(20L);
        betCheck50.setName("betCheckFiftyDollars"); // NOI18N
        betCheck50.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/fiftyDChipClick.png"))); // NOI18N
        betCheck50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiftyDChipClick(evt);
            }
        });
        getContentPane().add(betCheck50);
        betCheck50.setBounds(280, 533, 90, 90);

        betCheck100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/hundredDChipNormal.png"))); // NOI18N
        betCheck100.setBorderPainted(false);
        betCheck100.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        betCheck100.setFocusPainted(false);
        betCheck100.setMultiClickThreshhold(200L);
        betCheck100.setName("betCheckOneHundredDollars"); // NOI18N
        betCheck100.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/hundredDChipClick.png"))); // NOI18N
        betCheck100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hundredDChipClick(evt);
            }
        });
        getContentPane().add(betCheck100);
        betCheck100.setBounds(370, 533, 90, 90);

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/playNormal.png"))); // NOI18N
        playButton.setBorderPainted(false);
        playButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        playButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/playGray.png"))); // NOI18N
        playButton.setFocusPainted(false);
        playButton.setMultiClickThreshhold(200L);
        playButton.setName("playButton"); // NOI18N
        playButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/playClick.png"))); // NOI18N
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        getContentPane().add(playButton);
        playButton.setBounds(750, 520, 100, 30);

        clearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/clearNormal.png"))); // NOI18N
        clearButton.setBorderPainted(false);
        clearButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clearButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/clearGray.png"))); // NOI18N
        clearButton.setFocusPainted(false);
        clearButton.setMultiClickThreshhold(200L);
        clearButton.setName("clearButton"); // NOI18N
        clearButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/clearClick.png"))); // NOI18N
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonClicked(evt);
            }
        });
        getContentPane().add(clearButton);
        clearButton.setBounds(750, 560, 100, 30);

        hitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/hitNormal.png"))); // NOI18N
        hitButton.setBorderPainted(false);
        hitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hitButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/hitGray.png"))); // NOI18N
        hitButton.setFocusPainted(false);
        hitButton.setMultiClickThreshhold(200L);
        hitButton.setName("hitButton"); // NOI18N
        hitButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/hitClick.png"))); // NOI18N
        hitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hitButtonClicked(evt);
            }
        });
        getContentPane().add(hitButton);
        hitButton.setBounds(870, 520, 100, 30);

        doubleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/doubleNormal.png"))); // NOI18N
        doubleButton.setBorderPainted(false);
        doubleButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        doubleButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/doubleGray.png"))); // NOI18N
        doubleButton.setFocusPainted(false);
        doubleButton.setMultiClickThreshhold(200L);
        doubleButton.setName("doubleButton"); // NOI18N
        doubleButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/doubleClick.png"))); // NOI18N
        doubleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleButtonClicked(evt);
            }
        });
        getContentPane().add(doubleButton);
        doubleButton.setBounds(870, 560, 100, 30);

        standButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/standNormal.png"))); // NOI18N
        standButton.setBorderPainted(false);
        standButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        standButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/standGray.png"))); // NOI18N
        standButton.setFocusPainted(false);
        standButton.setMultiClickThreshhold(200L);
        standButton.setName("standButton"); // NOI18N
        standButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/standClick.png"))); // NOI18N
        standButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                standButtonClicked(evt);
            }
        });
        getContentPane().add(standButton);
        standButton.setBounds(870, 600, 100, 30);

        betLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        betLabel.setToolTipText("");
        betLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(betLabel);
        betLabel.setBounds(640, 520, 80, 40);

        stakeLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        stakeLabel.setText("1500");
        stakeLabel.setToolTipText("");
        getContentPane().add(stakeLabel);
        stakeLabel.setBounds(640, 590, 80, 30);

        timerLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        timerLabel.setForeground(new java.awt.Color(51, 255, 51));
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("1:00");
        timerLabel.setToolTipText("");
        getContentPane().add(timerLabel);
        timerLabel.setBounds(750, 600, 100, 30);

        player4Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/player1.png"))); // NOI18N
        player4Label.setText("jLabel1");
        player4Label.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/player1Gray.png"))); // NOI18N
        player4Label.setEnabled(false);
        getContentPane().add(player4Label);
        player4Label.setBounds(90, 170, 100, 100);

        player3Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/player2.png"))); // NOI18N
        player3Label.setText("jLabel1");
        player3Label.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/player2Gray.png"))); // NOI18N
        player3Label.setEnabled(false);
        getContentPane().add(player3Label);
        player3Label.setBounds(330, 330, 80, 110);

        player1Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/player4.png"))); // NOI18N
        player1Label.setText("jLabel1");
        player1Label.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/player4Gray.png"))); // NOI18N
        getContentPane().add(player1Label);
        player1Label.setBounds(810, 170, 100, 100);

        player2Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/player3.png"))); // NOI18N
        player2Label.setText("jLabel1");
        player2Label.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/player3Gray.png"))); // NOI18N
        player2Label.setEnabled(false);
        getContentPane().add(player2Label);
        player2Label.setBounds(600, 330, 90, 100);

        backGroundGraphic.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        backGroundGraphic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackjack/gui/BlackjackBackground.png"))); // NOI18N
        backGroundGraphic.setFocusable(false);
        backGroundGraphic.setName("backGroundGraphic"); // NOI18N
        backGroundGraphic.setNextFocusableComponent(betCheck1);
        backGroundGraphic.setOpaque(true);
        backGroundGraphic.setRequestFocusEnabled(false);
        backGroundGraphic.setVerifyInputWhenFocusTarget(false);
        getContentPane().add(backGroundGraphic);
        backGroundGraphic.setBounds(0, 0, 1000, 650);
        backGroundGraphic.getAccessibleContext().setAccessibleDescription("");

        getAccessibleContext().setAccessibleDescription("Capstone Casino Blackjack");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void hundredDChipClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hundredDChipClick
        System.out.print("100 Dollar Chip Clicked!\n");
        swingWorkerBet(betStakeUpdate.updateBet(100));
    }//GEN-LAST:event_hundredDChipClick

    private void oneDChipClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneDChipClick
         System.out.print("One Dollar Chip Clicked!\n");
         swingWorkerBet(betStakeUpdate.updateBet(1));
    }//GEN-LAST:event_oneDChipClick

    private void fiveDChipClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiveDChipClick
        System.out.print("Five Dollar Chip Clicked!\n");
        swingWorkerBet(betStakeUpdate.updateBet(5));
    }//GEN-LAST:event_fiveDChipClick

    private void twentyfiveDChipClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twentyfiveDChipClick
        System.out.print("25 Dollar Chip Clicked!\n");
        swingWorkerBet(betStakeUpdate.updateBet(25));
    }//GEN-LAST:event_twentyfiveDChipClick

    private void fiftyDChipClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiftyDChipClick
        System.out.print("50 Dollar Chip Clicked!\n");
        swingWorkerBet(betStakeUpdate.updateBet(50));
    }//GEN-LAST:event_fiftyDChipClick

    private void clearButtonClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonClicked
       swingWorkerBet(betStakeUpdate.resetBet());
    }//GEN-LAST:event_clearButtonClicked

    private void hitButtonClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hitButtonClicked

    }//GEN-LAST:event_hitButtonClicked

    private void doubleButtonClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doubleButtonClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_doubleButtonClicked

    private void standButtonClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_standButtonClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_standButtonClicked

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        swingWorkerStake(betStakeUpdate.updateStake());
        blackjackClient.sendMessageToServer("PLAY");
    }//GEN-LAST:event_playButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backGroundGraphic;
    private javax.swing.JButton betCheck1;
    private javax.swing.JButton betCheck100;
    private javax.swing.JButton betCheck25;
    private javax.swing.JButton betCheck5;
    private javax.swing.JButton betCheck50;
    public javax.swing.JLabel betLabel;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton doubleButton;
    private javax.swing.JButton hitButton;
    public javax.swing.JButton playButton;
    private javax.swing.JLabel player1Label;
    private javax.swing.JLabel player2Label;
    private javax.swing.JLabel player3Label;
    private javax.swing.JLabel player4Label;
    private javax.swing.JLabel stakeLabel;
    private javax.swing.JButton standButton;
    private javax.swing.JLabel timerLabel;
    // End of variables declaration//GEN-END:variables

    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
