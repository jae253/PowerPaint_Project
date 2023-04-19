/*
 * TCSS 305 Autumn 2022
 * Assignment 5
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.DefaultAction;
import model.DrawingPanel;
import tools.EllipseTool;
import tools.LineTool;
import tools.PencilTool;
import tools.RectangleTool;
import tools.ShapeTool;

/**
 * Creates a GUI for the Paint program.
 * 
 * @author Jacob Erickson
 * @version 2022.12.09
 */
public final class PaintGUI implements PropertyChangeListener {
    
    /** The icon of the application. */ 
    private static final ImageIcon IMG = new ImageIcon("files/paint_icon.png");
    
    /** A ToolKit. */
    private static final Toolkit KIT = Toolkit.getDefaultToolkit();
    
    /** The Dimension of the screen. */
    private static final Dimension SCREEN_SIZE = KIT.getScreenSize();
    
    /** The spacing between numbers on the thickness slider. */
    private static final int MAJOR_TICK_SPACING = 5;

    /** The spacing between tick marks on the thickness slider. */
    private static final int MINOR_TICK_SPACING = 1;
    
    /** The width of the screen. */
    private static final int SCREEN_WIDTH = SCREEN_SIZE.width;
    
    /** The height of the screen. */
    private static final int SCREEN_HEIGHT = SCREEN_SIZE.height;
    
    /** The scaling of the JFrame window relative to the size of the screen. */
    private static final int SCALE = 3;
    
    /** The initial brush thickness. */
    private static final int INITIAL_BRUSH_THICKNESS = 3;
    
    /** The max brush thickness. */
    private static final int MAX_BRUSH_THICKNESS = 15;

    /** The JFrame. */
    private static final JFrame FRAME = new JFrame("TCSS 305 Paint – Autumn 2022");
    
    /** Array of labels for the action buttons. */
    private static final String[] LABELS = {"Color...", "Clear", "About...", 
        "Fill Color...", "Fill"};
    
    /** The toolbar. */
    private JToolBar myToolBar;
    
    /** The slider. */
    private final JSlider mySlider = new JSlider(SwingConstants.HORIZONTAL, 0, 
            MAX_BRUSH_THICKNESS, INITIAL_BRUSH_THICKNESS);
    
    /** Array of actions. */
    private DefaultAction[] myActions = new DefaultAction[LABELS.length];
    
    /** The drawing panel. */
    private final DrawingPanel myDrawingPanel;
    
    /** The collection of shape actions. */
    private List<ShapeTool> myShapeActions;
    
    /** The clear button. */
    private final JMenuItem myClearButton;
    
    /** The default action for the fill button. */
    private final JCheckBoxMenuItem myFillButton;
    
    /** Default GUI constructor. */
    public PaintGUI() {
        super();
        
        myToolBar = new JToolBar();
        
        for (int i = 0; i < LABELS.length; i++) {
            myActions[i] = new DefaultAction(LABELS[i]); 
            myActions[i].addPropertyChangeListener(this);
        }
        
        myClearButton = new JMenuItem(myActions[1]);
        myFillButton = new JCheckBoxMenuItem(myActions[2 + 2]);
        
        myFillButton.setEnabled(false);
        
        myDrawingPanel = new DrawingPanel(mySlider, myClearButton, myFillButton);
        myDrawingPanel.addPropertyChangeListener(this);
        
        start();
    }
    
    /** Performs all tasks necessary to display the UI. */
    public void start() {
        
        
        try {           
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        
        } catch (final UnsupportedLookAndFeelException e) {
            System.out.println("UnsupportedLookAndFeelException");
        } catch (final ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
        } catch (final InstantiationException e) {
            System.out.println("InstantiationException");
        } catch (final IllegalAccessException e) {
            System.out.println("IllegalAccessException");
        }
        
        FRAME.setIconImage(IMG.getImage());
        
        myToolBar = createToolBar();

        FRAME.add(myToolBar, BorderLayout.SOUTH);
        
        FRAME.setJMenuBar(createMenuBar());
        
        FRAME.add(myDrawingPanel, BorderLayout.CENTER);
        
        
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setSize(new Dimension(SCREEN_WIDTH / SCALE, SCREEN_HEIGHT / SCALE));
        FRAME.setLocationRelativeTo(null);
        FRAME.setVisible(true); 
    }
    
