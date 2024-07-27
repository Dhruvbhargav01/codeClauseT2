import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HospitalManagementSystem extends JFrame {
    private JTextField txtName, txtAge, txtGender, txtAddress, txtPhone, txtMedicalHistory;
    private Connection connection;

    public HospitalManagementSystem() {
        // Database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "@Mahadev01");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // UI setup
        setTitle("Hospital Information System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2));

        add(new JLabel("Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("Age:"));
        txtAge = new JTextField();
        add(txtAge);

        add(new JLabel("Gender:"));
        txtGender = new JTextField();
        add(txtGender);

        add(new JLabel("Address:"));
        txtAddress = new JTextField();
        add(txtAddress);

        add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        add(txtPhone);

        add(new JLabel("Medical History:"));
        txtMedicalHistory = new JTextField();
        add(txtMedicalHistory);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePatient();
            }
        });
        add(btnSave);

        setVisible(true);
    }

    private void savePatient() {
        try {
            String query = "INSERT INTO patients_ (name, age, gender, address, phone, medical_history) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, txtName.getText());
            stmt.setInt(2, Integer.parseInt(txtAge.getText()));
            stmt.setString(3, txtGender.getText());
            stmt.setString(4, txtAddress.getText());
            stmt.setString(5, txtPhone.getText());
            stmt.setString(6, txtMedicalHistory.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient information saved successfully.");
            verifyPatientInfo();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving patient information.");
        }
    }

    private void verifyPatientInfo() {
        JFrame verifyFrame = new JFrame("Verify Patient Information");
        verifyFrame.setSize(400, 200);
        verifyFrame.setLayout(new GridLayout(3, 2));

        JTextField txtVerifyName = new JTextField();
        verifyFrame.add(new JLabel("Enter Name to Verify:"));
        verifyFrame.add(txtVerifyName);

        JButton btnVerify = new JButton("Verify");
        btnVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtVerifyName.getText();
                verifyPatientInDatabase(name);
            }
        });
        verifyFrame.add(btnVerify);

        JButton btnReturn = new JButton("Return to Add Info");
        btnReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyFrame.dispose();
                new HospitalManagementSystem();
            }
        });
        verifyFrame.add(btnReturn);

        verifyFrame.setVisible(true);
    }

    private void verifyPatientInDatabase(String name) {
        try {
            String query = "SELECT * FROM patients_ WHERE name = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                displayPatientInfo(rs);
            } else {
                JOptionPane.showMessageDialog(this, "No record found for the given name.");
                dispose();
                new HospitalManagementSystem();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error verifying patient information.");
        }
    }

    private void displayPatientInfo(ResultSet rs) {
        try {
            JFrame displayFrame = new JFrame("Patient Information");
            displayFrame.setSize(800, 200);
            displayFrame.setLayout(new GridLayout(2, 1));

            JPanel infoPanel = new JPanel(new GridLayout(1, 6));
            infoPanel.add(new JLabel("Name"));
            infoPanel.add(new JLabel("Age"));
            infoPanel.add(new JLabel("Gender"));
            infoPanel.add(new JLabel("Address"));
            infoPanel.add(new JLabel("Phone"));
            infoPanel.add(new JLabel("Medical History"));

            JPanel dataPanel = new JPanel(new GridLayout(1, 6));
            dataPanel.add(new JLabel(rs.getString("name")));
            dataPanel.add(new JLabel(String.valueOf(rs.getInt("age"))));
            dataPanel.add(new JLabel(rs.getString("gender")));
            dataPanel.add(new JLabel(rs.getString("address")));
            dataPanel.add(new JLabel(rs.getString("phone")));
            dataPanel.add(new JLabel(rs.getString("medical_history")));

            displayFrame.add(infoPanel);
            displayFrame.add(dataPanel);

            displayFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying patient information.");
        }
    }

    public static void main(String[] args) {
        new HospitalManagementSystem();
    }
}
