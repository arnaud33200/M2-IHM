
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ladoucar
 */
public class MarkingModel implements MouseMotionListener, MouseListener{
    
    public int INNERCIRCLESIZE = 40;
    public int OUTERCIRCLESIZE = 100;
    private static int WAITTIME = 1000;
    
    private boolean in;
    enum STATEMENU { NOMENU, WAIT, PIESHOW, PIEHIDE };
    STATEMENU state;
    Timer t;
    Point firstPoint;
    
    private Graphics gMouse;
    
    private ArrayList<Section> sections;
    private Section selectedSection;
    
     public MarkingModel(int nbSection) {
         if (nbSection < 2) {
             return;
         }
         sections = new ArrayList<>();

        state = STATEMENU.NOMENU;     
    }
     
    public void addSection(String n, Color c){
        sections.add(new Section(-1,(float)0.0,c,n));
        int nbSection = sections.size();

        if (nbSection > 1) {
            float interval = (float) (1.0 / (nbSection));
            float angle = interval;
            
            String msg = "new PIE : ";
             for (int i=0; i<nbSection; ++i) {
                 Section s = sections.get(i);
                 s.setAngle(angle);
                 s.setNumber(i);
                 msg += "[" + angle + "] - ";
                 angle += interval;
             }
             System.out.println(msg);
        }
    }

    private void doCommand() {
        System.out.println("DO COMMANDE : " + selectedSection.getName());
    }

    private void hideMenu() {
        System.out.println("HIDE");
    }
    
    private void showMenu() {
        int xi = firstPoint.x - (INNERCIRCLESIZE);
        int yi = firstPoint.y - (INNERCIRCLESIZE);
        int xo = firstPoint.x - (OUTERCIRCLESIZE);
        int yo = firstPoint.y - (OUTERCIRCLESIZE);
        gMouse.setColor(Color.GRAY);
        gMouse.fillOval(xo, yo, OUTERCIRCLESIZE*2, OUTERCIRCLESIZE*2);
        gMouse.setColor(Color.WHITE);
        gMouse.fillOval(xi, yi, INNERCIRCLESIZE*2, INNERCIRCLESIZE*2);
       System.out.println("SHOW MENU");
    }

    private void checkIfIn(MouseEvent e) {
        double d = Math.sqrt((e.getX() - firstPoint.x)*(e.getX()-firstPoint.x) + (e.getY()-firstPoint.y)*(e.getY()-firstPoint.y));
        if (d > INNERCIRCLESIZE && d < OUTERCIRCLESIZE) {
            if (!in) {
            //System.out.println(state + " - CHECK : IN ! " + d);
            in = true;
            }
        } else {
            if (in) {
            //System.out.println(state + " - CHECK : out ..." + d);
            in = false;
            }
        }
    }

    private void findSection(MouseEvent e) {
        float angle = getAngle(firstPoint, new Point(e.getX(),e.getY()));
        
        selectedSection = sections.get(sections.size()-1);
        float bot = (float) 0.0; 
       for (Section s : sections) {
            if (angle > bot && angle <= s.getAngle()) {
                selectedSection = s;
            }
            bot = s.getAngle();
        }
        
        Graphics g = e.getComponent().getGraphics();
        g.setColor(selectedSection.getColor());
        g.fillRect(e.getX(), e.getY(), 5, 5);
    }
    
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void startTimer()
    {
        System.out.println("WAITING ...");
        t = new Timer();
        t.schedule(new TimerTask() {
            
            @Override
            public void run() {
                
                switch (state) {
            case NOMENU: 
                break;
            case WAIT:
                showMenu();
                state = STATEMENU.PIESHOW;
                break;
            case PIESHOW: 
                break;
            case PIEHIDE: 
                break;
        }
            }            
        }, WAITTIME);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        gMouse = e.getComponent().getGraphics();
        
        switch (state) {
            case NOMENU:
                Graphics g = e.getComponent().getGraphics();
                g.drawLine(e.getX()-3, e.getY(), e.getX()+3, e.getY());
                g.drawLine(e.getX(), e.getY()-3, e.getX(), e.getY()+3);
                in = false;
                firstPoint = new Point(e.getX(), e.getY());
                startTimer();
                state = STATEMENU.WAIT;
                break;
            case WAIT:
                throw new RuntimeException("Mouse Down : WAIT");
                
            case PIESHOW:
                throw new RuntimeException("Mouse Down : PIESHOW");
                
            case PIEHIDE: 
                throw new RuntimeException("Mouse Down : PIEHIDE");
                
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Graphics g = e.getComponent().getGraphics();
        g.clearRect(0, 0, 500, 500);
        try {
            t.cancel();
        } catch (Exception exp) {}
        switch (state) {
            case NOMENU: 
                break;
            case WAIT: 
                in = false;
                state = STATEMENU.NOMENU;
                break;
            case PIESHOW:
                if (in){
                    doCommand();
                }
                hideMenu();
                state = STATEMENU.NOMENU;
                break;
            case PIEHIDE:
                doCommand();
                state = STATEMENU.NOMENU;
                break;
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
       switch (state) {
            case NOMENU: 
                break;
            case WAIT:
                checkIfIn(e);
                if(in) {
                    state = STATEMENU.PIEHIDE;
                } else {
                    state = STATEMENU.WAIT;
                }
                break;
            case PIESHOW: 
                checkIfIn(e);
                if(in) {
                    findSection(e);
                }
                state = STATEMENU.PIESHOW;
                break;
            case PIEHIDE: 
                checkIfIn(e);
                if(in) {
                    findSection(e);
                }
                state = STATEMENU.PIEHIDE;
                break;
        }  
    }
    
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
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

    @Override
    public void mouseMoved(MouseEvent e) {
       
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
    
}
