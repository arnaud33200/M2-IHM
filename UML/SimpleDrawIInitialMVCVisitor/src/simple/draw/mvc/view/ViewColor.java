/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.mvc.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import simple.draw.mvc.controler.DrawingControler;
import simple.draw.mvc.model.Shape;
import simple.draw.observer.Observer;
import simple.draw.visitor.GraphicalColorDrawing;

/**
 * Nouvelle vue du modèle qui affiche les formes du modèle dans une couleur différente
 * @author Vincent
 */
public class ViewColor extends JPanel implements Observer {
    List<Shape> list = new ArrayList<Shape>();
    DrawingControler dc = new DrawingControler();
    
    public ViewColor(){
        super();
        setBackground(java.awt.Color.white);
        dc.addObserverToModel(this);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(qualityHints);
        GraphicalColorDrawing gzd = new GraphicalColorDrawing(g2);
        for (Shape s : list) {
            s.accept(gzd);
        }
    }

    public void update(List<Shape> l) {
        this.list = l;
        repaint();
    }
    
}
