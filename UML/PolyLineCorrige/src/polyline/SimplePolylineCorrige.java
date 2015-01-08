package polyline;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.event.MouseInputListener;

public class SimplePolylineCorrige
	extends Applet implements KeyListener, MouseInputListener {

	static class PolyLine extends LinkedList<Point> {
	}

	static class Drawing extends LinkedList<PolyLine> {
	}
	private static final int MAX = 5;
	// Global variables of the StateChart
	private Drawing myDrawing = new Drawing();
	private PolyLine myTemporaryPolyline = new PolyLine();
	private Point myTemporaryPoint;
	// State Constants

	enum State {
		IDLE, ONEPOINT, MANYPOINTS
	};
	// State variable
	private State myState = State.IDLE;

	@Override
	public void init() {
		super.init();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}

	// Utility methods
	private void reinit() {
		myTemporaryPolyline = new PolyLine();
	}

	private void save() {
		myDrawing.add(myTemporaryPolyline);
		reinit();
	}

	private void eraseSegment(Point start, Point end) {
		drawSegment(start, end); // XOR Mode : tracer 2x revient à effacer
	}

	private void drawSegment(Point start, Point end) {
		Graphics g = this.getGraphics();
		g.setXORMode(getBackground());
		g.drawLine(start.x, start.y, end.x, end.y);
	}

	private void moveTemporaryPoint(Point p) {
		// Erase temporary line
		eraseSegment(myTemporaryPolyline.getLast(), myTemporaryPoint);
		myTemporaryPoint = p;
		// Draw new temporary line
		drawSegment(myTemporaryPolyline.getLast(), myTemporaryPoint);
	}

	private void removeLastPoint() {
		eraseSegment(myTemporaryPolyline.getLast(), myTemporaryPoint); // Erase rubber line
		Point lastPoint = myTemporaryPolyline.removeLast();
		eraseSegment(myTemporaryPolyline.getLast(), lastPoint); // Erase last segment of Polyline
		// Erase last point (drawn an odd number of times)
		eraseSegment(lastPoint, lastPoint);
		// Draw new temporary line
		drawSegment(myTemporaryPolyline.getLast(), myTemporaryPoint);
	}

	private void addOnePoint(Point p) {
		myTemporaryPolyline.add(p);
		myTemporaryPoint = p;
	}
	
	private void setFirstPoint(Point p) {
		addOnePoint(p);
		drawSegment(p, p); // Draw first point	
	}

	// "Logical" events
	private void addPointRequest(Point p) {
		switch (myState) {
			case IDLE:
				setFirstPoint(p);
				myState = State.ONEPOINT;
				break;
			case ONEPOINT:
				addOnePoint(p);
				myState = State.MANYPOINTS;
				break;
			case MANYPOINTS:
				addOnePoint(p);
				if (myTemporaryPolyline.size() == MAX) {
					save();
					myState = State.IDLE;
				}
				break;
		}
	}

	private void removePointRequest() {
		switch (myState) {
			case IDLE:
				// ignore
				break;
			case ONEPOINT:
				// ignore
				break;
			case MANYPOINTS:
				removeLastPoint();
				if (myTemporaryPolyline.size() == 1) {					
					myState = State.ONEPOINT;
				}
				break;
		}
	}

	private void terminateRequest() {
		switch (myState) {
			case IDLE:
				// ignore
				break;
			case ONEPOINT:
				eraseSegment(myTemporaryPolyline.getLast(), myTemporaryPoint); // Effacer la ligne temporaire
				reinit();
				myState = State.IDLE;
				break;
			case MANYPOINTS:
				save();
				myState = State.IDLE;
				break;
		}
	}

	private void moveRequest(Point p) {
		switch (myState) {
			case IDLE:
				// ignore
				break;
			case ONEPOINT:
				moveTemporaryPoint(p);
				break;
			case MANYPOINTS:
				moveTemporaryPoint(p);
				break;
		}
	}

	// Utility functions
	private boolean isLeftButton(MouseEvent e) {
		return javax.swing.SwingUtilities.isLeftMouseButton(e);
	}

	private boolean isDoubleClick(MouseEvent e) {
		return isLeftButton(e) && (e.getClickCount() == 2);
	}

	// "Physical" events provided by the toolkit
	public void mouseMoved(MouseEvent e) {
		moveRequest(e.getPoint());
	}

	public void mouseDragged(MouseEvent e) {
		moveRequest(e.getPoint());
	}

	public void mouseClicked(MouseEvent e) {
		if (isDoubleClick(e)) {
			// Remove last point, entered twice
			removePointRequest();
			terminateRequest();
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isLeftButton(e)) {
			addPointRequest(e.getPoint());
		} else if (e.isPopupTrigger()) {
			removePointRequest();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			removePointRequest();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			terminateRequest();
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			removePointRequest();
		}
	}

	public void keyReleased(KeyEvent e) {
	}
}
