package simpledraw;

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

public class LineTool
	extends DrawingTool {
	private boolean iAmActive = false;
	private Point myInitialPoint;
	private Point myFinalPoint;

	public LineTool(DrawingPanel panel) {
		super(panel);
	}

	public void mouseClicked(MouseEvent e) {
		if (!iAmActive) {
			// First point
			iAmActive = true;
			myInitialPoint = e.getPoint();
			myFinalPoint = myInitialPoint;
			myPanel.setCursor(Cursor.getPredefinedCursor(Cursor.
				MOVE_CURSOR));
			myPanel.repaint();
		} else {
			// Second point
			iAmActive = false;
			myDrawing.addShape(
				new Line(myInitialPoint, myFinalPoint)
				);
			myPanel.setCursor(Cursor.getDefaultCursor());
			myPanel.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (iAmActive) {
			myFinalPoint = e.getPoint();
			myPanel.repaint();
		}
	}

	void draw(Graphics2D g) {
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
