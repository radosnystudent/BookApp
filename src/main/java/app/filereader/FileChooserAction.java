package app.filereader;

import lombok.Getter;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FileChooserAction implements ActionListener {

    private List<String> isbnList = new ArrayList<>();

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        IsbnFileParser parser = new IsbnFileParser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                isbnList = parser.parse(selectedFile);
                JOptionPane.showMessageDialog(null, "Parsed ISBNs:\n" + String.join(", ", isbnList), "Result", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
