/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.observer;

import java.util.List;
import simple.draw.mvc.model.Shape;

/**
 * Un Obervable possède une liste d'observeurs qu'il pourra prévenir lorsque son état change
 * Permet la mise en place du pattern MVC, l'observable étant le modèle et les vues étant les observeurs
 * @author Vincent
 */
public interface Observable {

    public void addObserver(Observer obs);

    public void removeObserver();

    public void notifyObserver(List<Shape> l);
}
