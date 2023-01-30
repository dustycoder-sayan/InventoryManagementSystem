package com.inventory.DAO;

import com.inventory.DTO.IssueProductDTO;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IssueProductDAO implements DatabaseConstants {
    // No need to check for exists since it would be pre-checked in Datasource
    private final Connection conn;

    public IssueProductDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addIssueProduct(IssueProductDTO sale) throws SQLException {
        // Adds the issueProduct details, returns true if added, else returns false
        // Does not modify the Stock
        // If an invalid user, customer or product is found, returns a SQLException
        final String INSERT_ISSUE_PRODUCT = "INSERT INTO " + ISSUE_PRODUCT_TABLE + " VALUES (?,?,?,?,?)";
        final String QUERY_USER = "SELECT " + USERS_ID + " FROM " + USERS_TABLE + " WHERE " + USERS_ID + "=" + sale.getuId();
        final String QUERY_CUSTOMER = "SELECT " + CUSTOMER_ID + " FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + "=" + sale.getcId();
        final String QUERY_PRODUCT = "SELECT " + PRODUCTS_ID + " FROM " + PRODUCTS_TABLE + " WHERE " + PRODUCTS_ID + "=" + sale.getpId();

        // Checking foreign key violations
        ResultSet queryUser = conn.createStatement().executeQuery(QUERY_USER);
        if (!queryUser.next())
            throw new SQLException("No Such User is registered");

        ResultSet queryCustomer = conn.createStatement().executeQuery(QUERY_CUSTOMER);
        if (!queryCustomer.next())
            throw new SQLException("No Such Customer is registered");

        ResultSet queryProduct = conn.createStatement().executeQuery(QUERY_PRODUCT);
        if (!queryProduct.next())
            throw new SQLException("No Such Product available");

        // Add Issue Product if foreign key violations are avoided
        PreparedStatement insertIssueProduct = conn.prepareStatement(INSERT_ISSUE_PRODUCT);
        insertIssueProduct.setInt(1, sale.getuId());
        insertIssueProduct.setInt(2, sale.getcId());
        insertIssueProduct.setInt(3, sale.getpId());
        insertIssueProduct.setInt(4, sale.getQuantity());
        insertIssueProduct.setString(5, sale.getDateSold());

        int affectedRows = insertIssueProduct.executeUpdate();
        if (affectedRows != 1)
            throw new SQLException("More than one row affected");
        return true;
    }
}
