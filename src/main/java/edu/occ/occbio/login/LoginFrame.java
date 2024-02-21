package edu.occ.occbio.login;

import edu.occ.occbio.MainFrame;
import edu.occ.occbio.Validation;
import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame{

    private final JFrame loginFrame = new JFrame();
    private final JTextField userIdField = new JTextField();
    private final JPasswordField userPasswordField = new JPasswordField();


    public LoginFrame(){
        // This constructor creates and set up UI components.
        loginFrame.setVisible(true);
        loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        loginFrame.setResizable(false); // user can't change size of screen
        loginFrame.setLayout(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // a program terminates when a user closes the frame.
        loginFrame.setBackground(Color.GRAY);
        loginFrame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        loginPanel.setBounds(((int)(width/2))-350,300,700,100);
        loginPanel.setBorder(BorderFactory.createLineBorder(Color.black, 5, true));
        JLabel userIdLabel = new JLabel("user ID: ");
        userIdLabel.setFont(new Font("", Font.BOLD, 12 ));
        userIdField.setPreferredSize(new Dimension(200,50));
        JLabel userPasswordLabel = new JLabel("Password: ");
        userPasswordLabel.setFont(new Font("", Font.BOLD, 12 ));
        userPasswordField.setPreferredSize(new Dimension(200,50));
        JButton submitButton = new JButton("submit");
        submitButton.setPreferredSize(new Dimension(100,50));

        loginPanel.add(userIdLabel);
        loginPanel.add(userIdField);
        loginPanel.add(userPasswordLabel);
        loginPanel.add(userPasswordField);
        loginPanel.add(submitButton);
        loginFrame.add(loginPanel);

        // Calling event listeners
        submitButtonEventListener(submitButton);
    }

    private void submitButtonEventListener(JButton submitButton){
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userId = userIdField.getText();
                String userPassword = new String(userPasswordField.getPassword());
                //database query
                if(!Validation.validateUserIdDigits(userId)){
                    JOptionPane.showMessageDialog(null,"user id should be 4 digits");
                    return;
                }
                ResultSet resultSet = DatabaseUtility.getUserInformation(userId, userPassword);
                try {
                    while(resultSet.next()){
                        if(userId.equals(resultSet.getString("userId")) &&
                                userPassword.equals(resultSet.getString("password"))){
                            LoginSession.logIn = true;
                            LoginSession.firstName = resultSet.getString("FirstName");
                            LoginSession.lastName = resultSet.getString("LastName");
                            LoginSession.position = resultSet.getString("Position");
                            JOptionPane.showMessageDialog(null,"You have successfully logged in");
                            loginFrame.dispose(); //close frame
                            new MainFrame(); // open main frame
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null,"wrong userId or password");
            }
        });
    }

}// end of LoginFrame class
