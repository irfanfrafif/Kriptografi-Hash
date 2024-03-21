import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.ButtonGroup;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AppUI extends JFrame {
    private JTextArea textField1;
    private JTextArea textField2;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel contentPanel;
    private JPanel errorPanel;
    public static JTextArea progressArea;

    public AppUI() {
        getContentPane().setLayout(new GridBagLayout());
        createView();
        setTitle("Cipher App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void errorFieldHandler(String message) {
        // Create error message panel
        errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));

        JLabel errorLabel = new JLabel("Error:");
        errorLabel.setAlignmentX(CENTER_ALIGNMENT);

        JTextField errorField = new JTextField(10);
        errorField.setEditable(false);
        errorField.setHorizontalAlignment(JTextField.CENTER);
        errorField.setForeground(Color.RED);
        errorPanel.add(errorLabel);
        errorPanel.add(errorField);
        contentPanel.add(errorPanel);
        errorField.setText(message);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void createView() {
        // Create BoxLayout for content panel (biggest panel that includes all other
        // panels)
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        getContentPane().add(scrollPane);

        // Create form panel for input panel, button panel, and output panel
        // (there is form panel due to alignment issues with BoxLayout)
        // Flow Layout for horizontal alignment
        // BoxLayout for vertical alignment
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        textField1 = new JTextArea(10, 15);
        textField1.setLineWrap(true);
        textField1.setAlignmentX(LEFT_ALIGNMENT);
        JLabel label = new JLabel("Text to encrypt/decrypt:");
        inputPanel.add(label);
        inputPanel.add(textField1);
        formPanel.add(inputPanel);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");
        encryptButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        decryptButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        buttonPanel.add(encryptButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(decryptButton);
        formPanel.add(buttonPanel);

        // Create output panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        textField2 = new JTextArea(10, 15);
        textField2.setLineWrap(true);
        textField2.setEditable(false);
        textField2.setAlignmentX(LEFT_ALIGNMENT);
        JLabel label2 = new JLabel("Result:");
        outputPanel.add(label2);
        outputPanel.add(textField2);
        formPanel.add(outputPanel);

        contentPanel.add(formPanel);

        // Create key panel
        // This panel is separate from the form panel because it has a different layout
        // FlowLayout to simply make gap between components and with the form panel
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        getContentPane().add(keyPanel);
        JPanel keyFormPanel = new JPanel();
        keyFormPanel.setLayout(new BoxLayout(keyFormPanel, BoxLayout.X_AXIS));
        JLabel keyLabel = new JLabel("Key:");
        keyFormPanel.add(keyLabel);

        JPanel inputKeyPanel = new JPanel(new FlowLayout());
        JTextField keyField = new JTextField(10);
        JRadioButton hexButton = new JRadioButton("Hex");
        JRadioButton asciiButton = new JRadioButton("ASCII");
        asciiButton.setSelected(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(hexButton);
        buttonGroup.add(asciiButton);

        inputKeyPanel.add(keyField);
        inputKeyPanel.add(hexButton);
        inputKeyPanel.add(asciiButton);
        keyFormPanel.add(inputKeyPanel);
        keyPanel.add(keyFormPanel);

        contentPanel.add(keyPanel);

        progressArea = new JTextArea(10, 15);
        progressArea.setLineWrap(true);
        progressArea.append("Logs: \n");

        contentPanel.add(progressArea);

        // Add action listeners to buttons
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(progressArea != null) {
                        progressArea.revalidate();
                        progressArea.repaint();
                        progressArea.setText("Logs: \n");
                    }
                    else {
                        progressArea = new JTextArea(10, 15);
                        progressArea.setLineWrap(true);
                        progressArea.append("Logs: \n");
                        contentPanel.add(progressArea);
                    }

                    String text = textField1.getText();
                    String key = keyField.getText();
                    boolean isHex = hexButton.isSelected();
                    String result = Cipher.cipherText(text, key, isHex);
                    textField2.setText(result);


                    if (errorPanel != null) {
                        contentPanel.remove(errorPanel);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                        errorPanel = null;
                    }
                } catch (Exception ex) {
                    if (errorPanel != null) {
                        contentPanel.remove(errorPanel);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                        errorPanel = null;
                    }
                    errorFieldHandler(ex.getMessage());
                }
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(progressArea != null) {
                        progressArea.revalidate();
                        progressArea.repaint();
                        progressArea.setText("Logs: \n");
                    }
                    else {
                        progressArea = new JTextArea(10, 15);
                        progressArea.setLineWrap(true);
                        progressArea.append("Logs: \n");
                        contentPanel.add(progressArea);
                    }

                    String text = textField1.getText();
                    String key = keyField.getText();
                    boolean isHex = hexButton.isSelected();
                    String result = Cipher.decipherText(text, key, isHex);
                    textField2.setText(result);

                    if (errorPanel != null) {
                        contentPanel.remove(errorPanel);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                        errorPanel = null;
                    }
                } catch (Exception ex) {
                    if (errorPanel != null) {
                        contentPanel.remove(errorPanel);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                        errorPanel = null;
                    }
                    errorFieldHandler(ex.getMessage());
                }
            }

        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AppUI().setVisible(true);
            }
        });
    }
}
