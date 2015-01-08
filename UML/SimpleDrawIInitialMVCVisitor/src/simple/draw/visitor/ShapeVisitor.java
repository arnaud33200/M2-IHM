/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.visitor;

import simple.draw.mvc.model.Line;
import simple.draw.mvc.model.Circle;
import simple.draw.mvc.model.PolyLine;

/**
 * Une méthode visit qui remplace la méthode "draw" dans chaque sous classe de Shape
 * @author Vincent
 */
public interface ShapeVisitor {

    void visit(Circle shape);

    void visit(Line shape);

    void visit(PolyLine shape);
}
