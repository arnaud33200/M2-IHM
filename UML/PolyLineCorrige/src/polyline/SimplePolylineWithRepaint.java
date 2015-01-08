package polyline;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.JApplet;
import javax.swing.event.MouseInputListener;

public class SimplePolylineWithRepaint
	extends JApplet implements KeyListener, MouseInputListener {
	
	// A PolyLine is a list of Points
	static class PolyLine extends LinkedList<Point> {
		// Draw a polyline
		void draw(Graphics g) {
			for (int index = 1; index < this.size(); index++) {
				Point start = this.get(index - 1);
				Point end = this.get(index);
				g.drawLine(start.x, start.y, end.x, end.y);
			}
		}
	}
	
	// A drawing is a list of PolyLines
	static class Drawing extends LinkedList<PolyLine> {
		void draw(Graphics g) {
			// Draw all polylines
			for (PolyLine line : this) {
				line.draw(g);
			}
		}
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

	private void reinit() {
		myTemporaryPolyline = new PolyLine();
		repaint();
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

	private void drawRubber(Point p) {
		// Erase temporary line
		eraseSegment(myTemporaryPolyline.getLast(), myTemporaryPoint);
		myTemporaryPoint = p;
		// Draw new temporary line
		drawSegment(myTemporaryPolyline.getLast(), myTemporaryPoint);
	}

	private void removeLast() {
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

	// "Logical" events
	private void addPointRequest(Point p) {
		switch (myState) {
			case IDLE:
				addOnePoint(p);
				drawSegment(p, p); // Draw first point
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
				removeLast();
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
				reinit();
				myState = State.IDLE;
				break;
			case MANYPOINTS:
				save();
				reinit();
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
				drawRubber(p);
				break;
			case MANYPOINTS:
				drawRubber(p);
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

	@Override
	public void paint(Graphics g) {
		//g.drawString("Hello", 50, 50);
		super.paint(g);
		g.setColor(Color.red);
		myDrawing.draw(g);
		// Il se peut qu'on doive repeindre pendant qu'une ligne est en cours
		if (myState != State.IDLE) {
			g.setColor(Color.black);
			myTemporaryPolyline.draw(g);
			drawRubber(myTemporaryPoint);
		}
	}
}
