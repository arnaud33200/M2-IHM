/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpledraw.View;
import simpledraw.View.DrawingView;

import simpledraw.Model.DrawingModel;

/**
 *
 * @author arnaud
 */
public class ConsolView implements DrawingView {

    private int nbShape = 0;
    
    public void notify(DrawingModel m) {
        if (nbShape != m.getNumberOfShapes()) {
            nbShape = m.getNumberOfShapes();
            System.out.println("Shapes : " + m.toString());
        }
    }
}
