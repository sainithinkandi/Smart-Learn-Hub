import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TeacherDashboardForm {
    private JFrame frame;
    private String username;
    private JLabel profilePhotoLabel;
    private JComboBox<String> branchComboBox; // Added branch combo box
    private JComboBox<String> subjectComboBox; // Added subject combo box

    public TeacherDashboardForm(String username, String usertype) {
        this.username = username;
        frame = new JFrame("Teacher Dashboard - Smart Learn Hub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setLayout(new BorderLayout());

        JPanel panel = createDashboardPanel();

        frame.add(panel, BorderLayout.CENTER);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loadAndSetProfilePhoto(usertype + username + ".jpg");
            }
        });

        frame.setVisible(true);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        profilePhotoLabel = new JLabel();
        profilePhotoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton uploadMaterialsButton = new JButton("Upload Study Materials");
        JButton manageQuestionsButton = new JButton("Manage Q&A");
        JButton createQuizButton = new JButton("Create Quiz");
        JButton viewQuizResultsButton = new JButton("View Quiz Results");

        branchComboBox = new JComboBox<>();
        subjectComboBox = new JComboBox<>();
        JButton LogOutButton = new JButton("Logout");
        // Load branch and subject data
        String[] branches = {"CSE", "ECE", "MECH", "EEE"};
        branchComboBox.setModel(new DefaultComboBoxModel<>(branches));
        branchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String[] subjects = getSubjectsForBranch(selectedBranch);
                subjectComboBox.setModel(new DefaultComboBoxModel<>(subjects));
            }
        });

        viewQuizResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String selectedSubject = (String) subjectComboBox.getSelectedItem();
                loadParticipantsAndScores(selectedBranch, selectedSubject);
            }
        });
        LogOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new UserRegistrationForm();
            }
        });

        uploadMaterialsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUploadMaterialsForm();
            }
        });

        manageQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openManageQuestionsForm();
            }
        });

        createQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateQuizForm();
            }
        });

        panel.add(welcomeLabel);
        panel.add(profilePhotoLabel);
        panel.add(branchComboBox);
        panel.add(subjectComboBox);
        panel.add(uploadMaterialsButton);
        panel.add(manageQuestionsButton);
        panel.add(createQuizButton);
        panel.add(viewQuizResultsButton);
        panel.add(LogOutButton);
        return panel;
    }

    private void loadAndSetProfilePhoto(String imagePath) {
        int labelWidth = profilePhotoLabel.getWidth();
        int labelHeight = profilePhotoLabel.getHeight();
        if (labelWidth <= 0 || labelHeight <= 0) {
            return;
        }

        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            if (originalImage != null) {
                int newWidth;
                int newHeight;

                if ((double) originalImage.getWidth() / originalImage.getHeight() > (double) labelWidth / labelHeight) {
                    newWidth = labelWidth;
                    newHeight = (int) (labelWidth / (double) originalImage.getWidth() * originalImage.getHeight());
                } else {
                    newHeight = labelHeight;
                    newWidth = (int) (labelHeight / (double) originalImage.getHeight() * originalImage.getWidth());
                }

                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                g.dispose();

                profilePhotoLabel.setIcon(new ImageIcon(resizedImage));
            } else {
                profilePhotoLabel.setText("Profile Photo Not Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            profilePhotoLabel.setText("Error Loading Profile Photo");
        }
    }

    private void openUploadMaterialsForm() {
        JFrame uploadMaterialsFrame = new JFrame("Upload Study Materials");
        uploadMaterialsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        uploadMaterialsFrame.setSize(400, 250);
        uploadMaterialsFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel branchLabel = new JLabel("Select Branch:");
        String[] branches = {"CSE", "ECE", "MECH", "EEE"};
        JComboBox<String> branchComboBox = new JComboBox<>(branches);

        JLabel subjectLabel = new JLabel("Select Subject:");
        JComboBox<String> subjectComboBox = new JComboBox<>();

        branchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String[] subjects = getSubjectsForBranch(selectedBranch);
                subjectComboBox.setModel(new DefaultComboBoxModel<>(subjects));
            }
        });

        JTextArea materialsTextArea = new JTextArea();
        materialsTextArea.setWrapStyleWord(true);
        materialsTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(materialsTextArea);

        JButton uploadButton = new JButton("Upload Materials");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String selectedSubject = (String) subjectComboBox.getSelectedItem();
                String materials = materialsTextArea.getText();

                // Store the study materials in a file named after the branch and subject.
                String fileName = selectedBranch + selectedSubject + "material.txt";
                try (FileWriter writer = new FileWriter(fileName)) {
                    writer.write(materials);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(uploadMaterialsFrame, "Materials uploaded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(branchLabel);
        panel.add(branchComboBox);
        panel.add(subjectLabel);
        panel.add(subjectComboBox);
        panel.add(scrollPane);
        panel.add(uploadButton);

        uploadMaterialsFrame.add(panel, BorderLayout.CENTER);
        uploadMaterialsFrame.setVisible(true);
    }

    private void openManageQuestionsForm() {
        new ManageQuestionsForm(username);
    }

    private void openCreateQuizForm() {
        JFrame createQuizFrame = new JFrame("Create Quiz");
        createQuizFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createQuizFrame.setSize(600, 400);
        createQuizFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 10, 10));

        JLabel branchLabel = new JLabel("Select Branch:");
        String[] branches = {"CSE", "ECE", "MECH", "EEE"};
        JComboBox<String> branchComboBox = new JComboBox<>(branches);

        JLabel subjectLabel = new JLabel("Select Subject:");
        JComboBox<String> subjectComboBox = new JComboBox<>();

        branchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String[] subjects = getSubjectsForBranch(selectedBranch);
                subjectComboBox.setModel(new DefaultComboBoxModel<>(subjects));
            }
        });

        JTextArea quizTextArea = new JTextArea();
        quizTextArea.setBorder(BorderFactory.createTitledBorder("Quiz Questions"));
        quizTextArea.setWrapStyleWord(true);
        quizTextArea.setLineWrap(true);
        JScrollPane quizScrollPane = new JScrollPane(quizTextArea);

        JTextArea answersTextArea = new JTextArea();
        answersTextArea.setBorder(BorderFactory.createTitledBorder("Quiz Answers"));
        answersTextArea.setWrapStyleWord(true);
        answersTextArea.setLineWrap(true);
        JScrollPane answersScrollPane = new JScrollPane(answersTextArea);

        JButton addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quizTextArea.append("\n");
                answersTextArea.append("\n");
            }
        });

        JButton saveQuizButton = new JButton("Save Quiz");
        saveQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String selectedSubject = (String) subjectComboBox.getSelectedItem();
                String quizText = quizTextArea.getText();
                String answersText = answersTextArea.getText();

                // Store the quiz questions in a file named after the branch and subject.
                String quizFileName = selectedBranch + selectedSubject + "questions.txt";
                String answersFileName = selectedBranch + selectedSubject + "answers.txt";

                try (FileWriter writer = new FileWriter(quizFileName)) {
                    writer.write(quizText);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                try (FileWriter writer = new FileWriter(answersFileName)) {
                    writer.write(answersText);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(createQuizFrame, "Quiz questions and answers saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(branchLabel);
        panel.add(branchComboBox);
        panel.add(subjectLabel);
        panel.add(subjectComboBox);
        panel.add(quizScrollPane);
        panel.add(answersScrollPane);
        panel.add(addQuestionButton);
        panel.add(saveQuizButton);

        createQuizFrame.add(panel, BorderLayout.CENTER);
        createQuizFrame.setVisible(true);
    }

    private String[] getSubjectsForBranch(String branch) {
        if ("CSE".equals(branch)) {
            return new String[]{"ComputerProgramming", "DataStructures", "Algorithm Design"};
        } else if ("ECE".equals(branch)) {
            return new String[]{"Electronic Circuits", "Digital Signal Processing", "Wireless Communication"};
        } else if ("MECH".equals(branch)) {
            return new String[]{"Thermodynamics", "Mechanics of Materials", "Fluid Mechanics"};
        } else if ("EEE".equals(branch)) {
            return new String[]{"Electric Circuits", "Power Systems", "Renewable Energy"};
        } else {
            return new String[0];
        }
    }

    private void loadParticipantsAndScores(String branch, String subject) {
        String fileName = branch + subject + ".txt";
        List<String> participants = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;
    
            while ((line = reader.readLine()) != null) {
                if (lineNumber == 0) {
                    // Parse the number of participants from the first line
                } else {
                    // Parse participant and score
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String participantName = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        participants.add(participantName);
                        scores.add(score);
                    }
                }
                lineNumber++;
            }
    
            // Display quiz results in a separate frame
            JFrame resultsFrame = new JFrame("Quiz Results");
            resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            resultsFrame.setSize(400, 300);
    
            JTextArea resultsTextArea = new JTextArea();
            for (int i = 0; i < participants.size(); i++) {
                resultsTextArea.append("Participant: " + participants.get(i) + ", Score: " + scores.get(i) + "\n");
            }
            
            resultsTextArea.setEditable(false);
    
            JScrollPane resultsScrollPane = new JScrollPane(resultsTextArea);
            resultsFrame.add(resultsScrollPane);
    
            resultsFrame.setVisible(true);
    
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TeacherDashboardForm("t", "Teacher");
        });
    }
}
