package edu.occ.occbio.manager;

import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AddFormFrame {

    private static final Connection connection = DatabaseUtility.connection;

    private JTextField modelNameField = new JTextField();
    private JTextField quantityField = new JTextField();
    private JTextField manufacturerField = new JTextField();
    private JTextField locationField = new JTextField();
    private JTextField systemField = new JTextField();
    private JButton backButton = new JButton("Back");
    private JButton submitButton = new JButton("Submit");

    public AddFormFrame(){

        JFrame addFormFrame = new JFrame();
        addFormFrame.setUndecorated(true);
        addFormFrame.setSize(800,500);
        addFormFrame.setLocationRelativeTo(null);
        addFormFrame.setLayout(new GridLayout(6,1));
        addFormFrame.setVisible(true);

        JPanel namePanel = new JPanel();
        JLabel modelNameLabel = new JLabel("Model Name: ");
        modelNameField.setPreferredSize(new Dimension(300,50));
        namePanel.add(modelNameLabel);
        namePanel.add(modelNameField);

        JPanel quantityPanel = new JPanel();
        JLabel quantityLabel = new JLabel("Quantity: ");
        quantityField.setPreferredSize(new Dimension(300,50));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);

        JPanel manufacturerPanel = new JPanel();
        JLabel manufacturerLabel = new JLabel("Manufacturer: ");
        manufacturerField.setPreferredSize(new Dimension(300,50));
        manufacturerPanel.add(manufacturerLabel);
        manufacturerPanel.add(manufacturerField);

        JPanel locationPanel = new JPanel();
        JLabel locationLabel = new JLabel("Location: ");
        locationField.setPreferredSize(new Dimension(300,50));
        locationPanel.add(locationLabel);
        locationPanel.add(locationField);

        JPanel systemPanel = new JPanel();
        JLabel systemLabel = new JLabel("System: ");

        systemField.setPreferredSize(new Dimension(300,50));
        systemPanel.add(systemLabel);
        systemPanel.add(systemField);

        JPanel buttonPanel = new JPanel();
        backButton.setPreferredSize(new Dimension(100,60));
        submitButton.setPreferredSize(new Dimension(100,60));
        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);
        backEventListener(backButton, addFormFrame);
        addSubmitEventListener(submitButton, addFormFrame);

        addFormFrame.add(namePanel);
        addFormFrame.add(quantityPanel);
        addFormFrame.add(manufacturerPanel);
        addFormFrame.add(locationPanel);
        addFormFrame.add(systemPanel);
        addFormFrame.add(buttonPanel);
    }
    // add submit button event listener
    private void addSubmitEventListener(JButton submitButton, JFrame addFormFrame){
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modelName = modelNameField.getText();
                String quantity = quantityField.getText();
                String manufacturer = manufacturerField.getText();
                String location = locationField.getText();
                String systemName = systemField.getText();

                //check if a system name already exist in BIOSYSTEM table to avoid FK violation
                boolean systemNotExist = true;
                try {
                    ResultSet resultset = DatabaseUtility.getAllSystemNames();
                    while (resultset.next()) {
                        if ((resultset.getString("SystemName")).equalsIgnoreCase(systemName)) {
                            systemNotExist = false;
                        }
                    }
                }catch(SQLException ex){
                    System.out.println(ex.getMessage());
                }
                if (systemNotExist) {
                    DatabaseUtility.insertSystemName(systemName);
                }
                //Ok to insert a new model into the database
                DatabaseUtility.insertNewModel(modelName, quantity, manufacturer, location, systemName);
                JOptionPane.showMessageDialog(null,"A new model was added to the database");
                addFormFrame.dispose();
            }
        });
    }

    // back button event listener
    private void backEventListener(JButton backButton, JFrame addFormFrame){
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFormFrame.dispose();
            }
        });
    }

}// end of AddFormFrame
