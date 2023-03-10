package com.inventory.DataSource;

import com.inventory.DAO.CustomerDAO;
import com.inventory.DAO.IssueProductDAO;
import com.inventory.DAO.UsersDAO;
import com.inventory.DTO.*;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Seller implements DatabaseConstants {
    private final Connection conn;
    private final String username;

    public Seller(Connection conn, String username) {
        this.conn=conn;
        this.username = username;
    }

    public String getSellerFirstName() {
        final String GET_FIRST_NAME = "SELECT U_NAME FROM USERS WHERE USERNAME=?";
        try {
            PreparedStatement getFirstName = conn.prepareStatement(GET_FIRST_NAME);
            getFirstName.setString(1, username);
            ResultSet result = getFirstName.executeQuery();
            String name = result.getString(1);
            if(name.indexOf(' ')==-1)
                return name;
            return name.substring(0, name.indexOf(' '));
        } catch (Exception e) {
            return null;
        }
    }

    public String getSellerFullName() {
        final String GET_FIRST_NAME = "SELECT U_NAME FROM USERS WHERE USERNAME=?";
        try {
            PreparedStatement getFirstName = conn.prepareStatement(GET_FIRST_NAME);
            getFirstName.setString(1, username);
            ResultSet result = getFirstName.executeQuery();
            return result.getString(1);
        } catch (Exception e) {
            return null;
        }
    }

    public String getSellerPhone() {
        final String GET_PHONE = "SELECT U_PHONE FROM USERS WHERE USERNAME=?";
        try {
            PreparedStatement getFirstName = conn.prepareStatement(GET_PHONE);
            getFirstName.setString(1, username);
            ResultSet result = getFirstName.executeQuery();
            return result.getString(1);
        } catch (Exception e) {
            return null;
        }
    }

    public String getSellerLocation() {
        final String GET_LOCATION = "SELECT U_LOCATION FROM USERS WHERE USERNAME=?";
        try {
            PreparedStatement getFirstName = conn.prepareStatement(GET_LOCATION);
            getFirstName.setString(1, username);
            ResultSet result = getFirstName.executeQuery();
            return result.getString(1);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateUserDetails(String newPhone, String location) {
        try {
            UsersDTO user = new UsersDTO();
            user.setUserPhone(newPhone);
            user.setUserLocation(location);
            user.setUsername(username);
            user.setCategory("Seller");

            boolean updated = new UsersDAO(conn).updateUserDetails(user);
            if(!updated)
                throw new Exception("Error");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setNewPassword(String oldPassword, String newPassword) {
        try {
            if(!new UsersDAO(conn).checkPasswordCorrect(username, oldPassword))
                throw new Exception("Old Password Wrong");
            boolean updated = new UsersDAO(conn).updatePassword(username, newPassword);
            if(!updated)
                throw new Exception("Error");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Insert a new Customer
    public int insertCustomer(String custName, String custPhone, String location) {
        try {
            CustomerDTO customer = new CustomerDTO();
            customer.setCustomerName(custName);
            customer.setCustomerPhone(custPhone);
            customer.setCustomerLocation(location);

            int custId = new CustomerDAO(conn).addCustomer(customer);
            if(custId == -1)
                throw new Exception("Error");
            return custId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    // TODO: Though here oldLocation is not specified, place oldLocation in UI for better visuals
    // Update Already Existing Customer
    public boolean updateCustomer(String custName, String oldPhone, String custPhone, String location) {
        try {
            CustomerDTO customer = new CustomerDTO();
            customer.setCustomerPhone(custPhone);
            customer.setCustomerLocation(location);
            customer.setCustomerId(new CustomerDAO(conn).getCustomerId(custName, oldPhone));

            boolean updated = new CustomerDAO(conn).updateCustomerDetails(customer);
            if(!updated)
                throw new Exception("Error");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // View his Own Sales grouped by customer and date_sold ordered by customer and max sales
    public List<SellerOwnSales> totalSalesbyCustomerAndProduct() {
        final String VIEW_SALES = "SELECT    C."+CUSTOMER_NAME+",C."+CUSTOMER_LOCATION+",P."+PRODUCTS_NAME+",P."
                +PRODUCTS_BRAND+",IP."+ISSUE_QUANTITY+",IP."+ISSUE_DATE_SOLD+",P."+PRODUCTS_SELLING_PRICE+"* IP."
                +ISSUE_QUANTITY+" FROM "+CUSTOMER_TABLE+" C,"+ISSUE_PRODUCT_TABLE+" IP,"+PRODUCTS_TABLE+" P WHERE C."+
                CUSTOMER_ID+"=IP."+ISSUE_C_ID+" AND IP."+ISSUE_P_ID+"=P."+PRODUCTS_ID+" AND IP."+ISSUE_U_ID+"=? ORDER BY "
                +" IP."+ISSUE_C_ID+", P."+PRODUCTS_SELLING_PRICE+"* IP." +ISSUE_QUANTITY+" DESC";
        try {
            PreparedStatement sales = conn.prepareStatement(VIEW_SALES);
            sales.setInt(1, new UsersDAO(conn).getUserId(username));

            ResultSet resultSales = sales.executeQuery();
            List<SellerOwnSales> sellerOwnSales = new ArrayList<>();
            while(resultSales.next()) {
                SellerOwnSales seller = new SellerOwnSales(resultSales.getString(1), resultSales.getString(2), resultSales.getString(3),
                        resultSales.getString(4), resultSales.getString(5), resultSales.getString(6), resultSales.getDouble(7));
                sellerOwnSales.add(seller);
            }
            return sellerOwnSales;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // View Sales of All the Sellers
    public List<AllSellerSales> getAllSellerSales() {
        final String ALL_SELLER_SALES = "SELECT U.U_ID, U."+USERS_NAME+", SUM(IP."+ISSUE_QUANTITY+"*P."+PRODUCTS_SELLING_PRICE
        +") FROM "+USERS_TABLE+" U,"
                +ISSUE_PRODUCT_TABLE+" IP,"+PRODUCTS_TABLE+" P  WHERE U."+USERS_ID+"=IP."+ISSUE_U_ID+" AND IP."+PRODUCTS_ID
        +"=P."+PRODUCTS_ID+" GROUP BY IP."+USERS_ID+" ORDER BY "+" SUM(IP."+ISSUE_QUANTITY+"*P."+PRODUCTS_SELLING_PRICE
                +") DESC";
        try {
            PreparedStatement allSellerSales = conn.prepareStatement(ALL_SELLER_SALES);
            ResultSet results = allSellerSales.executeQuery();
            List<AllSellerSales> allSellers = new ArrayList<>();
            while(results.next()) {
                AllSellerSales allSeller = new AllSellerSales(results.getInt(1), results.getString(2), results.getInt(3));
                allSellers.add(allSeller);
            }
            return allSellers;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Get All Product Details
    public List<ProductsDTO> getProductDetails() {
        final String GET_PRODUCT_DETAILS = "SELECT * FROM "+PRODUCTS_TABLE+" GROUP BY "+PRODUCTS_S_ID;
        try {
            PreparedStatement getProductDetails = conn.prepareStatement(GET_PRODUCT_DETAILS);
            ResultSet results = getProductDetails.executeQuery();
            List<ProductsDTO> products = new ArrayList<>();
            while(results.next()) {
                ProductsDTO productsDTO = new ProductsDTO();
                productsDTO.setProductId(results.getInt(1));
                productsDTO.setProductName(results.getString(2));
                productsDTO.setProductBrand(results.getString(3));
                productsDTO.setCostPrice(results.getDouble(4));
                productsDTO.setSellingPrice(results.getDouble(5));
                productsDTO.setCurrentStock(results.getInt(6));
                productsDTO.setDateAdded(results.getString(7));
                products.add(productsDTO);
            }
            return products;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Get Total Earnings
    public double getTotalEarnings() {
        final String TOTAL_EARNINGS = "SELECT SUM(P."+PRODUCTS_SELLING_PRICE+"*IP."+ISSUE_QUANTITY+") FROM "
                +PRODUCTS_TABLE+" P, "+ISSUE_PRODUCT_TABLE+" IP WHERE IP.P_ID = P.P_ID AND IP."+ISSUE_U_ID+"=? ";
        try {
            PreparedStatement totalEarnings = conn.prepareStatement(TOTAL_EARNINGS);
            totalEarnings.setInt(1, new UsersDAO(conn).getUserId(username));

            return totalEarnings.executeQuery().getDouble(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1.0;
        }
    }

    // Get Total Profit
    public double getTotalProfit() {
        final String TOTAL_EARNINGS = "SELECT SUM(P."+PRODUCTS_SELLING_PRICE+"-"+PRODUCTS_COST_PRICE+"*IP."+ISSUE_QUANTITY+") FROM "
                +PRODUCTS_TABLE+" P, "+ISSUE_PRODUCT_TABLE+" IP WHERE IP.P_ID = P.P_ID AND IP."+ISSUE_U_ID+"=?";
        try {
            PreparedStatement totalEarnings = conn.prepareStatement(TOTAL_EARNINGS);
            totalEarnings.setInt(1, new UsersDAO(conn).getUserId(username));

            return totalEarnings.executeQuery().getDouble(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1.0;
        }
    }

    // Returns customer with most sales
    public List<MaxCustomer> getMaxSalesCustomer() {
        final String MAX_CUSTOMER = "SELECT C.C_ID, C."+CUSTOMER_NAME+", C.C_PHONE, SUM(P."+PRODUCTS_SELLING_PRICE+"*IP."+ISSUE_QUANTITY
                +") FROM "+CUSTOMER_TABLE+" C,"+PRODUCTS_TABLE+" P,"+ISSUE_PRODUCT_TABLE+" IP WHERE P."
                +PRODUCTS_ID+"=IP."+ISSUE_P_ID+" AND IP."+ISSUE_C_ID+"=C."+CUSTOMER_ID+" AND IP."+ISSUE_U_ID
                +"=? GROUP BY IP."+ISSUE_U_ID+",IP."+ISSUE_C_ID+" ORDER BY SUM(P."+PRODUCTS_SELLING_PRICE+"*IP."+ISSUE_QUANTITY+") DESC";
        try {
            PreparedStatement maxCustomer = conn.prepareStatement(MAX_CUSTOMER);
            maxCustomer.setInt(1, new UsersDAO(conn).getUserId(username));

            ResultSet results = maxCustomer.executeQuery();
            List<MaxCustomer> max = new ArrayList<>();
            while(results.next()) {
                MaxCustomer maxCus = new MaxCustomer(results.getInt(1), results.getString(2), results.getString(3),
                        results.getDouble(4));
                max.add(maxCus);
            }
            return max;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean sellProduct(int pId, int cId, int uId, int quantity) {
        try {
            IssueProductDTO issue = new IssueProductDTO();
            issue.setpId(pId);
            issue.setuId(uId);
            issue.setcId(cId);
            issue.setQuantity(quantity);
            issue.setDateSold(new java.sql.Date(System.currentTimeMillis()).toString());
            boolean added = new IssueProductDAO(conn).addIssueProduct(issue);
            if(!added)
                throw new SQLException("Could Not Add Sale");
            return true;
        } catch (Exception e ){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
