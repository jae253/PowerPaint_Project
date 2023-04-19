/*
 * TCSS 305 Autumn 2022
 * Assignment 5
 */

package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import tools.PencilTool;
import tools.ShapeTool;

/**
 * Represents a drawing panel for the Paint program.
 * 
 * @author Jacob Erickson
 * @version 2022.12.09
 */
public class DrawingPanel extends JPanel {

    /** The background color of the panel. */
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    
    /** UW Purple color. */
    private static final Color UW_PURPLE = new Color(51, 0, 111);
    
    /** UW Purple color. */
    private static final Color UW_GOLD = new Color(232, 211, 162);
    
    /** 
     * A constant to set the intial line outside the bounds of the drawing panel. 
     * DO NOT CHANGE!
     */
    private static final int INITIAL_COORDS = -15;
    
    /** Number of coordinates in the drawing shape. DO NOT CHANGE! */
    private static final int COORD_COUNT = 4;
    
    /** Generated serialization number. */
    private static final long serialVersionUID = 8080502492180943995L;
    
    /** The color to paint with. */
    private Color myCurrentColor = UW_PURPLE;
    
    /** The color to fill with. */
    private Color myCurrentFillColor = UW_GOLD;
    
    /** The slider. */
    private final JSlider mySlider;
    
    /** The coordinates of the shape. */
    private int[] myCoords = {INITIAL_COORDS, INITIAL_COORDS, INITIAL_COORDS, INITIAL_COORDS};
    
    /** The list of shapes to store in memory. */
    private final List<LegacyShape> myLegacyShapes;
    
    /** Support for firing property change events from this class. */
    private final PropertyChangeSupport myPCS = new PropertyChangeSupport(this);

    /** The current tool set. */
    private ShapeTool myCurrentTool;
    
    /** The default action for the clear button. */
    private final JMenuItem myClearAction;
    
    /** The default action for the clear button. */
    private final JCheckBoxMenuItem myFillAction;
    
    /** The current shape being drawn. */
    private Shape myCurrentShape;

    
    /** 
     * Constructs a new drawing panel.
     * 
     * @param The preferred height of the panel.
     * @param The preferred width of the panel.
     * @param The slider to read the thickness from.
     *  
     */
    public DrawingPanel(final JSlider theSlider, final JMenuItem theClearAction, 
            final JCheckBoxMenuItem theFillAction) {
        super();
        theClearAction.setEnabled(false);
        myClearAction = theClearAction;
        myFillAction = theFillAction;
        mySlider = theSlider;
        myLegacyShapes = new ArrayList<LegacyShape>();
        initializationHelper();
    }
    
