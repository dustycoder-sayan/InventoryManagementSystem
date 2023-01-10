package com.inventory.DataSource;

import com.inventory.DAO.UsersDAO;
import com.inventory.DTO.AllSellerSales;
import com.inventory.DTO.UsersDTO;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Analyst implements DatabaseConstants {
    private final Connection conn;
    private final String username;

    public Analyst(Connection conn, String username) {
        this.conn=conn;
        this.username = username;
    }

    public boolean updateUserDetails(String newPhone, String location) {
        try {
            UsersDTO user = new UsersDTO();
            user.setUserPhone(newPhone);
            user.setUserLocation(location);
            user.setUsername(username);
            user.setCategory("Analyst");

            boolean updated = new UsersDAO(conn).updateUserDetails(user);
            if(!updated)
                throw new Exception("Error");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setNewPassword(String password) {
        try {
            boolean updated = new UsersDAO(conn).updatePassword(username, password);
            if(!updated)
                throw new Exception("Error");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<AllSellerSales> getAllSellerSales() {
        final String ALL_SELLER_SALES = "SELECT U."+USERS_NAME+", SUM(IP."+ISSUE_QUANTITY+"*P."+PRODUCTS_SELLING_PRICE
                +") FROM "+USERS_TABLE+" U,"
                +ISSUE_PRODUCT_TABLE+" IP,"+PRODUCTS_TABLE+" P  WHERE U."+USERS_ID+"=IP."+ISSUE_U_ID+" AND IP."+PRODUCTS_ID
                +"=P."+PRODUCTS_ID+" GROUP BY IP."+USERS_ID;
        try {
            PreparedStatement allSellerSales = conn.prepareStatement(ALL_SELLER_SALES);
            ResultSet results = allSellerSales.executeQuery();
            List<AllSellerSales> allSellers = new ArrayList<>();
            while(results.next()) {
                AllSellerSales allSeller = new AllSellerSales(results.getString(1), results.getInt(2));
                allSellers.add(allSeller);
            }
            return allSellers;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
