package com.inventory.database;

import java.sql.*;

public class ConnectionFactory implements DatabaseConstants {

    private static ConnectionFactory instance = new ConnectionFactory();
    private Connection conn;
    private static final String GET_USER_CATEGORY = "SELECT "+USERS_CATEGORY+" FROM "+USERS_TABLE
            +" WHERE "+USERS_USERNAME+"=? AND "+USERS_PASSWORD+"=?";

    private  ConnectionFactory() {
    }

    public static ConnectionFactory getInstance() {
        return instance;
    }

    public Connection open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return conn;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to Database: "+e.getMessage());
            return null;
        }
    }

    public boolean close() {
        try {
            if(conn!=null)
                conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not close database connection: "+e.getMessage());
            return false;
        }
    }

    public String getUserCategory(String username, String password) {
        try {
            PreparedStatement getUserCategory = conn.prepareStatement(GET_USER_CATEGORY);
            getUserCategory.setString(1, username);
            getUserCategory.setString(2, password);

            ResultSet category = getUserCategory.executeQuery();

            if (category.next())
                return category.getString(1);
            else
                return null;
        } catch (SQLException e) {
            System.out.println("Couldn't fetch User details: "+e.getMessage());
            return null;
        }
    }
}
