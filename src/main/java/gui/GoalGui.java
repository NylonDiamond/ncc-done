package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class GoalGui {

    private JFrame frame;
    private JTextField passwordTextBox;
    private JTextField textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GoalGui window = new GoalGui();
                    window.frame.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GoalGui() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 574, 399);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnNewButton = new JButton("login");
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                JOptionPane.showMessageDialog(null, "yayayayayayay!");
            }
        });
        btnNewButton.setBounds(188, 213, 173, 66);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {}
        });
        frame.getContentPane().setLayout(null);

        JLabel lblUsername = new JLabel("username");
        lblUsername.setBounds(20, 31, 223, 66);
        frame.getContentPane().add(lblUsername);

        JLabel label = new JLabel("");
        label.setBounds(279, 0, 279, 120);
        frame.getContentPane().add(label);

        JLabel lblPassword = new JLabel("password");
        lblPassword.setBounds(39, 136, 152, 66);
        frame.getContentPane().add(lblPassword);

        passwordTextBox = new JTextField();
        passwordTextBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                //                JOptionPane.showMessageDialog(null, "yayayayayayay!");
            }
        });
        passwordTextBox.setBounds(128, 152, 279, 34);
        frame.getContentPane().add(passwordTextBox);
        passwordTextBox.setColumns(10);
        frame.getContentPane().add(btnNewButton);

        textField = new JTextField();
        textField.setColumns(10);
        textField.setBounds(128, 47, 279, 34);
        frame.getContentPane().add(textField);
    }
}
