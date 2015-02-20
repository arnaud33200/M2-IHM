import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


/*---------------------*/
/* Classe AppCurseur3d */
/*---------------------*/
/**
 * La classe <code>AppCurseur3d</code> ...
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	15 janvier 2011
 */
public class AppCurseur3d extends JFrame {
    /*---------*/
    /* Données */
    /*---------*/

    /**
     * Données relatives à la fenêtre.
     */
    private final int largeur;	// Taille
    private final int hauteur;
    private final int posx;		// Position
    private final int posy;

    /**
     * Objets composants la structure principale.
     */
    private final VirtualUniverse universe;
    private final Locale locale;
    private final View view;

    private final TransformGroup tgVue;			// Noeud de transformation attaché au point de vue
    private final TransformGroup tgVolume;		// Noeud de transformation attaché au volume

    private final BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);

    private Curseur3d curseur;		// Curseur
    private  Canvas3D canvas;


    /*--------------*/
    /* Constructeur */
    /*--------------*/
    public AppCurseur3d(int l,
            int h,
            int px,
            int py) {
        /*----- Instanciation de la fenêtre graphique -----*/
        this.setTitle("Curseur 3D");
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
        canvas.setFocusable(true);
        conteneur.add("Center", canvas);

        /*----- Création d'un bouton de remise à zéro de la position du curseur -----*/
        JButton raz = new JButton("Raz curseur");
        conteneur.add("South", raz);

        raz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                curseur.initialise();
            }
        });

        /*----- Création de l'univers virtuel -----*/
        this.universe = new VirtualUniverse();
        this.locale = new Locale(this.universe);

        /*----- Position du volume -----*/
        this.tgVolume = new TransformGroup();
        this.tgVolume.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.tgVolume.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.tgVolume.addChild(createBrancheVolume());

        /*----- Position de l'observateur -----*/
        ViewPlatform vp = new ViewPlatform();

        Transform3D t3d_oeil = new Transform3D();
        t3d_oeil.set(new Vector3d(0.0, 0.0, 10.0));

        this.tgVue = new TransformGroup(t3d_oeil);
        this.tgVue.addChild(vp);

        /*----- Création d'une vue et liaison de la plateforme de vue, du canvas et de l'univers physique à la vue -----*/
        this.view = new View();
        this.view.attachViewPlatform(vp);
        this.view.addCanvas3D(canvas);
        this.view.setBackClipDistance(100.0);
        this.view.setPhysicalBody(new PhysicalBody());
        this.view.setPhysicalEnvironment(new PhysicalEnvironment());

        /*----- Création du noeud de branchement de la vue -----*/
        BranchGroup racineVue = new BranchGroup();
        racineVue.addChild(this.tgVue);

        /*----- Création du noeud de branchement du volume -----*/
        BranchGroup racineVolume = new BranchGroup();
        racineVolume.addChild(this.tgVolume);

        /*----- Ajout d'un comportement souris -----*/
        MouseRotate mouseRot = new MouseRotate(this.tgVolume);
        mouseRot.setSchedulingBounds(this.bounds);
        racineVolume.addChild(mouseRot);

        /*----- Ajout à Locale des branches de volume et de vue -----*/
        this.locale.addBranchGraph(racineVue);
        this.locale.addBranchGraph(racineVolume);

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
        /*----- Création du noeud racine -----*/
        BranchGroup racine = new BranchGroup();

        /*----- Création de la source de lumière -----*/
        Color3f lumColor = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f lumDir = new Vector3f(-1.0f, -1.0f, -1.0f);

        DirectionalLight dirlum = new DirectionalLight(lumColor, lumDir);
        dirlum.setInfluencingBounds(this.bounds);
        racine.addChild(dirlum);

        /*----- Cône -----*/
        Transform3D t3d = new Transform3D();

        TransformGroup tgCone = new TransformGroup();
        tgCone.setName("Cone");
        tgCone.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgCone.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(3.0, 3.0, -3.0));
        tgCone.setTransform(t3d);

        Cone co = new Cone();
        co.setAppearance(this.createAppearance(3));
        tgCone.addChild(co);
        racine.addChild(tgCone);

        /*----- Sphère -----*/
        TransformGroup tgSphere = new TransformGroup();
        tgSphere.setName("Sphere");
        tgSphere.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgSphere.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(-3.0, 0.0, 0.0));
        tgSphere.setTransform(t3d);

        Sphere sph = new Sphere(1.0f, 1, 50);
        sph.setAppearance(this.createAppearance(1));
        tgSphere.addChild(sph);
        racine.addChild(tgSphere);

        /*----- Cylindre -----*/
        TransformGroup tgCyl = new TransformGroup();
        tgCyl.setName("Cylindre");
        tgCyl.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgCyl.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(0.0, 0.0, -3.0));
        tgCyl.setTransform(t3d);

        Cylinder cyl = new Cylinder(1.0f, 2.0f, 1, 30, 10, this.createAppearance(2));
        tgCyl.addChild(cyl);
        racine.addChild(tgCyl);

        /*----- Curseur -----*/
        this.curseur = new Curseur3d(true);
        TransformGroup tgc = this.curseur.getNoeudRacine();
        tgc.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgc.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        MovementHand m = new MovementHand(tgc, this.canvas);
        m.addTransformGroup(tgCone);
        m.addTransformGroup(tgSphere);
        m.addTransformGroup(tgCyl);
        tgc.addChild(m);
        racine.addChild(tgc);
        

        /*----- Optimisation du graphe de scène -----*/
        racine.compile();
        return racine;
    }

    /**
     * Retourne une apparence.
     */
    public Appearance createAppearance(int numero) {
        Appearance aspect = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCapability(PolygonAttributes.ALLOW_MODE_WRITE);
        aspect.setPolygonAttributes(pa);

        switch (numero) {
            case 1: {
                /*----- Polygone, couleur bleu, sans interpolation des normales -----*/
                ColoringAttributes color = new ColoringAttributes();
                color.setShadeModel(ColoringAttributes.SHADE_FLAT);
                aspect.setColoringAttributes(color);

                aspect.setMaterial(new Material(new Color3f(0.2f, 0.2f, 0.2f),
                        new Color3f(0.0f, 0.0f, 1.0f), // Couleur émise
                        new Color3f(1.0f, 1.0f, 1.0f),
                        new Color3f(1.0f, 1.0f, 1.0f),
                        64f));
                break;
            }

            case 2: {
                /*----- Polygone, couleur bleu, avec interpolation des normales -----*/
                aspect.setMaterial(new Material(new Color3f(0.2f, 0.2f, 0.2f),
                        new Color3f(0.0f, 0.0f, 1.0f), // Couleur émise
                        new Color3f(1.0f, 1.0f, 1.0f),
                        new Color3f(1.0f, 1.0f, 1.0f),
                        64f));
                break;
            }

            case 3: {
                /*----- Polygone, couleur jaune, avec interpolation des normales -----*/
                aspect.setMaterial(new Material(new Color3f(0.2f, 0.2f, 0.2f),
                        new Color3f(1.0f, 1.0f, 0.0f), // Couleur émise
                        new Color3f(1.0f, 1.0f, 1.0f),
                        new Color3f(1.0f, 1.0f, 1.0f),
                        64f));
                break;
            }

            default:
        }

        return aspect;
    }

} /*----- Fin de la classe AppCurseur3d -----*/
