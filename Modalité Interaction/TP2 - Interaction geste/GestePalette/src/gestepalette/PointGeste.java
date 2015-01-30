/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestepalette;

import java.awt.geom.Point2D;

/**
 *
 * @author ladoucar
 */
public class PointGeste extends Point2D 
{
    public double x;
    public double y;

    public PointGeste(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
}
