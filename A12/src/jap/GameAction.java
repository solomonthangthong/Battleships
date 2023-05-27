package jap;

import javax.swing.*;

public class GameAction {

    protected void languageButton(JComboBox<String> languageButton){
        String selectedLanguage = (String) languageButton.getSelectedItem();
        if (selectedLanguage.equals("English")) {
            System.out.println("English language set");
        } else if (selectedLanguage.equals("French")) {
            System.out.println("French language set");
        }
    }
    protected void designBoard(){
        System.out.println("It clicks");
        /* New JFrame for pop-up window to design board */
        JFrame designFrame = new JFrame();
        designFrame.setLocationRelativeTo(null);
        designFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        designFrame.setVisible(true);
    }

    protected void randBoard(){
        System.out.print("It clicks");
    }
}
