package simpledraw.Controler;

import simpledraw.Model.Line;
import simpledraw.Model.DrawingModel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * The tool to create Lines
 * @author Rémi Bastide
 * @version 1.0
 * @see simpledraw.Line
 */

public class LineTool extends DrawingTool {
	private boolean iAmActive = false;
	private Point myInitialPoint;
	private Point myFinalPoint;

	public LineTool(DrawingModel m) {
		super(m);
	}

	public void mouseClicked(MouseEvent e) {
		if (!iAmActive) {
			// First point
			iAmActive = true;
			myInitialPoint = e.getPoint();
			myFinalPoint = myInitialPoint;
                        myDrawing.updateEditedShape(new Line(myInitialPoint, myFinalPoint));
                        myDrawing.setShapeMode(true);
			//myPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			//myPanel.repaint();
		} else {
			// Second point
			iAmActive = false;
                        myDrawing.updateEditedShape(null);
			myDrawing.addShape(
				new Line(myInitialPoint, myFinalPoint)
				);
			myDrawing.setShapeMode(false);
			//myPanel.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (iAmActive) {
			myFinalPoint = e.getPoint();
                        myDrawing.updateEditedShape(new Line(myInitialPoint, myFinalPoint));
			//myPanel.repaint();
		}
	}

	public void draw(Graphics2D g) {
		if (iAmActive) {
			g.setColor(Color.red);
			g.drawLine(
				myInitialPoint.x,
				myInitialPoint.y,
				myFinalPoint.x,
				myFinalPoint.y
				);
		}
	}
}
