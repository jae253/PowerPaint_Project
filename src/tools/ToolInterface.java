/*
 * TCSS 305 Autumn 2022
 * Assignment 5
 */

package tools;

import java.awt.Shape;
import javax.swing.Icon;

/**
 * Interface for all tool shapes.
 * 
 * @author Jacob Erickson
 * @version 2022.12.09
 */
public interface ToolInterface {
    
    /**
     * Returns a shape based on a set of coordinates passed in.
     * 
     * @param theX1 The first x coordinate.
     * @param theY1 The first y coordinate.
     * @param theX2 The second x coordinate.
     * @param theY2 The second y coordinate.
     * @return The calculated shape.
     */
    Shape calculateShape(int theX1, int theY1, int theX2, int theY2);
    
    /**
     * Returns the name of the shape tool.
     * @return The name.
     */
    String getName();
    
    /**
     * Sets the icon of the shape.
     * @param theIcon The icon.
     */
    void setIcon(Icon theIcon);
    
    
}
