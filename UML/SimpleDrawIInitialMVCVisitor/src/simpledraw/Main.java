package simpledraw;

import simple.draw.mvc.view.ViewTerminal;
import simple.draw.mvc.view.MainFrame;
import simple.draw.mvc.view.ColorFrame;

/**
 * Main program of SimpleDraw
 *
 * @author Rémi Bastide
 * @version 1.0
 */
public class Main {

    /**
     * Construct the application
     */
    public Main() {
        MainFrame frame = new MainFrame();
        // Ajout de la vue d'affichage des formes du modèle dans le terminal
        ViewTerminal vt = new ViewTerminal();
        // Ajout de la vue d'affichage des formes du modèle dans une autre couleur
        ColorFrame cf = new ColorFrame();
        cf.validate();
        cf.setVisible(true);
        // Vue initiale
        frame.validate();
        frame.setVisible(true);
        System.out.println(this.getClass().equals(super.getClass()));

    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        new Main();
    }
}
