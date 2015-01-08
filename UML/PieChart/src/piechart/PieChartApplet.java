package piechart;

import javax.swing.JApplet;

/**
 * Titre :        Exemple d'architecture MVC
 * Description :  Ce projet illustre l'architecture MVC telle qu'elle est implémentée en Java/Swing.
 * Le modéle est un pourcentage (valeur réelle telle que  O <= x < 1)
 * On développe plusieurs vues et plusieurs contréleurs différents pour ce modéle
 * Copyright :    Copyright (c) 2000
 * Société :      LIHS - Université Toulouse 1 - DESS IHM
 * @author
 * @version 1.0
 */

public class PieChartApplet extends JApplet {

	/**Construire l'applet*/
	public PieChartApplet() {
	}

	/**Initialiser l'applet*/
	public void init() {
		getContentPane().add(new PercentagePanel());
	}
}
