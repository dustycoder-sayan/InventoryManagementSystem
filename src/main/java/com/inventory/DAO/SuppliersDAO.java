package com.inventory.DAO;

import com.inventory.DTO.SuppliersDTO;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TODO: Separate function for Phone Number update
public class SuppliersDAO implements DatabaseConstants {
    private final Connection conn;

    public SuppliersDAO(Connection conn) {
        this.conn = conn;
    }

    public int getSupplierId(String supplierName, String supplierPhone) {
        // Return the Supplier ID
        final String QUERY_SUPPLIER = "SELECT " + SUPPLIERS_ID + " FROM " + SUPPLIERS_TABLE + " WHERE " + SUPPLIERS_NAME + "=? AND "
                + SUPPLIERS_PHONE + "=?";
        try {
            PreparedStatement querySupplier = conn.prepareStatement(QUERY_SUPPLIER);
            querySupplier.setString(1, supplierName);
            querySupplier.setString(2, supplierPhone);

            ResultSet supplierResult = querySupplier.executeQuery();
            if (supplierResult.next())
                return supplierResult.getInt(1);
            else
                return -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public boolean supplierExists(String supplierName, String supplierPhone) {
        // Returns true if the supplier exists, else returns false
        String QUERY_SUPPLIER = "SELECT " + SUPPLIERS_ID + " FROM " + SUPPLIERS_TABLE + " WHERE " + SUPPLIERS_ID + "=?";
        try {
            PreparedStatement querySupplier = conn.prepareStatement(QUERY_SUPPLIER);
            int sId = getSupplierId(supplierName, supplierPhone);
            querySupplier.setInt(1, sId);
            ResultSet result = querySupplier.executeQuery();
            return result.next();
        } catch (SQLException e) {
            System.out.println("Supplier does not exist: " + e.getMessage());
            return false;
        }
    }

    public boolean supplierExists(int sId) {
        String QUERY_SUPPLIER = "SELECT " + SUPPLIERS_ID + " FROM " + SUPPLIERS_TABLE + " WHERE " + SUPPLIERS_ID + "=?";
        try {
            PreparedStatement querySupplier = conn.prepareStatement(QUERY_SUPPLIER);
            querySupplier.setInt(1, sId);
            ResultSet result = querySupplier.executeQuery();
            return result.next();
        } catch (SQLException e) {
            System.out.println("Supplier does not exist: " + e.getMessage());
            return false;
        }
    }

    public int addSupplier(SuppliersDTO supplier) throws SQLException {
        // Add Supplier if not exists and sets the DTO Id, return true if done else false
        // Can also be used to get the sId of an existing supplier

        final String ADD_TO_SUPPLIER = "INSERT INTO " + SUPPLIERS_TABLE + "(" + SUPPLIERS_NAME + "," + SUPPLIERS_PHONE + ","
                + SUPPLIERS_LOCATION + ") VALUES(?,?,?)";

        if (supplierExists(supplier.getSupplierName(), supplier.getSupplierPhone()))  // Return sId if supplier already exists
            return getSupplierId(supplier.getSupplierName(), supplier.getSupplierPhone());

        // Add supplier if supplier already does not exist
        PreparedStatement addSupplier = conn.prepareStatement(ADD_TO_SUPPLIER);
        addSupplier.setString(1, supplier.getSupplierName());
        addSupplier.setString(2, supplier.getSupplierPhone());
        addSupplier.setString(3, supplier.getSupplierLocation());

        int affectedRows = addSupplier.executeUpdate();
        if (affectedRows != 1)
            throw new SQLException("More than one row affected");
        return addSupplier.getGeneratedKeys().getInt(1);
    }

    public boolean updateSupplierDetails(SuppliersDTO supplier) {
        // Set ID with old details, then update with new Details
        // Update Supplier if exists - returns true, else returns false
        final String UPDATE_SUPPLIER = "UPDATE " + SUPPLIERS_TABLE + " SET " + SUPPLIERS_PHONE + "=?,"
                + SUPPLIERS_LOCATION + "=? WHERE " + SUPPLIERS_ID + "=?";
        try {
            // Do not Update if supplier does not exist
            if (!supplierExists(supplier.getSupplierId()))
                throw new SQLException("No such Supplier Exists");

            // Update if supplier exists
            PreparedStatement updateSupplier = conn.prepareStatement(UPDATE_SUPPLIER);
            updateSupplier.setString(1, supplier.getSupplierPhone());
            updateSupplier.setString(2, supplier.getSupplierLocation());
            updateSupplier.setInt(3, supplier.getSupplierId());

            int affectedRows = updateSupplier.executeUpdate();
            if (affectedRows == 1) {
                supplier.setSupplierId(updateSupplier.getGeneratedKeys().getInt(1));
            } else
                throw new SQLException("More than one row affected");
            return true;
        } catch (SQLException e) {
            System.out.println("Could not update Supplier Details: " + e.getMessage());
            return false;
        }
    }

    public ResultSet getAllSupplierDetails() {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + SUPPLIERS_TABLE + " ORDER BY " + SUPPLIERS_ID + " COLLATE NOCASE");
            if (!results.executeQuery().next())
                throw new SQLException("No Suppliers Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch Supplier Details: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getSupplierDetails(String supplierName, String supplierPhone) {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + SUPPLIERS_TABLE + " WHERE " + SUPPLIERS_ID + "=?");
            results.setInt(1, getSupplierId(supplierName, supplierPhone));
            if (!results.executeQuery().next())
                throw new SQLException("No Such Product Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch Product Details: " + e.getMessage());
            return null;
        }
    }
}
