/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetretestbeans;

import java.awt.Shape;
import javax.swing.JFrame;

/**
 *
 * @author ladoucar
 */
public class FenetreTestBeans {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //NewJFrame f = new NewJFrame();
        JFrame f = new JeuDesQuinzes();
        f.validate();
        f.setVisible(true);
    }
    
}
