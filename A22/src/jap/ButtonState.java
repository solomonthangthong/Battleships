package jap;

import javax.swing.*;

public class ButtonState {
    private JButton button;
    private Boat boat;
    private State state;

    /**
     * Constructor for JButton Object
     * @param button
     */
    public ButtonState(JButton button) {
        this.button = button;
        this.boat = null;
    }

    /**
     * Constructorf or Boat Object
     * @param boat
     */
    public ButtonState(Boat boat) {
        this.button = null;
        this.boat = boat;
    }


    protected void setState(State state){
        this.state = state;
        handleButtonState();
    }

    protected State getState(){
        return state;
    }
    //TODO figure out how to do this or remove implementation
    private void handleButtonState(){
        switch (state){
            // Set state to 0
            case DEFAULT:
                button.setText("0");
                break;
            case HIT:
                boat.setState(State.HIT);
                break;
            case MISS:
                break;
            default:
                break;
        }
    }


}
