import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizPanel extends JPanel {
    private List<String> questions;
    private List<String> answers;
    private List<String> studentAnswers; // Maintain student's answers
    private int currentQuestionIndex = 0;
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private QuizPanelListener quizListener;
    private String username;
    private String quizName;
    private int score = 0; // Initialize score

    public QuizPanel(String username, String quizName, List<String> questions, List<String> answers) {
        this.username = username;
        this.quizName = quizName;
        this.questions = questions;
        this.answers = answers;
        this.studentAnswers = new ArrayList<>(); // Initialize student's answers
        initialize();
    }

    public void setQuizListener(QuizPanelListener listener) {
        this.quizListener = listener;
    }

    private void initialize() {
        setLayout(new BorderLayout());

        questionLabel = new JLabel();
        add(questionLabel, BorderLayout.NORTH);

        answerField = new JTextField(2);
        add(answerField, BorderLayout.CENTER);

        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAnswer();
            }
        });
        add(submitButton, BorderLayout.SOUTH);

        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            questionLabel.setText(questions.get(currentQuestionIndex));
        } else {
            questionLabel.setText("Quiz Completed");
            answerField.setEnabled(false);
            submitButton.setEnabled(false);

            int numberOfParticipants = 1; // Initialize with the current student

            String resultFileName = quizName + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName))) {
                writer.write("Number of Participants: " + numberOfParticipants + "\n");
                writer.write(username + ": " + score);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Display the score in a dialog box
            JOptionPane.showMessageDialog(this, "Quiz Completed\nScore: " + score, "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
            new StudentDashboardForm(username,"Student");
        }
    }

    private void submitAnswer() {
        if (quizListener != null) {
            String answer = answerField.getText();
            studentAnswers.add(answer); // Store the student's answer
            if (answer.equals(answers.get(currentQuestionIndex))) {
                // Check if the submitted answer is correct
                score++; // Increase the score by 1 point for a correct answer
            }
            answerField.setText("");
            currentQuestionIndex++;

            displayQuestion();
        }
    }

    public String getStudentAnswer(int questionIndex) {
        if (questionIndex < studentAnswers.size()) {
            return studentAnswers.get(questionIndex);
        }
        return "";
    }
}
