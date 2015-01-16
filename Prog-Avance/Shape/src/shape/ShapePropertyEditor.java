/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shape;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author ladoucar
 */
public class ShapePropertyEditor extends PropertyEditorSupport{
    private int value;
    @Override
    public String[] getTags() {
        return new String[]{"ovale","rectangle"}; 
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
       switch(text){
           case "ovale": value = 1; break;
           default: value = 2;
       }
       firePropertyChange();
    }

    @Override
    public String getAsText() {
        return value == 2 ? "rectangle":"ovale";
    }

    @Override
    public String getJavaInitializationString() {
        return Integer.toString(value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        value = (int) value;
    }
    
    
}
