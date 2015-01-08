/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.observer;

import java.util.List;
import simple.draw.mvc.model.Shape;

/**
 * Un Observer attends une notification de changement d'état de son Observable et adapte son comportement en conséquence
 * Dans le cadre du pattern MVC, si le modèle notifie les vues d'un changement,
 * les vues se mettront à jour en accord avec la liste des formes du modèle envoyée dans la notification de changement.
 * @author Vincent
 */
public interface Observer {

    public void update(List<Shape> l);
}
