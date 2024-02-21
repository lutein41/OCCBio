package edu.occ.occbio;

import edu.occ.occbio.checkout.CheckoutFrame;
import edu.occ.occbio.login.LoginFrame;
import edu.occ.occbio.login.LoginSession;
import edu.occ.occbio.manager.ManagingFrame;
import edu.occ.occbio.register.RegisterFrame;
import edu.occ.occbio.returning.ReturnFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private JFrame mainFrame = new JFrame();

    public MainFrame(){
        // This constructor creates and set up UI components.
        mainFrame.setVisible(true);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setBackground(Color.GRAY);
        mainFrame.setLayout(new BorderLayout());

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
        northPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 25, 10));
        JLabel welcomeLabel = new JLabel("Welcome " + LoginSession.firstName + " " + LoginSession.lastName + "." +
                " You are " + LoginSession.position);
        welcomeLabel.setFont(new Font("", Font.PLAIN, 25 ));
        JButton logOutButton = new JButton("Log Out");
        logOutButton.setFont(new Font("",Font.BOLD,25));
        northPanel.add(welcomeLabel);
        northPanel.add(logOutButton);

        mainFrame.add(northPanel, BorderLayout.NORTH);
        mainFrame.add(southPanel, BorderLayout.SOUTH);
        mainFrame.add(eastPanel, BorderLayout.EAST);
        mainFrame.add(westPanel, BorderLayout.WEST);
        mainFrame.add(mainPanel, BorderLayout.CENTER);

        //Main Panel -> managingPanel
        JPanel managingPanel = new JPanel();
        managingPanel.setBackground(Color.white);
        managingPanel.setLayout(new BorderLayout(1,1));
        JButton managingButton = new JButton("Managing");
        managingButton.setBorder(BorderFactory.createLineBorder(Color.blue, 2, true));
        managingButton.setFont(new Font("",Font.BOLD,30));
        managingPanel.add(managingButton);
        //Main Panel -> checkoutPanel
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setBackground(Color.white);
        checkoutPanel.setLayout(new BorderLayout(1,1));
        JButton checkoutButton = new JButton("Check Out");
        checkoutButton.setFont(new Font("",Font.BOLD,30));
        checkoutButton.setBorder(BorderFactory.createLineBorder(Color.blue, 2, true));
        checkoutPanel.add(checkoutButton);
        //Main Panel -> ReturnPanel
        JPanel returnPanel = new JPanel();
        returnPanel.setBackground(Color.white);
        returnPanel.setLayout(new BorderLayout(1,1));
        JButton returnButton = new JButton("Return");
        returnButton.setFont(new Font("",Font.BOLD,30));
        returnButton.setBorder(BorderFactory.createLineBorder(Color.blue, 2, true));
        returnPanel.add(returnButton);
        //Main Panel -> Register
        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(Color.white);
        registerPanel.setLayout(new BorderLayout(1,1));
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("",Font.BOLD,30));
        registerButton.setBorder(BorderFactory.createLineBorder(Color.blue, 2, true));
        registerPanel.add(registerButton);

        //Main Panel
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        mainPanel.setLayout(new GridLayout(4,1,10,5));
        mainPanel.add(managingPanel);
        mainPanel.add(checkoutPanel);
        mainPanel.add(returnPanel);
        mainPanel.add(registerPanel);

        // Calling event listeners
        logOutButtonEventListener(logOutButton);
        checkoutEventListener(checkoutButton);
        returnEventListener(returnButton);
        managingEventListener(managingButton);
        registerEventListener(registerButton);

    }

    private void logOutButtonEventListener(JButton logOutButton){
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginSession.logIn = false;
                LoginSession.lastName = "";
                LoginSession.firstName = "";
                LoginSession.position = "";
                mainFrame.dispose();
                new LoginFrame();
            }
        });
    }

    private void checkoutEventListener(JButton checkoutButton){
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new CheckoutFrame();
            }
        });
    }

    private void returnEventListener(JButton returnButton){
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new ReturnFrame();
            }
        });
    }

    private void managingEventListener(JButton managingButton){
        managingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new ManagingFrame();
            }
        });
    }

    private void registerEventListener(JButton registerButton){
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new RegisterFrame();
            }
        });
    }

}// end of MainFrame class
