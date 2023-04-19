/*
 * TCSS 305 Autumn 2022
 * Assignment 5
 */

package tools;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import model.DrawingPanel;

/**
 * Represents a shape action for the buttons in the paint project.
 * 
 * @author Jacob Erickson
 * @version 2022.12.09
 */
public class ShapeTool extends AbstractAction implements ToolInterface {

    /** Generated serialization number. */
    private static final long serialVersionUID = 6965233873475493669L;
    
    /** The label on the Action. */
    protected final DrawingPanel myPanel;

    /** Support for firing property change events from this class. */
    private final PropertyChangeSupport myPCS = new PropertyChangeSupport(this);

    /** The label on the Action. */
    private final String myName;
    
    /**
     * Constructs an action to control a shape.
     * 
     * @param theName The name.
     * @param theIcon The icon.
     */
    public ShapeTool(final String theName, final DrawingPanel thePanel) {
        super(theName);
        myName = theName;
        setIcon(new ImageIcon("files/" + myName.toLowerCase(Locale.US) + "_bw.gif"));
        myPanel = thePanel;
        
        // coordinate button selection
        putValue(Action.SELECTED_KEY, true);
    }

    
    /** Returns the name of the object. 
     * @return The name. */
    public String getName() {
        return myName;
    }
    
    /** Sets the icon of the object. 
     * @param theNewIcon The new icon.
     */
    @Override
    public final void setIcon(final Icon theNewIcon) {
        putValue(Action.SMALL_ICON, theNewIcon);
    }
    
    /**
     * Fires an event based on the name of the Action.
     * 
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        myPCS.firePropertyChange(myName, null, myName); 
    }
    
    /**
     * Extends the currently designated path, for the Pencil tool.
     */
    public void extendPath(final int theX, final int theY2) {
    }
    
    /**
     * Helps change whether or not a line is being drawn.
     */
    public void toggleLineStatus() {
    }


    @Override
    /**
     * {@inheritDoc}
     */
    public Shape calculateShape(final int theX1, final int theY1, final int theX2, 
            final int theY2) {
        return new Line2D.Double(theX1, theY1, theX2, theY2); 
    }

}