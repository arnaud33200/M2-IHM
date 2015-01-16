package simpledraw.View;

import simpledraw.Model.DrawingModel;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import simpledraw.Controler.CircleTool;
import simpledraw.Controler.DrawingTool;
import simpledraw.Controler.LineTool;
import simpledraw.Controler.RectangleTool;
import simpledraw.Controler.SelectionTool;

/**
 * A Panel that displays a Drawing, and maintains a current DrawingTool<BR>
 * Uses the "State" design pattern
 * @author Rémi Bastide
 * @version 1.0
 * @see simpledraw.Drawing
 * @see simpledraw.DrawingTool
 */

public class DrawingPanel extends JPanel implements DrawingView {
	DrawingTool myCurrentTool;
	DrawingModel myDrawing = new DrawingModel();

    public DrawingPanel(DrawingModel myDrawing) {
        super();
         this.myDrawing = myDrawing;        
        setBackground(java.awt.Color.white);
        myCurrentTool = new SelectionTool(myDrawing);
        activate(myCurrentTool);
       
        
    }

	public void activateSelectionTool() {
		terminate(myCurrentTool);
		myCurrentTool = new SelectionTool(myDrawing);
		activate(myCurrentTool);
	}

	public void activateCircleTool() {
		terminate(myCurrentTool);
		myCurrentTool = new CircleTool(myDrawing);
		activate(myCurrentTool);
		myDrawing.clearSelection();
		//repaint();
	}

	public void activateLineTool() {
		terminate(myCurrentTool);
		myCurrentTool = new LineTool(myDrawing);
                activate(myCurrentTool);
		myDrawing.clearSelection();
		//repaint();
	}

	public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints qualityHints = new
                    RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHints(qualityHints);
            myDrawing.draw(g2);
            myCurrentTool.draw(g2);
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

    public void notify(DrawingModel m) {
        if (m.isShapeMode()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
        this.repaint();
    }

    public void activateRectangleTool() {
        terminate(myCurrentTool);
	myCurrentTool = new RectangleTool(myDrawing);
         activate(myCurrentTool);
	myDrawing.clearSelection();
    }

       

}
