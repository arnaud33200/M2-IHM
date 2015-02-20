
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ladoucar
 */
public class MarkingPieMenuViewTest extends javax.swing.JComponent {

    private int innerSize;
    private int outerSize;
    
    private int mouseX;
    private int mouseY;
    
    private ArrayList<Section> sections;

    public MarkingPieMenuViewTest() {
        this(50,150, null);
    }

    public MarkingPieMenuViewTest(int innerSize, int outerSize, ArrayList<Section> sections) {
        super();
        this.setBackground(new Color(0, 0, 0, 0));
        this.innerSize = innerSize;
        this.outerSize = outerSize;
        this.sections = sections;
       
        this.setSize(outerSize*2, outerSize*2);
        
        this.repaint();
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(outerSize*2, outerSize*2);
    }

    @Override
    public void paint(Graphics g) {

        // Creation d'un screenshot pour simuler la transparence
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(MarkingPieMenuViewTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage screen = robot.createScreenCapture(new Rectangle(mouseX, mouseY, outerSize*2 ,outerSize*2));

        int xi = outerSize - innerSize;
        int yi = outerSize - innerSize;
        int xo = 0;
        int yo = 0;
        
        g.setColor(Color.DARK_GRAY);
        g.fillOval(xo, yo, (outerSize*2), (outerSize*2));

        Point firstPoint = new Point(outerSize, outerSize);
    // Parcours de tous les pixels
        for (int y=0; y<outerSize*2; ++y) {
            for (int x=0; x<outerSize*2; ++x) {
                double d = firstPoint.distance(x, y);
                
        // Dessin des section à l'interieur
                if (d >= innerSize && d <= outerSize-3) {
                    
                    g.setColor(Color.black);
                    g.fillRect(x, y, 1, 1);
                    
                    float angle = getAngle(firstPoint, new Point(x,y));
                    Section selectedSection = findSection(x, y);
                    g.setColor(selectedSection.getColor());
                    g.fillRect(x, y, 1, 1);
                    if (x > 100 && y > 100) {
                        int i = 3;
                    }
                } else {
                    
        // Le pixel n'appartient pas au pie
        // on paint le pixel du screen shot pour faire la transparence
                    g.setColor(new Color(screen.getRGB(x, y)));
                    g.fillRect(x, y, 1, 1);
                }

            }
        }
        g.setColor(Color.DARK_GRAY);
        g.fillOval(xi-2, yi-2, (innerSize*2)+2, (innerSize*2)+2);
    }
    
    private float getAngle(Point p1, Point p2) {
			int mouseX = p2.x - p1.x;
			int mouseY = p2.y - p1.y;
			double l = Math.sqrt(mouseX * mouseX + mouseY * mouseY);
			double lx = mouseX / l;
			double ly = mouseY / l;
			double theta;
			if (lx > 0) {
				theta = Math.atan(ly / lx);
			} else if (lx < 0) {
				theta = -1 * Math.atan(ly / lx);
			} else {
				theta = 0;
			}

			if ( (mouseX > 0) && (mouseY < 0)) {
				theta = -1 * theta;
			} else if (mouseX < 0) {
				theta += Math.PI;
			} else {
				theta = 2 * Math.PI - theta;
			}

			return (float) (theta / (2 * Math.PI));              
    }

    private Section findSection(int x, int y) {
        Point firstPoint = new Point(outerSize, outerSize);
        float angle = getAngle(firstPoint, new Point(x,y));
        Section selectedSection = sections.get(sections.size()-1);
        float bot = (float) 0.0; 
       for (Section s : sections) {
            if (angle > bot && angle <= s.getAngle()) {
                selectedSection = s;
            }
            bot = s.getAngle();
        }
       return selectedSection;
    }
    
    
    
}
