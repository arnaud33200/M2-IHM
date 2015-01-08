/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shape;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author ladoucar
 */
public class Shape extends javax.swing.JComponent {
    public final static int Oval = 1;
    public final static int Rectangle = 2;
    private int myShape;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    
    private Color myColor;
    public final static String PROP_COLOR = "Color";
    public final static String PROP_SHAPE = "Shape";
    
    public Shape() {
        this(Oval, Color.GREEN);
    }
    
    public Shape(int myShape, Color myColor) {
        super();
        this.myShape = myShape;
        this.myColor = myColor;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }
    
    public int getMyShape() {
        return myShape;
    }

    public final void setMyShape(int myShape) {
        support.firePropertyChange(PROP_SHAPE, this.myShape, myShape);
        if (myShape == Oval || myShape == Rectangle) {
            this.myShape = myShape;
        }
        repaint();
    }
    
    public final void setToto(int jej) {
        
    }

    public Color getMyColor() {
        return myColor;
    }

    public void setMyColor(Color myColor) {
        support.firePropertyChange(PROP_COLOR, this.myColor, myColor);
        this.myColor = myColor;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Color old = g.getColor();
        g.setColor(myColor);
        switch (myShape) {
            case Oval: g.fillOval(0, 0, getWidth(),  getHeight());
                break;
            case Rectangle: g.fillRect(0, 0, getWidth(),  getHeight());
                break;
            default: g.fillOval(0, 0, getWidth(),  getHeight());
        }
        g.setColor(old);
    }
    
}
