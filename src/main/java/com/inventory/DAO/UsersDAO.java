package com.inventory.DAO;

import com.inventory.DTO.UsersDTO;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO implements DatabaseConstants {

    private final Connection conn;

    public UsersDAO(Connection conn) {
        this.conn = conn;
    }

    public String getUsername(String uName, String uPhone) {
        final String QUERY_USER = "SELECT "+USERS_USERNAME+" FROM "+USERS_TABLE+" WHERE "+USERS_NAME+"=? AND "
                +USERS_PHONE+"=?";
        try {
            PreparedStatement queryUser = conn.prepareStatement(QUERY_USER);
            ResultSet resultSet = queryUser.executeQuery();
            if(resultSet == null)
                throw new SQLException("Could not find User");
            return resultSet.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean userExists(int userId) {
        final String USER_EXISTS = "SELECT "+USERS_ID+" FROM "+USERS_TABLE+" WHERE "+USERS_ID+"=?";
        try {
            PreparedStatement userExist = conn.prepareStatement(USER_EXISTS);
            userExist.setInt(1, userId);
            ResultSet result = userExist.executeQuery();
            if(!result.next())
                throw new SQLException("No Such User Found");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int getUserId(String username) {
        final String QUERY_USER = "SELECT " + USERS_ID + " FROM " + USERS_TABLE + " WHERE " + USERS_USERNAME + "=?";
        try {
            PreparedStatement queryUser = conn.prepareStatement(QUERY_USER);
            queryUser.setString(1, username);

            ResultSet userResult = queryUser.executeQuery();
            if (userResult.next())
                return userResult.getInt(1);
            else
                throw new SQLException("Couldn't find User");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public boolean checkPasswordCorrect(String username, String password) {
        final String CHECK_PASSWORD = "SELECT PASSWORD FROM USERS WHERE USERNAME=?";
        try {
            PreparedStatement checkPassword = conn.prepareStatement(CHECK_PASSWORD);
            checkPassword.setString(1, username);
            ResultSet results = checkPassword.executeQuery();
            if(!results.next())
                throw new SQLException("No such User");
            if(!password.equals(results.getString(1)))
                throw new SQLException("Wrong Password");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int addUser(UsersDTO usersDTO) throws SQLException {
        // Add a new user to the Users table - Can be done only by an Admin
        // Can also be used to ger the Id of an already existing User - uses username
        // Check user exists using name and number
        // Check if username is already taken
        final String QUERY_USER = "SELECT " + USERS_ID + " FROM " + USERS_TABLE
                + " WHERE " + USERS_NAME + "=? AND " + USERS_PHONE + "=?";
        final String ADD_TO_USER = "INSERT INTO USERS (" + USERS_USERNAME + "," + USERS_PASSWORD + "," + USERS_NAME
                + "," + USERS_PHONE + "," + USERS_LOCATION + "," + USERS_CATEGORY + ") VALUES (?,?,?,?,?,?)";
        final String QUERY_USERNAME = "SELECT "+USERS_USERNAME+ " FROM "+USERS_TABLE+" WHERE "+USERS_USERNAME+"=?";

        PreparedStatement queryUser = conn.prepareStatement(QUERY_USER);
        queryUser.setString(1, usersDTO.getUserName());
        queryUser.setString(2, usersDTO.getUserPhone());

        // Return User Id if user already exists
        ResultSet userQueryResult = queryUser.executeQuery();
        if (userQueryResult.next())
            return userQueryResult.getInt(1);

        // Add user if new after checking if user type is valid
        if (usersDTO.getCategory().equalsIgnoreCase("Admin") || usersDTO.getCategory().equalsIgnoreCase("Seller")
                || usersDTO.getCategory().equalsIgnoreCase("Analyst")) {
            PreparedStatement queryUsername = conn.prepareStatement(QUERY_USERNAME);
            queryUsername.setString(1, usersDTO.getUsername());
            if(queryUsername.executeQuery().next())
                throw new SQLException("Username already taken");

            PreparedStatement addToUser = conn.prepareStatement(ADD_TO_USER);
            addToUser.setString(1, usersDTO.getUsername());
            addToUser.setString(2, usersDTO.getPassword());
            addToUser.setString(3, usersDTO.getUserName());
            addToUser.setString(4, usersDTO.getUserPhone());
            addToUser.setString(5, usersDTO.getUserLocation());
            addToUser.setString(6, usersDTO.getCategory());

            int affectedRows = addToUser.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("More than one row affected");
            return addToUser.getGeneratedKeys().getInt(1);
        }
        throw new SQLException("Category of User Invalid");
    }

    public boolean updateUserDetails(UsersDTO usersDTO) {
        // Update the Phone, Location, Category (only performed by Admin) of a User: key - username
        // Returns true if the details could be updated, else returns false
        final String UPDATE_USER = "UPDATE " + USERS_TABLE + " SET " + USERS_PHONE + "=?," + USERS_LOCATION + "=?," + USERS_CATEGORY
                + "=? WHERE " + USERS_USERNAME + "=?";
        try {

            PreparedStatement updateUser = conn.prepareStatement(UPDATE_USER);
            updateUser.setString(1, usersDTO.getUserPhone());
            updateUser.setString(2, usersDTO.getUserLocation());
            updateUser.setString(3, usersDTO.getCategory());
            updateUser.setString(4, usersDTO.getUsername());

            int affectedRows = updateUser.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("More than one row affected");
            return true;
        } catch (SQLException e) {
            System.out.println("Could not update User Details: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(String username, String newPassword) {
        // Returns true if password could be updated, else returns false
        final String QUERY_USER = "SELECT " + USERS_ID + " FROM " + USERS_TABLE
                + " WHERE " + USERS_USERNAME + "=?";
        final String UPDATE_USER_PASSWORD = "UPDATE " + USERS_TABLE + " SET " + USERS_PASSWORD + "=?"
                + " WHERE " + USERS_USERNAME + "=?";
        try {
            PreparedStatement queryUser = conn.prepareStatement(QUERY_USER);
            queryUser.setString(1, username);

            ResultSet userQueryResult = queryUser.executeQuery();
            if (!userQueryResult.next())
                throw new SQLException("No such User exists");

            PreparedStatement updatePassword = conn.prepareStatement(UPDATE_USER_PASSWORD);
            updatePassword.setString(1, newPassword);
            updatePassword.setString(2, username);

            int affectedRows = updatePassword.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("Multiple Rows Affected");
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't Update Password: " + e.getMessage());
            return false;
        }
    }

    public ResultSet getAllUserDetails() {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + USERS_TABLE);
            if (!results.executeQuery().next())
                throw new SQLException("No Such Product Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch User Details: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getUserDetails(String userName, String userPhone) {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + USERS_TABLE + " WHERE " +
                    USERS_NAME+ "=? AND "+USERS_PHONE+"=?");
            results.setString(1, userName);
            results.setString(2, userPhone);
            if (!results.executeQuery().next())
                throw new SQLException("No Such User Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch user Details: " + e.getMessage());
            return null;
        }
    }
}
