/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.draw.visitor;

import simple.draw.mvc.controler.LineTool;
import simple.draw.mvc.controler.SelectionTool;
import simple.draw.mvc.controler.CircleTool;

/**
 * Une méthode visit qui remplace la méthode "draw" dans chaque sous classe de DrawingTool
 * @author Vincent
 */
public interface ToolVisitor {

    void visit(CircleTool tool);

    void visit(LineTool tool);

    void visit(SelectionTool tool);
}
