package jap;

import javax.swing.*;
import java.awt.*;

public class Boat extends JButton {
    // Array to hold # of tiles in the boat
    private int boatLength;
    // true vertical, false horizontal
    private boolean boatOrientation;
    private JButton[][] position;
    private State state;
    // True for show, false for hidden
    private boolean isVisible;

    private Color color;

    public Boat(int boatSize, boolean orientation){
        boatLength = boatSize;
        boatOrientation = orientation;
    }
    @Override
    public void setBackground(Color color){
        super.setBackground(color);
    }

    //Can be removed later, only here to visually see if adhering to numerical representation
    public void setText(Integer size){
        super.setText(String.valueOf(size));
    }
    protected Integer getBoatLength(){
        return boatLength;
    }

    protected State getState(){
        return state;
    }

    protected void setState(State state){
        this.state = state;
    }

}