    /**
     * Returns the menu bar.
     * @return The menu bar.
     */
    private JMenuBar createMenuBar() {
        final JMenuBar mBar = new JMenuBar();
        final JMenu mOptions = new JMenu("Options");
        final JMenu mThickness = new JMenu("Thickness");
        final JMenu mHelp = new JMenu("Help");
        final JMenu mTools = new JMenu("Tools");
        
        
        mBar.add(mOptions);
        mBar.add(mTools);
        mBar.add(mHelp);
        
        mOptions.add(mThickness);
        mOptions.addSeparator();
        mOptions.add(myActions[0]);
        mOptions.add(myActions[2 + 1]);
        mOptions.addSeparator();
        mOptions.add(myFillButton);
        mOptions.addSeparator();
        mOptions.add(myClearButton);
        
        mySlider.setMajorTickSpacing(MAJOR_TICK_SPACING);
        mySlider.setMinorTickSpacing(MINOR_TICK_SPACING);
        mySlider.setPaintLabels(true);
        mySlider.setPaintTicks(true);
        mThickness.add(mySlider);
        
        final ButtonGroup bGroup = new ButtonGroup();
        for (final ShapeTool s : myShapeActions) {
            final JRadioButtonMenuItem rButton = new JRadioButtonMenuItem(s);
            s.addPropertyChangeListener(this);
            bGroup.add(rButton);
            mTools.add(rButton);
        }
        
        mHelp.add(myActions[2]);
        
        return mBar;
    }
    
    
    /** 
     * Creates the toolbar. 
     * 
     * @return The toolbar.
     */
    private JToolBar createToolBar() {
        
        myShapeActions = new ArrayList<ShapeTool>();
        
        myShapeActions.add(new LineTool(myDrawingPanel));
        myShapeActions.add(new RectangleTool(myDrawingPanel));
        myShapeActions.add(new EllipseTool(myDrawingPanel));
        myShapeActions.add(new PencilTool(myDrawingPanel));
        
        myDrawingPanel.setCurrentTool(myShapeActions.get(0));
        
        final JToolBar tb = new JToolBar();
        
        final ButtonGroup bGroup = new ButtonGroup();
        for (final ShapeTool s : myShapeActions) {
            final JToggleButton tButton = new JToggleButton(s);
            bGroup.add(tButton);
            tb.add(tButton);
        }
        
        return tb;
    }
    
    /**
     * Helper method for the "Color..." action.
     */
    private void colorHelper() {
        final Color result = JColorChooser.showDialog(null, "Color Chooser", 
                myDrawingPanel.getCurrentColor());
        
        // System.out.println("Color chosen: " + result);
        if (result != null) {
            myDrawingPanel.setCurrentColor(result);
        }  
    }
    
    /**
     * Helper method for the "Fill Color..." action.
     */
    private void fillColorHelper() {
        final Color result = JColorChooser.showDialog(null, "Fill Color Chooser", 
                myDrawingPanel.getCurrentFillColor(), true);
        
        if (result != null) {
            myDrawingPanel.setCurrentFillColor(result);
        }  
    }
    
    /**
     * 'Helper' method for the "Clear" action.
     */
    private void clearHelper() {
//        System.out.println("Clear button pressed!");
        myDrawingPanel.resetDrawing();
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        
        final Object s = theEvent.getSource();
        if (theEvent.getPropertyName().equals("SwingSelectedKey") 
                && theEvent.getNewValue().equals(true)) {
            
            if (s instanceof ShapeTool) {
                myDrawingPanel.setCurrentTool((ShapeTool) s);
                myFillButton.setEnabled(!(s instanceof LineTool 
                        || s instanceof PencilTool));
            }

            final ShapeTool cTool = myDrawingPanel.getCurrentTool();
            
            for (final ShapeTool t : myShapeActions) {
                final String prefix = "files/" + t.getName();
                if (!(t.equals(cTool))) {
                    t.setIcon(new ImageIcon(prefix + "_bw.gif"));
                }
            }
                
        } else {
            labelChecker(theEvent);
        }
        
        
    }
    
    /**
     * Helper method for the propertyChange method.
     * @param theEvent The event passed in from the propertyChange method.
     */
    public void labelChecker(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals(LABELS[1])) {
            clearHelper();
        } else if (theEvent.getPropertyName().equals(LABELS[0])) {
            colorHelper();
        } else if (theEvent.getPropertyName().equals(LABELS[2 + 1])) {
            fillColorHelper();
        } else if (theEvent.getPropertyName().equals(LABELS[2])) {
                       
            javax.swing.JOptionPane.showMessageDialog(
                    null, "Jacob Erickson\nTCSS 305 - Autumn 2022", 
                    "TCSS 305 Paint", JOptionPane.DEFAULT_OPTION, IMG);
        }
    }
    
    
    
}