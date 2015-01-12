/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

import Modele.GameModel;

/**
 *
 * @author ladoucar
 */
public class PlayTokenCommand implements ICommand {

    private GameModel model;
    private int t;

    public PlayTokenCommand(GameModel model, int t) {
        this.model = model;
        this.t = t;
    }
    
    @Override
    public void execute() {
        model.play(t);
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void redo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
