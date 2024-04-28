import javax.swing.*;
import java.awt.*;

public class StudyMaterialViewer {
    private JFrame frame;

    public StudyMaterialViewer(String content) {
        frame = new JFrame("Study Materials");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(512, 512);

        JTextArea textArea = new JTextArea(content);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
