import javax.swing.*;
import java.awt.event.*;
import java.util.stream.IntStream;
import java.sql.*;

public class Registration_form extends JFrame implements ActionListener {
     JFrame frame;
     JLabel title;
     JLabel nameLabel;
     JTextField nameField;
     JLabel mobileLabel;
     JTextField mobileField;
     JLabel genderLabel;
     JRadioButton male;
     JRadioButton female;
     ButtonGroup genderGroup;
     JLabel dobLabel;
     JComboBox<String> date;
     JComboBox<String> month;
     JComboBox<String> year;
     JLabel addressLabel;
     JTextArea addressField;
     JCheckBox terms;
     JButton submit;
     JButton reset;
     JTextArea output;

// Database credentials
static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/Registration";
static final String USER = "root";
static final String PASSWORD= "Crissy7164";

//The constructor for initialization of components
public Registration_form(){
    frame = new JFrame("Registration Form");
    frame.setBounds(300, 100, 900, 600);
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLayout(null);

    title = new JLabel("Registration Form");
    title.setBounds(300, 30, 300, 30);
    frame.add(title);

    nameLabel = new JLabel("NAME");
    nameLabel.setBounds(100, 100, 100, 20);
    frame.add(nameLabel);

    nameField = new JTextField();
    nameField.setBounds(200, 100, 200, 20);
    frame.add(nameField);

    mobileLabel = new JLabel("MOBILE");
    mobileLabel.setBounds(100, 150, 100, 20);
    frame.add(mobileLabel);

    mobileField = new JTextField();
    mobileField.setBounds(200, 150, 200, 20);
    frame.add(mobileField);

    genderLabel = new JLabel("GENDER");
    genderLabel.setBounds(100, 200, 100, 20);
    frame.add(genderLabel);

    male = new JRadioButton("male");
    male.setBounds(200, 200, 75, 20);
    frame.add(male);

    female = new JRadioButton("female");
    female.setBounds(275,200,80,20);
    frame.add(female);

    genderGroup = new ButtonGroup();
    genderGroup.add(male);
    genderGroup.add(female);

    dobLabel = new JLabel("DOB");
    dobLabel.setBounds(100, 250, 100, 20);
    frame.add(dobLabel);

    //creating days arrays
    String[] dates = IntStream.rangeClosed(1, 31).mapToObj(String::valueOf).toArray(String[]::new);
    date = new JComboBox<>(dates);
    date.setBounds(200, 250, 50, 20);
    frame.add(date);

    //creating array for months
    String[]months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    month = new JComboBox<>(months);
    month.setBounds(260, 250, 60, 20);
    frame.add(month);

    //creating years arrays
    int startYear = 1990;
    int endYear = 2020;

    String[] years = IntStream.rangeClosed(startYear, endYear).mapToObj(String::valueOf).toArray(String[]::new);
    year = new JComboBox<>(years);
    year.setBounds(330, 250, 60, 20);
    frame.add(year);

    addressLabel = new JLabel("ADDRESS");
    addressLabel.setBounds(100, 300, 100, 20);
    frame.add(addressLabel);

    addressField = new JTextArea();
    addressField.setBounds(200, 300, 200, 75);
    frame.add(addressField);

    terms = new JCheckBox("Accept terms and conditions");
    terms.setBounds(150, 350, 250, 20);
    frame.add(terms);

    submit = new JButton("SUBMIT");
    submit.setBounds(150, 450, 100, 20);
    submit.addActionListener(this);
    frame.add(submit);

    reset = new JButton("RESET");
    reset.setBounds(250, 450, 100,20);
    reset.addActionListener(this);
    frame.add(reset);

    output = new JTextArea();
    output.setBounds(450,100,300,200);
    output.setEditable(false);
    frame.add(output);

    frame.setVisible(true);
}
  //Handling button actions
     public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            if (terms.isSelected()) {
                String name = nameField.getText();
                String mobile = mobileField.getText();
                String gender = male.isSelected() ? "Male" : "Female";
                String dob = date.getSelectedItem() + "-" + 
                             month.getSelectedItem() + "-" + 
                             year.getSelectedItem();
                String address = addressField.getText();

                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)")) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, mobile);
                    pstmt.setString(3, gender);
                    pstmt.setString(4, dob);
                    pstmt.setString(5, address);
                    pstmt.executeUpdate();
                    
                    output.setText("Name: " + name + "\n" +
                                   "Mobile: " + mobile + "\n" +
                                   "Gender: " + gender + "\n" +
                                   "DOB: " + dob + "\n" +
                                   "Address: " + address);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                output.setText("Please accept the terms and conditions.");
            }
        } else if (e.getSource() == reset) {
            nameField.setText("");
            mobileField.setText("");
            genderGroup.clearSelection();
            date.setSelectedIndex(0);
            month.setSelectedIndex(0);
            year.setSelectedIndex(0);
            addressField.setText("");
            terms.setSelected(false);
            output.setText("");
        }
    }

    public static void main(String[] args) throws Exception {
        new Registration_form();
    }
}
