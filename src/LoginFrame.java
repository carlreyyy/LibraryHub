import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import BackEnds.Login;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField passwordField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(50, 50, 80, 25);
		contentPane.add(lblUsername);

		usernameField = new JTextField();
		usernameField.setBounds(140, 50, 200, 25);
		contentPane.add(usernameField);
		usernameField.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(50, 100, 80, 25);
		contentPane.add(lblPassword);

		passwordField = new JTextField();
		passwordField.setBounds(140, 100, 200, 25);
		contentPane.add(passwordField);
		passwordField.setColumns(10);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(183, 162, 80, 25);
		contentPane.add(loginButton);

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login log = new Login();
				String username = usernameField.getText();
				String password = passwordField.getText();
				
				log.checkInfo(username,password);
			}
		});
	}
}
