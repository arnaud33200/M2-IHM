
package piechart;

/**
 * Une vue du pourcentage qui affiche dans la console
 * @author Rémi Bastide
 */
public class ConsoleView implements PercentageView {

	public void notify(PercentageModel model) {
		System.out.println("La nouvelle valeur du pourcentage est : " + model.getValue() * 100 + "%");
	}
	
}
