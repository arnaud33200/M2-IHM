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
public class PhongShading {

    private BufferedImage myImage;
    private double xlum = 50;
    private double ylum = 50;
    private double zlum = 500;
    private double xcircle = 200;
    private double ycircle = 200;
    private double rcircle = 100;

    public PhongShading() {
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
        private boolean isInsideCircle;

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
            Polygon cercle = new Polygon();
            cercle.addPoint(100, 150);

            myImage = new BufferedImage(400, 400, IndexColorModel.OPAQUE);
// Tracer du polygone dans l'image
            Graphics myGraphics = myImage.getGraphics();
            //myGraphics.drawPolygon(p1);
            myGraphics.dispose();
            this.scanLineAlgorithm(xcircle, ycircle, rcircle);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.scale(zoom, zoom);
            g.translate(xpan, ypan);
            g.setColor(Color.black);
            g.drawImage(myImage, null, this);
            g.setColor(Color.white);
            g.drawString("xpan = " + xpan , (int)(15-xpan), (int)(15-ypan));
            g.drawString("ypan = " + ypan ,(int)(15-xpan), (int)(35-ypan));
            g.drawString("zoom = " + zoom ,(int)(15-xpan), (int)(55-ypan));
        }

        private void scanLineAlgorithm(double xc, double yc, double r) {

            for (int y = 0; y < myImage.getHeight(); ++y) {
                for (int x = 0; x < myImage.getWidth(); ++x) {

                    if (insideCircle(x, y, xc, yc, r)) {

                        double[] n, l;
                        n = vecNorm(x * 1., y * 1., xc * 1., yc * 1., r * 1.);
                        l = vecLum(xlum, ylum, zlum, xc, yc);

                        double k = Math.max((n[0] * l[0]) + (n[1] * l[1]) + (n[2] * l[2]), 0);

                        Color c = Color.WHITE;
                        /*if( k >0.9) c = Color.ORANGE;
                        if( k > 0.99) c = Color.BLACK;
                        int red = (int) Math.min(c.getRed() * k, 255);
                        int green = (int) Math.min(c.getGreen() * k, 255);
                        int blue = (int) Math.min(k * c.getBlue(), 255);
                        c = new Color(red, green, blue);*/
                        c = Color.BLACK;
                        myImage.setRGB(x, y, (int)(c.getRGB()/k));
                    }
                }
            }
        }

        private boolean insideCircle(double x, double y, double xc, double yc, double r) {
            return (Math.pow((xc - x), 2) + Math.pow((yc - y), 2) <= Math.pow(r, 2));
        }

        private double[] vecNorm(double x, double y, double xc, double yc, double r) {
            double[] v = new double[3];
            v[0] = (x - xc) / r;
            v[1] = (y - yc) / r;
            double t = Math.pow(r, 2) - Math.pow(x - xc, 2) - Math.pow(y - yc, 2);
            v[2] = Math.sqrt(t) / r;
            return v;
        }

        private double[] vecLum(double xl, double yl, double zl, double xc, double yc) {
            double[] v = new double[3];
            double t = Math.sqrt(Math.pow(xl - xc, 2) + Math.pow(yl - yc, 2) + Math.pow(zl, 2));
            v[0] = (xl - xc) / t;
            v[1] = (yl - yc) / t;
            v[2] = zl / t;
            return v;
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON2) {
                return; // prevent accidental press of mouse wheel
            }
            int x = evt.getX();
            int y = evt.getY();
            int dx = x - xlast, dy = y - ylast;
            if (isInsideCircle) {
                xlum += dx / zoom;
                ylum += dy / zoom;
            } else {
                xpan += dx / zoom;
                ypan += dy / zoom;
            }
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
            isInsideCircle = insideCircle(xlast-xpan, ylast-ypan, xcircle, ycircle, rcircle);
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON2) {
                return; // prevent accidental press of mouse wheel
            }
            setCursor(Cursor.getDefaultCursor());
            isInsideCircle = false;
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
