package simple.draw.mvc.model;

import java.awt.Point;
import simple.draw.visitor.ShapeVisitor;

/**
 * A circle
 *
 */
public class Circle
        extends Shape {

    private Point myCenter;
    private int myRadius;

    /**
     * Construct a Circle
     *
     * @param center The center of the circle
     * @param radius The radius of the circle
     *
     */
    public Circle(Point center, int radius) {
        myCenter = center;
        myRadius = radius;
    }

    public Point getMyCenter() {
        return myCenter;
    }

    public int getMyRadius() {
        return myRadius;
    }

    public void translateBy(int dx, int dy) {
        myCenter.translate(dx, dy);
    }

    public boolean isPickedBy(Point p) {
        return (Math.abs(myCenter.distance(p) - myRadius) <= 2);
    }

    @Override
    public void accept(ShapeVisitor v) {
        // Extraction de la méthode draw dans un visiteur
        v.visit(this);
    }
}
