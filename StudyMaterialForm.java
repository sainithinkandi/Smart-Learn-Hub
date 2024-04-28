import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudyMaterialForm {
    private JFrame frame;
    private JComboBox<String> branchComboBox;
    private JComboBox<String> subjectComboBox;
    private JTextField chapterField;
    private JTextArea contentArea;
    private StudyMaterial studyMaterial;

    public StudyMaterialForm(StudyMaterial studyMaterial) {
        this.studyMaterial = studyMaterial;

        frame = new JFrame("Study Material");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setLayout(new BorderLayout());

        JPanel panel = createStudyMaterialPanel();

        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createStudyMaterialPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel branchLabel = new JLabel("Select Branch:");
        branchComboBox = new JComboBox<>(studyMaterial.getBranches());
        branchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSubjectComboBox();
            }
        });

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectComboBox = new JComboBox<>(studyMaterial.getSubjects(branchComboBox.getSelectedItem().toString()));

        JLabel chapterLabel = new JLabel("Chapter:");
        chapterField = new JTextField();

        JLabel contentLabel = new JLabel("Content:");
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudyMaterial();
            }
        });

        panel.add(branchLabel);
        panel.add(branchComboBox);
        panel.add(subjectLabel);
        panel.add(subjectComboBox);
        panel.add(chapterLabel);
        panel.add(chapterField);
        panel.add(contentLabel);
        panel.add(new JScrollPane(contentArea));
        panel.add(saveButton);

        return panel;
    }

    private void updateSubjectComboBox() {
        String selectedBranch = branchComboBox.getSelectedItem().toString();
        subjectComboBox.removeAllItems();
        String[] subjects = studyMaterial.getSubjects(selectedBranch);
        for (String subject : subjects) {
            subjectComboBox.addItem(subject);
        }
    }

    private void saveStudyMaterial() {
        String branch = branchComboBox.getSelectedItem().toString();
        String subject = subjectComboBox.getSelectedItem().toString();
        String chapter = chapterField.getText();
        String content = contentArea.getText();

        if (!chapter.isEmpty() && !content.isEmpty()) {
            studyMaterial.addStudyMaterial(branch, subject, chapter, content);
            clearFields();
            JOptionPane.showMessageDialog(frame, "Study material saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Chapter and Content cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        chapterField.setText("");
        contentArea.setText("");
    }
}
