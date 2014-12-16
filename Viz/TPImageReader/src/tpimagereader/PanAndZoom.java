/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpimagereader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author ladoucar
 */
public class PanAndZoom {
   
    BufferedImage img;
    
    public PanAndZoom() throws IOException {
        init();
    }
    
    public void init() throws IOException {
        JFrame jf = new JFrame("YEAH");;
        PanAndZoom.SCanvas sc = new PanAndZoom.SCanvas();
        sc.setPreferredSize(new java.awt.Dimension(1200,800));
        
       
        jf.getContentPane().add(sc);
        jf.pack(); // ?
        jf.setResizable(true);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        sc.setFocusable(true);
        sc.requestFocusInWindow();
        jf.addMouseListener(sc);
        jf.addMouseMotionListener(sc);
        jf.addMouseWheelListener(sc);
        jf.repaint();
    }
    
    class SCanvas extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {

         private ArrayList<Square> squares;
         private int panX, panY;
         private double zoom;
        private MouseEvent previousMouse;
        
        public SCanvas() {
            super();
            panX = 0;
            panY = 0;
            zoom = 1;
            squares = new ArrayList<>();
            Random r = new Random();
            for (int i=0; i < 251; ++i) {
                Square s = new Square();
                squares.add(s);
            }
        }

        @Override
        public void paint(Graphics g_) {
            Graphics2D g = (Graphics2D) g_;
            
            for (Square s : squares) {
                g.setColor(s.c);
                g.fillRect(fX(s.x), fY(s.y), fW(s.w), fW(s.w));
                g.drawString("" + s.x + " - " + s.y,fX(s.x), fY(s.y) );
            }
            g.setColor(Color.BLACK);
            g.drawString("PAN X = " + panX, 0, 15);
            g.drawString("PAN Y = " + panY, 0, 30);
            g.drawString("ZOOM = " + zoom, 0, 45);
            if (previousMouse != null) {
                g.drawString("" + gX(previousMouse.getX()) + " - " + gY(previousMouse.getY()), previousMouse.getX(), previousMouse.getY());
            }
            g.drawRect((int)(panX*zoom), (int)(panY*zoom), (int)(getWidth()*zoom), (int)(getHeight()*zoom));
        }
        
        private int fX(int x) {
            return (int) ((x + panX) * zoom);
        }
        
        private int fY(int y) {
            return (int) ((y + panY) * zoom);
        }
        
        private int fW(int w) {
            return (int) (w * zoom);
        }

        private int gX(int x) {
            return (int) ((x / zoom) - panX);
        }
        
        private int gY(int y) {
            return (int) ((y / zoom) - panY);
        }
        
        private int gW(int w){
            return (int) (w / zoom);   
        }

        
         @Override
        public void mousePressed(MouseEvent e) {
//            System.out.println("MOUSE Pressed");
            previousMouse = e;
            repaint();
            
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            //System.out.println("MOUSE DRAG");
            if (previousMouse != null) {
                int dX = previousMouse.getX() - e.getX();
                int dY = previousMouse.getY() - e.getY();
                panX -= dX / zoom;
                panY -= dY / zoom;
                previousMouse = e;
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //System.out.println("MOUSE RELEASE");
            previousMouse = null;
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            System.out.println("MOUSE WHEEL MOVED");
            int r = e.getWheelRotation();
            if (r > 0) {
                zoom -= 0.03;
            } else {
                zoom += 0.03;
            }
            /*panX -= (gX(e.getX()) - e.getX()) / zoom;
            panY -= (gY(e.getY()) - e.getY()) / zoom;*/
             
            repaint();
        }
        
         @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            
        }

    }
}
