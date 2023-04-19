/*
 * TCSS 305 Autumn 2022
 * Assignment 5
 */

package tools;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import model.DrawingPanel;

/**
 * Represents a pencil tool.
 * 
 * @author Jacob Erickson
 * @version 2022.12.09
 */
public class PencilTool extends ShapeTool implements ToolInterface {
    
    /** Generated serialization number. */
    private static final long serialVersionUID = 1610501114443484553L;
    
    /** The name of the shape for the icon. */
    private static final String SHAPE_NAME = "Pencil";
    
    /** The path object. */
    private Path2D.Double myLine = new Path2D.Double();
    
    /** Deteremines whether or not the line has been started yet. */
    private boolean myLineStarted = true;
    
    /**
     * Constructs a new pencil tool.
     * @param thePanel The drawing panel to reference.
     */
    public PencilTool(final DrawingPanel thePanel) {
        super(SHAPE_NAME, thePanel);
        myLine.setWindingRule(GeneralPath.WIND_NON_ZERO);
        toggleLineStatus();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public Shape calculateShape(final int theX1, final int theY1, 
            final int theX2, final int theY2) {
        return myLine;
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public void extendPath(final int theX, final int theY) {
        if (!myLineStarted) {
            myLine.moveTo(theX, theY);
            toggleLineStatus();
        }
        myLine.lineTo(theX, theY);
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public final void toggleLineStatus() {
        myLineStarted = !myLineStarted;
        if (!myLineStarted) {
            myLine = new Path2D.Double();
        }
    }
    
}
