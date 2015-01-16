/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpledraw.Model;

import simpledraw.Visitors.ShapeVisitor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author arnaud
 */
public class Rectangle extends Shape {
    
    private Point myStart;
    private float myHeight;
    private float myWidth;

    public Rectangle(Point myStart, float myHeight, float myWidth) {
        this.myStart = myStart;
        this.myHeight = myHeight;
        this.myWidth = myWidth;
    }
    
    @Override
    public void draw(Graphics2D g) {
        try {
            g.setColor(
			isSelected() ?
			Color.red :
			Color.black
			);
		g.drawRect(myStart.x, myStart.y, (int)myHeight, (int)myWidth);
        } catch (NullPointerException e) { }
    }

    @Override
    public void translateBy(int dx, int dy) {
        myStart.translate(dx, dy);
    }

    @Override
    public boolean isPickedBy(Point p) {
        return isPointBelongTo(p);
    }

    public Point getMyStart() {
        return myStart;
    }

    public void setMyStart(Point myStart) {
        this.myStart = myStart;
    }

    public float getMyHeight() {
        return myHeight;
    }

    public void setMyHeight(float myHeight) {
        this.myHeight = myHeight;
    }

    public float getMyWidth() {
        return myWidth;
    }

    public void setMyWidth(float myWidth) {
        this.myWidth = myWidth;
    }

    private boolean isPointBelongTo(Point p) {
        
        if ( (p.x > myStart.x-2 && p.x < myStart.x+2) || (p.x > myWidth-2 && p.x < myWidth+2) ) {
            return (p.y > myStart.y-2 && p.y < myHeight+2);
        }
        else if ( (p.y > myStart.y-2 && p.y < myStart.y+2) || (p.y > myHeight-2 && p.y < myHeight+2) ) {
            return (p.x > myStart.x-2 && p.x < myWidth+2);
        }
        return false;
    }

    @Override
    public void accept(ShapeVisitor v) {
        v.visit(this);
    }
    
    
}
