package edu.occ.occbio.manager;

import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateFormFrame {

    private static final Connection connection = DatabaseUtility.connection;

    private JFrame updateFormFrame;
    private JTextField modelNameField = new JTextField();
    private JTextField quantityField = new JTextField();
    private JTextField manufacturerField = new JTextField();
    private JTextField locationField = new JTextField();
    private JTextField systemField = new JTextField();
    private JButton backButton = new JButton("Back");
    private JButton updateButton = new JButton("Update");
    private String modelId;
    private String modelName;
    private String quantity;
    private String availableQuantity;
    private String manufacturer;
    private String location;
    private String systemName;

    public UpdateFormFrame(String updateModelId){
        modelId = updateModelId;
        //fetch model information from the database
        try {
            ResultSet resultSet = DatabaseUtility.getAllModelInformation(modelId);
            if(resultSet.next()){
                modelName = resultSet.getString("ModelName");
                quantity = resultSet.getString("Quantity");
                manufacturer = resultSet.getString("Manufacturer");
                location = resultSet.getString("Location");
                systemName = resultSet.getString("SystemName");
                availableQuantity = resultSet.getString("AvailableQuantity");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        updateFormFrame = new JFrame();
        updateFormFrame .setUndecorated(true);
        updateFormFrame .setSize(800,500);
        updateFormFrame .setLocationRelativeTo(null);
        updateFormFrame .setLayout(new GridLayout(6,1));
        updateFormFrame .setVisible(true);

        JPanel namePanel = new JPanel();
        JLabel modelNameLabel = new JLabel("Model Name: ");
        modelNameField.setPreferredSize(new Dimension(300,50));
        modelNameField.setText(modelName);
        namePanel.add(modelNameLabel);
        namePanel.add(modelNameField);

        JPanel quantityPanel = new JPanel();
        JLabel quantityLabel = new JLabel("Quantity: ");
        quantityField.setPreferredSize(new Dimension(300,50));
        quantityField.setText(quantity);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);

        JPanel manufacturerPanel = new JPanel();
        JLabel manufacturerLabel = new JLabel("Manufacturer: ");
        manufacturerField.setPreferredSize(new Dimension(300,50));
        manufacturerField.setText(manufacturer);
        manufacturerPanel.add(manufacturerLabel);
        manufacturerPanel.add(manufacturerField);

        JPanel locationPanel = new JPanel();
        JLabel locationLabel = new JLabel("Location: ");
        locationField.setPreferredSize(new Dimension(300,50));
        locationField.setText(location);
        locationPanel.add(locationLabel);
        locationPanel.add(locationField);

        JPanel systemPanel = new JPanel();
        JLabel systemLabel = new JLabel("System: ");
        systemField.setPreferredSize(new Dimension(300,50));
        systemField.setText(systemName);
        systemPanel.add(systemLabel);
        systemPanel.add(systemField);

        JPanel buttonPanel = new JPanel();
        backButton.setPreferredSize(new Dimension(100,60));
        updateButton.setPreferredSize(new Dimension(100,60));
        buttonPanel.add(backButton);
        buttonPanel.add(updateButton);
        backEventListener(backButton, updateFormFrame );
        updateEventListener(updateButton, updateFormFrame );

        updateFormFrame .add(namePanel);
        updateFormFrame .add(quantityPanel);
        updateFormFrame .add(manufacturerPanel);
        updateFormFrame .add(locationPanel);
        updateFormFrame .add(systemPanel);
        updateFormFrame .add(buttonPanel);
    }

    //update button event listener
    private void updateEventListener(JButton backButton, JFrame addFormFrame){
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSystemName(); //check before updating data in the MODEL table to avoid violating FK constraint.
                String userInputModelName = modelNameField.getText();
                String userInputQuantity = quantityField.getText();
                String userInputManufacturer = manufacturerField.getText();
                String userInputLocation = locationField.getText();
                String userInputSystemName = systemField.getText();

                DatabaseUtility.updateModelInformation(userInputModelName,
                            userInputQuantity, userInputManufacturer, userInputLocation, userInputSystemName,modelId);

                JOptionPane.showMessageDialog(null," UPDATED!!! ");
                updateFormFrame.dispose();
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

    private void updateSystemName(){
        //check if a system name already exist in BIOSYSTEM table to avoid FK violation
        //Add a new system name in the BIOSYSTEM table if it doesn't exist in the table.
        boolean systemNotExist = true;
        try {
            ResultSet resultset = DatabaseUtility.getAllSystemNames();
            while(resultset.next()){
                if(resultset.getString("SystemName").equals(systemName)){
                    systemNotExist = false;
                }
            }
            if(systemNotExist){
                DatabaseUtility.insertSystemName(systemName);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
