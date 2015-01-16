/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpimagereader;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author ladoucar
 */
public final class BezierCurve {
     
    BufferedImage img;
    
    public BezierCurve() throws IOException {
        init();
    }
    
    public void init() throws IOException {
        JFrame jf = new JFrame("YEAH");
        SCanvas sc = new SCanvas();
        sc.setPreferredSize(new java.awt.Dimension(1000,600));
        
        jf.getContentPane().add(sc);
        jf.pack(); // ?
        jf.setResizable(true);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        sc.setFocusable(true);
        sc.requestFocusInWindow();
        jf.addMouseListener(sc);
        jf.addMouseMotionListener(sc);
        jf.repaint();
    }
    
    class SCanvas extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

        private Point p0 = new Point(115,115);
        private Point p1 = new Point(115,330);
        private Point p2 = new Point(300,130);
        private Point p3 = new Point(350,400);
        private Point selectedPoint = null;
        
        public SCanvas() {
            super();
        }

        @Override
        public void paint(Graphics g_) {
            Graphics2D g = (Graphics2D) g_;
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            //drawSpiral(g, 150, 100, 200,200);
            //drawCircle(g, 150, 100, 200,200);
           // drawOval(g, 150, 100, 200, 200, 300, 150);
            drawBezierCurve(g, p0, p1, p2, p3);
        }
        
        private void drawCircle(Graphics2D g, int r, int s, int px, int py) {
            double pi = Math.PI;
                double step = 2*pi / s;
                g.setColor(Color.BLACK);
                boolean first = true;
                Point p = null;
                Point fp = new Point();
                for (double t=0; t < 2*pi; t+=step) {
                    int y = (int) (r*Math.sin(t));
                    int x = (int) (r*Math.cos(t));
                    if (first) {
                       g.fillRect(x+py, y+py, 1, 1);  
                       first = false;
                       p = new Point(x+px, y+py);
                       fp = new Point(x+px, y+py);
                    } else {
                        g.drawLine(p.x, p.y, x+px, y+py);
                        p = new Point(x+px, y+py);
                    }           
                }
                g.drawLine(p.x, p.y, fp.x, fp.y);
        }
        
        private void drawOval(Graphics2D g, int r, int s, int px, int py, int h, int w) {
            double pi = Math.PI;
                double step = 2*pi / s;
                g.setColor(Color.BLACK);
                boolean first = true;
                Point p = null;
                Point fp = new Point();
                for (double t=0; t < 2*pi; t+=step) {
                    int y = (int) (r*Math.sin(t)) + h;
                    int x = (int) (r*Math.cos(t)) + w;
                    if (first) {
                       g.fillRect(x+py, y+py, 1, 1);  
                       first = false;
                       p = new Point(x+px, y+py);
                       fp = new Point(x+px, y+py);
                    } else {
                        g.drawLine(p.x, p.y, x+px, y+py);
                        p = new Point(x+px, y+py);
                    }           
                }
                g.drawLine(p.x, p.y, fp.x, fp.y);
        }
        
        private void drawSpiral(Graphics2D g, int r, int s, int px, int py) {
            double pi = Math.PI;
                double step = 2*pi / s;
                int stepR = r / s;
                g.setColor(Color.BLACK);
                boolean first = true;
                Point p = null;
                Point fp = new Point();
                for (double t=0; t < 2*pi; t+=step) {
                    int y = (int) (r*Math.sin(t));
                    int x = (int) (r*Math.cos(t));
                    if (first) {
                       g.fillRect(x+py, y+py, 1, 1);  
                       first = false;
                       p = new Point(x+px, y+py);
                       fp = new Point(x+px, y+py);
                    } else {
                        g.drawLine(p.x, p.y, x+px, y+py);
                        p = new Point(x+px, y+py);
                    }
                    r -= stepR;
                }
                g.drawLine(p.x, p.y, fp.x, fp.y);
        }

        private void drawBezierCurve(Graphics2D g, Point p0, Point p1, Point p2, Point p3) {
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            Point fp = null;
            for (double t=0; t<1; t += 0.01){
                double a = (1-t);
                double x = (Math.pow(a, 3) * p0.x) + (3 * t * Math.pow(a,2) * p1.x) + (3 * Math.pow(t,2) * a * p2.x) + (Math.pow(t,3)*p3.x);
                double y = (Math.pow(a, 3) * p0.y) + (3 * t * Math.pow(a,2) * p1.y) + (3 * Math.pow(t,2) * a * p2.y) + (Math.pow(t,3)*p3.y);
                if (fp == null) {
                    g.fillOval((int)x-6,(int)y-6, 12, 12);
                    fp = new Point((int)x, (int)y);
                } else {
                    g.drawLine(fp.x, fp.y, (int)x, (int)y);
                    fp = new Point((int)x, (int)y);
                }
            }
            g.fillOval(fp.x-6, fp.y-6, 12, 12);
            g.setStroke(new BasicStroke(1));
            g.drawLine((int)p0.x, (int)p0.y, (int)p1.x, (int)p1.y);
            drawSpiral(g, 6, 100, (int)p1.x, (int)p1.y);
            g.fillOval((int)p1.x-2, (int)p1.y-2, 4, 4);
            g.drawLine((int)p2.x, (int)p2.y, (int)p3.x, (int)p3.y);
            drawSpiral(g, 6, 100, (int)p2.x, (int)p2.y);
            g.fillOval((int)p2.x-2, (int)p2.y-2, 4, 4);
        }

        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedPoint != null) {
                int mx = e.getX();
            int my = e.getY();
            int tx = mx - selectedPoint.x;
            int ty = my - selectedPoint.y;
            
            selectedPoint.move(mx, my);
            repaint();
            }
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int mx = e.getX();
            int my = e.getY();
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(BezierCurve.class.getName()).log(Level.SEVERE, null, ex);
            }
            Color c = r.getPixelColor(mx, my);
            //if (mx >= (p0.x-4) && mx <= (p0.x+4) && my >= (p0.y-4) && my <= (p0.y+4)) {
            if ( !c.equals(Color.WHITE)) {
                selectedPoint = p0;
                System.out.println("FOUND !");
            } else {
                selectedPoint = null;
            }
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
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

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            
        }

        @Override
        public void keyTyped(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            
        }
    }  
}
