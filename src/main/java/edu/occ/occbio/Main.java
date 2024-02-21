package edu.occ.occbio;

import edu.occ.occbio.database.DatabaseUtility;
import edu.occ.occbio.login.LoginFrame;

public class Main {
    public static void main(String[] args) {

        new LoginFrame();
        DatabaseUtility.connectToDatabase(); // connect to MySQL server

    }
}