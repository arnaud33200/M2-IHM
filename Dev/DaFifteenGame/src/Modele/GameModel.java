/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Command.UndoManager;
import View.GameView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author arnaud
 */
public abstract class GameModel {

    protected HashMap<Integer, Token> availableTokens;
    protected PlayerManager pManager;
    protected ArrayList<GameView> viewers;
    protected ArrayList<GameListener> listeners;
    protected Player winner = null;
    protected UndoManager undomanager = new UndoManager();

    public GameModel(Player... players) {
        viewers = new ArrayList<>();
        listeners = new ArrayList<>();
        availableTokens = new HashMap<>();
        initJetonsDisponibles();
        pManager = new PlayerManager(players);
    }
    
    public void addViewListener(GameView v) {
        viewers.add(v);
        v.notify(this);
    }
    
    public void removeViewListener(GameView v) {
        viewers.remove(v);
    }
    
    protected void notifyAllView() {
        for (GameView v : viewers) {
            v.notify(this);
        }
    }
    
    public void addGameListener(GameListener l) {
        listeners.add(l);
    }
    
    public void removeGameListener(GameListener l) {
        listeners.remove(l);
    }
    
    protected void fireAllNextPlayerListener() {
        for (GameListener l : listeners) {
            l.GamePlayerChanged(new GameEvent(pManager.getCurrentPlayer()));
        }
    }
    
    protected void fireAllGameIsOverListener() {
        for (GameListener l : listeners) {
            l.GameIsOver(new GameEvent(winner));
        }
    }
    
    protected void fireAllGameHasBegunListener() {
        for (GameListener l : listeners) {
            l.GamehasBeggun(new GameEvent(pManager.getCurrentPlayer()));
        }
    }

    protected void initJetonsDisponibles() {
        availableTokens.clear();
        for (int i=0; i<9; ++i) {
            availableTokens.put(i+1, new Token(i+1));
        }
    }
    
    public abstract void play(int t);
    public abstract void resetGame();
    public abstract void undo();
    public abstract void redo();
    public void startGame(int playerNumber) {}

    public HashMap<Integer, Token> getAvailableTokens() {
        return availableTokens;
    }

    public PlayerManager getpManager() {
        return pManager;
    }

    public Player getCurrentPlayer() {
        return pManager.getCurrentPlayer();
    }
}
