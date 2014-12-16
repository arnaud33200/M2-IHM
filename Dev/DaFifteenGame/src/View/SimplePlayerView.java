/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Modele.GameEvent;
import Modele.GameListener;
import Modele.GameModel;
import Modele.Player;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

/**
 *
 * @author ladoucar
 */
public class SimplePlayerView extends javax.swing.JPanel implements GameView{
    private final Player player;
    private final GameModel model;

    /**
     * Creates new form SimplePlayerView
     */
    public SimplePlayerView(GameModel m, Player p) {
        initComponents();
        Color c = p.getColor();
        setBackground(bighterColor(c));
        player = p;
        model = m;
        jLabel1.setText(player.getName());
        jPanel1.setLayout(new FlowLayout());
       //setLayout(new FlowLayout());
        
        model.addGameListener(new GameListener() {

            @Override
            public void GamePlayerChanged(GameEvent e) {
                Player pp = (Player) e.getSource();
                if (p.equals(pp)) {
                    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16));
                } else {
                    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10));
                }
            }

            @Override
            public void GameIsOver(GameEvent e) {
                
            }

            @Override
            public void GamehasBeggun(GameEvent e) {
                
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMinimumSize(new java.awt.Dimension(500, 100));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setText("PLAYER 1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 105, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void notify(GameModel m) {
        ArrayList<Integer> l = player.getJetonsCollectes();
        jPanel1.removeAll();
        for (Integer i : l){
            String txt = "" + i;
            shapelabel.ShapeLabel sl = new shapelabel.ShapeLabel(txt, Color.BLACK, player.getColor());
            sl.setBounds(0, 0, 40, 40);
            jPanel1.add(sl);
        }
        jPanel1.revalidate();
        repaint();
    }

    private Color bighterColor(Color c) {
        int r = (c.getRed() + 100);
        int g = (c.getGreen()+ 100);
        int b = (c.getBlue() + 100);
        if (r>255) r=255;
        if (g>255) g=255;
        if (b>255) b=255;
        return new Color(r, g, b);
    }
}