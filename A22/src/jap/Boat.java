package jap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * Class Name: Boat
 * Method List:
 * Constants List:
 * Boat Object that extends JButton
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1.0
 * @see JButton
 * @see Serializable
 * @since 11.0.19
 */
public class Boat extends JButton {
    private static final long serialVersionUID = 1L;

    // Array to hold # of tiles in the boat
    private int boatLength;
    // true vertical, false horizontal
    private Boolean boatOrientation;
    private boolean checkedForDesignPlacement;

    private boolean isPlaced;
    private JButton[][] position;
    private State state;
    // True for show, false for hidden
    private boolean isVisible;

    private Color color;

    /**
     * Constructor for Boat Object
     *
     * @param boatSize
     * @param orientation
     */
    public Boat(int boatSize, boolean orientation) {
        boatLength = boatSize;
        boatOrientation = orientation;
        isPlaced = false;
        checkedForDesignPlacement = false;

    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param color the desired background <code>Color</code>
     */
    public void setBackground(Color color) {
        super.setBackground(color);
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param size
     */
    public void setText(Integer size) {
        super.setText(String.valueOf(size));
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @return
     */
    protected Integer getBoatLength() {
        return boatLength;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param size
     */
    protected void setBoatLength(Integer size) {
        this.boatLength = size;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @return
     */
    protected Boolean getPlaced() {
        return isPlaced;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param placed
     */
    protected void setPlaced(Boolean placed) {
        this.isPlaced = placed;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @return
     */
    protected Boolean getBoatOrientation() {
        return boatOrientation;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param orientation
     */
    protected void setBoatOrientation(Boolean orientation) {

        this.boatOrientation = orientation;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param position
     */
    protected void setBoatPosition(JButton[][] position) {
        this.position = position;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @return
     */
    protected Boolean getVisibility(){
        return isVisible;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param visible
     */
    protected void setVisibility(Boolean visible){
        this.isVisible = visible;
    }
}
