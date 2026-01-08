import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MunchMinderGUI extends JFrame {
    private JPanel contentPane;
    private JTextField itemField;
    private JTextField dateField;
    private DefaultListModel<String> listModel;
    private ArrayList<String> itemInfo;

    public MunchMinderGUI() {
        setTitle("MunchMinder 🍎");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 600);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(252, 177, 3)); // warm food-tone background
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JLabel titleLabel = new JLabel("Welcome to MunchMinder!");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(titleLabel, BorderLayout.NORTH);

        // Center panel for inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(new Color(255, 228, 196)); // food vibe

        inputPanel.add(new JLabel("Item Name:"));
        itemField = new JTextField();
        inputPanel.add(itemField);

        inputPanel.add(new JLabel("Expiration Date (MM/dd/yyyy):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        JButton addButton = new JButton("Add Item 🍕");
        addButton.setBackground(new Color(255, 165, 0));
        addButton.setForeground(Color.black);
        addButton.setFocusPainted(false);
        inputPanel.add(addButton);

        contentPane.add(inputPanel, BorderLayout.CENTER);

        // List to show items
        listModel = new DefaultListModel<>();
        JList<String> itemList = new JList<>(listModel);
        itemList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Items & Expiration Dates"));
        contentPane.add(scrollPane, BorderLayout.SOUTH);

        itemInfo = new ArrayList<>();

        // Button action
        addButton.addActionListener(e -> addItem());

        setVisible(true);
    }

    private void addItem() {
        String itemName = itemField.getText().trim();
        String expireDate = dateField.getText().trim();

        if (itemName.isEmpty() || expireDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Parse expiration date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate expDate = LocalDate.parse(expireDate, formatter);
            LocalDate today = LocalDate.now();

            // Calculate days until expiration
            long daysLeft = ChronoUnit.DAYS.between(today, expDate);

            // Add to list
            listModel.addElement(itemName + " - " + expireDate);

            // Show warnings
            if (daysLeft == 7) {
                JOptionPane.showMessageDialog(this,
                        itemName + " will expire in 1 week!",
                        "Expiration Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else if (daysLeft < 0) {
                JOptionPane.showMessageDialog(this,
                        itemName + " has already expired!",
                        "Expired Item",
                        JOptionPane.ERROR_MESSAGE);
            }

            // Google recipe link prompt
            String searchQuery = itemName.replace(" ", "+") + "+recipes";
            String googleLink = "https://www.google.com/search?q=" + searchQuery;

            int openLink = JOptionPane.showConfirmDialog(this,
                    "Find recipes for " + itemName + "?",
                    "Recipe Link",
                    JOptionPane.YES_NO_OPTION);

            if (openLink == JOptionPane.YES_OPTION) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI(googleLink));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening browser: " + ex.getMessage());
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Please use MM/dd/yyyy.", "Date Error", JOptionPane.ERROR_MESSAGE);
        }

        // Reset input fields
        itemField.setText("");
        dateField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MunchMinderGUI::new);
    }
}