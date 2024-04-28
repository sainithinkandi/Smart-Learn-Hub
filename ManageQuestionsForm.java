import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageQuestionsForm extends JFrame {
    private JTextArea questionTextArea;
    private JTextArea answerTextArea;
    private JButton previousButton;
    private JButton nextButton;
    private JButton saveAnswerButton;
    private JComboBox<String> studentComboBox;
    private JComboBox<String> branchComboBox;
    private JComboBox<String> subjectComboBox;
    private List<String> students;
    private List<String> questions;
    private int currentStudentIndex;
    private int currentQuestionIndex;
    private Set<String> answeredQuestions;

    public ManageQuestionsForm(String teacherUsername) {
        students = loadStudentsFromUserData("user_data.txt");
        currentStudentIndex = 0;
        currentQuestionIndex = 0;
        answeredQuestions = new HashSet<>();

        setTitle("Manage Questions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel panel = createQuestionPanel();

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createQuestionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel studentSelectionPanel = new JPanel();
        studentSelectionPanel.setLayout(new FlowLayout());

        studentComboBox = new JComboBox<>(students.toArray(new String[0]));
        studentComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStudentIndex = studentComboBox.getSelectedIndex();
                currentQuestionIndex = 0;
                loadQuestionsForSelectedStudent();
            }
        });

        studentSelectionPanel.add(new JLabel("Select Student:"));
        studentSelectionPanel.add(studentComboBox);

        JPanel branchAndSubjectSelectionPanel = new JPanel();
        branchAndSubjectSelectionPanel.setLayout(new FlowLayout());

        JLabel branchLabel = new JLabel("Select Branch:");
        String[] branches = {"CSE", "ECE", "MECH", "EEE"};
        branchComboBox = new JComboBox<>(branches);

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectComboBox = new JComboBox<>();

        branchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String[] subjects = getSubjectsForBranch(selectedBranch);
                subjectComboBox.setModel(new DefaultComboBoxModel<>(subjects));
                loadQuestionsForSelectedStudent();
            }
        });

        subjectComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadQuestionsForSelectedStudent();
            }
        });

        branchAndSubjectSelectionPanel.add(branchLabel);
        branchAndSubjectSelectionPanel.add(branchComboBox);
        branchAndSubjectSelectionPanel.add(subjectLabel);
        branchAndSubjectSelectionPanel.add(subjectComboBox);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(1, 2));

        questionTextArea = new JTextArea();
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setLineWrap(true);
        questionTextArea.setEditable(false);
        JScrollPane questionScrollPane = new JScrollPane(questionTextArea);

        answerTextArea = new JTextArea();
        answerTextArea.setWrapStyleWord(true);
        answerTextArea.setLineWrap(true);
        JScrollPane answerScrollPane = new JScrollPane(answerTextArea);

        questionPanel.add(questionScrollPane);
        questionPanel.add(answerScrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        saveAnswerButton = new JButton("Save Answer");

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousQuestion();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextQuestion();
            }
        });

        saveAnswerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAnswer();
            }
        });

        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(saveAnswerButton);

        panel.add(studentSelectionPanel, BorderLayout.NORTH);
        panel.add(branchAndSubjectSelectionPanel, BorderLayout.CENTER);
        panel.add(questionPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    protected String[] getSubjectsForBranch(String selectedBranch) {
        if ("CSE".equals(selectedBranch)) {
            return new String[]{"ComputerProgramming", "DataStructures", "AlgorithmDesign"};
        } else if ("ECE".equals(selectedBranch)) {
            return new String[]{"Electronic Circuits", "Digital Signal Processing", "Wireless Communication"};
        } else if ("MECH".equals(selectedBranch)) {
            return new String[]{"Thermodynamics", "Mechanics of Materials", "Fluid Mechanics"};
        } else if ("EEE".equals(selectedBranch)) {
            return new String[]{"Electric Circuits", "Power Systems", "Renewable Energy"};
        } else {
            return new String[0];
        }
    }

    private void showCurrentQuestion() {
        if (!questions.isEmpty()) {
            if (currentQuestionIndex < 0) {
                currentQuestionIndex = 0;
            } else if (currentQuestionIndex >= questions.size()) {
                currentQuestionIndex = questions.size() - 1;
            }

            // Check if the question has been answered
            String currentQuestion = questions.get(currentQuestionIndex);
            if (!answeredQuestions.contains(currentQuestion)) {
                questionTextArea.setText(currentQuestion);
                answerTextArea.setText("");
            } else {
                // Move to the next question
                showNextQuestion();
            }
        } else {
            questionTextArea.setText("No questions available for this student.");
            answerTextArea.setText("");
        }
    }

    private void showNextQuestion() {
        currentQuestionIndex++;
        showCurrentQuestion();
    }

    private void showPreviousQuestion() {
        currentQuestionIndex--;
        showCurrentQuestion();
    }

    private void saveAnswer() {
        if (currentStudentIndex < students.size() && currentQuestionIndex < questions.size()) {
            String answer = answerTextArea.getText();
            if (!answer.isEmpty()) {
                String studentName = students.get(currentStudentIndex);
                String branch = (String) branchComboBox.getSelectedItem();
                String subject = (String) subjectComboBox.getSelectedItem();
                String question = questions.get(currentQuestionIndex);

                // Check if the question has already been answered
                if (!answeredQuestions.contains(question)) {
                    String answerFileName = studentName + "_answers.txt";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(answerFileName, true))) {
                        writer.write("Question: " + question + "\n");
                        writer.write("Answer: " + answer + "\n\n");
                        answeredQuestions.add(question); // Add the question to the set of answered questions
                        JOptionPane.showMessageDialog(this, "Answer saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Failed to save answer. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "This question has already been answered.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private List<String> loadStudentsFromUserData(String userDataFilePath) {
        List<String> studentList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userDataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && "student".equalsIgnoreCase(parts[2])) {
                    studentList.add(parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    private void loadQuestionsForSelectedStudent() {
        String studentName = students.get(currentStudentIndex);
        String questionsFilePath = studentName + "_questions.txt";
        questions = loadStudentQuestions(questionsFilePath);
        currentQuestionIndex = 0;
        showCurrentQuestion();
    }

    private List<String> loadStudentQuestions(String questionsFilePath) {
        List<String> studentQuestions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(questionsFilePath))) {
            StringBuilder questionBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                questionBuilder.append(line).append("\n");
                if (line.trim().endsWith("?") || line.trim().endsWith(".")) {
                    studentQuestions.add(questionBuilder.toString());
                    questionBuilder.setLength(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentQuestions;
    }
}
