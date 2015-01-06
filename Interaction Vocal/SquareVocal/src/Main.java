import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import fr.dgac.ivy.BindType;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ladoucar
 */
public class Main extends javax.swing.JFrame {

    private static Ivy bus;
    private static HashMap<String, IvyMessageListener> bindings;
    private static IvyMessageListener daListener;
    private static String pos = "";
    private static Double c = new Double(1);
    private static Point p;
    private static Timer t = new Timer(200, null);

    private static void addNewBinding(String filter, IvyMessageListener l) {
        bindings.put(filter, l);
        try {
            bus.bindAsyncMsg(filter, l, BindType.SWING);
        } catch (IvyException ex) {
            //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        
        t.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (pos.contains("haut"))
                    p.y -= 5;
                else if (pos.contains("bas"))
                    p.y += 5;
                else if (pos.contains("droite"))
                    p.x += 5;
                else if (pos.contains("gauche"))
                    p.x -= 5;

                if (p.x > 638-25) p.x = 638-25;
                if (p.y > 543-25) p.y = 543-25;
                if (p.x < 0) p.x = 0;
                if (p.y < 0) p.y = 0;
                repaint();
            }
        });
        
        t.start();
        
        p = new Point(300, 300);
        repaint();
        bindings = new HashMap<String, IvyMessageListener>();
        bus = new Ivy("TEST", "HEllo i'm mister TEST", null);
        daListener = new IvyMessageListener() {
            
            @Override
            public void receive(IvyClient ic, String[] strings) {
                String msg = strings[0];
                pos = msg.substring(msg.indexOf("Position:"));
                pos = pos.substring(pos.indexOf(":")+1, pos.indexOf(" "));
                String conf = msg.substring(msg.indexOf("Confidence="));
                conf = conf.substring(conf.indexOf("=")+1, conf.indexOf(" "));
                conf = conf.replace(",", ".");
                c = Double.parseDouble(conf);
                jLabel1.setText(" - " + pos + " / " + c);
                
                
            }

        };
        try {
            bus.start("127.255.255.255:666");
        } catch (IvyException ex) {
            System.out.println("J'AI RECUT un bug");
// Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        addNewBinding("Action:deplacement(.*)", daListener);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        int v = (int) (255 - (254 * c ));
        Color color = new Color(v,v , v);
        g.setColor(color);
        g.fillRect(p.x, p.y, 50, 50);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(518, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
