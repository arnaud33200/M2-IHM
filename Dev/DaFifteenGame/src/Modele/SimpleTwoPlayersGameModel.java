/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Command.ICommand;

/**
 *
 * @author arnaud
 */
public class SimpleTwoPlayersGameModel extends GameModel {
    
    private boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

    public SimpleTwoPlayersGameModel(Player p1, Player p2) {
        super(p1, p2);
    }
    
    @Override
    public void play(int t) {
        if (gameOver) {
            return;
        }
        Token st = availableTokens.get(t);
        boolean removed = availableTokens.remove(t,st);
        if (removed){
            Player p = pManager.getCurrentPlayer();
            p.addToken(st);
            pManager.nextPlayer();
            notifyAllView();
            if (p.isWinner()) {
                winner = p;
                gameOver = true;
                fireAllGameIsOverListener();
            } else {
            fireAllNextPlayerListener();
            }
        }
    }

    @Override
    public void resetGame() {
        initJetonsDisponibles();
        gameOver = false;
        pManager.resetMananger();
        notifyAllView();
    }

    @Override
    public void startGame(int playerNumber) {
        resetGame();
        pManager.setCurrentPlayer(playerNumber);
        fireAllNextPlayerListener();
        fireAllGameHasBegunListener();
    }

    @Override
    public void undo() {
        ICommand c = undomanager.undo();
        if (c != null) {
            c.undo();
        }
    }

    @Override
    public void redo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
