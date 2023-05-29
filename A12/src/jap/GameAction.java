package jap;

import javax.swing.*;
import java.awt.*;

public class GameAction {
    /**
     * Generic method to print control panel history log in GUI.
     * @param eventSource - Generic parameter, could be JButton, JComboBox.
     * @param controlPanelText - JLabel use to extract .getName().
     * @param <T> - Type of elements passed from GameFrame.
     */
    protected <T> void historyLog(T eventSource, JLabel controlPanelText) {
        //cast selected item to string
        String currentGameLog = controlPanelText.getText();
        String selectedLanguage;
        Integer selectedDimension;
        //more buttons to be added defined here

        if (eventSource instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) eventSource;
            Object selectedItem = comboBox.getSelectedItem();
            // Check whether we pass String or Int JComboBox
            if (selectedItem instanceof String) {
                selectedLanguage = (String) comboBox.getSelectedItem();
                //output the old game log text + the new text
                controlPanelText.setText(currentGameLog + "Language set to " + selectedLanguage + "<br>");
            } else if (selectedItem instanceof Integer) {
                selectedDimension = (Integer) comboBox.getSelectedItem();
                //output the old game log text + the new text
                controlPanelText.setText(currentGameLog + "Dimensions set to " + selectedDimension + "<br>");
            }
            //when design is clicked,print btn
        } else if (eventSource instanceof JButton) {
            JButton button = (JButton) eventSource;
            controlPanelText.setText(currentGameLog + button.getName() + " clicked " + "<br>");
        }
    }

    /**
     * Pop-up window to design ship placement for user actor.
     */
    protected void designBoatPlacement() {
        /* New JFrame for pop-up window to design board */
        JFrame designFrame = new JFrame();
        designFrame.setLocationRelativeTo(null);
        designFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        designFrame.setVisible(true);
    }

    /**
     * Rnadomize ship placement for machine, and if user desires to randomize.
     */
    protected void randBoatPlacement() {

    }
}
