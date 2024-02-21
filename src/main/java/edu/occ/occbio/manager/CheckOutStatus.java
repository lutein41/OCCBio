package edu.occ.occbio.manager;

import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CheckOutStatus {

    private static final Connection connection = DatabaseUtility.connection;

    public CheckOutStatus(){

        Object[][] resultListArray = null;
        JFrame checkOutStatusframe = new JFrame();
        checkOutStatusframe.setUndecorated(true);
        checkOutStatusframe.setSize(800,500);
        DefaultTableModel defaultTable = new DefaultTableModel();
        defaultTable.setDataVector( getAllCheckOutInformation(),
                new Object[]{"ID", "Name", "Quantity", "availQuant","Manufacturer","firstName","lastName","rentedDate","expire"});
        JTable table = new JTable(defaultTable);
        JScrollPane scroll = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.getColumnModel().getColumn(8).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(150);
        checkOutStatusframe .add(scroll);
        checkOutStatusframe .setLocationRelativeTo(null);
        checkOutStatusframe .setVisible(true);

        JButton closeButton = new JButton("close");
        closeButton.setPreferredSize(new Dimension(100,100));
        checkOutStatusframe .add(closeButton, BorderLayout.NORTH);
        closeEventListener(closeButton, checkOutStatusframe);
    }

    // close button event listener
    private void closeEventListener(JButton closeButton, JFrame checkOutStatusframe){
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkOutStatusframe.dispose();
            }
        });
    }

    private Object[][] getAllCheckOutInformation(){
        ArrayList<ArrayList<Object>> resultList = new ArrayList<>();
        // First, get all tuples from MODEL table and store them in the resultList 2D arraylist.
        String sql="SELECT * FROM MODEL AS M INNER JOIN LEND AS L ON M.ModelId = L.ModelID " +
                "INNER JOIN USER AS U ON L.UserId = U.UserId ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int index = -1;
            while(resultSet.next()){
                resultList.add(new ArrayList<>());
                index++;
                resultList.get(index).add(resultSet.getString("ModelId"));
                resultList.get(index).add(resultSet.getString("ModelName"));
                resultList.get(index).add(resultSet.getString("Quantity"));
                resultList.get(index).add(resultSet.getString("AvailableQuantity"));
                resultList.get(index).add(resultSet.getString("Manufacturer"));
                resultList.get(index).add(resultSet.getString("FirstName"));
                resultList.get(index).add(resultSet.getString("LastName"));
                resultList.get(index).add(resultSet.getString("dateTime"));
                resultList.get(index).add(resultSet.getString("expire"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        Object[][] resultListArray = new Object[resultList.size()][9];
        for(int i=0; i<resultList.size(); i++){
            for(int j=0; j<9; j++){
                resultListArray[i][j] = resultList.get(i).get(j);
            }
        }
        return resultListArray;
    }// end of getAllCheckOutInformation( )

}// end of CheckOutStatus class
