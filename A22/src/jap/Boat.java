package jap;

import javax.swing.*;
import java.awt.*;

public class Boat extends JButton {
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
     * @param boatSize
     * @param orientation
     */
    public Boat(int boatSize, boolean orientation){
        boatLength = boatSize;
        boatOrientation = orientation;
        isPlaced = false;
        checkedForDesignPlacement = false;

    }

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

    protected Boolean getCheckedForDesign(){
        return checkedForDesignPlacement;
    }
    protected void setCheckedForDesign(Boolean checked){
        this.checkedForDesignPlacement = checked;
    }
    protected Boolean getPlaced(){
        return isPlaced;
    }

    protected void setPlaced(Boolean placed){
        this.isPlaced = placed;
    }

    protected Boolean getBoatOrientation(){
        return boatOrientation;
    }

    protected void setBoatOrientation(Boolean orientation){

            this.boatOrientation = orientation;
        }
    protected JButton[][] getBoatPosition(){
        return position;
    }
    protected void setBoatPosition(JButton[][] position){
        this.position = position;
    }

    //TODO make boat visible or not (opponent side should not show)

    //TODO add position JButton[][]

}