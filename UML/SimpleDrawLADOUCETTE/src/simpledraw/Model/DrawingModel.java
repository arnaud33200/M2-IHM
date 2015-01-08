/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpledraw.Model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import simpledraw.View.DrawingView;

/**
 *
 * @author arnaud
 */
public class DrawingModel extends Drawing {
    
    private List<DrawingView> myViews = new LinkedList<DrawingView>();
    private Shape editedShape = null;
    private boolean ShapeMode = false;

    public boolean isShapeMode() {
        return ShapeMode;
    }

    public void setShapeMode(boolean ShapeMode) {
        this.ShapeMode = ShapeMode;
    }
    
    public void updateEditedShape(Shape s){
        editedShape = s;
        if (editedShape != null) {
            notifyAllView();
        }
    }
    
    public DrawingModel() {
        super();
    }
    
    private void notifyAllView() {
        for (DrawingView v : myViews) {
            v.notify(this);
        }
    }
    
    public void addView(DrawingView v){
         myViews.add(v);
         v.notify(this);
    }
    
    public void removeView(DrawingView v) {
        myViews.remove(v);
    }

    @Override
    public void addShape(Shape s) {
        super.addShape(s); //To change body of generated methods, choose Tools | Templates.
        notifyAllView();
    }

    @Override
    public void deleteShape(Shape s) {
        super.deleteShape(s); //To change body of generated methods, choose Tools | Templates.
        notifyAllView();
    }

    @Override
    public Shape pickShapeAt(Point p) {
        Shape s = super.pickShapeAt(p);//To change body of generated methods, choose Tools | Templates.
        notifyAllView();
        return s; 
    }

    @Override
    public void clearSelection() {
        super.clearSelection(); //To change body of generated methods, choose Tools | Templates.
        notifyAllView();
    }

    public int getNumberOfShapes() {
        return myShapes.size();
    }

    @Override
    public String toString() {
        String txt = "";
        for (Shape s : myShapes) {
            txt += s.toString() + " - ";
        }
        return txt;
    }

    public List<Shape> getShapes() {
        return myShapes;
    }
    
    
    

    
}
