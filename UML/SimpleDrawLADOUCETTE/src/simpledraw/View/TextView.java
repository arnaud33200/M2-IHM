/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpledraw.View;

import simpledraw.Model.DrawingModel;
import javax.swing.JTextField;

/**
 *
 * @author arnaud
 */
public class TextView extends JTextField implements DrawingView {

    private int count = -1  ;
    
    public void notify(DrawingModel m) {
        if (count != m.getNumberOfShapes()) {
            count = m.getNumberOfShapes();
            this.setText(Integer.toString(m.getNumberOfShapes()));
        }
    }
    
}
