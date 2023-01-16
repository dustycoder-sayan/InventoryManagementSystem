package com.inventory.DataSource;

import com.inventory.DAO.*;
import com.inventory.DTO.*;
import com.inventory.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Admin implements DatabaseConstants {
    private final Connection conn;
    private final String username;

    public Admin(Connection conn, String username) {
        this.conn = conn;
        this.username = username;
    }

    public String getAdminFirstName() {
        final String GET_FIRST_NAME = "SELECT U_NAME FROM USERS WHERE USERNAME=?";
        try {
            PreparedStatement getFirstName = conn.prepareStatement(GET_FIRST_NAME);
            getFirstName.setString(1, username);
            ResultSet result = getFirstName.executeQuery();
            String name = result.getString(1);
            return name.substring(0, name.indexOf(' '));
        } catch (Exception e) {
            return null;
        }
    }

    public String getAdminFullName() {
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

    public String getAdminPhone() {
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

    public String getAdminLocation() {
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

    public String getAdminCategory() {
        final String GET_CATEGORY = "SELECT U_CATEGORY FROM USERS WHERE USERNAME=?";
        try {
            PreparedStatement getFirstName = conn.prepareStatement(GET_CATEGORY);
            getFirstName.setString(1, username);
            ResultSet result = getFirstName.executeQuery();
            return result.getString(1);
        } catch (Exception e) {
            return null;
        }
    }

    // Update Own Details
    public boolean updateUserDetails(String newPhone, String location) {
        try {
            UsersDTO user = new UsersDTO();
            user.setUserPhone(newPhone);
            user.setUserLocation(location);
            user.setUsername(username);
            user.setCategory("Admin");

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

    // Insert a new Supplier
    public int insertSupplier(String supplierName, String supplierPhone, String location) {
        try {
            SuppliersDTO supplier = new SuppliersDTO();
            supplier.setSupplierName(supplierName);
            supplier.setSupplierPhone(supplierPhone);
            supplier.setSupplierLocation(location);

            int supId = new SuppliersDAO(conn).addSupplier(supplier);
            if(supId == -1)
                throw new Exception("Error");
            return supId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    // TODO: Though here oldLocation is not specified, place oldLocation in UI for better visuals
    public boolean updateSupplier(String supName, String oldPhone, String newPhone, String location) {
        try {
            SuppliersDTO supplier = new SuppliersDTO();
            supplier.setSupplierName(supName);
            supplier.setSupplierPhone(newPhone);
            supplier.setSupplierLocation(location);
            supplier.setSupplierId(new SuppliersDAO(conn).getSupplierId(supName, oldPhone));

            boolean updated = new SuppliersDAO(conn).updateSupplierDetails(supplier);
            if(!updated)
                throw new Exception("Error");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int addNewUser(String userName, String userPhone, String userLocation, String category) {
        try {
            UsersDTO user = new UsersDTO();

            user.setUserName(userName);
            Random random = new Random();

            if(category.equalsIgnoreCase("Admin") || category.equalsIgnoreCase("Seller")) {
                String usernameNew = userName.toLowerCase().substring(0,3)+userLocation.toLowerCase().substring(0,3)
                        +"."+category.toLowerCase()+String.format("%04d", random.nextInt(10000));
                String password = userName.toLowerCase().substring(0,4)+"01234";

                user.setUsername(usernameNew);
                user.setPassword(password);
                user.setUserPhone(userPhone);
                user.setUserLocation(userLocation);
                user.setCategory(category);

                int userId = new UsersDAO(conn).addUser(user);
                if(userId == -1)
                    throw new Exception("Error");
                return userId;
            } else {
                throw new SQLException("Error");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public boolean updateAnotherUser(int userId, String uPhone, String location, String category) {
        try {
            UsersDTO user = new UsersDTO();
            user.setUserId(userId);
            user.setUserPhone(uPhone);
            user.setUserLocation(location);
            user.setCategory(category);
            user.setUsername(new UsersDAO(conn).getUsername(userId));

            boolean updated = new UsersDAO(conn).updateUserDetails(user);
            if(!updated)
                throw new Exception("Error");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int uId) {
        final String GET_USERNAME = "SELECT USERNAME FROM "+USERS_TABLE+" WHERE U_ID=?";
        final String DELETE_USER = "DELETE FROM "+USERS_TABLE+" WHERE "+USERS_USERNAME+"=?";
        try {
            PreparedStatement getUsername = conn.prepareStatement(GET_USERNAME);
            getUsername.setInt(1, uId);
            ResultSet resultSet = getUsername.executeQuery();
            if(!resultSet.next())
                throw new SQLException("No Such User found");
            String username = resultSet.getString(1);
            PreparedStatement delete = conn.prepareStatement(DELETE_USER);
            delete.setString(1, username);
            int affectedRows = delete.executeUpdate();
            if(affectedRows != 1)
                throw new SQLException("More than one rows affected");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int addProduct(String pName, String pBrand, double costPrice, double sellingPrice, int stock,
                          String dateAdded, String sName, String sPhone) {
        try {
            ProductsDTO products = new ProductsDTO();
            products.setProductName(pName);
            products.setProductBrand(pBrand);
            products.setCostPrice(costPrice);
            products.setSellingPrice(sellingPrice);
            products.setCurrentStock(stock);
            products.setDateAdded(dateAdded);
            products.setsId(new SuppliersDAO(conn).getSupplierId(sName, sPhone));

            int pId = new ProductsDAO(conn).addProduct(products);
            if(pId == -1)
                throw new Exception("Error");
            return pId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public boolean updateStock(String prodName, String prodBrand, String supplierName, String supplierPhone, int newStock,
                               String dateAdded) {
        try {
            int sId = new SuppliersDAO(conn).getSupplierId(supplierName, supplierPhone);
            int pId = new ProductsDAO(conn).getProductId(prodName, prodBrand, sId);
            return new ProductsDAO(conn).addStock(pId, newStock, dateAdded);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<CustomerDTO> getAllCustomers() {
        try {
            ResultSet results = new CustomerDAO(conn).getAllCustomerDetails();
            List<CustomerDTO> customers = new ArrayList<>();
            while(results.next()) {
                CustomerDTO customer = new CustomerDTO();
                customer.setCustomerId(results.getInt(1));
                customer.setCustomerName(results.getString(2));
                customer.setCustomerPhone(results.getString(3));
                customer.setCustomerLocation(results.getString(4));
                customers.add(customer);
            }
            return customers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<CustomerDTO> getCustomerByNameAndPhone(String custName, String custPhone) {
        try {
            ResultSet results = new CustomerDAO(conn).getCustomerDetails(custName, custPhone);
            List<CustomerDTO> customers = new ArrayList<>();
            while(results.next()) {
                CustomerDTO customer = new CustomerDTO();
                customer.setCustomerId(results.getInt(1));
                customer.setCustomerName(results.getString(results.getString(2)));
                customer.setCustomerPhone(results.getString(results.getString(3)));
                customer.setCustomerLocation(results.getString(results.getString(4)));
                customers.add(customer);
            }
            return customers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SuppliersDTO> getAllSuppliers() {
        try {
            ResultSet results = new SuppliersDAO(conn).getAllSupplierDetails();
            List<SuppliersDTO> suppliers = new ArrayList<>();
            while(results.next()) {
                SuppliersDTO supplier = new SuppliersDTO();
                supplier.setSupplierId(results.getInt(1));
                supplier.setSupplierName(results.getString(2));
                supplier.setSupplierPhone(results.getString(3));
                supplier.setSupplierLocation(results.getString(4));

                suppliers.add(supplier);
            }
            return suppliers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SuppliersDTO> getSupplierByNameAndPhone(String name, String phone) {
        try {
            ResultSet results = new SuppliersDAO(conn).getSupplierDetails(name, phone);
            List<SuppliersDTO> suppliers = new ArrayList<>();
            while(results.next()) {
                SuppliersDTO supplier = new SuppliersDTO();
                supplier.setSupplierId(results.getInt(1));
                supplier.setSupplierName(results.getString(2));
                supplier.setSupplierPhone(results.getString(3));
                supplier.setSupplierLocation(results.getString(4));

                suppliers.add(supplier);
            }
            return suppliers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<UsersDTO> getAllUsers() {
        try {
            ResultSet results = new UsersDAO(conn).getAllUserDetails();
            List<UsersDTO> users = new ArrayList<>();
            while(results.next()) {
                UsersDTO user = new UsersDTO();
                user.setUserId(results.getInt(1));
                user.setUsername(results.getString(2));
                user.setPassword(results.getString(3));
                user.setUserName(results.getString(4));
                user.setUserPhone(results.getString(5));
                user.setUserLocation(results.getString(6));
                user.setCategory(results.getString(7));

                users.add(user);
            }
            return users;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<UsersDTO> getUserByNameAndPhone(String userName, String userPhone) {
        try {
            ResultSet results = new UsersDAO(conn).getUserDetails(userName, userPhone);
            List<UsersDTO> users = new ArrayList<>();
            while(results.next()) {
                UsersDTO user = new UsersDTO();
                user.setUserId(results.getInt(1));
                user.setUsername(results.getString(2));
                user.setUserName(results.getString(4));
                user.setUserPhone(results.getString(5));
                user.setUserLocation(results.getString(6));
                user.setCategory(results.getString(7));

                users.add(user);
            }
            return users;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ProductsSupNameDTO> getAllProducts() {
        final String GET_ALL_PRODUCTS = "SELECT "+PRODUCTS_ID+","+PRODUCTS_NAME+","+PRODUCTS_BRAND+","+PRODUCTS_COST_PRICE
                +","+PRODUCTS_SELLING_PRICE+","+PRODUCTS_CURRENT_STOCK+","+PRODUCTS_DATE_ADDED+","+SUPPLIERS_NAME+","
                +SUPPLIERS_LOCATION+" FROM "+PRODUCTS_TABLE+","+SUPPLIERS_TABLE+" WHERE "+PRODUCTS_TABLE+"."+PRODUCTS_S_ID+"="
                +SUPPLIERS_TABLE+"."+SUPPLIERS_ID;
        try {
            PreparedStatement allProducts = conn.prepareStatement(GET_ALL_PRODUCTS);
            ResultSet results = allProducts.executeQuery();
            List<ProductsSupNameDTO> products = new ArrayList<>();
            while(results.next()) {
                ProductsSupNameDTO product = new ProductsSupNameDTO();
                product.setProductId(results.getInt(1));
                product.setProductName(results.getString(2));
                product.setProductBrand(results.getString(3));
                product.setCostPrice(results.getDouble(4));
                product.setSellingPrice(results.getDouble(5));
                product.setCurrentStock(results.getInt(6));
                product.setDateAdded(results.getString(7));
                product.setSupplierName(results.getString(8));
                product.setSupplierLocation(results.getString(9));

                products.add(product);
            }
            return products;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ProductsSupNameDTO> getProductsByProductNameAndBrand(String prodName, String prodBrand) {
        final String GET_ALL_PRODUCTS = "SELECT "+PRODUCTS_ID+","+PRODUCTS_NAME+","+PRODUCTS_BRAND+","+PRODUCTS_COST_PRICE
                +","+PRODUCTS_SELLING_PRICE+","+PRODUCTS_CURRENT_STOCK+","+PRODUCTS_DATE_ADDED+","+SUPPLIERS_NAME+","
                +SUPPLIERS_LOCATION+" FROM "+PRODUCTS_TABLE+","+SUPPLIERS_TABLE+" WHERE "+PRODUCTS_TABLE+"."+PRODUCTS_S_ID+"="
                +SUPPLIERS_TABLE+"."+SUPPLIERS_ID+" AND "+PRODUCTS_NAME+"=? AND "+PRODUCTS_BRAND+"=? GROUP BY PRODUCTS."
                +PRODUCTS_S_ID;
        try {
            PreparedStatement allProducts = conn.prepareStatement(GET_ALL_PRODUCTS);
            allProducts.setString(1, prodName);
            allProducts.setString(2, prodBrand);
            ResultSet results = allProducts.executeQuery();
            List<ProductsSupNameDTO> products = new ArrayList<>();
            while(results.next()) {
                ProductsSupNameDTO product = new ProductsSupNameDTO();
                product.setProductId(results.getInt(1));
                product.setProductName(results.getString(2));
                product.setProductBrand(results.getString(3));
                product.setCostPrice(results.getDouble(4));
                product.setSellingPrice(results.getDouble(5));
                product.setCurrentStock(results.getInt(6));
                product.setDateAdded(results.getString(7));
                product.setSupplierName(results.getString(8));
                product.setSupplierLocation(results.getString(4));

                products.add(product);
            }
            return products;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Show Issue Products Table: Sales Report
    public List<IssueProductNamesDTO> getSales() {
        final String QUERY_SALES = "SELECT U."+USERS_NAME+",C."+CUSTOMER_NAME+",C.C_LOCATION, P."+PRODUCTS_NAME
                +",P."+PRODUCTS_BRAND+",IP."+ISSUE_DATE_SOLD+",IP."+ISSUE_QUANTITY+"*P."+PRODUCTS_SELLING_PRICE
                +" FROM "+USERS_TABLE+" U,"+CUSTOMER_TABLE+" C,"+PRODUCTS_TABLE+" P,"+ISSUE_PRODUCT_TABLE+" IP WHERE "
                +"IP."+ISSUE_C_ID+"=C."+CUSTOMER_ID+" AND IP."+ISSUE_P_ID+"=P."+PRODUCTS_ID+" AND IP."+ISSUE_U_ID
                +"=U."+USERS_ID;
        try {
            PreparedStatement querySales = conn.prepareStatement(QUERY_SALES);
            ResultSet results = querySales.executeQuery();
            List<IssueProductNamesDTO> issueProducts = new ArrayList<>();
            while(results.next()) {
                IssueProductNamesDTO products = new IssueProductNamesDTO();
                products.setUserName(results.getString(1));
                products.setCustomerName(results.getString(2));
                products.setCustomerLocation(results.getString(3));
                products.setProductName(results.getString(4));
                products.setProductBrand(results.getString(5));
                products.setDateSold(results.getString(6));
                products.setTotalSale(results.getDouble(7));

                issueProducts.add(products);
            }
            return issueProducts;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Get the maximum product stock that has been sold
    public List<ProductsDTO> maxProductSold() {
        final String MAX_PROD_SOLD = "SELECT P.P_ID, P."+PRODUCTS_NAME+",P."+PRODUCTS_BRAND+",S.S_NAME,SUM(IP.QUANTITY*P.SELLING_PRICE)" +
                " FROM "+PRODUCTS_TABLE+" P,"
                +SUPPLIERS_TABLE+" S,"+ISSUE_PRODUCT_TABLE+" IP WHERE P."+PRODUCTS_S_ID+"=S."+SUPPLIERS_ID
                +" AND IP."+ISSUE_P_ID+"=P."+PRODUCTS_ID+" GROUP BY IP."+ISSUE_P_ID
                +" ORDER BY SUM(IP.QUANTITY*P.SELLING_PRICE) DESC";
        try {
            PreparedStatement maxProdSold = conn.prepareStatement(MAX_PROD_SOLD);
            ResultSet results = maxProdSold.executeQuery();
            List<ProductsDTO> products = new ArrayList<>();
            while(results.next()) {
                ProductsDTO product = new ProductsDTO();
                product.setProductId(results.getInt(1));
                product.setProductName(results.getString(2));
                product.setProductBrand(results.getString(3));
                product.setSupplierName(results.getString(4));
                product.setTotalSales(results.getDouble(5));

                products.add(product);
            }
            return products;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // get customers with max buys
    public List<CustomerDTO> maxCustomerSold() {
        final String MAX_CUSTOMER_SOLD = "SELECT C.C_ID, C.C_NAME, C.C_PHONE, C.C_LOCATION, SUM(IP.QUANTITY*P.SELLING_PRICE)"
        +" FROM CUSTOMER C, PRODUCTS P, ISSUE_PRODUCT IP"
        +" WHERE IP.C_ID = C.C_ID"
        +" AND P.P_ID = IP.P_ID"
        +" GROUP BY (IP.C_ID)"
        +" ORDER BY SUM(IP.QUANTITY*P.SELLING_PRICE) DESC";
        try {
            PreparedStatement maxCustomerSold = conn.prepareStatement(MAX_CUSTOMER_SOLD);
            List<CustomerDTO> customers = new ArrayList<>();
            ResultSet results = maxCustomerSold.executeQuery();
            while(results.next()) {
                CustomerDTO customer = new CustomerDTO();
                customer.setCustomerId(results.getInt(1));
                customer.setCustomerName(results.getString(2));
                customer.setCustomerPhone(results.getString(3));
                customer.setCustomerLocation(results.getString(4));
                customer.setSales(results.getDouble(5));

                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Get seller with most sales
    public List<UsersDTO> maxUserSold() {
        final String MAX_USER_SOLD = "SELECT U.U_ID, U.U_NAME, U.U_PHONE, U.U_LOCATION,"
                +" SUM(IP.QUANTITY*P.SELLING_PRICE)"
        +" FROM USERS U, PRODUCTS P, ISSUE_PRODUCT IP"
        +" WHERE IP.U_ID=U.U_ID"
        +" AND P.P_ID = IP.P_ID"
        +" GROUP BY (IP.U_ID) ORDER BY SUM(IP.QUANTITY*P.SELLING_PRICE) DESC";
        try {
            PreparedStatement maxUserSold = conn.prepareStatement(MAX_USER_SOLD);
            List<UsersDTO> users = new ArrayList<>();
            ResultSet results = maxUserSold.executeQuery();
            while(results.next()) {
                UsersDTO user = new UsersDTO();
                user.setUserId(results.getInt(1));
                user.setUserName(results.getString(2));
                user.setUserPhone(results.getString(3));
                user.setUserLocation(results.getString(4));
                user.setTotalSales(results.getDouble(5));

                users.add(user);
            }
            return users;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean deleteProduct(String prodName, String prodBrand, String supplierName, String supplierPhone) {
        final String DELETE_PRODUCT = "DELETE FROM "+PRODUCTS_TABLE+" WHERE P_ID=?";
        try {
            int sId = new SuppliersDAO(conn).getSupplierId(supplierName, supplierPhone);
            int pId = new ProductsDAO(conn).getProductId(prodName, prodBrand, sId);

            PreparedStatement deleteProd = conn.prepareStatement(DELETE_PRODUCT);
            deleteProd.setInt(1, pId);
            int affectedRows = deleteProd.executeUpdate();
            if(affectedRows != 1)
                throw new SQLException("More than one row affected");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteSupplier(String supName, String supNumber) {
        final String DELETE_SUPPLIER = "DELETE FROM "+SUPPLIERS_TABLE+" WHERE S_ID=?";
        try {
            boolean exists = new SuppliersDAO(conn).supplierExists(supName, supNumber);
            if(!exists)
                throw new SQLException("No Such Supplier");
            int sId = new SuppliersDAO(conn).getSupplierId(supName, supNumber);
            PreparedStatement delSup = conn.prepareStatement(DELETE_SUPPLIER);
            delSup.setInt(1, sId);
            int affRows = delSup.executeUpdate();
            if(affRows != 1)
                throw new SQLException("More than one row affected");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
// TODO: Definitely to be done - Add code to delete user, supplier, product
// TODO: Only if Time Permits:- Get Sales by Location