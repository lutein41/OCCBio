package edu.occ.occbio.database;

import java.sql.*;

public class DatabaseUtility {

    private static final String URL = "jdbc:mysql://localhost:3306/OCCBio";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static Connection connection;

    public static void connectToDatabase(){
        try{
            Class.forName(DatabaseUtility.DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database: connected");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static ResultSet getWhenModelWillBeAvailable(String modelId) {
        String sql = "SELECT FirstName, LastName, expire FROM LEND INNER JOIN MODEL ON LEND.ModelId = MODEL.ModelId INNER JOIN USER ON LEND.UserId = USER.UserId WHERE LEND.ModelId = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, modelId);
            return statement.executeQuery();
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String databaseCheckoutUpdate(String userId, String modelId){
        String insertSql = "INSERT INTO LEND VALUES (null, NOW(), ADDTIME(NOW(), '2:0:0'),?,?)";
        String updateSql = "UPDATE MODEL SET AvailableQuantity=AvailableQuantity-1 WHERE ModelId=?";
        String querySql = "SELECT expire FROM LEND WHERE UserId = ?";
        try{
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            PreparedStatement statement = connection.prepareStatement(insertSql);
            PreparedStatement queryStatement = connection.prepareStatement(querySql);
            updateStatement.setString(1,modelId);
            statement.setInt(1, Integer.parseInt(userId));
            statement.setInt(2, Integer.parseInt(modelId));
            queryStatement.setString(1, userId);
            updateStatement.executeUpdate();
            statement.executeUpdate();
            ResultSet resultSet = queryStatement.executeQuery();
            while(resultSet.next()){
                return resultSet.getString("expire");
            }
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public static void incrementNumberOfLateReturn(String userId){
        String sql = "UPDATE USER SET NumberOfLateReturn=NumberOfLateReturn+1 WHERE UserId=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userId);
            statement.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public static void deleteModel(String modelId){
        String updateSql = "DELETE FROM MODEL WHERE ModelId = ?";
        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, modelId);
            updateStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void deleteLend(String userId){
        String updateSql = "DELETE FROM LEND WHERE UserId = ?";
        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, userId);
            updateStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static String getNumberOfLateReturn(String userId){
        String sql = "SELECT NumberOfLateReturn FROM USER WHERE UserId=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("NumberOfLateReturn");
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return "0";
    }

    public static void insertSuspendedDate(String userId){
        String sql = "UPDATE USER SET SuspendedDate=NOW() WHERE UserId=? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userId);
            statement.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public static void updateModelInformation(String modelName, String quantity, String manufacturer, String location, String system, String modelId){
        String sql = "UPDATE MODEL SET ModelName=?, Quantity=?," +
                "AvailableQuantity=?, Manufacturer=?," +
                "Location=?, SystemName=? WHERE ModelId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,modelName);
            statement.setString(2,quantity);
            statement.setInt(3,(Integer.parseInt(quantity)));
            statement.setString(4,manufacturer);
            statement.setString(5,location);
            statement.setString(6,system);
            statement.setString(7,modelId);
            statement.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public static ResultSet getAllSystemNames(){
        String sql = "SELECT * FROM BIOSYSTEM";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public static void insertSystemName(String systemName){
        String sql = "INSERT INTO BIOSYSTEM VALUES (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,systemName);
            statement.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public static void insertNewModel(String modelName, String quantity, String manufacturer, String location, String systemName){
        String sql = "INSERT INTO MODEL VALUES(null,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,modelName);
            statement.setString(2,quantity);
            statement.setString(3,quantity);
            statement.setString(4,manufacturer);
            statement.setString(5,location);
            statement.setString(6,systemName);
            statement.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public static ResultSet getUserInformation(String userId, String userPassword){
        String sql = "SELECT UserId,Password,FirstName,LastName,Position FROM USER NATURAL JOIN ADMIN_USER";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery(sql);
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public static ResultSet getAllUserId(String userId){
        String sql = "SELECT COUNT(*) AS count FROM USER WHERE UserId ="+userId ;
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery(sql);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void insertUserData(int userId, String firstName, String lastName){
        String sql = "INSERT INTO USER VALUES (?,?,?,null,0)" ;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,userId);
            statement.setString(2,firstName);
            statement.setString(3,lastName);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void insertRating(String userId, String modelId, String stars, String comments){
        String sql = "INSERT INTO RATING VALUES(null,?,?,?,?,NOW())";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userId);
            statement.setString(2,modelId);
            statement.setString(3,stars);
            statement.setString(4,comments);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void incrementAvailableQuantity(String modelId){
        String sql = "UPDATE MODEL SET AvailableQuantity=AvailableQuantity+1 WHERE ModelId=?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,modelId);
            statement.executeUpdate();
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public static ResultSet searchBy(String key, String system, String manufacturer){
        String sql="";
        if(key.equals("system")){
            sql =  "SELECT *\n" +
                    "FROM MODEL NATURAL LEFT OUTER JOIN (SELECT Modelname, AVG(NumberOfStars) AS avgStars\n" +
                    "FROM MODEL NATURAL JOIN RATING\n" +
                    "WHERE SystemName = ?\n" +
                    "GROUP BY ModelName) AS star\n" +
                    "WHERE SystemName = ?";
        }else if(key.equals("manufacturer")){
            sql =   "SELECT *\n" +
                    "FROM MODEL NATURAL LEFT OUTER JOIN (SELECT Modelname, AVG(NumberOfStars) AS avgStars\n" +
                    "FROM MODEL NATURAL JOIN RATING\n" +
                    "WHERE manufacturer = ?\n" +
                    "GROUP BY ModelName) AS star\n" +
                    "WHERE manufacturer = ?";
        }else{
            sql = "SELECT *\n" +
                    "FROM MODEL NATURAL LEFT OUTER JOIN (SELECT Modelname, AVG(NumberOfStars) AS avgStars\n" +
                    "FROM MODEL NATURAL JOIN RATING\n" +
                    "WHERE SystemName = ? and manufacturer = ?\n" +
                    "GROUP BY ModelName) AS star\n" +
                    "WHERE SystemName = ? and manufacturer = ?";
        }
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            if(key.equals("system") || key.equals("manufacturer")){
                statement.setString(1,(key.equals("system")?system:manufacturer));
                statement.setString(2,(key.equals("system")?system:manufacturer));
            }else{
                statement.setString(1,system);
                statement.setString(2,manufacturer);
                statement.setString(3,system);
                statement.setString(4,manufacturer);
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet searchByString(String key){
        String sql= "SELECT *\n" +
                "FROM MODEL NATURAL LEFT OUTER JOIN (SELECT Modelname, AVG(NumberOfStars) AS avgStars\n" +
                "FROM MODEL NATURAL JOIN RATING\n" +
                "GROUP BY ModelName) AS star\n";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getAllSystemName(){
        String sql = "SELECT DISTINCT SystemName FROM MODEL";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getAllManufacturerName(){
        String sql = "SELECT DISTINCT Manufacturer FROM MODEL";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getModelId(String modelId){
        String sql = "SELECT ModelId FROM MODEL WHERE ModelId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, modelId);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getModelAvailabilityQuntity(String modelId){
        String sql = "SELECT AvailableQuantity FROM MODEL WHERE ModelId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, modelId);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getAllModelInformation(String modelId){
        String sql = "SELECT * FROM MODEL WHERE ModelId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, modelId );
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getUserId(String userId){
        String sql = "SELECT UserId FROM USER WHERE UserId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getuserSuspendedDate(String userId){
        String sql = "SELECT SuspendedDate FROM USER WHERE UserId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getUsersWhoRented(String userId) {
        String Sql = "SELECT UserId FROM LEND";
        try {
            PreparedStatement statement = connection.prepareStatement(Sql);
            return statement.executeQuery();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ResultSet getUserIdandModelIdFromLend(){
        String Sql = "SELECT UserId, ModelID FROM LEND";
        try{
            PreparedStatement statement = connection.prepareStatement(Sql);
            return statement.executeQuery();
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ResultSet getExpireDateFromLend(String userId){
        String sql = "SELECT expire FROM LEND WHERE UserId = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userId);
            return statement.executeQuery();
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


}// end of DatabaseUtility
