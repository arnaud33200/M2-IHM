/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpledraw.Visitors;

import simpledraw.Model.Circle;
import simpledraw.Model.Line;
import simpledraw.Model.Rectangle;

/**
 *
 * @author arnaud
 */
public interface ShapeVisitor {
    void visit(Rectangle s);
    void visit(Line s);
    void visit(Circle s);
}
