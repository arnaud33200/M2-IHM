package simple.draw.mvc.controler;

import simple.draw.mvc.view.DrawingPanel;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import simple.draw.mvc.model.Shape;
import simple.draw.visitor.ToolVisitor;

/**
 * The tool to select, move and delete Shapes in the Drawing
 *
 * @author Rémi Bastide
 * @version 1.0
 */
public class SelectionTool
        extends DrawingTool {

    private Shape mySelectedShape = null;
    private Point myLastPoint;

    public SelectionTool(DrawingPanel panel) {
        super(panel);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_DELETE) {
            if (mySelectedShape != null) {
                deleteShapeFromModel(mySelectedShape);
                myPanel.repaint();
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        Shape pickedShape = pickShapeFromModelAt(e.getPoint());
        myLastPoint = e.getPoint();
        if (mySelectedShape != null) {
            mySelectedShape.setSelected(false);
        }
        mySelectedShape = pickedShape;
        if (mySelectedShape != null) {
            mySelectedShape.setSelected(true);
            myPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
        myPanel.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseMoved(MouseEvent e) {
        Shape pickedShape = pickShapeFromModelAt(e.getPoint());
        if (pickedShape != null) {
            myPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            myPanel.setCursor(Cursor.getDefaultCursor());
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (mySelectedShape != null) {
            mySelectedShape.translateBy(
                    e.getX() - myLastPoint.x,
                    e.getY() - myLastPoint.y
            );
            myLastPoint = e.getPoint();
            myPanel.repaint();
        }
    }

    @Override
    public void accept(ToolVisitor v) {
        // Extraction de la méthode draw dans un visiteur
        v.visit(this);
    }

}
