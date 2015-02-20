import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;

public class RollingAction extends Behavior {
    
    private double angle = Math.PI/124;
    
    private TransformGroup myTg;
    private Transform3D tg_angle;
    private WakeupOnElapsedTime condition;
    
    private int count;

    public RollingAction(TransformGroup myTg) {
        count = 16;
        this.myTg = myTg;
        tg_angle = new Transform3D();
        tg_angle.rotX(angle);
        long tmp = 30;
        condition = new WakeupOnElapsedTime(tmp);
    }

    @Override
    public void initialize() {
        this.wakeupOn(condition); // Initialisation de la condition d'activation
        this.setSchedulingBounds(new BoundingSphere(new Point3d(),100.0));
    }

    @Override
    public void processStimulus(Enumeration enmrtn) {
        if (count > 32) {
            angle *= -1.f;
            tg_angle.rotX(angle);
            count = 0;
        }
        Transform3D t = new Transform3D();
        myTg.getTransform(t);
        t.mul(tg_angle);
        myTg.setTransform(t);
        this.wakeupOn(condition); // Réarme la condition pour la prochaine commande
        count++;
    }
    
}