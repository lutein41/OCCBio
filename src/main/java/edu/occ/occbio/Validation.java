package edu.occ.occbio;

import edu.occ.occbio.database.DatabaseUtility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Validation {

    public static boolean isThereDuplicateUserId(String userId){
        /**
         * checks if a given userID exists in the database
         */
        ResultSet resultSet = DatabaseUtility.getUserId(userId);
        try{
            if(resultSet.next()){
                if(resultSet.getString("UserId").equals(userId)){
                    return true;
                }
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return false;
    }

    public static boolean validateUserIdDigits(String userId){
        /**
         * checks if a given userId has 4 digits.
         */
        if(userId.length() != 4){
            return false;
        }
        return true;
    }

    public static boolean isThisModelAvailable(String modelId){
        /**
         * check if a given model is available.
         */
        ResultSet resultSet = DatabaseUtility.getModelAvailabilityQuntity(modelId);
        try{
            if(resultSet.next()) {
                String availableQuantity = resultSet.getString("AvailableQuantity");
                if (availableQuantity.equals("0")) {
                    return false;
                }
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return true;
    }

    public static boolean doesModelexist(String modelId){
        /**
         * check if a given model exists in the database.
         */
        ResultSet resultSet = DatabaseUtility.getModelId(modelId);
        try {
            if (!resultSet.next()) {
                return false;
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return true;
    }

    public static boolean isStudnetRegistered(String userId){
        /**
         * check if a given user id is registered
         */
        ResultSet resultSet = DatabaseUtility.getUserId(userId);
        try {
            if (resultSet.next()) {
                return true;
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return false;
    }

    public static boolean isStudentSuspended(String userId){
        /**
         * check if a given user id is suspended.
         */
        ResultSet resultSet = DatabaseUtility.getuserSuspendedDate(userId);
        try {
            if(resultSet.next()){
                if(resultSet.getString("SuspendedDate") != null) {
                    return true;
                }
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return false;
    }

    public static boolean didStudentRentAlready(String userId){
        /**
         * check if a given user id already rented a model
         */
        ResultSet resultSet = DatabaseUtility.getUsersWhoRented(userId);
        try {
            while(resultSet.next()){
                if(resultSet.getString("UserId").equals(userId)){ // student already checked out a model
                    return true;
                }
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return false;
    }

    public static boolean didStudentLendThisModel(String userId, String modelId){
        /**
         * check if a given user rented a given model.
         */
        ResultSet resultSet = DatabaseUtility.getUserIdandModelIdFromLend();
        boolean rented = false;
        try{
            while(resultSet.next()){
                if(resultSet.getString("UserId").equals(userId) &&
                        resultSet.getString("ModelID").equals(modelId)){
                    rented = true;
                }
            }
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return rented;
    }

    public static boolean isThisLateReturn(String userId){
        /**
         * check if a given user id is returning a model late
         */
        boolean late = false;
        try{
            ResultSet resultSet = DatabaseUtility.getExpireDateFromLend(userId);
            String expire ="";
            if(resultSet.next()){
                expire = resultSet.getString("expire");
            }
            Calendar timeNow = Calendar.getInstance();
            Calendar returnTime = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try{
                returnTime.setTime(dateFormat.parse(expire));
            }catch(ParseException ex){
                System.out.println(ex.getMessage());
            }
            if(returnTime.getTimeInMillis() < timeNow.getTimeInMillis()){
                late = true;
            }
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return late;
    }

}// end of validation class
