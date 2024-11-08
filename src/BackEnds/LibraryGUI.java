package BackEnds;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryGUI {
    private Librarian librarian;

    public LibraryGUI() {
        librarian = new Librarian(); // Create instance of Librarian
        
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        JTextField reservationIdField = new JTextField(10);
        JButton addRequestButton = new JButton("Add Request");

        // Add components to the panel
        panel.add(new JLabel("Enter Reservation ID:"));
        panel.add(reservationIdField);
        panel.add(addRequestButton);

        // Add ActionListener to the button
        addRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int reservationId = Integer.parseInt(reservationIdField.getText());
                    librarian.addRequest(reservationId); // Call addRequest method
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid integer ID.");
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LibraryGUI();
    }
}
