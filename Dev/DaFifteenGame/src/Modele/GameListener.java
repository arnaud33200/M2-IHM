/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.EventListener;

/**
 *
 * @author ladoucar
 */
public abstract class GameListener implements EventListener {
    public abstract void GamePlayerChanged(GameEvent e);
    public abstract void GameIsOver(GameEvent e);
    public abstract void GamehasBeggun(GameEvent e);
}
