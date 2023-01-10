package com.inventory.DAO;

import com.inventory.DTO.ProductsDTO;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsDAO implements DatabaseConstants {
    private final Connection conn;

    public ProductsDAO(Connection conn) {
        this.conn = conn;
    }

    public int getProductId(String prodName, String prodBrand, int sId) {
        // Returns the pId if product exists, else returns -1
        final String QUERY_PRODUCT = "SELECT " + PRODUCTS_ID + " FROM " + PRODUCTS_TABLE + " WHERE " + PRODUCTS_NAME + "=? AND "
                + PRODUCTS_BRAND + "=? AND " + PRODUCTS_S_ID + "=?";
        try {
            PreparedStatement queryProduct = conn.prepareStatement(QUERY_PRODUCT);
            queryProduct.setString(1, prodName);
            queryProduct.setString(2, prodBrand);
            queryProduct.setInt(3, sId);

            ResultSet queryProductResult = queryProduct.executeQuery();
            if (!queryProductResult.next())
                return -1;
            return queryProductResult.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public boolean productExists(int pId) {
        // Returns true if the pId exists, else returns false
        final String QUERY_PRODUCT = "SELECT " + PRODUCTS_ID + " FROM " + PRODUCTS_TABLE + " WHERE " + PRODUCTS_ID + "=?";
        try {
            PreparedStatement queryProduct = conn.prepareStatement(QUERY_PRODUCT);
            queryProduct.setInt(1, pId);
            ResultSet result = queryProduct.executeQuery();
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean productExists(String prodName, String prodBrand, int sId) {
        // Returns true if the pId exists, else returns false
        final String QUERY_PRODUCT = "SELECT " + PRODUCTS_ID + " FROM " + PRODUCTS_TABLE + " WHERE " + PRODUCTS_NAME + "=? AND "
                + PRODUCTS_BRAND + "=? AND " + PRODUCTS_S_ID + "=?";
        try {
            PreparedStatement queryProduct = conn.prepareStatement(QUERY_PRODUCT);
            queryProduct.setString(1, prodName);
            queryProduct.setString(2, prodBrand);
            queryProduct.setInt(3, sId);
            ResultSet result = queryProduct.executeQuery();
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public int addProduct(ProductsDTO product) throws SQLException {
        // Add a new Product and return the pId
        // Returns an Exception if Supplier does not Exist or Product already Exists
        final String ADD_TO_PRODUCT = "INSERT INTO " + PRODUCTS_TABLE + "(" + PRODUCTS_NAME + "," + PRODUCTS_BRAND + ","
                + PRODUCTS_COST_PRICE + "," + PRODUCTS_SELLING_PRICE + "," + PRODUCTS_CURRENT_STOCK + ","
                + PRODUCTS_DATE_ADDED + "," + PRODUCTS_S_ID + ") VALUES (?,?,?,?,?,?,?)";

        int sId = product.getsId();

        // Checking for foreign key violations
        if (!new SuppliersDAO(conn).supplierExists(sId))
            throw new SQLException("No Such Supplier Exists");

        // Return pId if product already exists
        if (productExists(product.getProductName(), product.getProductBrand(), product.getsId()))
            return getProductId(product.getProductName(), product.getProductBrand(), product.getsId());

        // Insert values to product if not already exists and sId exists
        PreparedStatement addProduct = conn.prepareStatement(ADD_TO_PRODUCT);
        addProduct.setString(1, product.getProductName());
        addProduct.setString(2, product.getProductBrand());
        addProduct.setDouble(3, product.getCostPrice());
        addProduct.setDouble(4, product.getSellingPrice());
        addProduct.setInt(5, product.getCurrentStock());
        addProduct.setString(6, product.getDateAdded());
        addProduct.setInt(7, sId);

        int affectedRows = addProduct.executeUpdate();
        if (affectedRows != 1)
            throw new SQLException("More than one row affected");
        return addProduct.getGeneratedKeys().getInt(1);
    }

    public boolean updateProductDetails(ProductsDTO product) {
        // updates all Product details including
        // Does not modify Stock
        final String UPDATE_PRODUCT = "UPDATE " + PRODUCTS_TABLE + " SET " + PRODUCTS_COST_PRICE + "=?," + PRODUCTS_SELLING_PRICE + "=?,"
                + PRODUCTS_DATE_ADDED + "=?," + PRODUCTS_S_ID + "=? WHERE " + PRODUCTS_ID + "=?";
        try {

            // Check for existence of product
            if (!productExists(product.getProductName(), product.getProductBrand(), product.getsId()))
                throw new SQLException("No such product exists");

            // Check for foreign key violations
            if (!new SuppliersDAO(conn).supplierExists(product.getsId()))
                throw new SQLException("The Supplier does not Exist");

            PreparedStatement updateProduct = conn.prepareStatement(UPDATE_PRODUCT);
            updateProduct.setDouble(1, product.getCostPrice());
            updateProduct.setDouble(2, product.getSellingPrice());
            updateProduct.setString(3, product.getDateAdded());
            updateProduct.setInt(4, product.getsId());
            updateProduct.setInt(5, product.getProductId());

            int affectedRows = updateProduct.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("More than one row affected");
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't Update Product: " + e.getMessage());
            return false;
        }
    }

    public boolean addStock(int pId, int newStock, String date) {
        // adds the newStock to the currentStock if pId exists
        final String QUERY_STOCK = "SELECT " + PRODUCTS_CURRENT_STOCK + " FROM " + PRODUCTS_TABLE + " WHERE "
                + PRODUCTS_ID + "=?";
        final String UPDATE_STOCK = "UPDATE " + PRODUCTS_TABLE + " SET " + PRODUCTS_CURRENT_STOCK + "=?," + PRODUCTS_DATE_ADDED
                + "=? WHERE " + PRODUCTS_ID + "=?";
        try {
            if (!productExists(pId))
                throw new SQLException("Product Does not Exist");

            PreparedStatement queryStock = conn.prepareStatement(QUERY_STOCK);
            queryStock.setInt(1, pId);

            int currentStock = queryStock.executeQuery().getInt(1);
            currentStock += newStock;

            PreparedStatement updateStock = conn.prepareStatement(UPDATE_STOCK);
            updateStock.setInt(1, currentStock);
            updateStock.setString(2, date);
            updateStock.setInt(3, pId);

            int affectedRows = updateStock.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("More than one row affected");
            return true;
        } catch (SQLException e) {
            System.out.println("Could not add Stock: " + e.getMessage());
            return false;
        }
    }

    public boolean removeStock(int pId, int oldStock) {
        // subtracts the oldStock from currentStock if pId Exists
        final String QUERY_STOCK = "SELECT " + PRODUCTS_CURRENT_STOCK + " FROM " + PRODUCTS_TABLE + " WHERE "
                + PRODUCTS_ID + "=?";
        final String UPDATE_STOCK = "UPDATE " + PRODUCTS_TABLE + " SET " + PRODUCTS_CURRENT_STOCK + "=? WHERE " + PRODUCTS_ID + "=?";
        try {
            if (!productExists(pId))
                throw new SQLException("Product Does not Exist");

            if (!checkStock(pId, oldStock))
                throw new SQLException("Not enough Stock");

            PreparedStatement queryStock = conn.prepareStatement(QUERY_STOCK);
            queryStock.setInt(1, pId);

            int currentStock = queryStock.executeQuery().getInt(1);
            currentStock -= oldStock;

            PreparedStatement updateStock = conn.prepareStatement(UPDATE_STOCK);
            updateStock.setInt(1, currentStock);
            updateStock.setInt(2, pId);

            int affectedRows = updateStock.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("More than one row affected");
            return true;
        } catch (SQLException e) {
            System.out.println("Could not remove stock: " + e.getMessage());
            return false;
        }
    }

    public boolean checkStock(int pId, int quantity) throws SQLException {
        final String QUERY_STOCK = "SELECT " + PRODUCTS_CURRENT_STOCK + " FROM " + PRODUCTS_TABLE + " WHERE "
                + PRODUCTS_ID + "=?";
        PreparedStatement queryStock = conn.prepareStatement(QUERY_STOCK);
        queryStock.setInt(1, pId);

        int currentStock = queryStock.executeQuery().getInt(1);

        return currentStock >= quantity;
    }

    public ResultSet getAllProductDetails() {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + PRODUCTS_TABLE + " ORDER BY " + PRODUCTS_NAME + " COLLATE NOCASE");
            if (!results.executeQuery().next())
                throw new SQLException("No Such Product Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch Product Details: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getProductDetails(String productName, String productBrand, int sId) {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + PRODUCTS_TABLE + " WHERE " + PRODUCTS_ID + "=?");
            results.setInt(1, getProductId(productName, productBrand, sId));
            if (!results.executeQuery().next())
                throw new SQLException("No Such Product Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch Product Details: " + e.getMessage());
            return null;
        }
    }
}
