package com.inventory.database;

public interface DatabaseConstants {

    String DATABASE_NAME = "inventorymanagement.db";
    String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\LENOVO\\OneDrive\\Documents\\Projects\\DBMS Mini Project\\InventoryUI\\src\\main\\java\\com\\inventory\\database\\"+DATABASE_NAME;

    String CUSTOMER_TABLE = "CUSTOMER";
    String ISSUE_PRODUCT_TABLE = "ISSUE_PRODUCT";
    String PRODUCTS_TABLE = "PRODUCTS";
    String SUPPLIERS_TABLE = "SUPPLIERS";
    String USERS_TABLE = "USERS";

    String CUSTOMER_ID = "C_ID";
    String CUSTOMER_NAME = "C_NAME";
    String CUSTOMER_PHONE = "C_PHONE";
    String CUSTOMER_LOCATION = "C_LOCATION";

    String ISSUE_U_ID = "U_ID";
    String ISSUE_C_ID = "C_ID";
    String ISSUE_P_ID = "P_ID";
    String ISSUE_QUANTITY = "QUANTITY";
    String ISSUE_DATE_SOLD = "DATE_SOLD";

    String PRODUCTS_ID = "P_ID";
    String PRODUCTS_NAME = "P_NAME";
    String PRODUCTS_BRAND = "P_BRAND";
    String PRODUCTS_COST_PRICE = "COST_PRICE";
    String PRODUCTS_SELLING_PRICE = "SELLING_PRICE";
    String PRODUCTS_CURRENT_STOCK = "CURRENT_STOCK";
    String PRODUCTS_DATE_ADDED = "DATE_ADDED";
    String PRODUCTS_S_ID = "S_ID";

    String SUPPLIERS_ID = "S_ID";
    String SUPPLIERS_NAME = "S_NAME";
    String SUPPLIERS_PHONE = "S_PHONE";
    String SUPPLIERS_LOCATION = "S_LOCATION";

    String USERS_ID = "U_ID";
    String USERS_USERNAME = "USERNAME";
    String USERS_PASSWORD = "PASSWORD";
    String USERS_NAME = "U_NAME";
    String USERS_PHONE = "U_PHONE";
    String USERS_LOCATION = "U_LOCATION";
    String USERS_CATEGORY = "CATEGORY";
}
