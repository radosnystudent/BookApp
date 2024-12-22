package gui;

import api.IsbnApiCaller;
import app.filereader.FileChooserAction;
import app.filereader.FileChooserListener;
import model.BookInfo;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Frame extends JFrame implements FileChooserListener {

    private final IsbnApiCaller api;
    private final JTextArea responseArea;

    public Frame(int width, int height, int x, int y) {
        super();

        api = new IsbnApiCaller();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocation(x, y);

        responseArea = new JTextArea();
        responseArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(responseArea);

        final JPanel inputPanel = getInputPanel();

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel getInputPanel() {
        final JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        final FileChooserAction action = new FileChooserAction();
        action.addListener(this);
        final Button chooseFileButton = new Button("Choose .txt file", action);

        inputPanel.add(chooseFileButton, BorderLayout.EAST);
        return inputPanel;
    }

    @Override
    public void onFileParsed(List<String> isbnList) {
        final List<BookInfo> results = api.callIsbnApi(isbnList);
        if (CollectionUtils.isNotEmpty(results)) {
            results.forEach(r -> responseArea.setText(String.format("%s - %s", r.getTitle(), String.join(", ", r.getAuthors()))));
        }
    }
}
