/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelabel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import shape.Shape;

/**
 *
 * @author ladoucar
 */
public class ShapeLabel extends javax.swing.JComponent implements MouseListener {

    private Shape myShape;
    private JLabel myLabel;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private ArrayList<ShapeLabelListener> listeners;
    
    public ShapeLabel() {
        this("Test", Color.BLACK, Color.WHITE);
    }
    
    public ShapeLabel(String txt, Color fc, Color bc) {
        super();
        myLabel = new JLabel(txt);
        myLabel.setForeground(fc);
        myShape = new Shape(Shape.Oval, bc);
        
        myLabel.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        myLabel.setVerticalAlignment((int) CENTER_ALIGNMENT);
        
        add(myLabel, JLayeredPane.DEFAULT_LAYER);
        add(myShape, JLayeredPane.PALETTE_LAYER);
        
        listeners = new ArrayList<>();
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
                setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
                setBackground(Color.white);
            }
            
            
        });
    }

    @Override
    public boolean contains(int x, int y) {
                double a = (double) getWidth()/2;
                double b = (double) getHeight()/2;
                double dx = x - a;
                double dy = y - b;
                return ((dx*dx)/(a*a) + (dy*dy)/(b*b) <= 1);
        //return (Math.sqrt(x-getWidth()/2)/Math.sqrt(getWidth()/2))+(Math.sqrt(y-getHeight()/2)/Math.sqrt(getHeight()/2)) <= 1;
    }

    @Override
    public void paint(Graphics g) {
        myShape.paint(g);
        myLabel.paint(g);
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x,y,width,height);
        myLabel.setBounds(0, 0, width, height);
        myShape.setBounds(0, 0, width, height);
        
    }
    
    private void fireAllShapeLalbelListener() {
        for (ShapeLabelListener l : listeners) {
            l.ShapeLabelClicked(new ShapeLabelEvent(myLabel.getText()));
        }
    }
    
    public void addShapeLabelClicked(ShapeLabelListener l) {
        listeners.add(l);
    }
    
    public void removeShapeLabelClicked(ShapeLabelListener l) {
        listeners.remove(l);
    }

    @Override
     public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }
    
    public final String getText() {
        return myLabel.getText();
    }
    
    public final Color getForeground() {
        return myLabel.getForeground();
    }
    
    public final Color getBackground() {
        return myShape.getMyColor();
    }
    
    public final void setText(String txt) {
        myLabel.setText(txt);
        repaint();
    }
    
    public final void setForeground(java.awt.Color c) {
        myLabel.setForeground(c);
        repaint();
    }
    
    public final void setBackground(java.awt.Color c) {
        myShape.setMyColor(c);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (myShape.contains(e.getPoint())) {
            fireAllShapeLalbelListener();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
