package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Button extends JButton {

    public Button(String text, ActionListener event) {
        super(text);

        addActionListener(event);
    }
}
