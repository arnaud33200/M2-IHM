import com.sun.j3d.utils.behaviors.keyboard.KeyNavigator;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.pickfast.behaviors.PickRotateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickZoomBehavior;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.vecmath.Vector3d;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.Material;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;


/*----------------*/
/* Classe SceneSU */
/*----------------*/
/**
 * Scène 3D construire sur un SimpleUniverse.
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 */
public class SceneSU extends JFrame {
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

    /**
     * Objets composants la structure principale.
     */
    private SimpleUniverse universe;
    
    private TransformGroup tgVolume; // Noeud de transformation attaché au volume

    private DirectionalLight dl;
     private Canvas3D canvas;
    

    /*--------------*/
    /* Constructeur */
    /*--------------*/
    public SceneSU(int l,
            int h,
            int px,
            int py) {
        /*----- Instanciation de la fenêtre graphique -----*/
        this.setTitle("Visualisation 3D");
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
        
        Transform3D t3d_1 = new Transform3D();
        t3d_1.rotX(Math.PI / 8);
        Transform3D t3d_2 = new Transform3D();
        t3d_2.rotY(-Math.PI / 3);
        t3d_1.mul(t3d_2);
        this.tgVolume.setTransform(t3d_1);


        /*----- Position de l'observateur -----*/
        Transform3D t3d_oeil = new Transform3D();
        t3d_oeil.set(new Vector3d(0.0, 2.0, 13.0));
        
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
        
        TextureLoader bgTexture = new TextureLoader("./fond.jpg",this);
        Background bg = new Background(bgTexture.getImage());
        bg.setApplicationBounds(bounds);
        racineVolume.addChild(bg);

        /*----- Ajout de la branche de volume -----*/
        this.universe.addBranchGraph(racineVolume);
        // NE PLUS MODIFIER LA SCENE EN DESSOUS !

        /*----- Rend la fenêtre visible -----*/
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
        
        TransformGroup tg1 = new TransformGroup();
        t3d_1.setTranslation(new Vector3d(0.0, 2.5, 0.0));
        tg1.setTransform(t3d_1);
        
        tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        Cylinder c = new Cylinder();
        c.setAppearance(createAppearance(0));
        tg1.addChild(c);
        
        TransformGroup tg2 = new TransformGroup();
        t3d_1.setTranslation(new Vector3d(3.0, 0.0, 0.0));
        tg2.setTransform(t3d_1);
        tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        Cone co = new Cone();
        co.setAppearance(createAppearance(1));
        tg2.addChild(co);
        
        TransformGroup tg3 = new TransformGroup();
        t3d_1.setTranslation(new Vector3d(0.0, 0.0, 2.0));
        tg3.setTransform(t3d_1);
        tg3.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        tg3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        //Sphere s = new Sphere();
        Sphere s = new Sphere(0.9f, Primitive.GENERATE_NORMALS|Primitive.GENERATE_TEXTURE_COORDS,30);
        s.setAppearance(createAppearance(2));
        tg3.addChild(s);

        /*----- Création du noeud racine -----*/
        BranchGroup racine = new BranchGroup();
        tg2.addChild(tg3);
        tg1.addChild(tg2);
        racine.addChild(tg1);

        /*----- Création du Volume -----*/
        // racine.addChild(new ColorCube());

        /*----- Optimisation du graphe de scène -----*/
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
                
                TextureLoader textload = new TextureLoader("./s.jpg", this);
                aspect.setTexture(textload.getTexture());
                aspect.setTextureAttributes(new TextureAttributes(TextureAttributes.REPLACE, new Transform3D(), new Color4f(Color.BLACK), TextureAttributes.NICEST));
                break;
        }
        return aspect;
    }

    /*------*/
    /* Main */
    /*------*/
    public static void main(String s[]) {
        /*----- Fenêtre -----*/
        new SceneSU(800, 600, 0, 0);
    }
    
} /*----- Fin de la classe SceneSU -----*/
