/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author arnaud
 */
public class Player {
    
    private String name;
    private Color color;
    private ArrayList<Integer> jetonsCollectes;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        jetonsCollectes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<Integer> getJetonsCollectes() {
        return jetonsCollectes;
    }

    public void setJetonsCollectes(ArrayList<Integer> jetonsCollectes) {
        this.jetonsCollectes = jetonsCollectes;
    }

    void addToken(Token st) {
        jetonsCollectes.add(st.getNumero());
    }
    
    void clearTokens() {
        jetonsCollectes.clear();
    }

    boolean isWinner() {
        if (jetonsCollectes.size() >= 3) {
            for (int j1 : jetonsCollectes) {
                for (int j2 : jetonsCollectes) {
                    for (int j3 : jetonsCollectes) {
                        if ((j1+j2+j3) == 15) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    
    
    
}
