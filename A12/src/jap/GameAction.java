package jap;

import javax.swing.*;

public class GameAction {
    protected <T> void historyLog(T eventSource, JLabel controlPanelText) {
        //cast selected item to string
        String currentGameLog = controlPanelText.getText();
        String selectedLanguage;
        Integer selectedDimension;

        if (eventSource instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) eventSource;
            Object selectedItem = comboBox.getSelectedItem();
            // Check whether we pass String or Int JComboBox
            if (selectedItem instanceof String) {
                selectedLanguage = (String) comboBox.getSelectedItem();
                //output the old gamelog text + the new text
                controlPanelText.setText(currentGameLog + "Language set to " + selectedLanguage + "<br>");
            } else if (selectedItem instanceof Integer) {
                selectedDimension = (Integer) comboBox.getSelectedItem();
                //output the old gamelog text + the new text
                controlPanelText.setText(currentGameLog + "Dimensions set to " + selectedDimension + "<br>");
            }
        } else if (eventSource instanceof JButton) {
            JButton button = (JButton) eventSource;
            controlPanelText.setText(currentGameLog + button.getText() + " clicked " + "<br>");
        }
    }

    protected void designBoard() {
        /* New JFrame for pop-up window to design board */
        JFrame designFrame = new JFrame();
        designFrame.setLocationRelativeTo(null);
        designFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        designFrame.setVisible(true);
    }

    protected void randBoard() {
        System.out.print("It clicks");
    }
}
