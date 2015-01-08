/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.mvc.controler;

import simple.draw.mvc.model.Drawing;
import java.awt.Point;
import simple.draw.observer.Observer;
import simple.draw.mvc.model.Shape;

/**
 * @author Vincent
 */
public class DrawingControler {

    private Drawing myDrawing;

    public DrawingControler() {
        myDrawing = Drawing.getDrawing();
    }

    public void addShapeToModel(Shape s) {
        myDrawing.addShape(s);
    }

    public void deleteShapeFromModel(Shape s) {
        myDrawing.deleteShape(s);
    }

    public Shape pickShapeFromModelAt(Point p) {
        return myDrawing.pickShapeAt(p);
    }

    public void clearModelSelection() {
        myDrawing.clearSelection();
    }

    public void addObserverToModel(Observer o) {
        myDrawing.addObserver(o);
    }
}
