package simple.draw.mvc.model;

/**
 * Drawing, a collection of Shapes
 *
 * @author Rémi Bastide
 * @version 1.0
 * @see simpledraw.Shape
 */
import java.util.*;

import java.awt.Point;

import simple.draw.observer.Observable;
import simple.draw.observer.Observer;

public class Drawing implements Observable {

    /**
     * A drawing is a collection of shapes
     */
    private List<Shape> myShapes = new LinkedList<Shape>();

    /**
     * Liste des vues qui "observent" le modèle
     */
    private ArrayList<Observer> listObserver = new ArrayList<Observer>();

    /**
     * Utilisation du pattern Sinfleton afin d'assurer qu'il n'existe qu'une seule instance du modèle.
     * Ainsi chaque nouveau controleur du pattern MVC sera lié au même et unique modèle.
     */
    private static Drawing INSTANCE = new Drawing();

    public static Drawing getDrawing() {
        return INSTANCE;
    }

    private Drawing() {
    }

    /**
     * Add a shape to the Drawing
     *
     * @param s The Shape to add
     *
     */
    public void addShape(Shape s) {
        myShapes.add(s);
        notifyObserver(myShapes);
    }

    /**
     * Delete a shape from the Drawing
     *
     * @param s The Shape to delete
     *
     */
    public void deleteShape(Shape s) {
        myShapes.remove(s);
        notifyObserver(myShapes);
    }

    /**
     * Determines whether the given Point lies whithin a Shape
     *
     * @param p The Point to test
     * @return A Shape selected by this Point or null if no Shape is there
     *
     */
    public Shape pickShapeAt(Point p) {
        Shape result = null;
        for (Shape s : myShapes) {
            if (s.isPickedBy(p)) {
                result = s;
                notifyObserver(myShapes);
                break;
            }
        }
        return result;
    }

    /**
     * Ensures that no Shape is currently selected
     */
    public void clearSelection() {
        for (Shape s : myShapes) {
            s.setSelected(false);
        }
        notifyObserver(myShapes);
    }

    /**
     * Ajoute une vue à la liste des observeurs
     * @param obs 
     */
    public void addObserver(simple.draw.observer.Observer obs) {
        this.listObserver.add(obs);
    }

    /**
     * Supprime toutes les vues de la liste des oberveurs
     */
    public void removeObserver() {
        this.listObserver = new ArrayList<Observer>();
    }

    /**
     * Le modèle notifie les vues d'un changement. Afin que les vues puissent se mettre à jour
     * en conséquence, le modèle leur transmet sa liste de formes.
     * @param l 
     */
    public void notifyObserver(List<Shape> l) {
        for (Observer obs : listObserver) {
            obs.update(l);
        }
    }

}
