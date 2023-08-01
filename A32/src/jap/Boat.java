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
     * @param boatSize - Size of Boat
     * @param orientation - Orientation boat is facing
     */
    public Boat(int boatSize, boolean orientation) {
        boatLength = boatSize;
        boatOrientation = orientation;
        isPlaced = false;
        checkedForDesignPlacement = false;

    }

    /**
     * Method Name: setBackground
     * Purpose: set color for JButton
     * Algorithm: super class setBackground
     *
     * @param color the desired background <code>Color</code>
     */
    public void setBackground(Color color) {
        super.setBackground(color);
    }

    /**
     * Method Name: setText
     * Purpose: setText for boat
     * Algorithm: super class setText
     *
     * @param size - Size of boat
     */
    public void setText(Integer size) {
        super.setText(String.valueOf(size));
    }

    /**
     * Method Name: getBoatLength
     * Purpose: return Length
     * Algorithm: return variable
     *
     * @return - boatLength
     */
    protected Integer getBoatLength() {
        return boatLength;
    }

    /**
     * Method Name: setBoatLength
     * Purpose: set global variable with passed argument
     * Algorithm: this variable =
     *
     * @param size - boat size
     */
    protected void setBoatLength(Integer size) {
        this.boatLength = size;
    }

    /**
     * Method Name: getPlaced
     * Purpose: get Boolean if boat is placed
     * Algorithm: return
     *
     * @return - boolean if it is placed or not
     */
    protected Boolean getPlaced() {
        return isPlaced;
    }

    /**
     * Method Name: setPlaced
     * Purpose: This global variable with passed argument
     * Algorithm: this variable = passed argument
     *
     * @param placed - boolean true or false
     */
    protected void setPlaced(Boolean placed) {
        this.isPlaced = placed;
    }

    /**
     * Method Name: getBoatOrientation
     * Purpose: Getter method for boat orientation
     * Algorithm: return variable
     *
     * @return - boatOrientation
     */
    protected Boolean getBoatOrientation() {
        return boatOrientation;
    }

    /**
     * Method Name: setBoatOrientation
     * Purpose: Setter method
     * Algorithm: set global variable to passed argument
     *
     * @param orientation - true or false boolean for Vertical or Horizontal
     */
    protected void setBoatOrientation(Boolean orientation) {

        this.boatOrientation = orientation;
    }

    /**
     * Method Name: setBoatPosition
     * Purpose: Save the position of JButton
     * Algorithm: This variable = passed argument
     *
     * @param position - JButton Position
     */
    protected void setBoatPosition(JButton[][] position) {
        this.position = position;
    }

    /**
     * Method Name: getVisibility
     * Purpose: Getter method to determine visibility
     * Algorithm: return variable
     *
     * @return boolean if visible or not
     */
    protected Boolean getVisibility(){
        return isVisible;
    }

    /**
     * Method Name: setVisibility
     * Purpose: Setter method
     * Algorithm: set global variable to passed argument
     *
     * @param visible - Boolean if visible or not
     */
    protected void setVisibility(Boolean visible){
        this.isVisible = visible;
    }
}
