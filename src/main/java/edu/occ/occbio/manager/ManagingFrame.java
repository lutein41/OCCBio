package edu.occ.occbio.manager;

import edu.occ.occbio.MainFrame;
import edu.occ.occbio.Validation;
import edu.occ.occbio.database.DatabaseUtility;
import edu.occ.occbio.login.LoginSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagingFrame implements ActionListener {

    private static final Connection connection = DatabaseUtility.connection;

    private final JTextField searchField = new JTextField();
    private final JTextField updateField = new JTextField();
    private final JTextField deleteField = new JTextField();
    private  JComboBox<String> bySystem;
    private  JComboBox<String> byManufacturer;

    private final JFrame managingFrame = new JFrame();
    private final JPanel northPanel = new JPanel();
    private final JPanel southPanel = new JPanel();
    private final JPanel eastPanel = new JPanel();
    private final JPanel westPanel = new JPanel();
    private final JPanel mainPanel = new JPanel();
    private final JButton searchButton = new JButton("Search");
    private final JButton addButton = new JButton("Add");
    private final JButton updateButton = new JButton("Update");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton homeButton = new JButton("Home");

    private String[] systemArray;
    private String[] manufacturerArray;

    private String system = "none";
    private String manufacturer = "none";

    public ManagingFrame()  {
        managingFrame.setVisible(true);
        managingFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        managingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managingFrame.setResizable(false);
        managingFrame.setBackground(Color.GRAY);
        managingFrame.setLayout(new BorderLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        northPanel.setPreferredSize(new Dimension(100,(int)(height*0.25)));
        southPanel.setPreferredSize(new Dimension(100,(int)(height*0.25)));
        westPanel.setPreferredSize(new Dimension((int)(height*0.55),100 ));
        eastPanel.setPreferredSize(new Dimension((int)(height*0.55),100 ));
        mainPanel.setPreferredSize(new Dimension(100,100));

        managingFrame.add(northPanel, BorderLayout.NORTH);
        managingFrame.add(southPanel, BorderLayout.SOUTH);
        managingFrame.add(eastPanel, BorderLayout.EAST);
        managingFrame.add(westPanel, BorderLayout.WEST);
        managingFrame.add(mainPanel, BorderLayout.CENTER);
        //north panel
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
        JLabel titleLabel = new JLabel("Managing Models");
        titleLabel.setFont(new Font("", Font.BOLD, 50 ));
        northPanel.add(titleLabel);
        // search panel
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        mainPanel.setLayout(new GridLayout(4,1,10,5));

        //1.search panel
        JPanel searchPanel = new JPanel(); // contains searchFieldPanel, searchButtonPanel, and filterPanel
        searchPanel.setLayout(new GridLayout(1,4,3,3));

        JPanel searchFieldPanel = new JPanel();
        searchFieldPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 50));
        searchField.setPreferredSize(new Dimension(300,50));

        searchFieldPanel.add(searchField);
        JPanel searchButtonPanel = new JPanel();
        searchButton.setPreferredSize(new Dimension(100,50));
        searchButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 50));
        searchButtonPanel.add(searchButton);
        // search panel : filter sub panel
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

        // combo box component (drop-down widget)
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
        // end of filter sub pane
        searchPanel.add(searchFieldPanel);
        searchPanel.add(searchButtonPanel);
        searchPanel.add(filterPanel);

        //2.Add, update, and delete panel
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 50, 50));
        addPanel.add(addButton);

        JPanel CUDPanel = new JPanel();
        CUDPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 50, 50));
        addButton.setPreferredSize(new Dimension(100,50));
        JPanel updatePanel = new JPanel();
        JLabel updateLabel = new JLabel("Enter model id: ");
        updateField.setPreferredSize(new Dimension(100,50));
        updateButton.setPreferredSize(new Dimension(100,50));
        updatePanel.add(updateLabel);
        updatePanel.add(updateField);
        updatePanel.add(updateButton);
        JPanel deletePanel = new JPanel();
        JLabel deleteLabel = new JLabel("Enter model id: ");
        deleteButton.setPreferredSize(new Dimension(100,50));
        deleteField.setPreferredSize(new Dimension(100,50));
        deletePanel.add(deleteLabel);
        deletePanel.add(deleteField);
        deletePanel.add(deleteButton);

        CUDPanel.add(addButton);
        CUDPanel.add(updatePanel);
        CUDPanel.add(deletePanel);

        //3. Checkout status panel
        JPanel checkOutStatusPanel = new JPanel();
        JButton checkOutStatusButton = new JButton("check out status");
        checkOutStatusButton.setPreferredSize(new Dimension(150,50));
        checkOutStatusPanel.add(checkOutStatusButton);

        //4. Home panel
        JPanel homePanel = new JPanel();
        homeButton.setPreferredSize(new Dimension(100,50));
        homePanel.add(homeButton);

        // adding components to main Panel
        mainPanel.add(searchPanel);
        // business constraint : only lab manager can add, update, and delete models. Lab technician will not see these buttons.
        if(LoginSession.position.equals("Lab Manager")){
            mainPanel.add(CUDPanel);
        }
        mainPanel.add(checkOutStatusPanel);
        mainPanel.add(homePanel);
        // adding event listener
        searchEventListener();
        addEventListener(addButton);
        updateEventListener(updateButton);
        homeEventListener(homeButton);
        deleteEventListener(deleteButton);
        checkOutStatusEventListener(checkOutStatusButton);
    }


    // check out status button event listener
    private void checkOutStatusEventListener(JButton checkOutStatusButton) {
        checkOutStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckOutStatus();
            }
        });
    }

    // Home button event listener
    private void homeEventListener(JButton homeButton){
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                managingFrame.dispose();
                new MainFrame(); // go back to main frame
            }
        });
    }

    // add button event listener
    private void addEventListener(JButton addButton){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddFormFrame();
            }
        });
    }

    // delete button event listener
    private void deleteEventListener(JButton deleteButton){
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // first check if a given model id is valid
                String userInputModelId = deleteField.getText();
                boolean exist = checkIfGivenModelIdExists(userInputModelId);
                if(exist){
                    DatabaseUtility.deleteModel(userInputModelId);
                    JOptionPane.showMessageDialog(null," DELETED!!! ");
                    managingFrame.dispose();
                    new MainFrame();
                }
            }
        });
    }

    // update button event listener
    private void updateEventListener(JButton updateButton){
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // first check if a given model id is valid
                String userInputModelId = updateField.getText();
                boolean exist = checkIfGivenModelIdExists(userInputModelId);
                if(exist){
                    new UpdateFormFrame(userInputModelId);
                }
            }
        });
    }

    private boolean checkIfGivenModelIdExists(String userInputModelId) {
        boolean exist = Validation.doesModelexist(userInputModelId);
        if (exist) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "You entered a wrong model ID");
            return false;
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

                //setting search result table and frame
                JFrame searchResultframe = new JFrame();
                managingFrame.setEnabled(false);
                searchResultframe.setUndecorated(true);
                searchResultframe.setSize(800,500);
                DefaultTableModel defaultTable = new DefaultTableModel();
                defaultTable.setDataVector( resultListArray,
                        new Object[]{"Model_ID", "Model_Name", "Quantity", "availableQuantity","Manufacturer", "Location","system_name","average stars"});
                JTable table = new JTable(defaultTable);
                JScrollPane scroll = new JScrollPane(table);
                table.setPreferredScrollableViewportSize(table.getPreferredSize());
                table.getColumnModel().getColumn(0).setPreferredWidth(50);
                searchResultframe.add(scroll);
                searchResultframe.setLocationRelativeTo(null);
                searchResultframe.setVisible(true);

                JButton closeButton = new JButton("close");
                closeButton.setPreferredSize(new Dimension(100,100));
                searchResultframe.add(closeButton, BorderLayout.NORTH);
                closeEventListener(closeButton, searchResultframe);
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
                resultList.get(index).add(resultSet.getString("avgStars"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Object[][] resultListArray = new Object[resultList.size()][8];
        for(int i=0; i<resultList.size(); i++){
            for(int j=0; j<8; j++){
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
        //String sql="SELECT * FROM MODEL";
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
                resultList.get(index).add(resultSet.getString("avgStars"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        userInput = userInput.replaceAll("[!@#$%^&*()><:+_]",""); // remove all special characters
        StringBuffer userInputBuffer = new StringBuffer(userInput);

        int index = 0; // index for newResultList
        boolean foundMatches = false;
        Object[][] resultListArray = new Object[newResultList.size()][8];
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
                    newResultList.get(index).add(resultList.get(i).get(7));
                    index++;
                    foundMatches = true;
                }
            }
            if(foundMatches){
                //done finding matches. I will return object[][] object instead of ArrayList[][] object.
                resultListArray = new Object[newResultList.size()][8];
                for(int i=0; i<newResultList.size(); i++){
                    for(int j=0; j<8; j++){
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

    // close button event listner
    private void closeEventListener(JButton closeButton, JFrame searchResultframe) {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                managingFrame.setEnabled(true);
                searchResultframe.setVisible(false);
            }
        });
    }
}// end of ManagingFrame
