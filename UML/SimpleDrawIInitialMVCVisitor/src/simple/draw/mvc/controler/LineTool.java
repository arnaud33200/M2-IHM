package simple.draw.mvc.controler;

import simple.draw.mvc.view.DrawingPanel;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import simple.draw.mvc.model.Line;
import simple.draw.visitor.ToolVisitor;

/**
 * The tool to create Lines
 *
 * @author Rémi Bastide
 * @version 1.0
 * @see simple.draw.mvc.model.Line
 */
public class LineTool
        extends DrawingTool {

    private boolean iAmActive = false;
    private Point myInitialPoint;
    private Point myFinalPoint;

    public LineTool(DrawingPanel panel) {
        super(panel);
    }

    public boolean getIAmActive() {
        return iAmActive;
    }

    public Point getMyInitialPoint() {
        return myInitialPoint;
    }

    public Point getMyFinalPoint() {
        return myFinalPoint;
    }

    public void mouseClicked(MouseEvent e) {
        if (!iAmActive) {
            // First point
            iAmActive = true;
            myInitialPoint = e.getPoint();
            myFinalPoint = myInitialPoint;
            myPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            myPanel.repaint();
        } else {
            // Second point
            iAmActive = false;
            addShapeToModel(
                    new Line(myInitialPoint, myFinalPoint)
            );
            myPanel.setCursor(Cursor.getDefaultCursor());
            myPanel.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (iAmActive) {
            myFinalPoint = e.getPoint();
            myPanel.repaint();
        }
    }
    
    @Override
    public void accept(ToolVisitor v) {
        // Extraction de la méthode draw dans un visiteur
        v.visit(this);
    }
}