    /**
     * Helper method to setup the drawing panel.
     */
    private void initializationHelper() {
        setCursor(new Cursor(1));
        setBackground(BACKGROUND_COLOR);
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseListener());
    }
    
    /** 
     * Sets the color of the brush.
     * 
     * @param The color.
     */
    public void setCurrentColor(final Color theColor) {
        myCurrentColor = theColor;
    }
    
    /** 
     * Returns the color of the brush.
     * 
     * @return The current color.
     */
    public Color getCurrentColor() {
        return myCurrentColor;
    }
    
    /** 
     * Sets the color of the brush.
     * 
     * @param The color.
     */
    public void setCurrentFillColor(final Color theColor) {
        myCurrentFillColor = theColor;
    }
    
    /** 
     * Returns the color of the brush.
     * 
     * @return The current color.
     */
    public Color getCurrentFillColor() {
        return myCurrentFillColor;
    }
    
    /** 
     * Sets the tool used.
     * 
     * @param The tool.
     */
    public void setCurrentTool(final ShapeTool theTool) {
        myCurrentTool = theTool;
        theTool.setIcon(new ImageIcon("files/" + theTool.getName().toLowerCase(Locale.US)
                + ".gif"));
    }
    
    /** 
     * Returns the tool being used.
     * 
     * @return The current tool.
     */
    public ShapeTool getCurrentTool() {
        return myCurrentTool;
    }
    
    
    
    /** 
     * Clears the drawing panel.
     */
    public void resetDrawing() {
        myLegacyShapes.clear();
        myClearAction.setEnabled(false);
        repaint();
    }

    /**
     * Paints the current path.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        
        final Graphics2D g2d = (Graphics2D) theGraphics;
        
        // Calculates shape based on tool selected
        myCurrentShape = myCurrentTool.calculateShape(myCoords[0], myCoords[1], myCoords[2], 
                myCoords[COORD_COUNT - 1]);
        
        // Line smoothing/anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draws all previous shapes
        for (final LegacyShape s : myLegacyShapes) {
            
            final Shape pastShape = s.getShape();
            final Color fillColor = s.getFillColor();
            
            // Draws fill color first if applicable
            if (fillColor != null) {
                g2d.setStroke(new BasicStroke(0));
                g2d.setPaint(s.getFillColor());
                g2d.fill(pastShape); 
            }        
            g2d.setPaint(s.getColor());
            g2d.setStroke(new BasicStroke(s.getThickness(), BasicStroke.CAP_ROUND, 
                    BasicStroke.JOIN_ROUND));
            g2d.draw(pastShape);
            
        }
        
        // Draws the current shape
        if (myCurrentFillColor != null && myFillAction.isSelected() 
                && !(myCurrentTool instanceof PencilTool)) {
            g2d.setPaint(myCurrentFillColor);
            g2d.fill(myCurrentShape);
        }
        g2d.setPaint(myCurrentColor);
        g2d.setStroke(new BasicStroke(mySlider.getValue(), BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_ROUND));
        g2d.draw(myCurrentShape);
        
    }
    
    /**
     * Adds a listener for property change events from this class.
     * 
     * @param theListener a PropertyChangeListener to add.
     */
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPCS.addPropertyChangeListener(theListener);
    }
    
    /**
     * Removes a listener for property change events from this class.
     * 
     * @param theListener a PropertyChangeListener to remove.
     */
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPCS.removePropertyChangeListener(theListener);
    }
    
    

    // Inner Class

    /**
     * Listens for mouse clicks, to draw on our panel.
     */
    private class MyMouseListener extends MouseAdapter {
        /**
         * Handles a click event.
         * 
         * @param theEvent The event.
         */
        
        @Override
        public void mousePressed(final MouseEvent theEvent) {
            if (mySlider.getValue() > 0) { 
                for (int i = 0; i < 2; i++) {
                    myCoords[i * 2] = theEvent.getX();
                    myCoords[(i * 2) + 1] = theEvent.getY();
                }
                if (myCurrentTool instanceof PencilTool) {
                    myCurrentTool.extendPath(theEvent.getX(), theEvent.getY());
                    repaint();
                }
            } 
        }
        
        /**
         * Handles a drag event.
         * 
         * @param theEvent The event.
         */
        @Override
        public void mouseDragged(final MouseEvent theEvent) {
            if (mySlider.getValue() > 0) {
                
                if (myCurrentTool instanceof PencilTool) {
                    myCurrentTool.extendPath(theEvent.getX(), theEvent.getY());
                }
                
                myCoords[2] = theEvent.getX();
                myCoords[COORD_COUNT - 1] = theEvent.getY();
                repaint();
            }
        }
        
        /**
         * Handles a release event.
         * 
         * @param theEvent The event.
         */
        @Override
        public void mouseReleased(final MouseEvent theEvent) {
            if (mySlider.getValue() > 0) {
                
                myCurrentTool.toggleLineStatus();
                
                myClearAction.setEnabled(true);
                if (myFillAction.isSelected() && myCurrentFillColor != null 
                        && !(myCurrentTool instanceof PencilTool)) {
                    myLegacyShapes.add(new LegacyShape(myCurrentShape, mySlider.getValue(), 
                            myCurrentColor, myCurrentFillColor));
                } else {
                    myLegacyShapes.add(new LegacyShape(myCurrentShape, mySlider.getValue(), 
                            myCurrentColor)); 
                }

                for (int i = 0; i < COORD_COUNT; i++) {
                    myCoords[i] = INITIAL_COORDS;
                }
                
                repaint();
            }
        } 
    }
}
