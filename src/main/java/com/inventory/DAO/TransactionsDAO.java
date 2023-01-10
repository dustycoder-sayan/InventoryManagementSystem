package com.inventory.DAO;

import com.inventory.DTO.BuyDTO;
import com.inventory.DTO.ProductsDTO;
import com.inventory.DTO.TransactionsDTO;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransactionsDAO implements DatabaseConstants {
    private Connection conn;

    public TransactionsDAO(Connection conn) {
        this.conn = conn;
    }

    public int getTransactionId(int cId, String dateSold) {
        // Returns the tId if transaction exists, else returns -1
        final String QUERY_TRANSACTION = "SELECT " + TRANSACTIONS_ID + " FROM " + TRANSACTIONS_TABLE + " WHERE " +
                TRANSACTIONS_C_ID + "=? AND " + TRANSACTIONS_DATE + "=?";
        try {
            PreparedStatement queryTransaction = conn.prepareStatement(QUERY_TRANSACTION);
            queryTransaction.setInt(1, cId);
            queryTransaction.setString(2, dateSold);

            ResultSet queryTransactionResult = queryTransaction.executeQuery();
            if (queryTransactionResult.next())
                return queryTransactionResult.getInt(1);
            else
                throw new SQLException("Couldn't find Transaction");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int addTransaction(TransactionsDTO transaction) throws SQLException {
        // Before using, it should be made sure that the customer exists
        // Adds the transaction returns the pId
        final String QUERY_TRANSACTIONS = "SELECT " + TRANSACTIONS_ID + " FROM " + TRANSACTIONS_TABLE + " WHERE " +
                TRANSACTIONS_C_ID + "=? AND " + TRANSACTIONS_DATE + "=?";
        final String ADD_TO_TRANSACTION = "INSERT INTO " + TRANSACTIONS_TABLE + "(" + TRANSACTIONS_C_ID + "," +
                TRANSACTIONS_PROD_AMT + "," + TRANSACTIONS_DISCOUNT + "," + TRANSACTIONS_GST + "," + TRANSACTIONS_METHOD
                + "," + TRANSACTIONS_DATE + "," + TRANSACTIONS_TOTAL_AMT + ") VALUES (?,?,?,?,?,?,?)";
        PreparedStatement queryTransaction = conn.prepareStatement(QUERY_TRANSACTIONS);
        queryTransaction.setInt(1, transaction.getcId());
        queryTransaction.setString(2, transaction.getDate());

        // If transaction already exists
        ResultSet queryTransactionResult = queryTransaction.executeQuery();
        if (queryTransactionResult.next())
            return queryTransactionResult.getInt(1);

        // Check foreign key violations
        int cId = transaction.getcId();
        if (!new CustomerDAO(conn).customerExists(cId))
            throw new SQLException("Customer does not exist!");

        // Insert into Transactions if constraints are satisfied
        PreparedStatement addTransaction = conn.prepareStatement(ADD_TO_TRANSACTION);
        addTransaction.setInt(1, cId);
        addTransaction.setDouble(2, transaction.getProdAmt());
        addTransaction.setDouble(3, transaction.getDiscount());
        addTransaction.setDouble(4, transaction.getGst());
        addTransaction.setString(5, transaction.getMethod());
        addTransaction.setString(6, transaction.getDate());
        addTransaction.setDouble(7, transaction.getTotalAmt());

        int affectedRows = addTransaction.executeUpdate();
        if (affectedRows != 1)
            throw new SQLException("More than one row affected");
        return addTransaction.getGeneratedKeys().getInt(1);
    }

    public boolean updateTransactionMethod(String method, int tId) {
        final String QUERY_TRANSACTIONS = "SELECT " + TRANSACTIONS_ID + " FROM " + TRANSACTIONS_TABLE + " WHERE " +
                TRANSACTIONS_ID + "=?";
        final String UPDATE_TRANSACTIONS = "UPDATE " + TRANSACTIONS_TABLE + " SET " + TRANSACTIONS_METHOD + "=? WHERE " +
                TRANSACTIONS_ID + "=?";
        try {
            PreparedStatement queryTransactions = conn.prepareStatement(QUERY_TRANSACTIONS);
            queryTransactions.setInt(1, tId);

            if (!queryTransactions.executeQuery().next())
                throw new SQLException("No Such Transaction Exists");

            PreparedStatement updateTransactions = conn.prepareStatement(UPDATE_TRANSACTIONS);
            updateTransactions.setString(1, method);
            updateTransactions.setInt(2, tId);

            int affectedRows = updateTransactions.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("More than one row affected");
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't update Transaction Method: " + e.getMessage());
            return false;
        }
    }


    public ResultSet getAllTransactionDetails() {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + TRANSACTIONS_TABLE);
            if (!results.executeQuery().next())
                throw new SQLException("No Such Product Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch Transaction Details: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getTransactionDetails(int customerId, String date) {
        try {
            PreparedStatement results = conn.prepareStatement("SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE " + TRANSACTIONS_ID + "=?");
            results.setInt(1, getTransactionId(customerId, date));
            if (!results.executeQuery().next())
                throw new SQLException("No Such Product Exists");
            return results.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't fetch Product Details: " + e.getMessage());
            return null;
        }
    }

    public TransactionsDTO calcTransactions(int cId, List<BuyDTO> productsAndQuantity, String method) throws SQLException {
            double prodAmt = 0, totalAmt = 0, discount = 0, discountAmt = 0, gstAmt = 0;
            final double gst = 18;

            ProductsDTO products = new ProductsDTO();

//            for (ProductsDTO product : products)
//                prodAmt += product.getSellingPrice();

            // Considering Discount rates are set by Company beforehand
            if (prodAmt < 1000)
                discount = 0;
            else if (prodAmt >= 1000 && prodAmt <= 2000)
                discount = 10;
            else if (prodAmt >= 2000 && prodAmt <= 3000)
                discount = 20;
            else
                discount = 30;

            discountAmt = discount / 100 * prodAmt;
            totalAmt = prodAmt - discountAmt;
            gstAmt = gst / 100 * totalAmt;
            totalAmt += gstAmt;

            TransactionsDTO transactions = new TransactionsDTO();
            transactions.setcId(cId);
            transactions.setProdAmt(prodAmt);
            transactions.setDiscount(discountAmt);
            transactions.setGst(gstAmt);
            transactions.setTotalAmt(totalAmt);
            transactions.setMethod(method);

            return transactions;
    }
}
