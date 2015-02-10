import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.pickfast.behaviors.PickRotateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickZoomBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.io.FileNotFoundException;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Geometry;
import javax.media.j3d.Light;
import javax.media.j3d.Material;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class GrandLarge extends JFrame {
    /*---------*/
    /* Données */
    /*---------*/

    /**
     * Données relatives à la fenêtre.
     */
    private int largeur;	// Taille
    private int hauteur;
    private int posx;		// Position
    private int posy;

    private SimpleUniverse universe;
    private TransformGroup tgVolume; // Noeud de transformation attaché au volume

    private DirectionalLight dl;
    private Canvas3D canvas;

    public GrandLarge(int l, int h, int px, int py) {
        /*----- Instanciation de la fenêtre graphique -----*/
        this.setTitle("GRAND LARGE");
        this.largeur = l;
        this.hauteur = h;
        this.setSize(largeur, hauteur);
        this.posx = px;
        this.posy = py;
        this.setLocation(posx, posy);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*----- Contenu de la fenêtre -----*/
        Container conteneur = getContentPane();
        conteneur.setLayout(new BorderLayout());

        /*----- Création du Canvas -----*/
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        conteneur.add("Center", canvas);

        /*----- Création de l'univers virtuel -----*/
        this.universe = new SimpleUniverse(canvas);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        /*----- Position du volume -----*/
        this.tgVolume = new TransformGroup();
        this.tgVolume.addChild(this.createBrancheVolume());

        /* Transform3D t3d_1 = new Transform3D();
         t3d_1.rotX(Math.PI / 8);
         Transform3D t3d_2 = new Transform3D();
         t3d_2.rotY(-Math.PI / 3);
         t3d_1.mul(t3d_2);
         this.tgVolume.setTransform(t3d_1);*/
        /*----- Position de l'observateur -----*/
        Transform3D t3d_oeil = new Transform3D();
        t3d_oeil.set(new Vector3d(0.0, 0.0, 13.0));
        this.universe.getViewingPlatform().getViewPlatformTransform().setTransform(t3d_oeil);

        /*----- Création du noeud de branchement du volume -----*/
        BranchGroup racineVolume = new BranchGroup();
        PickTranslateBehavior ptb = new PickTranslateBehavior(racineVolume, canvas, new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        tgVolume.addChild(ptb);

        KeyNavigatorBehavior knb = new KeyNavigatorBehavior(canvas, tgVolume);
        knb.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        racineVolume.addChild(knb);

        PickRotateBehavior prb = new PickRotateBehavior(racineVolume, canvas, new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        tgVolume.addChild(prb);

        PickZoomBehavior pzb = new PickZoomBehavior(racineVolume, canvas, new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        tgVolume.addChild(pzb);

        tgVolume.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        tgVolume.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgVolume.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        racineVolume.addChild(this.tgVolume);

        /*----- Ajout des sources lumineuse -----*/
        AmbientLight al = new AmbientLight(true, new Color3f(Color.DARK_GRAY));

        dl = new DirectionalLight();
        dl.setDirection(1.0f, 0.0f, -1.0f);
        dl.setEnable(true);
        dl.setCapability(Light.ALLOW_STATE_WRITE);
        dl.setColor(new Color3f(1f, 1f, 1f));

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        dl.setInfluencingBounds(bounds);
        al.setInfluencingBounds(bounds);

        racineVolume.addChild(al);
        racineVolume.addChild(dl);

        TextureLoader bgTexture = new TextureLoader("./fond.jpg", this);
        Background bg = new Background(bgTexture.getImage());
        bg.setApplicationBounds(bounds);
        racineVolume.addChild(bg);

        /*----- Ajout de la branche de volume -----*/
        this.universe.addBranchGraph(racineVolume);
        // NE PLUS MODIFIER LA SCENE EN DESSOUS !

        this.setVisible(true);
    }


    /*----------*/
    /* Méthodes */
    /*----------*/
    /**
     * Création du volume.
     */
    private BranchGroup createBrancheVolume() {

        Transform3D t3d_1 = new Transform3D();
        Transform3D t3d_2 = new Transform3D();

        TransformGroup tg1 = new TransformGroup();
        tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        RollingAction r = new RollingAction(tg1);
        tg1.addChild(r);

        Scene s = null;
        ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);
        try {
            s = (Scene) loader.load("galleon.obj");
        } catch (FileNotFoundException | ParsingErrorException | IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        tg1.addChild(s.getSceneGroup());

        TransformGroup tgy = new TransformGroup();
        t3d_1.setTranslation(new Vector3d(0.0, 1.0, 0.0));
        tgy.setTransform(t3d_1);
        tgy.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgy.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        AutoRotation a = new AutoRotation(tgy);
        tgy.addChild(a);

        TransformGroup tgx = new TransformGroup();
        t3d_1.setTranslation(new Vector3d(2.5, 0.0, 0.0));
        tgx.setTransform(t3d_1);

        TransformGroup tgrx = new TransformGroup();
        t3d_1 = new Transform3D();
        t3d_1.rotX(-Math.PI/2);
        t3d_2.rotZ(Math.PI/2);
        t3d_1.mul(t3d_2);
        tgrx.setTransform(t3d_1);

        TransformGroup tgMouette = new TransformGroup();
        Shape3D mouette = null;
        Appearance appMouette = new Appearance();
        appMouette.setMaterial(new Material());
        Geometry geomMouette = new GullCG();
        mouette = new Shape3D(geomMouette, appMouette);
        mouette.setAppearance(createAppearance(2));
        tgMouette.addChild(mouette);

        /*----- Création du noeud racine -----*/
        BranchGroup racine = new BranchGroup();
        tgrx.addChild(tgMouette);
        tgx.addChild(tgrx);
        tgy.addChild(tgx);
        racine.addChild(tgy);
        racine.addChild(tg1);

        racine.compile();
        return racine;
    }

    public Appearance createAppearance(int numero) {
        Appearance aspect = new Appearance();
        Material m = new Material();
        switch (numero) {
            case 0:
                aspect.setPointAttributes(new PointAttributes((float) 0.2, false));
                aspect.setColoringAttributes(new ColoringAttributes(new Color3f(Color.yellow), posx));
                aspect.setMaterial(J3dUtils.MAT_BRONZE);
                break;
            case 1:
                aspect.setPointAttributes(new PointAttributes((float) 0.2, false));
                aspect.setColoringAttributes(new ColoringAttributes(new Color3f(Color.BLUE), posx));
                aspect.setMaterial(J3dUtils.MAT_OR);
                break;
            case 2:

                aspect.setPointAttributes(new PointAttributes((float) 0.2, false));
                aspect.setColoringAttributes(new ColoringAttributes(new Color3f(Color.GREEN), ColoringAttributes.SHADE_GOURAUD));
                aspect.setMaterial(J3dUtils.MAT_OBJET_BLEU);

                TextureLoader textload = new TextureLoader("./l.png ", this);
                aspect.setTexture(textload.getTexture());
                aspect.setTextureAttributes(new TextureAttributes(TextureAttributes.REPLACE, new Transform3D(), new Color4f(Color.BLACK), TextureAttributes.NICEST));
                break;
        }
        return aspect;
    }

}
