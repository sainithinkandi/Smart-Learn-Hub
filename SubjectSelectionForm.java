import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SubjectSelectionForm {
    private JFrame frame;
    private JComboBox<String> subjectComboBox;
    private StudyMaterial studyMaterial;
    private String selectedBranch;

    public SubjectSelectionForm(StudyMaterial studyMaterial, String selectedBranch) {
        this.studyMaterial = studyMaterial;
        this.selectedBranch = selectedBranch;

        frame = new JFrame("Select Subject");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setLayout(new BorderLayout());

        JPanel panel = createSubjectSelectionPanel();

        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createSubjectSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectComboBox = new JComboBox<>(studyMaterial.getSubjects(selectedBranch));

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new StudyMaterialForm(studyMaterial); // Opening the Study Material frame for the selected branch and subject.
            }
        });

        panel.add(subjectLabel);
        panel.add(subjectComboBox);
        panel.add(selectButton);

        return panel;
    }
}
