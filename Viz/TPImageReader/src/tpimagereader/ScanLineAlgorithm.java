/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpimagereader;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author ladoucar
 */
public class ScanLineAlgorithm {

    private BufferedImage myImage;

    public ScanLineAlgorithm() {
        init();
    }

    public void init() {
        JFrame jf = new JFrame("ScanLine Algorithm");
        SCanvas sc = new SCanvas();
        sc.setPreferredSize(new java.awt.Dimension(500, 500));
        jf.getContentPane().add(sc);
        jf.pack();
        jf.setResizable(true);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sc.setFocusable(true);
        sc.requestFocusInWindow();
    }

    private class SCanvas extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

        int xlast, ylast;
        double zoom;
        double xpan, ypan;
//Font font;

        public SCanvas() {
            super();
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
            addKeyListener(this);
            xpan = ypan = 0;
            zoom = 1;
        }

        public void paint(Graphics g_) {
            Graphics2D g = (Graphics2D) g_;
            g.setColor(Color.GRAY);
            Polygon p1 = new Polygon();
            p1.addPoint(100, 150);
            p1.addPoint(200, 100);
            p1.addPoint(300, 150);
            p1.addPoint(300, 300);
            p1.addPoint(100, 300);
            p1.addPoint(50, 200);
            Polygon p2 = new Polygon();
            p2.addPoint(100, 50);
            p2.addPoint(200, 200);
            p2.addPoint(300, 100);
            p2.addPoint(300, 300);
            p2.addPoint(100, 300);
            p2.addPoint(50, 200);
            Polygon p3 = new Polygon();
            p3.addPoint(100, 150);
            p3.addPoint(200, 200);
            p3.addPoint(300, 100);
            p3.addPoint(300, 300);
            p3.addPoint(100, 300);
            p3.addPoint(50, 200);
            myImage = new BufferedImage(400, 400, IndexColorModel.OPAQUE);
// Tracer du polygone dans l'image
            Graphics myGraphics = myImage.getGraphics();
            myGraphics.drawPolygon(p3);
            myGraphics.dispose();
            this.scanLineAlgorithm();
            g.fillRect(0, 0, getWidth(), getHeight());
            g.scale(zoom, zoom);
            g.translate(xpan, ypan);
            g.setColor(Color.black);
            g.drawImage(myImage, null, this);
        }

        private void scanLineAlgorithm() {
// A COMPLETER with myImage.SetRGB()…. and myImage.getRGB()….
            boolean in = false;
            boolean next = false;
            boolean previous = false;
            int first = 0;
            int cb = (Color.WHITE).getRGB();
            for (int y = 0; y < myImage.getHeight(); ++y) {
                in = false;
                for (int x = 0; x < myImage.getWidth(); ++x) {

                    if (next) {
                        myImage.setRGB(x, y, (Color.ORANGE).getRGB());
                        next = false;
                    }

                    if (x > 0 && x < myImage.getWidth() - 1) {
                        int c0 = myImage.getRGB(x - 1, y);
                        int c1 = myImage.getRGB(x, y);
                        int c2 = myImage.getRGB(x + 1, y);
                        if (c1 == cb && c2 != cb) {
                            first = x;
                            in = !in;
                            next = true;
                        }
                    }

                    if (in && !next) {

                        if (!previous) {
                            myImage.setRGB(x, y, (Color.ORANGE).getRGB());
                        } else {
                            previous = false;
                        }

                        if (x < myImage.getWidth() - 1) {
                            int c2 = myImage.getRGB(x + 1, y);
                            if (c2 == cb) {
                                previous = true;
                            }
                        }
                    }
                }
                if (in) {
                    int x = myImage.getWidth() - 1;
                    boolean change = false;
                    for (; x > first; --x) {
                        int c0 = myImage.getRGB(x - 1, y);
                        int c1 = myImage.getRGB(x, y);
                        if (c1 != cb) {
                            if (change) {
                                myImage.setRGB(x, y, (Color.ORANGE).getRGB());
                            } else {
                                myImage.setRGB(x, y, (Color.GRAY).getRGB());
                            }
                        }

                        if (c0 == cb && c1 != cb) {
                            change = !change;
                        }

                    }
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON2) {
                return; // prevent accidental press of mouse wheel
            }
            int x = evt.getX();
            int y = evt.getY();
            int dx = x - xlast, dy = y - ylast;
            xpan += dx / zoom;
            ypan += dy / zoom;
            xlast = x;
            ylast = y;
            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent evt) {
            double x = evt.getX();
            double y = evt.getY();
            int dw = evt.getWheelRotation();
            double ady = Math.abs(dw);
            double dzoom = (0.00303 * ady + 0.00482) * ady + 1.001;
            if (dw > 0) {
                dzoom = 1 / dzoom;
            }
            double nz = zoom * dzoom;
            xpan += x / nz - x / zoom;
            ypan += y / nz - y / zoom;
            zoom = nz;
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON2) {
                return; // prevent accidental press of mouse wheel
            }
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (evt.getButton() == MouseEvent.BUTTON2) {
                return; // prevent accidental press of mouse wheel
            }
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            xlast = evt.getX();
            ylast = evt.getY();
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON2) {
                return; // prevent accidental press of mouse wheel
            }
            setCursor(Cursor.getDefaultCursor());
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
