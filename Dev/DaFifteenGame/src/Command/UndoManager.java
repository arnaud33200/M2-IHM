/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

import java.util.Stack;

/**
 *
 * @author ladoucar
 */
public class UndoManager {
    private Stack<ICommand> undoStack;
    private Stack<ICommand> redoStack;

    public UndoManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }
    
    public void registerUndoCommand(ICommand c){
        undoStack.push(c);
    }
    
    public ICommand undo() {
        ICommand c = undoStack.pop();
        redoStack.push(c);
        return c;
    }
}
