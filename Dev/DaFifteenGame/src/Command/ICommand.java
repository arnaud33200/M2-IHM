/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

/**
 *
 * @author ladoucar
 */
public interface ICommand {
    public void execute();
    public void undo();
    public void redo();
}
