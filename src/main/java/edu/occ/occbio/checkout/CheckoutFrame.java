package edu.occ.occbio.checkout;

import edu.occ.occbio.MainFrame;
import edu.occ.occbio.database.DatabaseUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;


public class CheckoutFrame implements ActionListener {

    //Fields
    JFrame checkoutFrame = new JFrame();
    JTextField userIdField = new JTextField();
    JTextField searchField = new JTextField();
    JComboBox<String> bySystem;
    JComboBox<String> byManufacturer;

    private JLabel searchLabel = new JLabel("Search: ");
    private JButton searchButton = new JButton("Search");
    private JButton homeButton = new JButton("Home");

    // list of distinct all available system and manufacturer names
    private String[] systemArray;
    private String[] manufacturerArray;

    // search by user's choices
    private String system = "none";
    private String manufacturer = "none";


    public CheckoutFrame(){
        // This constructor creates and set up UI components.
        checkoutFrame.setVisible(true);
        checkoutFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        checkoutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        checkoutFrame.setResizable(false);
        checkoutFrame.setBackground(Color.GRAY);
        checkoutFrame.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel mainPanel = new JPanel();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        northPanel.setPreferredSize(new Dimension(100,(int)(height*0.25)));
        southPanel.setPreferredSize(new Dimension(100,(int)(height*0.25)));
        westPanel.setPreferredSize(new Dimension((int)(height*0.55),100 ));
        eastPanel.setPreferredSize(new Dimension((int)(height*0.55),100 ));
        mainPanel.setPreferredSize(new Dimension(100,100));

        checkoutFrame.add(northPanel, BorderLayout.NORTH);
        checkoutFrame.add(southPanel, BorderLayout.SOUTH);
        checkoutFrame.add(eastPanel, BorderLayout.EAST);
        checkoutFrame.add(westPanel, BorderLayout.WEST);
        checkoutFrame.add(mainPanel, BorderLayout.CENTER);

        //north panel
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
        JLabel titleLabel = new JLabel("Checkout Form");
        titleLabel.setFont(new Font("", Font.BOLD, 50 ));
        northPanel.add(titleLabel);
        //main panel
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        mainPanel.setLayout(new GridLayout(3,1,10,5));
        //main panel -> user id panel
        JPanel userIdPanel = new JPanel();
        userIdPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 50));
        JLabel userIdLabel = new JLabel("user ID: ");
        userIdLabel.setFont(new Font("", Font.PLAIN, 40));
        userIdField.setPreferredSize(new Dimension(300,50));
        userIdPanel.add(userIdLabel);
        userIdPanel.add(userIdField);
        //main panel -> search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1,4,3,3));
        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("", Font.PLAIN, 40));
        JPanel searchFieldPanel = new JPanel();
        searchFieldPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 50));
        searchField.setPreferredSize(new Dimension(300,50));
        searchFieldPanel.add(searchField);
        JPanel searchButtonPanel = new JPanel();
        //JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100,50));
        searchButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 50));
        searchButtonPanel.add(searchButton);
        //main panel -> search panel -> search by
        JPanel filterPanel = new JPanel();
        filterPanel.setBorder(BorderFactory.createLineBorder(Color.black,3,true));
        filterPanel.setLayout(new GridLayout(3,1,5,5));

        JLabel filterTitleLabel = new JLabel("Search By");
        filterTitleLabel.setFont(new Font("", Font.BOLD, 20));
        JLabel systemLabel = new JLabel("System: ");
        systemLabel.setFont(new Font("", Font.PLAIN, 15));
        JLabel manufacturerLabel = new JLabel("Manufacturer: ");
        manufacturerLabel.setFont(new Font("", Font.PLAIN, 15));

        JPanel filterTitlePanel = new JPanel();
        filterTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
        filterTitlePanel.add(filterTitleLabel);

        fetchAllSystemName();
        fetchAllManufacturerName();
        bySystem = new JComboBox<>(systemArray);
        bySystem.addActionListener(this);
        byManufacturer = new JComboBox<>(manufacturerArray);
        byManufacturer.addActionListener(this);

        JPanel systemPanel = new JPanel();
        systemPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 15));
        systemPanel.add(systemLabel);
        systemPanel.add(bySystem);

        JPanel manufacturerPanel = new JPanel();
        manufacturerPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 15));
        manufacturerPanel.add(manufacturerLabel);
        manufacturerPanel.add(byManufacturer);

        filterPanel.add(filterTitlePanel);
        filterPanel.add(systemPanel);
        filterPanel.add(manufacturerPanel);

        searchPanel.add(searchFieldPanel);
        searchPanel.add(searchFieldPanel);
        searchPanel.add(searchButtonPanel);
        searchPanel.add(filterPanel);

        //submit panel
        JPanel homePanel = new JPanel(); // contains submit button
        homePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 50, 50));
        homeButton.setPreferredSize(new Dimension(100,50));
        homePanel.add(homeButton);

        mainPanel.add(userIdPanel);
        mainPanel.add(searchPanel);
        mainPanel.add(homePanel);

        // adding event listener
        searchEventListener();
        homeEventListener();
    }

    // Home button event listener
    private void homeEventListener(){
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutFrame.dispose();
                new MainFrame(); // go back to main frame
                return;
            }
        });
    }

    private void fetchAllSystemName(){
        /**
         *  This method fetches all system names (no duplicates) from the database
         */
        List<String> systemList = new ArrayList<>();
        ResultSet resultSet = DatabaseUtility.getAllSystemName();
        try {
            while(resultSet.next()){
                systemList.add(resultSet.getString("SystemName"));
                }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        systemList.add(0,"none");
        systemArray = new String[systemList.size()];
        for(int i=0; i<systemArray.length; i++){
            systemArray[i] = systemList.get(i);
        }
    }

    private void fetchAllManufacturerName(){
        /**
         *  This method fetches all manufacturer names (no duplicates) from the database
         */
        List<String> manufacturerList = new ArrayList<>();
        try {
            ResultSet resultSet = DatabaseUtility.getAllManufacturerName();
            while(resultSet.next()){
                manufacturerList.add(resultSet.getString("Manufacturer"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        manufacturerList.add(0,"none");
        manufacturerArray = new String[manufacturerList.size()];

        for(int i=0; i<manufacturerArray.length; i++){
            manufacturerArray[i] = manufacturerList.get(i);
        }
    }
    //combo box event listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == bySystem){
            system = (String)bySystem.getSelectedItem();
            if(system.equals("none") && manufacturer.equals("none")){
                searchField.setEnabled(true);
            }else{
                searchField.setEnabled(false);
            }
        }
        if(e.getSource() == byManufacturer){
            manufacturer = (String)byManufacturer.getSelectedItem();
            System.out.println(manufacturer);
            if(system.equals("none") && manufacturer.equals("none")){
                searchField.setEnabled(true);
            }else{
                searchField.setEnabled(false);
            }
        }
    }


    // search button event listener.
    private void searchEventListener(){
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] resultListArray = null;
                // need to get result table from database
                if(!system.equals("none") && manufacturer.equals("none")){
                    resultListArray = searchBy("system");
                }else if(system.equals("none") && !manufacturer.equals("none")){
                    resultListArray = searchBy("manufacturer");
                }else if(!system.equals("none") && !system.equals("none")){
                    resultListArray = searchBy("both");
                }else{
                    resultListArray =  searchByString(searchField.getText());
                }
                new SearchResultFrame(resultListArray,checkoutFrame,userIdField.getText());
            }
        });
    }

    // close button event listener
    private void closeEventListener(JButton closeButton, JFrame searchResultframe){
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutFrame.setEnabled(true);
                searchResultframe.setVisible(false);
            }
        });
    }


    private Object[][] searchBy(String key){
        /**
         * This method takes a key value (3 options: 1. search by only system 2. search by only manufacturer 3. both)
         * and return a 2D list of models after executing a query.
         */
        ResultSet resultSet = DatabaseUtility.searchBy(key, system, manufacturer);
        ArrayList<ArrayList<Object>> resultList = new ArrayList<>();
        try {
            int index = -1;
            while(resultSet.next()){
                resultList.add(new ArrayList<>());
                index++;
                resultList.get(index).add(resultSet.getString("ModelId"));
                resultList.get(index).add(resultSet.getString("ModelName"));
                resultList.get(index).add(resultSet.getString("Quantity"));
                resultList.get(index).add(resultSet.getString("AvailableQuantity"));
                resultList.get(index).add(resultSet.getString("Manufacturer"));
                resultList.get(index).add(resultSet.getString("Location"));
                resultList.get(index).add(resultSet.getString("SystemName"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Object[][] resultListArray = new Object[resultList.size()][7];
        for(int i=0; i<resultList.size(); i++){
            for(int j=0; j<7; j++){
                resultListArray[i][j] = resultList.get(i).get(j);
            }
        }
        return resultListArray;
    }

    private Object[][] searchByString(String userInput){
        /**
         * This method takes a user's string search keyword and find best matches from the database.
         * If a user's input is an empty string, it will return all available models.
         */
        ArrayList<ArrayList<Object>> resultList = new ArrayList<>();
        ArrayList<ArrayList<Object>> newResultList = new ArrayList<>();
        ResultSet resultSet = DatabaseUtility.searchByString(userInput);
        String sql="SELECT * FROM MODEL";
        try {
            int index = -1;
            while(resultSet.next()){
                resultList.add(new ArrayList<>());
                index++;
                resultList.get(index).add(resultSet.getString("ModelId"));
                resultList.get(index).add(resultSet.getString("ModelName"));
                resultList.get(index).add(resultSet.getString("Quantity"));
                resultList.get(index).add(resultSet.getString("AvailableQuantity"));
                resultList.get(index).add(resultSet.getString("Manufacturer"));
                resultList.get(index).add(resultSet.getString("Location"));
                resultList.get(index).add(resultSet.getString("SystemName"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        userInput = userInput.replaceAll("[!@#$%^&*()><:+_]",""); // remove all special characters
        StringBuffer userInputBuffer = new StringBuffer(userInput);

        int index = 0; // index for newResultList
        boolean foundMatches = false;
        Object[][] resultListArray = new Object[newResultList.size()][7];
        while(true){
            for(int i=0; i<resultList.size(); i++){
                if((((String)resultList.get(i).get(1))).toLowerCase().startsWith(userInput.toLowerCase())){ // check if model name contains user's input model name.
                    newResultList.add(new ArrayList<>());
                    newResultList.get(index).add(resultList.get(i).get(0));
                    newResultList.get(index).add(resultList.get(i).get(1));
                    newResultList.get(index).add(resultList.get(i).get(2));
                    newResultList.get(index).add(resultList.get(i).get(3));
                    newResultList.get(index).add(resultList.get(i).get(4));
                    newResultList.get(index).add(resultList.get(i).get(5));
                    newResultList.get(index).add(resultList.get(i).get(6));
                    index++;
                    foundMatches = true;
                }
            }
            if(foundMatches){
                //done finding matches. I will return object[][] object instead of ArrayList[][] object.
                resultListArray = new Object[newResultList.size()][7];
                for(int i=0; i<newResultList.size(); i++){
                    for(int j=0; j<7; j++){
                        resultListArray[i][j] = newResultList.get(i).get(j);
                    }
                }
                return resultListArray;
            }
            if(newResultList.size() == 0){
                userInputBuffer.delete( userInputBuffer.length()-1, userInputBuffer.length()); // delete last character and try again
                userInput = userInputBuffer.toString();
                if(userInput.isEmpty()){
                    return resultListArray;
                }
            }
        }
    }

}// end of class






