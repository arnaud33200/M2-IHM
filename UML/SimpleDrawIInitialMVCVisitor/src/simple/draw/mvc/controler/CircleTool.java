package simple.draw.mvc.controler;

/**
 * The tool to create circles
 *
 */
import simple.draw.mvc.view.DrawingPanel;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import simple.draw.mvc.model.Circle;
import simple.draw.visitor.ToolVisitor;

public class CircleTool
        extends DrawingTool {

    private boolean iAmActive = false;
    private Point myCenter;
    private int myRadius;

    public CircleTool(DrawingPanel panel) {
        super(panel);
    }

    public boolean getIAmActive() {
        return iAmActive;
    }

    public Point getMyCenter() {
        return myCenter;
    }

    public int getMyRadius() {
        return myRadius;
    }

    public void mouseClicked(MouseEvent e) {
        if (!iAmActive) {
            // Center
            iAmActive = true;
            myCenter = e.getPoint();
            myRadius = 0;
            myPanel.setCursor(
                    Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR)
            );
            myPanel.repaint();
        } else {
            // Radius
            iAmActive = false;
            addShapeToModel(
                    new Circle(myCenter, myRadius)
            );
            myPanel.setCursor(Cursor.getDefaultCursor());
            myPanel.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (iAmActive) {
            myRadius = (int) (myCenter.distance(e.getPoint()));
            myPanel.repaint();
        }
    }

    @Override
    public void accept(ToolVisitor v) {
        // Extraction de la méthode draw dans un visiteur
        v.visit(this);
    }
}
