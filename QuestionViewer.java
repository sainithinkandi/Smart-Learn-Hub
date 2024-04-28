import javax.swing.*;
import java.awt.*;

public class QuestionViewer extends JFrame {
    public QuestionViewer(String title, String content) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(512, 512);

        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
