package jap;

import javax.swing.*;

public class GameAction {
    protected <T> void historyLog(T eventSource, JLabel controlPanelText) {
        //cast selected item to string
        String currentGameLog = controlPanelText.getText();

        if (eventSource instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) eventSource;
            String selectedLanguage = (String) comboBox.getSelectedItem();
            //get text currently in game log to later append it

            //output the old gamelog text + the new text
            controlPanelText.setText(currentGameLog + "Language set to " + selectedLanguage + "<br>");
        } else if (eventSource instanceof JButton) {
            JButton button = (JButton) eventSource;
            controlPanelText.setText(currentGameLog + button.getText() + " clicked " + "<br>");
        }
    }

    protected void designBoard() {
        System.out.println("It clicks");
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
