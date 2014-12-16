/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelabel;

import java.util.EventListener;

/**
 *
 * @author ladoucar
 */
public abstract class ShapeLabelListener implements EventListener {
    public abstract void ShapeLabelMouseClicked(ShapeLabelEvent e);
}
