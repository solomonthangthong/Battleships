package jap;

import javax.swing.*;
import java.awt.*;

public class Boat extends JButton {
    // Array to hold # of tiles in the boat
    private JButton[] boatLength;
    // true vertical, false horizontal
    private boolean boatOrientation;
    private JButton[][] position;
    private ButtonState state;
    // True for show, false for hidden
    private boolean isVisible;

    private Color color;

    public Boat(int boatSize){
        boatLength = new JButton[boatSize];
    }
    @Override
    public void setBackground(Color color){
        super.setBackground(color);
    }
    protected JButton[] getBoatLength(){
        return boatLength;
    }

    protected void setBoatLength(int size, JButton button){
        boatLength[size] = button;
    }


}
