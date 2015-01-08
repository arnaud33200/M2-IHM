package simple.draw.mvc.model;

/**
 * The Shape abstract class, super-class of all Shapes
 *
 * @author Rémi Bastide
 * @version 1.0
 */
import java.awt.Point;
import simple.draw.visitor.ShapeVisitor;

public abstract class Shape {

    private boolean iAmSelected = false;

    /**
     * Is this shape selected ?
     *
     * @return true if selected, false otherwise
     */
    public boolean isSelected() {
        return iAmSelected;
    }

    /**
     * Sets the selected status of this shape
     *
     * @param selected is the shape selected or not ?
     */
    public void setSelected(boolean selected) {
        iAmSelected = selected;
    }

    /**
     * Translates this shape
     *
     * @param dx delta x
     * @param dy delta y
     */
    abstract public void translateBy(int dx, int dy);

    /**
     * Determines if the given point is inside this shape
     *
     * @param p the point to test
     * @return true if <code>p</code> inside the shape, false otherwise
     */
    abstract public boolean isPickedBy(Point p);

    public abstract void accept(ShapeVisitor v);
}
