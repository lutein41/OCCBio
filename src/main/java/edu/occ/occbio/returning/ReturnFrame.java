package edu.occ.occbio.returning;

import edu.occ.occbio.MainFrame;
import edu.occ.occbio.Validation;
import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class ReturnFrame {

    private static final Connection connection = DatabaseUtility.connection;

    JFrame returnFrame = new JFrame();
    JPanel northPanel = new JPanel();
    JPanel southPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel westPanel = new JPanel();
    JPanel mainPanel = new JPanel();

    JPanel userIdPanel = new JPanel();
    JPanel modelIdPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    private JTextField userIdField = new JTextField();
    private JTextField modelIdField = new JTextField();
    private JLabel userIdLabel = new JLabel("user ID: ");
    private JLabel modelIdLabel = new JLabel("model ID: ");
    private JButton submitButton = new JButton("Submit");
    private JButton homeButton = new JButton("Home");



    public ReturnFrame(){
        returnFrame.setVisible(true);
        returnFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        returnFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        returnFrame.setResizable(false);
        returnFrame.setBackground(Color.GRAY);
        returnFrame.setLayout(new BorderLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        northPanel.setPreferredSize(new Dimension(100,(int)(height*0.25)));
        southPanel.setPreferredSize(new Dimension(100,(int)(height*0.25)));
        westPanel.setPreferredSize(new Dimension((int)(height*0.55),100 ));
        eastPanel.setPreferredSize(new Dimension((int)(height*0.55),100 ));
        mainPanel.setPreferredSize(new Dimension(100,100));

        returnFrame.add(northPanel, BorderLayout.NORTH);
        returnFrame.add(southPanel, BorderLayout.SOUTH);
        returnFrame.add(eastPanel, BorderLayout.EAST);
        returnFrame.add(westPanel, BorderLayout.WEST);
        returnFrame.add(mainPanel, BorderLayout.CENTER);

        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
        JLabel titleLabel = new JLabel("Returning Model");
        titleLabel.setFont(new Font("", Font.BOLD, 50 ));
        northPanel.add(titleLabel);

        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        mainPanel.setLayout(new GridLayout(3,1,10,5));

        userIdPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 50));
        userIdLabel.setFont(new Font("", Font.PLAIN, 40));
        userIdField.setPreferredSize(new Dimension(300,50));
        userIdPanel.add(userIdLabel);
        userIdPanel.add(userIdField);

        modelIdPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 50));
        modelIdLabel.setFont(new Font("", Font.PLAIN, 40));
        modelIdField.setPreferredSize(new Dimension(300,50));
        modelIdPanel.add(modelIdLabel);
        modelIdPanel.add(modelIdField);

        homeButton.setPreferredSize(new Dimension(100,50));
        submitButton.setPreferredSize(new Dimension(100,50));
        buttonPanel.add(homeButton);
        buttonPanel.add(submitButton);

        mainPanel.add(userIdPanel);
        mainPanel.add(modelIdPanel);
        mainPanel.add(buttonPanel);

        // adding event listener
        homeEventListener();
        submitEventListener();
    }

    // Home button event listener
    private void homeEventListener(){
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnFrame.dispose();
                new MainFrame(); // go back to main frame
            }
        });
    }

    // Submit button event listener
    private void submitEventListener() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String modelId = modelIdField.getText();
                boolean rented = Validation.didStudentLendThisModel(userIdField.getText(), modelId);
                if(rented){
                    new ReturnFormFrame(userId, modelId);
                }else{
                    JOptionPane.showMessageDialog(null," Check user id and model id again!!! ");
                }
            }
        });
    }

}// end of ReturnFrame class
