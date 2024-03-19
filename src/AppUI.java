import javax.swing.*;

public class AppUI {

    private static final String APPLICATION_TITLE = "Cipher App";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 40;

    private final JFrame window;

    AppUI() {
        window = new JFrame(APPLICATION_TITLE);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);

        componentInit();

        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private void componentInit() {
        JButton b1 = new JButton("Click");
        JTextField tf = new JTextField();
        tf.setBounds(30, 30, 200, 20);
        b1.setBounds(30, 60, 100, 20);
        window.add(tf);
        window.add(b1);
    }

    public static void main(String[] args) {
        new AppUI();
    }
}
