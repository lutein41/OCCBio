package edu.occ.occbio.checkout;

import edu.occ.occbio.MainFrame;
import edu.occ.occbio.Validation;
import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class SearchResultFrame {

    private JFrame searchFrame = new JFrame();
    private JFrame checkoutFormFrame;
    private JTextField submitField;
    private String userId;

    SearchResultFrame(Object[][] resultListArray, JFrame checkoutFormFrame, String userId){
        this.checkoutFormFrame = checkoutFormFrame;
        this.userId = userId;
        searchFrame.setUndecorated(true);
        searchFrame.setSize(800,500);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setVisible(true);
        DefaultTableModel defaultTable = new DefaultTableModel();
        checkoutFormFrame.setEnabled(false);
        defaultTable.setDataVector( resultListArray,
                new Object[]{"Model_ID", "Model_Name", "Quantity", "availableQuantity","Manufacturer", "Location","system_name"});
        JTable table = new JTable(defaultTable);
        JScrollPane scroll = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        searchFrame.add(scroll);

        JButton closeButton = new JButton("close");
        closeButton.setPreferredSize(new Dimension(100,100));

        JLabel submitLabel = new JLabel("Enter model ID here:");
        submitField = new JTextField();
        submitField.setPreferredSize(new Dimension(150,50));
        JButton submitModelButton = new JButton("Submit");

        JPanel submitPanel = new JPanel();
        submitPanel.add(submitLabel);
        submitPanel.add(submitField);
        submitPanel.add(submitModelButton);

        searchFrame.add(submitPanel, BorderLayout.SOUTH);
        searchFrame.add(closeButton, BorderLayout.NORTH);

        //adding event listener
        closeEventListener(closeButton, searchFrame);
        submitDataEventListener(submitModelButton);
    }

    // close button event listener
    private void closeEventListener(JButton closeButton, JFrame searchResultframe){
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutFormFrame.setEnabled(true);
                searchFrame.dispose();
            }
        });
    }

    private void submitDataEventListener(JButton submitModelButton) {
        submitModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if selected model is available or insert a correct model id

                String userInputModelId = submitField.getText();
                boolean isModelAvailable = Validation.isThisModelAvailable(userInputModelId);
                boolean doesModelExist = Validation.doesModelexist(userInputModelId);
                if(!doesModelExist){
                    JOptionPane.showMessageDialog(null,"You entered a wrong model ID");
                    return;
                }
                if(!isModelAvailable){
                    displayWhenModelWillBeAvailable(userInputModelId);
                    return;
                }
                /**
                 * business constraint : check if a student is registered
                 * business constraint : check if a student is suspended
                 * business constraint : Student can lend only single model.
                 */
                if(userId.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Please enter user Id");
                    checkoutFormFrame.setEnabled(true);
                    searchFrame.dispose();
                    return;
                }
                boolean isStudentRegistered = Validation.isStudnetRegistered(userId);
                if(!isStudentRegistered){
                    JOptionPane.showMessageDialog(null,"Please register a student first");
                    checkoutFormFrame.setEnabled(true);
                    searchFrame.dispose();
                    return;
                }
                boolean isStudentSuspended = Validation.isStudentSuspended(userId);
                if(isStudentSuspended){
                    JOptionPane.showMessageDialog(null, "This student is suspended");
                    checkoutFormFrame.setEnabled(true);
                    searchFrame.dispose();
                    return;
                }
                boolean didStudentRentAlready = Validation.didStudentRentAlready(userId);
                if(didStudentRentAlready){
                    JOptionPane.showMessageDialog(null,"This student already checked out a model");
                    checkoutFormFrame.setEnabled(true);
                    searchFrame.dispose();
                    return;
                }
                // Now Student is OK to rent a model
                String expires = DatabaseUtility.databaseCheckoutUpdate(userId, userInputModelId);
                JOptionPane.showMessageDialog(null,"Student need to return the model no later than " + expires);
                searchFrame.dispose();
                new MainFrame(); // go back to main frame
            }
        });
    }

    private void displayWhenModelWillBeAvailable(String modelId){
        /**
         * This method will pop up a frame that display students who rented unavailable models and when it will be available again
         */
        //fetch data
        ArrayList<ArrayList<Object>> resultList = new ArrayList<>();
        ResultSet resultSet = DatabaseUtility.getWhenModelWillBeAvailable(modelId);
        try{
            int index = -1;
            while(resultSet.next()){
                resultList.add(new ArrayList<>());
                index++;
                resultList.get(index).add(resultSet.getString("expire"));
                resultList.get(index).add(resultSet.getString("FirstName"));
                resultList.get(index).add(resultSet.getString("LastName"));
            }
        }catch(SQLException ex){

        }
        Object[][] resultListArray = new Object[resultList.size()][3];
        for(int i=0; i<resultList.size(); i++){
            for(int j=0; j<3; j++){
                resultListArray[i][j] = resultList.get(i).get(j);
            }
        }
        JFrame frame = new JFrame();
        frame.setSize(800,500);
        DefaultTableModel defaultTable = new DefaultTableModel();
        defaultTable.setDataVector( resultListArray,
                new Object[]{"Avaiable_at" , "firstName", "LastName"});
        JTable table = new JTable(defaultTable);
        JScrollPane scroll = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        frame.add(scroll);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JLabel messageLabel = new JLabel("I am sorry. This model is not available.");
        frame.add(messageLabel, BorderLayout.NORTH);
    }

}// end of SearchResultFrame class



