package app;

import gui.Frame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Frame frame = new Frame(500, 500, 50, 50);
            frame.setVisible(true);
        });
    }
}
