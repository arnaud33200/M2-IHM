package simple.draw.mvc.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import simple.draw.observer.Observer;
import simple.draw.mvc.controler.CircleTool;
import simple.draw.mvc.controler.DrawingTool;
import simple.draw.visitor.GraphicalDrawing;
import simple.draw.mvc.controler.LineTool;
import simple.draw.mvc.controler.SelectionTool;
import simple.draw.mvc.model.Shape;

/**
 * A Panel that displays a Drawing, and maintains a current DrawingTool<BR>
 * Uses the "State" design pattern
 *
 * @author Rémi Bastide
 * @version 1.0
 * @see simple.draw.mvc.model.Drawing
 * @see simple.draw.mvc.controler.DrawingTool
 */
public class DrawingPanel
        extends JPanel implements Observer {

    DrawingTool myCurrentTool;
    // Liste des formes du modèle qui sera mise à jour lors d'une notification de changement par le modèle
    List<Shape> list = new ArrayList<Shape>();

    public DrawingPanel() {
        super();
        setBackground(java.awt.Color.white);
        myCurrentTool = new SelectionTool(this);
        myCurrentTool.addObserverToModel(this);
        activate(myCurrentTool);
    }

    void activateSelectionTool() {
        terminate(myCurrentTool);
        myCurrentTool = new SelectionTool(this);
        activate(myCurrentTool);
    }

    void activateCircleTool() {
        terminate(myCurrentTool);
        myCurrentTool = new CircleTool(this);
        activate(myCurrentTool);
        myCurrentTool.clearModelSelection();
        repaint();
    }

    void activateLineTool() {
        terminate(myCurrentTool);
        myCurrentTool = new LineTool(this);
        activate(myCurrentTool);
        myCurrentTool.clearModelSelection();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(qualityHints);
        // Utilisation du pattern Visiteur pour remplacer les méthodes "draw"
        GraphicalDrawing gd = new GraphicalDrawing(g2);
        for (Shape s : list) {
            s.accept(gd);
        }
        myCurrentTool.accept(gd);

    }

    private void terminate(DrawingTool t) {
        removeKeyListener(t);
        removeMouseListener(t);
        removeMouseMotionListener(t);
    }

    private void activate(DrawingTool t) {
        addKeyListener(t);
        addMouseListener(t);
        addMouseMotionListener(t);
    }

    public void update(List<Shape> l) {
        this.list = l;
    }

}
