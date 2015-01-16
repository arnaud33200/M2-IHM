package simpledraw;

import simpledraw.View.TextView;
import simpledraw.View.DrawingPanel;
import simpledraw.View.ConsolView;
import simpledraw.Model.DrawingModel;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import simpledraw.Model.Shape;
import simpledraw.Visitors.ShapeVisitor;
import simpledraw.Visitors.ZoomInDoubleShape;
import simpledraw.Visitors.ZoomOutDoubleShape;

/**
 * Main Frame of SimpleDraw
 * @author Rémi Bastide
 * @version 1.0
 */

public class MainFrame extends JFrame {
	JToggleButton mySelectButton = new JToggleButton("Select");
	JToggleButton myLineButton = new JToggleButton("Line");
	JToggleButton myCircleButton = new JToggleButton("Circle");
        JToggleButton myRectangleButton = new JToggleButton("Rectangle");
        JButton myZoomInButton = new JButton("Zoom++");
        JButton myZoomOutButton = new JButton("Zoom--");
	DrawingPanel myDrawingPanel;
        DrawingModel myDrawing = new DrawingModel();
        TextView txtView = new TextView();
        ConsolView cslView = new ConsolView();

	/**Construct the frame*/
	public MainFrame() {
                myDrawingPanel = new DrawingPanel(myDrawing);
                
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
                myDrawing.addView(myDrawingPanel);
                myDrawing.addView(txtView);
                myDrawing.addView(cslView);
	}

	/**Component initialization*/
	private void jbInit() throws Exception {
		getContentPane().setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		mySelectButton.setSelected(true);
		mySelectButton.setToolTipText("Select and move shapes");
		myCircleButton.setToolTipText("Draw a Circle");
		myLineButton.setToolTipText("Draw a Line");
                myRectangleButton.setToolTipText("Draw a Rectangle");

		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.add(mySelectButton, null);
		buttonPanel.add(myLineButton, null);
		buttonPanel.add(myCircleButton, null);
                buttonPanel.add(myRectangleButton, null);
                buttonPanel.add(txtView,null);
                buttonPanel.add(myZoomInButton, null);
                buttonPanel.add(myZoomOutButton, null);
                
		getContentPane().add(myDrawingPanel, BorderLayout.CENTER);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(mySelectButton);
		buttonGroup.add(myLineButton);
		buttonGroup.add(myCircleButton);

		setSize(new Dimension(400, 300));
		setTitle("Simple Draw");
                
                myZoomInButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        List<Shape> shapes = myDrawing.getShapes();
                        ShapeVisitor v = new ZoomInDoubleShape();
                        for (Shape s : shapes) {
                            s.accept(v);
                        }
                        myDrawingPanel.repaint();
                    }
                });
                
                myZoomOutButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        List<Shape> shapes = myDrawing.getShapes();
                        ShapeVisitor v = new ZoomOutDoubleShape();
                        for (Shape s : shapes) {
                            s.accept(v);
                        }
                        myDrawingPanel.repaint();
                    }
                });

		mySelectButton.addActionListener(
			new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDrawingPanel.activateSelectionTool();
			}
		}
		);

		myLineButton.addActionListener(
			new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDrawingPanel.activateLineTool();
			}
		}
		);

		myCircleButton.addActionListener(
			new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDrawingPanel.activateCircleTool();
			}
		}
		);
                
                myRectangleButton.addActionListener(
			new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDrawingPanel.activateRectangleTool();
			}
		}
		);
	}

	/**Overridden so we can exit when window is closed*/
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}
}
