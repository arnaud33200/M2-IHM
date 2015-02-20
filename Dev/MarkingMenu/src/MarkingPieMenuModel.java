import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.stage.Popup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ladoucar
 */
/*newpopup = PopupFactory.getSharedInstace().getPopup(invoker, composantToPopup,x,y);
 newpopup.show();
 newpopup.hide();*/
public class MarkingPieMenuModel implements MouseMotionListener, MouseListener {

    private static boolean DEBUGMODE = true;

    public int INNERCIRCLESIZE = 40;
    public int OUTERCIRCLESIZE = 100;
    private static int WAITTIME = 1000;

    private boolean in;

    enum STATEMENU {

        IDLE, MARKING, SHOW, HIDE, MENU
    };
    STATEMENU state;
    Timer t;
    Point firstPoint;

    private MouseEvent gMouse;
    private Component myComponent;
    private Component myRootComponent;
    private javax.swing.Popup myPopup;

    private ArrayList<Section> sections;
    private Section selectedSection;
    private MarkingPieMenuViewTest myPieView;

    public MarkingPieMenuModel(Component c) {
        sections = new ArrayList<>();
        myComponent = c;
        myRootComponent = SwingUtilities.getRoot(c);
        
        myPopup = null;
        myPieView = new MarkingPieMenuViewTest(INNERCIRCLESIZE, OUTERCIRCLESIZE, sections);
        
        myComponent.addMouseListener(this);
        myComponent.addMouseMotionListener(this);
        
        myPieView.addMouseListener(this);
        myPieView.addMouseMotionListener(this);
        
        state = STATEMENU.IDLE;
    }

    public void addSection(String n, Color c) {
        sections.add(new Section(-1, (float) 0.0, c, n));
        int nbSection = sections.size();

        if (nbSection > 1) {
            float interval = (float) (1.0 / (nbSection));
            float angle = interval;

            String msg = "new PIE : ";
            for (int i = 0; i < nbSection; ++i) {
                Section s = sections.get(i);
                s.setAngle(angle);
                s.setNumber(i);
                msg += "[" + angle + "] - ";
                angle += interval;
            }
            printDebug(msg);
        }
    }

    private void doCommand() {
        printDebug("DO COMMANDE : " + selectedSection.getName());
    }

    private void hideMenu() {
        if (myPopup != null) {
            myPopup.hide();
        }
        printDebug("HIDE");
    }
    
    private void highlight() {
        
    }

    private void showMenu() {
        int mx = gMouse.getLocationOnScreen().x - OUTERCIRCLESIZE;
        int my = gMouse.getLocationOnScreen().y - OUTERCIRCLESIZE;
        myPieView.setMouseX(mx);
        myPieView.setMouseY(my);
        myPopup = PopupFactory.getSharedInstance().getPopup(gMouse.getComponent(), myPieView, mx, my);

        myPopup.show();
        printDebug("SHOW MENU");
    }

    private void checkIfIn(MouseEvent e) {
        double d = firstPoint.distance(e.getLocationOnScreen().x, e.getLocationOnScreen().y);
        if (d > INNERCIRCLESIZE && d < OUTERCIRCLESIZE) {
            if (!in) {
                printDebug(state + " - CHECK : IN ! " + d);
                in = true;
            }
        } else {
            if (in) {
                printDebug(state + " - CHECK : out ..." + d);
                in = false;
            }
        }
    }

    private void findSection(MouseEvent e) {
        float angle = getAngle(firstPoint, new Point(e.getX(), e.getY()));
        selectedSection = sections.get(sections.size() - 1);
        float bot = (float) 0.0;
        for (Section s : sections) {
            if (angle > bot && angle <= s.getAngle()) {
                selectedSection = s;
            }
            bot = s.getAngle();
        }

        /* Graphics g = e.getComponent().getGraphics();
         g.setColor(selectedSection.getColor());
         g.fillRect(e.getX(), e.getY(), 5, 5);*/
    }

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void startTimer() {
        printDebug("MARKING WAITING ...");
        t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {

                switch (state) {
                    case IDLE:
                        break;
                    case MARKING:
                        showMenu();
                        state = STATEMENU.SHOW;
                        break;
                    case SHOW:
                        break;
                    case HIDE:
                        break;
                    case MENU:
                        break;
                }
            }
        }, WAITTIME);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gMouse = e;
        printDebug("click : " + e.getButton());

        switch (state) {
            case IDLE:
                // clic gauche
                if (e.getButton() == 3) {
                    in = false;
                    firstPoint = new Point(e.getLocationOnScreen().x, e.getLocationOnScreen().y);
                    startTimer();
                    state = STATEMENU.MARKING;
                }
                break;
            case MARKING:
                throw new RuntimeException("Mouse Down : WAIT");
            case SHOW:
                throw new RuntimeException("Mouse Down : PIESHOW");
            case HIDE:
                throw new RuntimeException("Mouse Down : PIEHIDE");
            case MENU:

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        /*Graphics g = e.getComponent().getGraphics();
         g.clearRect(0, 0, 500, 500);*/
        try {
            t.cancel();
        } catch (Exception exp) {
        }
        switch (state) {
            case IDLE:
                break;
            case MARKING:
                in = false;
                state = STATEMENU.MENU;
                showMenu();
                break;
            case SHOW:
                if (in) {
                    doCommand();
                }
                hideMenu();
                state = STATEMENU.IDLE;
                break;
            case HIDE:
                doCommand();
                state = STATEMENU.IDLE;
                break;
            case MENU:
                if (e.getButton() == 1) {
                    if (in) {
                        doCommand();
                    }
                    hideMenu();
                    state = STATEMENU.IDLE;
                }
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (state) {
            case IDLE:
                break;
            case MARKING:
                checkIfIn(e);
                if (in) {
                    state = STATEMENU.HIDE;
                } else {
                    state = STATEMENU.MARKING;
                }
                break;
            case SHOW:
                checkIfIn(e);
                if (in) {
                    findSection(e);
                    highlight();
                }
                state = STATEMENU.SHOW;
                break;
            case HIDE:
                checkIfIn(e);
                if (in) {
                    findSection(e);
                    highlight();
                }
                state = STATEMENU.HIDE;
                break;
            case MENU:
                checkIfIn(e);
                if (in) {
                    findSection(e);
                    highlight();
                }
                state = STATEMENU.MENU;
                break;
        }
    }
    
      @Override
    public void mouseMoved(MouseEvent e) {
        switch (state) {
            case IDLE:
                break;
            case MARKING:
                break;
            case SHOW:
                break;
            case HIDE:
                break;
            case MENU:
                checkIfIn(e);
                if (in) {
                    findSection(e);
                    highlight();
                }
                state = STATEMENU.MENU;
                break;
        }
    }
    
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
  

    @Override
    public void mouseClicked(MouseEvent e) {
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

        if ((mouseX > 0) && (mouseY < 0)) {
            theta = -1 * theta;
        } else if (mouseX < 0) {
            theta += Math.PI;
        } else {
            theta = 2 * Math.PI - theta;
        }

        return (float) (theta / (2 * Math.PI));
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void printDebug(String msg) {
        if (DEBUGMODE) {
            System.out.println(msg);
        }
    }

}
