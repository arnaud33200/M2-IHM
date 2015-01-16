/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.visitor;

import simple.draw.mvc.model.Line;
import simple.draw.mvc.model.Circle;
import simple.draw.mvc.model.PolyLine;
import simple.draw.mvc.controler.LineTool;
import simple.draw.mvc.controler.SelectionTool;
import simple.draw.mvc.controler.CircleTool;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;

/**
 * Visiteur pour dessiner graphiquement les formes du modèle
 * @author Vincent
 */
public class GraphicalDrawing implements ShapeVisitor, ToolVisitor {

    Graphics2D g;

    public GraphicalDrawing(Graphics2D g) {
        this.g = g;
    }

    public void visit(Circle shape) {
        g.setColor(
                shape.isSelected()
                        ? Color.red
                        : Color.black
        );
        g.drawOval(shape.getMyCenter().x - shape.getMyRadius(),
                shape.getMyCenter().y - shape.getMyRadius(),
                shape.getMyRadius() * 2,
                shape.getMyRadius() * 2
        );
    }

    public void visit(Line shape) {
        g.setColor(
                shape.isSelected()
                        ? Color.red
                        : Color.black
        );
        g.drawLine(shape.getMyStart().x, shape.getMyStart().y, shape.getMyEnd().x, shape.getMyEnd().y);
    }

    public void visit(PolyLine shape) {
        g.setColor(
                shape.isSelected()
                        ? Color.red
                        : Color.black
        );

        Iterator<Point> points = shape.getMyPoints().iterator();
        // A polyline has at least two points
        Point last = points.next();
        do {
            Point current = points.next();
            g.drawLine(last.x, last.y, current.x, current.y);
            last = current;
        } while (points.hasNext());
    }

    public void visit(CircleTool tool) {
        if (tool.getIAmActive()) {
            g.setColor(Color.red);
            g.drawOval(
                    tool.getMyCenter().x - tool.getMyRadius(),
                    tool.getMyCenter().y - tool.getMyRadius(),
                    tool.getMyRadius() * 2,
                    tool.getMyRadius() * 2
            );
        }
    }

    public void visit(LineTool tool) {
        if (tool.getIAmActive()) {
            g.setColor(Color.red);
            g.drawLine(
                    tool.getMyInitialPoint().x,
                    tool.getMyInitialPoint().y,
                    tool.getMyFinalPoint().x,
                    tool.getMyFinalPoint().y
            );
        }
    }

    public void visit(SelectionTool tool) {
    }

}
