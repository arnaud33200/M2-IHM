/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.visitor;

import simple.draw.mvc.model.Line;
import simple.draw.mvc.model.Circle;
import simple.draw.mvc.model.PolyLine;
import java.awt.Point;
import java.util.List;
import simple.draw.mvc.controler.CircleTool;
import simple.draw.mvc.controler.LineTool;
import simple.draw.mvc.controler.SelectionTool;

/**
 * Visiteur pour lister les formes du modèle dans le terminal
 * @author Vincent
 */
public class TerminalDrawing implements ShapeVisitor {

    public void visit(Circle shape) {
        System.out.println("Center X=" + shape.getMyCenter().x + "\nY=" + shape.getMyCenter().y + "\nRadius=" + shape.getMyRadius());
    }

    public void visit(Line shape) {
        System.out.println("X=" + shape.getMyStart().x + "\nY=" + shape.getMyStart().y);
    }

    public void visit(PolyLine shape) {
        List<Point> myPoints = shape.getMyPoints();
        for (Point p : myPoints) {
            System.out.println("X=" + p.x + "\nY=" + p.y);
        }
    }

    public void visit(CircleTool tool) {
        if (tool.getIAmActive()) {
            System.out.println("ACTIVE\nCenter X=" + tool.getMyCenter().x + "\nY=" + tool.getMyCenter().y + "\nRadius=" + tool.getMyRadius());
        }
    }

    public void visit(LineTool tool) {
        if (tool.getIAmActive()) {

        }
    }

    public void visit(SelectionTool tool) {
    }
}
