package com.inventory.DAO;

import com.inventory.DTO.CustomerDTO;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TODO: Separate function for Phone Number update
public class CustomerDAO implements DatabaseConstants {
    private final Connection conn;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    public int getCustomerId(String customerName, String customerPhone) {
        // Returns Customer ID if exists
        final String QUERY_CUSTOMER = "SELECT " + CUSTOMER_ID + " FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_NAME + "=?"
                + " AND " + CUSTOMER_PHONE + "=?";
        try {
            PreparedStatement queryCustomer = conn.prepareStatement(QUERY_CUSTOMER);
            queryCustomer.setString(1, customerName);
            queryCustomer.setString(2, customerPhone);

            ResultSet customerResult = queryCustomer.executeQuery();
            if (customerResult.next())
                return customerResult.getInt(1);
            else
                return -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public boolean customerExists(String customerName, String customerPhone) {
        // Returns true if cId exists, else returns false
        String QUERY_CUSTOMER = "SELECT " + CUSTOMER_ID + " FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + "=?";
        try {
            PreparedStatement queryCustomer = conn.prepareStatement(QUERY_CUSTOMER);
            int cId = getCustomerId(customerName, customerPhone);
            queryCustomer.setInt(1, cId);
            ResultSet result = queryCustomer.executeQuery();
            return result.next();
        } catch (SQLException e) {
            System.out.println("Customer does not exist: " + e.getMessage());
            return false;
        }
    }

    public boolean customerExists(int cId) {
        // Returns true if cId exists, else returns false
        String QUERY_CUSTOMER = "SELECT " + CUSTOMER_ID + " FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + "=?";
        try {
            PreparedStatement queryCustomer = conn.prepareStatement(QUERY_CUSTOMER);
            queryCustomer.setInt(1, cId);
            ResultSet result = queryCustomer.executeQuery();
            return result.next();
        } catch (SQLException e) {
            System.out.println("Customer does not exist: " + e.getMessage());
            return false;
        }
    }

    public int addCustomer(CustomerDTO customer) throws SQLException {
        // Check if Customer exists. If exists, returns true, else adds a new customer, sets the DTO.Id and returns true

        final String ADD_TO_CUSTOMER = "INSERT INTO " + CUSTOMER_TABLE + "(" + CUSTOMER_NAME + "," + CUSTOMER_PHONE
                + "," + CUSTOMER_LOCATION + ")" + " VALUES (?,?,?)";

        if (customerExists(customer.getCustomerName(), customer.getCustomerPhone()))  // return custId if already exists
            return getCustomerId(customer.getCustomerName(), customer.getCustomerPhone());


        // Add new customer if not exists
        PreparedStatement addCustomer = conn.prepareStatement(ADD_TO_CUSTOMER);
        addCustomer.setString(1, customer.getCustomerName());
        addCustomer.setString(2, customer.getCustomerPhone());
        addCustomer.setString(3, customer.getCustomerLocation());

        int affectedRows = addCustomer.executeUpdate();
        if (affectedRows != 1)
            throw new SQLException("Multiple Rows affected");
        return addCustomer.getGeneratedKeys().getInt(1);
    }

    public boolean updateCustomerDetails(CustomerDTO customerDTO) {
        // Set ID with old details, then update with new Details
        // Update the Phone, Location of a Customer
        // Returns true if the details could be updated, else returns false
        final String UPDATE_CUSTOMER = "UPDATE " + CUSTOMER_TABLE + " SET " + CUSTOMER_PHONE + "=?," + CUSTOMER_LOCATION
                + "=? WHERE " + CUSTOMER_ID + "=?";
        try {
            if (!customerExists(customerDTO.getCustomerId()))
                throw new SQLException("No such Customer Exists");  // No updates if Cust does not exist

            // Update Customer details
            PreparedStatement updateCustomer = conn.prepareStatement(UPDATE_CUSTOMER);
            updateCustomer.setString(1, customerDTO.getCustomerPhone());
            updateCustomer.setString(2, customerDTO.getCustomerLocation());
            updateCustomer.setInt(3, customerDTO.getCustomerId());

            int affectedRows = updateCustomer.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("More than one row affected");
            return true;
        } catch (SQLException e) {
            System.out.println("Could not update Customer Details: " + e.getMessage());
            return false;
        }
    }

    public ResultSet getAllCustomerDetails() {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + CUSTOMER_TABLE);
            if (!results.executeQuery().next())
                throw new SQLException("No Customer Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch Customer Details: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getCustomerDetails(String customerName, String customerPhone) {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + "=?");
            results.setInt(1, getCustomerId(customerName, customerPhone));
            if (!results.executeQuery().next())
                throw new SQLException("No Such Product Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch Product Details: " + e.getMessage());
            return null;
        }
    }
}
