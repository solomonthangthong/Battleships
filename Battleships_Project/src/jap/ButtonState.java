package jap;

import javax.swing.*;

/**
 * Class Name: Button State
 * Method List:
 * Constants List:
 * ButtonState checks and handles if JButton/Boat has been hit or miss
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1.0
 * @see JButton
 * @since 11.0.19
 */
public class ButtonState {
    private JButton button;
    private Boat boat;
    private State state;

    /**
     * Method Name: ButtonState
     * Purpose: Constructor for JButton Object
     * Algorithm: Initialize variables
     *
     * @param button - Passed JButton from User/Machine grid
     */
    public ButtonState(JButton button) {
        this.button = button;
        this.boat = null;

        this.state = State.DEFAULT;
    }

    /**
     * Method Name: ButtonState
     * Purpose: Constructor for Boat Object
     * Algorithm: Initialize variables
     *
     * @param boat - Passed Boat Object from User/Machine grid
     */
    public ButtonState(Boat boat) {
        this.button = null;
        this.boat = boat;

        this.state = State.DEFAULT;
    }

    /**
     * Method Name: setState
     * Purpose: Set the state of the JButton/Boat
     * Algorithm: Set global variable to passed state and call method to handle
     *
     * @param state - Hit, Miss, Default
     */
    protected void setState(State state) {
        this.state = state;
        handleButtonState();
    }

    /**
     * Method Name: getState
     * Purpose: Getter method for state
     * Algorithm: return variable
     *
     * @return - JButton state
     */
    protected State getState() {
        return state;
    }

    /**
     * Method Name: handleButtonState
     * Purpose: Based on user action update the state of JButton
     * Algorithm: Switch case and handle according to current JButton state
     */
    private void handleButtonState() {
        switch (state) {
            // Set state to 0
            case DEFAULT:
                button.setText("0");
                break;
            case HIT:
                boat.setText("HIT");
                break;
            case MISS:
                button.setText("MISS");
                break;
            default:
                break;
        }
    }
}
