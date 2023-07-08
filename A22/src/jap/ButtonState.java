package jap;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

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
     * Method Name:
     * Purpose: Constructor for JButton Object
     * Algorithm:
     *
     * @param button
     */
    public ButtonState(JButton button) {
        this.button = button;
        this.boat = null;

        this.state = State.DEFAULT;
    }

    /**
     * Method Name:
     * Purpose: Constructor for Boat Object
     * Algorithm:
     *
     * @param boat
     */
    public ButtonState(Boat boat) {
        this.button = null;
        this.boat = boat;

        this.state = State.DEFAULT;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param state
     */
    protected void setState(State state) {
        this.state = state;
        handleButtonState();
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @return
     */
    protected State getState() {
        return state;
    }

    //TODO figure out how to do this or remove implementation
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
        if (boat != null) {
            boat.setText(state == State.HIT ? "HIT" : "MISS");
        }
    }
}
