import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import java.io.FileNotFoundException;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;


/*------------------*/
/* Classe Curseur3d */
/*------------------*/
/**
 * La classe <code>Curseur3d</code> ...
 *
 * @author	<a href="mailto:berro@univ-tlse1.fr">Alain Berro</a>
 * @version	15 janvier 2011
 */
public class Curseur3d
{
	/*---------*/
	/* Données */
	/*---------*/

	private final BranchGroup bgOuvert;

	private final BranchGroup bgFerme;

	private final TransformGroup tgPosition;

	private final TransformGroup tgOrientation;

	private final TransformGroup tgCurseur;

	private boolean ouvert;

	private final boolean tourne; // Si vrai alors on effectue la rotation de +PI/2 en X.


	/*--------------*/
	/* Constructeur */
	/*--------------*/

	/**
	 * Construit et initialise.
	 */
	public Curseur3d (boolean tourne)
		{
		this.tourne = tourne;

		/*----- Chargement du curseur (main ouverte) -----*/
		Scene s1 = null;
		ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);

		try {
			s1 = loader.load("./ressource/hand1.obj");
			}
			catch (FileNotFoundException e)
				{
				System.err.println(e);
				System.exit(1);
				}
			catch (ParsingErrorException e)
				{
				System.err.println(e);
				System.exit(1);
				}
			catch (IncorrectFormatException e)
				{
				System.err.println(e);
				System.exit(1);
				}

		/*----- Chargement du curseur (main fermée) -----*/
		Scene s2 = null;

		try {
			s2 = loader.load("./ressource/hand3.obj");
			}
			catch (FileNotFoundException e)
				{
				System.err.println(e);
				System.exit(1);
				}
			catch (ParsingErrorException e)
				{
				System.err.println(e);
				System.exit(1);
				}
			catch (IncorrectFormatException e)
				{
				System.err.println(e);
				System.exit(1);
				}

		/*----- Curseur (main ouverte) -----*/
		this.bgOuvert = new BranchGroup();
		this.bgOuvert.setCapability(BranchGroup.ALLOW_DETACH);
		this.bgOuvert.addChild(s1.getSceneGroup());

		/*----- Curseur (main fermé) -----*/
		this.bgFerme = new BranchGroup();
		this.bgFerme.setCapability(BranchGroup.ALLOW_DETACH);
		this.bgFerme.addChild(s2.getSceneGroup());

		/*----- TransformGroup du curseur -----*/
		this.tgCurseur = new TransformGroup();
		this.tgCurseur.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		this.tgCurseur.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

		this.tgCurseur.addChild(this.bgOuvert);

		/*----- Orientation du curseur -----*/
		this.tgOrientation = new TransformGroup();
		Transform3D t3d = new Transform3D();
		if (this.tourne) t3d.rotX(Math.PI/2);
		this.tgOrientation.setTransform(t3d);

		this.tgOrientation.addChild(this.tgCurseur);

		/*----- Position du curseur -----*/
		this.tgPosition = new TransformGroup();
		this.tgPosition.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.tgPosition.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		this.tgPosition.addChild(this.tgOrientation);

		/*----- Etat du curseur -----*/
		this.ouvert = true;
		}


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Retourne le noeud (group) racine du curseur.
	 */
	public TransformGroup getNoeudRacine () { return this.tgPosition; }


	/**
	 * Retourne l'état du curseur.
	 */
	public boolean isOuvert () { return this.ouvert; }


	/**
	 * Mise à jour de l'état du curseur.
	 */
	public void setOuvert (boolean etat)
		{
		if	(this.ouvert)
			{
			if	(!etat)
				{
				this.bgOuvert.detach();
				this.tgCurseur.addChild(this.bgFerme);
				this.ouvert = false;
				}
			}
		else
			{
			if	(etat)
				{
				this.bgFerme.detach();
				this.tgCurseur.addChild(this.bgOuvert);
				this.ouvert = true;
				}
			}
		}


	/**
	 * Réinitialise le position du curseur.
	 */
	public void initialise () { this.tgPosition.setTransform(new Transform3D()); }


	/**
	 * Retourne la position (x, y, z) du curseur.
	 */
	public Vector3d getPosition ()
		{
		Transform3D t3d = new Transform3D();
		Vector3d v3d = new Vector3d();
		this.tgPosition.getTransform(t3d);
		t3d.get(v3d);
		return v3d;
		}


	/**
	 * Mise à jour de la position (x, y, z) du curseur.
	 */
	public void setPosition (Vector3d v3d)
		{
		Transform3D t3d = new Transform3D();
		this.tgPosition.getTransform(t3d);
		t3d.setTranslation(v3d);
		this.tgPosition.setTransform(t3d);
		}

} /*----- Fin de la classe Curseur3d -----*/
