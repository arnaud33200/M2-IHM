package simple.draw.mvc.controler;

import simple.draw.mvc.view.DrawingPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import simple.draw.visitor.ToolVisitor;

/**
 * A Drawing tool in the drawing panel
 */
public abstract class DrawingTool extends DrawingControler
        implements KeyListener, MouseListener, MouseMotionListener {

    DrawingPanel myPanel;

    DrawingTool(DrawingPanel panel) {
        super();
        myPanel = panel;
    }
    
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        myPanel.requestFocus();
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
    
    public abstract void accept(ToolVisitor v);
}
