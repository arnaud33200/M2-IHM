/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.mvc.view;

import simple.draw.mvc.controler.DrawingControler;
import java.util.List;
import simple.draw.observer.Observer;
import simple.draw.mvc.model.Shape;
import simple.draw.visitor.TerminalDrawing;

/**
 * Nouvelle vue du modèle qui affiche les formes du modèle dans le terminal
 * @author Vincent
 */
public class ViewTerminal implements Observer {

    DrawingControler myDrawingControler;
    TerminalDrawing td;

    public ViewTerminal() {
        td = new TerminalDrawing();
        myDrawingControler = new DrawingControler();
        myDrawingControler.addObserverToModel(this);
    }

    public void update(List<Shape> l) {
        for (Shape s : l) {
            s.accept(td);
        }
    }

}
