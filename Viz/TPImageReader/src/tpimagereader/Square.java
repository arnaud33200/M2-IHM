/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpimagereader;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author ladoucar
 */
public class Square {

    
    public Square(int x, int y, int w, Color c) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.c = c;
    }
    
    public Square() {
        this(0, 0, 0, Color.red);
        Random r = new Random();
        this.x = r.nextInt(2000);
        this.y = r.nextInt(2000);
        this.w = r.nextInt(31)+4;
        this.c = pickColor(r.nextInt(3));
    }
    
    public int x,y,w;
    public Color c;

    private Color pickColor(int nextInt) {
        Color c = Color.BLACK;
        switch (nextInt) {
            case 0: c = Color.ORANGE;
                break;
            case 1: c = Color.pink;
                break;
            case 2: c = Color.yellow;
                break;
        }
        return c;
    }
}
