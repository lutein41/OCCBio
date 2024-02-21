package edu.occ.occbio.returning;

import edu.occ.occbio.Validation;
import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class ReturnFormFrame {

    private static final Connection connection = DatabaseUtility.connection;

    private JFrame returnFormFrame = new JFrame();
    private JComboBox<String> reviewStars;
    private JTextArea commentField = new JTextArea(5,20);
    private JButton backButton = new JButton("Back");
    private JButton submitButton = new JButton("Submit");

    private String userId;
    private String modelId;
    private boolean late = false;

    public ReturnFormFrame(String userId, String modelId){
        this.userId = userId;
        this.modelId = modelId;

        returnFormFrame = new JFrame();
        returnFormFrame.setUndecorated(true);
        returnFormFrame.setSize(800,500);
        returnFormFrame.setLocationRelativeTo(null);
        returnFormFrame.setLayout(new GridLayout(4,1));
        returnFormFrame.setVisible(true);

        JPanel lateMessagePanel = new JPanel();
        JLabel lateMessageLabel = new JLabel("THIS IS LATE RETURNING");
        lateMessageLabel.setFont(new Font("", Font.BOLD, 25));
        //display "late return"
        late = Validation.isThisLateReturn(userId);
        if(late){
            lateMessagePanel.add(lateMessageLabel);
        }

        JPanel reviewStarPanel = new JPanel();
        JLabel quantityLabel = new JLabel("Review Stars: ");
        reviewStars = new JComboBox<>(new String[]{"1","2","3","4","5"});
        reviewStarPanel.add(quantityLabel);
        reviewStarPanel.add(reviewStars);

        JPanel commentPanel = new JPanel();
        JLabel commentLabel = new JLabel("Comments: ");
        commentField.setPreferredSize(new Dimension(150,100));
        commentField.setMaximumSize(new Dimension(150,100));
        commentPanel.add(commentLabel);
        commentPanel.add(commentField);

        JPanel buttonPanel = new JPanel();
        backButton.setPreferredSize(new Dimension(100,50));
        submitButton.setPreferredSize(new Dimension(100,50));
        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        returnFormFrame.add(lateMessagePanel);
        returnFormFrame.add(reviewStarPanel);
        returnFormFrame.add(commentPanel);
        returnFormFrame.add(buttonPanel);

        //adding event listener
        backEventListener(backButton);
        submitButtonEventListener(submitButton);
    }

    // back button event listener
    private void backEventListener(JButton backButton){
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnFormFrame.dispose();
            }
        });
    }

    // submit button event listener
    private void submitButtonEventListener(JButton submitButton){
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stars = (String)reviewStars.getSelectedItem();
                String comments = commentField.getText();
                DatabaseUtility.insertRating(userId,modelId,stars,comments);
                DatabaseUtility.incrementAvailableQuantity(modelId);
                DatabaseUtility.deleteLend(userId);
                //if this is late return
                if(late){
                    DatabaseUtility.incrementNumberOfLateReturn(userId);
                    int numberOfLateReturn = Integer.parseInt(DatabaseUtility.getNumberOfLateReturn(userId));
                    if(numberOfLateReturn == 3){
                        DatabaseUtility.insertSuspendedDate(userId);
                    }
                }
                JOptionPane.showMessageDialog(null,"Returned the item successfully");
                returnFormFrame.dispose();
            }
        });
    }

}// end of ReturnFormFrame class
