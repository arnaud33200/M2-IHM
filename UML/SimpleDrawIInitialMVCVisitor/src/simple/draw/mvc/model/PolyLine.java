package simple.draw.mvc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.awt.Point;
import simple.draw.visitor.ShapeVisitor;

public class PolyLine
        extends Shape {

    /**
     * The points of this PolyLine
     */
    private final List<Point> myPoints;

    public PolyLine(Collection<Point> points) {
        if (points.size() < 2) {
            throw new IllegalArgumentException(
                    "A PolyLine needs at least 2 Points");
        }
        myPoints = new ArrayList<Point>(points);
    }

    public List<Point> getMyPoints() {
        return myPoints;
    }

    public void translateBy(int dx, int dy) {
        Iterator i = myPoints.iterator();
        while (i.hasNext()) {
            Point p = (Point) i.next();
            p.translate(dx, dy);
        }
    }

    public boolean isPickedBy(Point p) {
        boolean result = false;
        Iterator<Point> points = myPoints.iterator();
        // A polyline has at least two points
        Point last = points.next();
        do {
            Point current = points.next();
            if (Line.segmentIsPickedBy(last, current, p)) {
                result = true;
                break;
            }
            last = current;
        } while (points.hasNext());

        return result;
    }

    @Override
    public void accept(ShapeVisitor v) {
        // Extraction de la méthode draw dans un visiteur
        v.visit(this);
    }

}
