import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
public class UserRegistrationForm {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JPanel registrationPanel;

    public UserRegistrationForm() {
        frame = new JFrame("Smart LearnHub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        registrationPanel = createRegistrationPanel();

        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(registrationPanel, BorderLayout.CENTER);


        frame.setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel headerLabel = new JLabel("Smart LearnHub");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel quotationLabel = new JLabel("Enriching Minds, Empowering Futures");
        quotationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(headerLabel);
        headerPanel.add(quotationLabel);

        return headerPanel;
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        usernameField.setHorizontalAlignment(JTextField.CENTER);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setHorizontalAlignment(JTextField.CENTER);

        JLabel userTypeLabel = new JLabel("User Type:");
        String[] userTypes = {"Student", "Teacher"};
        userTypeComboBox = new JComboBox<>(userTypes);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(userTypeLabel);
        panel.add(userTypeComboBox);
        panel.add(registerButton);
        panel.add(loginButton);

        return panel;
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeComboBox.getSelectedItem();
    
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Both username and password are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (isUserExists(username, userType)) {
            JOptionPane.showMessageDialog(frame, "User with the same username and user type already exists. Please choose a different username or user type.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try (FileWriter writer = new FileWriter("user_data.txt", true)) {
            String userEntry = username + "," + password + "," + userType + "\n";
            writer.write(userEntry);
            JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new UploadImage(username, userType);
    
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        usernameField.setText("");
        passwordField.setText("");
    }
    

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeComboBox.getSelectedItem();

        if (isUserValid(username, password, userType)) {
            frame.setTitle(userType + " Dashboard");
            if ("Student".equals(userType)) {
                openStudentDashboard(username,userType);
            } else if ("Teacher".equals(userType)) {
                openTeacherDashboard(username,userType);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Login failed! incorrect Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openStudentDashboard(String username,String usertype) {
        frame.dispose();
        new StudentDashboardForm(username,usertype);
    }

    private void openTeacherDashboard(String username,String usertype) {
        frame.dispose();
        new TeacherDashboardForm(username,usertype);
    }

    private boolean isUserExists(String username, String userType) {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String storedUsername = parts[0];
                String storedUserType = parts[2];
                if (username.equals(storedUsername) && userType.equals(storedUserType)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    private boolean isUserValid(String username, String password, String userType) {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String storedUsername = parts[0];
                String storedPassword = parts[1];
                String storedUserType = parts[2];
                if (username.equals(storedUsername) && password.equals(storedPassword) && userType.equals(storedUserType)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserRegistrationForm();
            }
        });
    }
}
