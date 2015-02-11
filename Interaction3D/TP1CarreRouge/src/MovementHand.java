import com.sun.j3d.utils.geometry.Primitive;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.swing.JFrame;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ladoucar
 */
public class MovementHand extends Behavior implements KeyListener {

    private WakeupOnElapsedTime condition;
    private TransformGroup myHandTg;
    private Canvas3D myFrame;
    
    private TransformGroup myGrabed;

    private boolean upX;
    private boolean DownX;
    private boolean upY;
    private boolean DownY;
    private boolean upZ;
    private boolean DownZ;
    private boolean Ctrl;
    
    private ArrayList<TransformGroup> myTgs;

    public MovementHand(TransformGroup myHandTg, Canvas3D f) {
        myGrabed = null;
        this.myTgs = new ArrayList();
        this.myFrame = f;
        this.myFrame.addKeyListener(this);
        this.myHandTg = myHandTg;
        long tmp = 30;
        condition = new WakeupOnElapsedTime(tmp);
        
        upX = false;
        DownX = false;
        upY = false;
        DownY = false;
        upZ = false;
        DownZ = false;
        Ctrl = false;
    }
    
    public void addTransformGroup(TransformGroup t) {
        myTgs.add(t);
    }

    @Override
    public void initialize() {
        this.wakeupOn(condition);
        this.setSchedulingBounds(new BoundingSphere(new Point3d(), 100.0));
    }

    @Override
    public void processStimulus(Enumeration enmrtn) {
        Transform3D t = new Transform3D();
        Transform3D m = new Transform3D();
        
        if (upX) { m.setTranslation(new Vector3f(0.1f, 0, 0)); t.mul(m); }
        if (DownX) { m.setTranslation(new Vector3f(-0.1f, 0, 0)); t.mul(m); }
        if (upZ) { m.setTranslation(new Vector3f(0, 0, 0.1f)); t.mul(m); }
        if (DownZ) { m.setTranslation(new Vector3f(0, 0, -0.1f)); t.mul(m); }
        if (upY) { m.setTranslation(new Vector3f(0, 0.1f, 0)); t.mul(m); }
        if (DownY) { m.setTranslation(new Vector3f(0, -0.1f, 0)); t.mul(m); }

        Transform3D thand = new Transform3D();
        myHandTg.getTransform(thand);
        thand.mul(t);
        myHandTg.setTransform(thand);
        if (upX || DownX || upZ || DownZ || upY || DownY) {           
            findColision(thand);
        }
        
        if (Ctrl && myGrabed != null) {
            Transform3D tgrab = new Transform3D();
            myGrabed.getTransform(tgrab);
            tgrab.mul(t);
            myGrabed.setTransform(tgrab);
        }
        
        this.wakeupOn(condition);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 37: DownX = true; break;
            case 39: upX = true; break;
            case 38: upZ = true; break;
            case 40: DownZ = true; break;
            case 65: upY = true; break;
            case 81: DownY = true; break;
            case 17: Ctrl = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 37: DownX = false; break;
            case 39: upX = false; break;
            case 38: upZ = false; break;
            case 40: DownZ = false; break;
            case 65: upY = false; break;
            case 81: DownY = false; break;
            case 17: Ctrl = false; break;
        }
    }

    private void findColision(Transform3D t) {
        Point3d in = new Point3d(.0, .0, .0);
        Point3d outHand = new Point3d();
        Point3d outObject = new Point3d();
        t.transform(in, outHand);
        double distance = 3000;
        Primitive selected = null;
        for (TransformGroup g : myTgs) {
            Primitive p = (Primitive) g.getChild(0);
            p.getAppearance().getPolygonAttributes().setPolygonMode(PolygonAttributes.POLYGON_FILL);
            Transform3D g3d = new Transform3D();
            g.getTransform(g3d);
            g3d.transform(in, outObject);
            double d = outHand.distance(outObject);
            if (d < distance) {
                distance = d;
                selected = p;
                myGrabed = g;
            }
        }
        if (distance < 0.75) {
           selected.getAppearance().getPolygonAttributes().setPolygonMode(PolygonAttributes.POLYGON_LINE);
        } else {
            myGrabed = null;
        }
       
    }

}
