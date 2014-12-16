/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tpimagereader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author ladoucar
 */
public class TPImageReader {

    public TPImageReader() throws IOException {
        init();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //TPImageReader r = new TPImageReader();
            //BezierCurve b = new BezierCurve();
            //PanAndZoom p = new PanAndZoom();
            //ScanLineAlgorithm s = new ScanLineAlgorithm();
            PhongShading p = new PhongShading();
        } catch (Exception ex) {
            Logger.getLogger(TPImageReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    BufferedImage img;
    
    public void init() throws IOException {
        JFrame jf = new JFrame("YEAH");
        SCanvas sc = new SCanvas();
        sc.setPreferredSize(new java.awt.Dimension(500,500));
        
        jf.getContentPane().add(sc);
        jf.pack(); // ?
        jf.setResizable(true);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        sc.setFocusable(true);
        sc.requestFocusInWindow();
        
        img = ImageIO.read(new File("./logo.gif"));
        
        jf.repaint();
    }
    
    class SCanvas extends JComponent {

    public SCanvas() {
        super();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if (img != null) {
            Color c;
            for (int y=0; y<img.getHeight(); ++y) {
                for (int x=0; x<img.getWidth(); ++x) {
                    c = new Color(img.getRGB(x, y));
                    
                   // c = blurColor(c,x,y);
                    //c = convertToGrey(c);
                  //  c = threshold(c, 120);
                    int[][] m = {
                        {-1, 0, 1},
                        {-1, 0, 1},
                        {-1, 0, 1}
                    };
                    c = applyConvolutionMatrice(c, m, x, y);
                    g.setColor(c);
                    g.fillRect(x, y, 1, 1);  
                }
            }
        }
    }
        private Color averageColor(Color c1, Color c2) {
            int r = (c1.getRed() + c2.getRed()) / 2;
            r = normalizeColor(r);
            int g = (c1.getGreen() + c2.getGreen()) / 2;
            r = normalizeColor(r);
            int b = (c1.getBlue() + c2.getBlue()) / 2;
            r = normalizeColor(r);
            return new Color(r, g, b);
        }

        private Color convertToGrey(Color c) {
            int grey = (c.getRed() + c.getGreen() + c.getBlue())/3;
            return new Color(grey,grey,grey);
        }

        private Color blurColor(Color c, int x, int y) {
            int count = 0;
            for (int z=y-1; z<=y+1; ++z) {
                for (int w=x-1; w<=x+1; ++w ) {
                    if (z>=0 && z<img.getHeight() && w>=0 && w<img.getWidth() && z != y && w != x) {
                        c = averageColor(c, new Color(img.getRGB(w, z)));
                       count++; 
                    }
                }
            }
            System.out.println(""+count);
            return c;
        }

        private Color threshold(Color c, int i) {
            int grey = (c.getRed() + c.getGreen() + c.getBlue())/3;
            if (grey <i) {
                return Color.BLACK;
            } else {
                return Color.WHITE;
            }
        }
        
        // Matrice size = 3 x 3
        private Color applyConvolutionMatrice(Color c,int[][] m, int x, int y) {
            int sum = convertToGrey(c).getBlue();
            for (int z=y-1; z<=y+1; ++z) {
                for (int w=x-1; w<=x+1; ++w ) {
                    if (z>=0 && z<img.getHeight() && w>=0 && w<img.getWidth() && z != y && w != x) {
                        int a = z-y+1;
                        int b = w-x+1;
                        Color n = new Color(img.getRGB(w, z));
                        n = convertToGrey(n);
                        sum += m[a][b] * n.getBlue();
                    }
                }              
            }
            return new Color(sum, sum, sum);
        }

        private int normalizeColor(int c) {
            if (c < 0) { c = 0; } else if ( c > 255 ) { c = 255; };
            return c;
        }
    }
}
