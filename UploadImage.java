import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class UploadImage extends JFrame {
    private String username;
    private String usertype;
    private JPanel closePanel;
    private JButton browseButton;
    private JLabel statusLabel;

    public UploadImage(String username,String usertype) {
        this.username=username;
        this.usertype=usertype;
        setTitle("Image Uploader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        browseButton = new JButton("Browse for Image");
        statusLabel = new JLabel("Status: ");

        panel.add(browseButton);
        panel.add(statusLabel);

        add(panel, BorderLayout.CENTER);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        saveImage(selectedFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        statusLabel.setText("Status: Error saving the image.");
                    }
                }
            }
        });

        setVisible(true);
    }

    private void saveImage(File selectedFile) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(selectedFile);
        final BufferedImage image1 = ImageIO.read(fileInputStream);
        fileInputStream.close(); // ImageIO.read does not close the input stream

        final BufferedImage convertedImage = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(image1, 0, 0, Color.WHITE, null);

        final FileOutputStream fileOutputStream = new FileOutputStream(usertype+username+".jpg");
        final boolean canWrite = ImageIO.write(convertedImage, "jpg", fileOutputStream);
        fileOutputStream.close(); // ImageIO.write does not close the output stream

        if (!canWrite) {
            throw new IllegalStateException("Failed to write image.");
        }

        statusLabel.setText("Status: Image saved " );
        JButton closeButton = new JButton("Close");
        closePanel = new JPanel();
        closePanel.add(closeButton);
        add(closePanel, BorderLayout.SOUTH);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the frame
                dispose();
                new UserRegistrationForm();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserRegistrationForm();

        });
    }
}
