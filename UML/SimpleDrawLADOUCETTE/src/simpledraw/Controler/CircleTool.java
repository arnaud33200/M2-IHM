package simpledraw.Controler;

/**
 * The tool to create circles
 **/

import simpledraw.Model.DrawingModel;
import simpledraw.Model.Circle;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class CircleTool extends DrawingTool {
	private boolean iAmActive = false;
	private Point myCenter;
	private int myRadius;

	public CircleTool(DrawingModel m) {
		super(m);
	}

	public void mouseClicked(MouseEvent e) {
		if (!iAmActive) {
			// Center
			iAmActive = true;
			myCenter = e.getPoint();
                        myDrawing.updateEditedShape(new Circle(myCenter, myRadius));
			myRadius = 0;
                        myDrawing.setShapeMode(true);
			//myPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			//myPanel.repaint();
		} else {
			// Radius
			iAmActive = false;
			myDrawing.addShape(
				new Circle(myCenter, myRadius)
				);
                        myDrawing.updateEditedShape(null);
                        myDrawing.setShapeMode(false);
			//myPanel.setCursor(Cursor.getDefaultCursor());
			//myPanel.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (iAmActive) {
			myRadius = (int) (
				myCenter.distance(e.getPoint())
				);
                        myDrawing.updateEditedShape(new Circle(myCenter, myRadius));
			//myPanel.repaint();
		}
	}

	public void draw(Graphics2D g) {
		if (iAmActive) {
			g.setColor(Color.red);
			g.drawOval(
				myCenter.x - myRadius,
				myCenter.y - myRadius,
				myRadius * 2,
				myRadius * 2
				);
		}
	}
}