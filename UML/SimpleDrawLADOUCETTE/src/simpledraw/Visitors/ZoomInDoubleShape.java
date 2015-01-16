/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpledraw.Visitors;

import java.awt.Point;
import simpledraw.Model.Circle;
import simpledraw.Model.Line;
import simpledraw.Model.Rectangle;

/**
 *
 * @author arnaud
 */
public class ZoomInDoubleShape implements ShapeVisitor {

    public void visit(Rectangle s) {
        s.setMyHeight(s.getMyHeight()*2);
        s.setMyWidth(s.getMyWidth()*2);
    }

    public void visit(Line s) {
        s.setMyEnd(new Point(s.getMyEnd().x*2, s.getMyEnd().y*2));
    }

    public void visit(Circle s) {
        s.setMyRadius(s.getMyRadius()*2);
    }
    
}
