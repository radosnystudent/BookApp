package gui;

import api.IsbnApiCaller;
import app.filereader.FileChooserAction;
import model.BookInfo;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Frame extends JFrame {

    public Frame(int width, int height, int x, int y) {
        super();

        IsbnApiCaller api = new IsbnApiCaller();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocation(x, y);

        JTextArea responseArea = new JTextArea();
        responseArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(responseArea);

        JTextField isbnField = new JTextField("9788326841705");
        JLabel isbnLabel = new JLabel("ISBN:");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(isbnLabel, BorderLayout.WEST);
        inputPanel.add(isbnField, BorderLayout.CENTER);

        Button chooseFileButton = new Button("Choose .txt file", new FileChooserAction());

//        Button callApiButton = getApiButton(isbnField, api, responseArea);

        inputPanel.add(chooseFileButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }



//    private static Button getApiButton(JTextField isbnField, IsbnApiCaller api, JTextArea responseArea) {
//        return new Button("Call API", _ -> {
//            String isbn = isbnField.getText();
//            try {
//                BookInfo response = api.callIsbnApi(isbn);
//                if (Objects.nonNull(response)) {
//                    responseArea.setText(String.format("%s - %s", response.getTitle(), String.join(", ", response.getAuthors())));
//                }
//            } catch (Exception ex) {
//                responseArea.setText("Error: " + ex.getMessage());
//            }
//        });
//    }
}
