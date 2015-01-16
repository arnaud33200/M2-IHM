/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpledraw.Controler;

import simpledraw.Model.Rectangle;
import simpledraw.Model.DrawingModel;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author arnaud
 */
public class RectangleTool extends DrawingTool {

    private boolean iAmActive = false;
    private Point myInitialPoint;
    private Point myFinalPoint;
    private float myHeight;
    private float myWidth;
        
    public RectangleTool(DrawingModel m) {
        super(m);
    }
    
    public void mouseClicked(MouseEvent e) {
		if (!iAmActive) {
			// First point
			iAmActive = true;
			myInitialPoint = e.getPoint();
			myFinalPoint = myInitialPoint;
                        myDrawing.updateEditedShape(new Rectangle(myInitialPoint, 5,5));
                        myDrawing.setShapeMode(true);
			//myPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			//myPanel.repaint();
		} else {
			// Second point
			iAmActive = false;
                        myDrawing.updateEditedShape(null);
			myDrawing.addShape(
				new Rectangle(myFinalPoint, myHeight, myWidth)
				);
                        myDrawing.setShapeMode(false);
			//myPanel.setCursor(Cursor.getDefaultCursor());
			//myPanel.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (iAmActive) {
			myFinalPoint = e.getPoint();
                        updateHeightWidth();
                        myDrawing.updateEditedShape(new Rectangle(myInitialPoint, myHeight, myWidth));
		}
	}

    public void draw(Graphics2D g) {
        Rectangle r = new Rectangle(myFinalPoint, myHeight, myWidth);
        r.draw(g);
    }

    private void updateHeightWidth() {
        myHeight = Math.abs(myInitialPoint.y - myFinalPoint.y);
        myWidth = Math.abs(myInitialPoint.x - myFinalPoint.x);
        
    }

    
}
