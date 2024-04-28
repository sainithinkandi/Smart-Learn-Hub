import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDashboardForm extends JFrame {
    private int numberOfParticipants = 0;
    private JLabel profilePhotoLabel;
    private String username;
    private JComboBox<String> branchComboBox;
    private JComboBox<String> subjectComboBox;

    public StudentDashboardForm(String username, String usertype) {
        this.username = username;

        setTitle("Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(512, 512);
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Welcome, " + username));
        profilePhotoLabel = new JLabel();
        panel.add(new JLabel("Profile Photo:"));
        panel.add(profilePhotoLabel);

        JLabel branchLabel = new JLabel("Select Branch:");
        String[] branches = {"CSE", "ECE", "MECH", "EEE"};
        branchComboBox = new JComboBox<>(branches);
        panel.add(branchLabel);
        panel.add(branchComboBox);

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectComboBox = new JComboBox<>();
        subjectComboBox.setEnabled(false);
        panel.add(subjectLabel);
        panel.add(subjectComboBox);

        JButton viewMaterialsButton = new JButton("View Study Materials");
        viewMaterialsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String selectedSubject = (String) subjectComboBox.getSelectedItem();
                String content = getStudyMaterialContent(selectedBranch, selectedSubject);
                openStudyMaterialViewer(selectedBranch, selectedSubject, content);
            }
        });
        panel.add(new JPanel());
        panel.add(viewMaterialsButton);

        JButton askQuestionButton = new JButton("Ask a Question");
        askQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openQuestionForm();
            }
        });
        panel.add(new JPanel());
        panel.add(askQuestionButton);

        JButton viewQuestionsButton = new JButton("View Questions");
        viewQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openQuestionViewer(username);
            }
        });
        panel.add(new JPanel());
        panel.add(viewQuestionsButton);

        JButton viewAnswersButton = new JButton("View Answers");
        viewAnswersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAnswerViewer(username);
            }
        });
        panel.add(new JPanel());
        panel.add(viewAnswersButton);

        JButton takeQuizButton = new JButton("Take Quiz");
        takeQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String selectedSubject = (String) subjectComboBox.getSelectedItem();
                openQuizForBranchAndSubject(selectedBranch, selectedSubject);
            }
        });
        panel.add(new JPanel());
        panel.add(takeQuizButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                openLoginDialog();
            }
        });
        panel.add(new JPanel());
        panel.add(logoutButton);

        add(panel, BorderLayout.CENTER);

        branchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectComboBox.setEnabled(true);
                subjectComboBox.setModel(new DefaultComboBoxModel<>(getSubjectsForBranch((String) branchComboBox.getSelectedItem())));
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                String sourceFilePath = StudentDashboardForm.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                new File(sourceFilePath).getParentFile();

                loadAndSetProfilePhoto(usertype + username + ".jpg");
            }
        });

        setVisible(true);
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

    private String[] getSubjectsForBranch(String branch) {
        if ("CSE".equals(branch)) {
            return new String[]{"ComputerProgramming", "DataStructures", "AlgorithmDesign"};
        } else if ("ECE".equals(branch)) {
            return new String[]{"ElectronicCircuits", "Digital Signal Processing", "Wireless Communication"};
        } else if ("MECH".equals(branch)) {
            return new String[]{"Thermodynamics", "Mechanics of Materials", "Fluid Mechanics"};
        } else if ("EEE".equals(branch)) {
            return new String[]{"Electric Circuits", "Power Systems", "Renewable Energy"};
        } else {
            return new String[0];
        }
    }

    private String getStudyMaterialContent(String branch, String subject) {
        String fileName = branch + subject + "material.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Content not found";
        }
    }

    private void openStudyMaterialViewer(String branch, String subject, String content) {
        new StudyMaterialViewer("Branch: " + branch + "\nSubject: " + subject + "\n\n" + content);
    }

    private void openQuestionForm() {
        JFrame questionFrame = new JFrame("Ask a Question");
        questionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        questionFrame.setSize(400, 250);
        questionFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel questionLabel = new JLabel("Your Question:");
        JTextArea questionArea = new JTextArea();
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(questionArea);

        JButton submitButton = new JButton("Submit Question");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String questionText = questionArea.getText();
                if (!questionText.isEmpty()) {
                    String selectedBranch = (String) branchComboBox.getSelectedItem();
                    String selectedSubject = (String) subjectComboBox.getSelectedItem();
                    saveQuestion(username, selectedBranch, selectedSubject, questionText);
                    JOptionPane.showMessageDialog(questionFrame, "Question submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    questionFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(questionFrame, "Please enter a question.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(questionLabel);
        panel.add(scrollPane);
        panel.add(new JPanel());
        panel.add(new JPanel());
        panel.add(submitButton);

        questionFrame.add(panel, BorderLayout.CENTER);
        questionFrame.setVisible(true);
    }

    private void saveQuestion(String studentName, String branch, String subject, String question) {
        String fileName = studentName+"_questions.txt";
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(question + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openQuestionViewer(String username) {
        String fileName = username + "_questions.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder questionsText = new StringBuilder("Your Questions:\n");
            String line;
            while ((line = reader.readLine()) != null) {
                questionsText.append(line).append("\n\n");
            }
            new QuestionViewer("Questions by " + username, questionsText.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No questions available.", "Questions", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openAnswerViewer(String studentName) {
        String fileName = username + "_answers.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder answersText = new StringBuilder("Answers for " + username + ":\n");
            String line;
            while ((line = reader.readLine()) != null) {
                answersText.append(line).append("\n");
            }
            new AnswerViewer("Answers for " + username, answersText.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No answers available.", "Answers", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openQuizForBranchAndSubject(String branch, String subject) {
        String questionsFileName = branch + subject + "questions.txt";
        String answersFileName = branch + subject + "answers.txt";

        List<String> questions = loadQuestions(questionsFileName);
        List<String> answers = loadAnswers(answersFileName);

        if (questions.isEmpty() || questions.size() != answers.size()) {
            JOptionPane.showMessageDialog(this, "No questions available for this quiz.", "Quiz Error", JOptionPane.INFORMATION_MESSAGE);
        } else {
            QuizPanel quizPanel = new QuizPanel(username, branch + subject, questions, answers);
            quizPanel.setQuizListener(new QuizPanelListener() {
                @Override
                public void onAnswerSubmitted(int questionIndex, String answer) {
                    String correctAnswer = answers.get(questionIndex);
                    if (correctAnswer.equalsIgnoreCase(answer)) {
                        JOptionPane.showMessageDialog(quizPanel, "Correct answer!", "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(quizPanel, "Incorrect answer. The correct answer is: " + correctAnswer, "Quiz Result", JOptionPane.ERROR_MESSAGE);
                    }
                    incrementParticipants(); // Increment the number of participants
                }

                @Override
                public int getScore() {
                    int score = 0; // Initialize the score to 0
                    for (int i = 0; i < questions.size(); i++) {
                        if (i < answers.size()) {
                            String correctAnswer = answers.get(i);
                            String studentAnswer = quizPanel.getStudentAnswer(i);
                            if (correctAnswer.equalsIgnoreCase(studentAnswer)) {
                                score++; // Increment the score for correct answers
                            }
                        }
                    }
                    return score;
                }

                @Override
                public int getNumberOfParticipants() {
                    return numberOfParticipants; // Return the number of participants
                }
            });
            setGlassPane(quizPanel);
            quizPanel.setVisible(true);
        }
    }

    private void incrementParticipants() {
        numberOfParticipants++;
    }

    private List<String> loadQuestions(String fileName) {
        List<String> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                questions.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }

    private List<String> loadAnswers(String fileName) {
        List<String> answers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                answers.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answers;
    }

    private void openLoginDialog() {
        new UserRegistrationForm();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentDashboardForm("m", "Student");
            }
        });
    }
}