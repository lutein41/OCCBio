package edu.occ.occbio.register;

import edu.occ.occbio.MainFrame;
import edu.occ.occbio.Validation;
import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {

    private final JFrame registerFrame = new JFrame();
    private final JTextField userIdField = new JTextField();
    private final JTextField firstNameField = new JTextField();

    JLabel userIdLabel = new JLabel("user ID: ");
    JLabel firstNameLabel = new JLabel("first Name: ");
    JLabel lastNameLabel = new JLabel("last Name: ");
    JTextField lastNameField = new JTextField();
    JButton submitButton = new JButton("Submit");
    JButton homeButton = new JButton("Home");


    public RegisterFrame(){
        // This constructor creates and set up UI components.
        registerFrame.setVisible(true);
        registerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setResizable(false);
        registerFrame.setBackground(Color.GRAY);
        registerFrame.setLayout(new BorderLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel mainPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(100,(int)(height*0.25)));
        southPanel.setPreferredSize(new Dimension(100,(int)(height*0.25)));
        westPanel.setPreferredSize(new Dimension((int)(height*0.55),100 ));
        eastPanel.setPreferredSize(new Dimension((int)(height*0.55),100 ));
        mainPanel.setPreferredSize(new Dimension(100,100));
        //North Panel
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
        JLabel titleLabel = new JLabel("Register Form");
        titleLabel.setFont(new Font("", Font.BOLD, 50 ));
        northPanel.add(titleLabel);
        //Main Panel
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        mainPanel.setLayout(new GridLayout(4,1,10,5));

        JPanel userIdPanel = new JPanel();
        userIdLabel.setFont(new Font("", Font.PLAIN, 30));
        userIdField.setPreferredSize(new Dimension(300,50));
        userIdPanel.add(userIdLabel);
        userIdPanel.add(userIdField);
        JPanel firstNamePanel = new JPanel();
        firstNameLabel.setFont(new Font("", Font.PLAIN, 30));
        firstNameField.setPreferredSize(new Dimension(300,50));
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameField);
        JPanel lastNamePanel = new JPanel();
        lastNameLabel.setFont(new Font("", Font.PLAIN, 30));
        lastNameField.setPreferredSize(new Dimension(300,50));
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameField);
        JPanel buttonPanel = new JPanel();
        submitButton.setPreferredSize(new Dimension(100,50));
        homeButton.setPreferredSize(new Dimension(100,50));
        buttonPanel.add(homeButton);
        buttonPanel.add(submitButton);

        mainPanel.add(userIdPanel);
        mainPanel.add(firstNamePanel);
        mainPanel.add(lastNamePanel);
        mainPanel.add(buttonPanel);

        registerFrame.add(northPanel, BorderLayout.NORTH);
        registerFrame.add(southPanel, BorderLayout.SOUTH);
        registerFrame.add(eastPanel, BorderLayout.EAST);
        registerFrame.add(westPanel, BorderLayout.WEST);
        registerFrame.add(mainPanel, BorderLayout.CENTER);

        //calling event listener
        homeButtonEventListener(homeButton);
        submitEventListener(submitButton);
    }

    private void homeButtonEventListener(JButton homeButton){
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               registerFrame.dispose();
               new MainFrame();
            }
        });
    }

    private void submitEventListener(JButton submitButton){
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                if(!Validation.validateUserIdDigits(userId)){
                    JOptionPane.showMessageDialog(null,"user id should be 4 digits");
                    return;
                }
                boolean duplicate = Validation.isThereDuplicateUserId(userId);
                if(duplicate){
                    JOptionPane.showMessageDialog(null,"user id already exist");
                }else{
                    DatabaseUtility.insertUserData(Integer.parseInt(userId),firstName,lastName);
                    JOptionPane.showMessageDialog(null,"successfully registered");
                    registerFrame.dispose();
                    new MainFrame();
                }
            }
        });
    }

}// end of RegisterFrame class
