/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;

/**
 *
 * @author arnaud
 */
public class PlayerManager {

    private ArrayList<Player> players;
    private int turn;
    
    public PlayerManager(Player... players) {
        this.players = new ArrayList<>();
        for (Player p : players) {
            this.players.add(p);
        }
        turn = 0;
    }
    
    public Player nextPlayer() {
        turn = (turn + 1) % players.size();
        return players.get(turn);
    }
    
    public Player getCurrentPlayer() {
        return players.get(turn);
    }
    
    public void resetMananger() {
        for (Player p : players) {
            p.clearTokens();
        }
        turn = 0;
    }

    public void setCurrentPlayer(int p) {
        p -= 1;
        if (p < 0 || p > players.size()-1) {
            p = 0;
        }
        turn = p;
    }
    
}
