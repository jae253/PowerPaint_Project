/*
 * TCSS 305 Autumn 2022
 * Assignment 5
 */

package tools;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import model.DrawingPanel;

/**
 * Represents a rectangle tool.
 * 
 * @author Jacob Erickson
 * @version 2022.12.09
 */
public class RectangleTool extends ShapeTool implements ToolInterface {
    
    /** Generated serialization number. */
    private static final long serialVersionUID = 1610501114443484553L;
    
    /** The name of the shape for the icon. */
    private static final String SHAPE_NAME = "Rectangle";
    
    public RectangleTool(final DrawingPanel thePanel) {
        super(SHAPE_NAME, thePanel);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public Shape calculateShape(final int theX1, final int theY1, 
            final int theX2, final int theY2) {
        int finalX = 0;
        int finalY = 0;
        final int width = Math.abs(theX2 - theX1);
        final int height = Math.abs(theY2 - theY1);
        if (theX1 < theX2) {
            finalX = theX1;
        } else {
            finalX = theX2;
        }
        if (theY1 < theY2) {
            finalY = theY1;
        } else {
            finalY = theY2;
        }
        
        return new Rectangle2D.Double(finalX, finalY, width, height);
    }
}
