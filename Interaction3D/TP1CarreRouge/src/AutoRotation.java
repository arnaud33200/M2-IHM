import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;

public class AutoRotation extends Behavior {
    
    private double angle = Math.PI/16;
    
    private TransformGroup myTg;
    private Transform3D tg_angle;
    private WakeupOnElapsedTime condition;
    
    private int count;

    public AutoRotation(TransformGroup myTg) {
        count = 0;
        this.myTg = myTg;
        tg_angle = new Transform3D();
        tg_angle.rotY(angle);
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
        if (count >= 32) {
            angle *= -1.f;
            tg_angle.rotY(angle);
            count = 0;
        }
        Transform3D t = new Transform3D();
        myTg.getTransform(t);
        t.mul(tg_angle);
        Transform3D s = new Transform3D();
        s.setScale(1.001f);
        t.mul(s);
        myTg.setTransform(t);
        this.wakeupOn(condition); // Réarme la condition pour la prochaine commande
        count++;
    }
    
}
