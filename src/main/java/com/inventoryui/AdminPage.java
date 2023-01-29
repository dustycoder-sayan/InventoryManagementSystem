package com.inventoryui;

import com.inventory.DTO.*;
import com.inventory.DataSource.Admin;
import com.inventory.DataSource.Seller;
import com.inventory.database.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;

public class AdminPage {

    private Label nameLabel, userName, userUsername, userCategory, userPhone, userLocation, userTotalEarningsLabel, userTotalEarnings;
    private TreeView<String> tree;
    private String username;
    private Connection conn;
    private TextField productName, brandName;

    public AdminPage(String username, Connection conn) {
        this.username = username;
        this.conn = conn;
    }

    public Scene adminScene(Stage stage) {
        stage.setTitle("Welcome, "+new Admin(conn, username).getAdminFirstName());
        BorderPane layout = new BorderPane();

        // Top Part
        HBox top = new HBox();
        top.setPadding(new Insets(10, 10, 10, 10));
        top.setSpacing(10);
        nameLabel = new Label();
        nameLabel.setText("Hi, "+new Admin(conn, username).getAdminFirstName()+"!");
        nameLabel.setFont(new Font("Calibri", 30));
        top.getChildren().add(nameLabel);
        top.setAlignment(Pos.CENTER);
        top.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        nameLabel.setAlignment(Pos.CENTER);
        layout.setTop(top);

        // Left
        TreeItem<String> root, customers, suppliers, users, products, sales, settings;

        root = new TreeItem<>();
        root.setExpanded(true);

        products = makeBranch("Products", root);
        makeBranch("View All Products", products);
        makeBranch("Products Most Sold", products);
        makeBranch("Add New Product", products);
        makeBranch("Update Stock", products);
        makeBranch("Delete Product", products);

        suppliers = makeBranch("Suppliers", root);
        makeBranch("View All Suppliers", suppliers);
        makeBranch("Add New Supplier", suppliers);
        makeBranch("Update Supplier", suppliers);
        makeBranch("Delete Supplier", suppliers);

        users = makeBranch("Employees", root);
        makeBranch("View All Employees", users);
        makeBranch("Most Sold", users);
        makeBranch("Add New Employee", users);
        makeBranch("Update Employee", users);
        makeBranch("Delete Employee", users);

        customers = makeBranch("Customers", root);
        makeBranch("View All Customers", customers);
        makeBranch("Most Buys", customers);

        sales = makeBranch("Sales", root);
        makeBranch("Sales Report", sales);

        settings = makeBranch("Settings", root);
        makeBranch("Update Details", settings);
        makeBranch("Update Password", settings);
        makeBranch("Logout", settings);

        tree = new TreeView<>(root);
        tree.setShowRoot(false);
        tree.setMinHeight(775);
        tree.setPadding(new Insets(10, 10, 10, 10));
        tree.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(3))));

        VBox left = new VBox(20);
        left.getChildren().add(tree);
        layout.setLeft(left);

        // Right
        Label about = new Label("Personal Details");
        about.setPadding(new Insets(10, 10, 10, 10));
        userName = new Label(new Admin(conn, username).getAdminFullName());
        userName.setPadding(new Insets(20, 10, 10, 10));
        userName.setAlignment(Pos.CENTER);
        userName.setFont(new Font("Calibri", 20));
        userUsername = new Label(username);
        userUsername.setPadding(new Insets(10, 10, 10, 10));
        userUsername.setAlignment(Pos.CENTER);
        userCategory = new Label("Admin");
        userCategory.setPadding(new Insets(10, 10, 10, 10));
        userCategory.setAlignment(Pos.CENTER);
        userPhone = new Label(new Admin(conn, username).getAdminPhone());
        userPhone.setPadding(new Insets(10, 10, 10, 10));
        userPhone.setAlignment(Pos.CENTER);
        userLocation = new Label(new Admin(conn, username).getAdminLocation());
        userLocation.setPadding(new Insets(10, 10, 10, 10));
        userLocation.setAlignment(Pos.CENTER);

        userTotalEarningsLabel = new Label("Total Sales: ");
        userTotalEarningsLabel.setPadding(new Insets(10, 10, 10, 10));
        userTotalEarningsLabel.setAlignment(Pos.CENTER);
        userTotalEarningsLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

        double totalEarnings = new Admin(conn, username).getTotalSales();
        if(totalEarnings == -1)
            totalEarnings=0;

        userTotalEarnings = new Label(String.valueOf(totalEarnings));
        userTotalEarnings.setPadding(new Insets(10, 10, 10, 10));
        userTotalEarnings.setAlignment(Pos.CENTER);
        userTotalEarnings.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

        HBox spacing = new HBox();
        spacing.setMinHeight(50);
        VBox right = new VBox(20);
        right.setMinHeight(775);
        right.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        right.setMinWidth(250);
        right.getChildren().addAll(userName, userCategory, userUsername, userPhone, userLocation,
                userTotalEarningsLabel, userTotalEarnings);
        right.setAlignment(Pos.TOP_CENTER);
        right.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(3))));
        layout.setRight(right);

        // center
        TableView<IssueProductNamesDTO> table = salesReport();
        layout.setCenter(table);

        tree.getSelectionModel().selectedItemProperty().addListener( (v, oldChoice, newChoice) -> {
            String valueSelected = newChoice.getValue();
            if(valueSelected.equals("Sales Report")) {
                TableView<IssueProductNamesDTO> table1 = salesReport();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("View All Products")) {
                TableView<ProductsSupNameDTO> table1 = viewAllProducts();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("Products Most Sold")) {
                TableView<ProductsDTO> table1 = maxProductsSold();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("View All Suppliers")) {
                TableView<SuppliersDTO> table1 = viewAllSuppliers();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("View All Customers")) {
                TableView<CustomerDTO> table1 = viewAllCustomers();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("Most Buys")) {
                TableView<CustomerDTO> table1 = maxSaleCustomer();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("View All Employees")) {
                TableView<UsersDTO> table1 = viewAllUsers();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("Most Sold")) {
                TableView<UsersDTO> table1 = maxSaleUser();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("Add New Product")) {
                new AddProducts(conn).start();
            }
            else if(valueSelected.equals("Update Stock")) {
                new UpdateStock(conn).start();
            }
            else if(valueSelected.equals("Delete Product")) {
                new DeleteProduct(conn).start();
            }
            else if(valueSelected.equals("Add New Supplier")) {
                new AddSupplier(conn).start();
            }
            else if(valueSelected.equals("Update Supplier")) {
                new UpdateSupplier(conn).start();
            }
            else if(valueSelected.equals("Delete Supplier")) {
                new DeleteSupplier(conn).start();
            }
            else if(valueSelected.equals("Add New Employee")) {
                new AddUser(conn).start();
            }
            else if(valueSelected.equals("Update Employee")) {
                new UpdateUser(conn).start();
            }
            else if(valueSelected.equals("Delete Employee")) {
                new DeleteUser(conn).start();
            }
            else if(valueSelected.equalsIgnoreCase("Update Details")) {
                new UpdateAdminDetails(username, conn).start();
            }
            else if(valueSelected.equalsIgnoreCase("Update Password")) {
                new PasswordUpdate(username, conn).start();
            }
            else if(valueSelected.equals("Logout")) {
                boolean answer = Logout.alert();
                if(answer)
                    stage.close();
            }
        } );


        return new Scene(layout, 1250, 825);
    }

    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> treeItem = new TreeItem<>(title);
        treeItem.setExpanded(true);
        parent.getChildren().add(treeItem);      // root is the parent of treeItem - add child to parent
        return treeItem;
    }

    public TableView<IssueProductNamesDTO> salesReport() {
        TableColumn<IssueProductNamesDTO, String> prodName = new TableColumn<>("Product");
        prodName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        prodName.setMinWidth(135);
        prodName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<IssueProductNamesDTO, String> prodBrand = new TableColumn<>("Brand");
        prodBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        prodBrand.setMinWidth(135);
        prodBrand.setStyle( "-fx-alignment: CENTER;");

        TableColumn<IssueProductNamesDTO, String> userName = new TableColumn<>("Seller");
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userName.setMinWidth(135);
        userName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<IssueProductNamesDTO, String> custName = new TableColumn<>("Customer");
        custName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custName.setMinWidth(135);
        custName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<IssueProductNamesDTO, String> custLocation = new TableColumn<>("Location");
        custLocation.setCellValueFactory(new PropertyValueFactory<>("customerLocation"));
        custLocation.setMinWidth(135);
        custLocation.setStyle( "-fx-alignment: CENTER;");

        TableColumn<IssueProductNamesDTO, String> dateSold = new TableColumn<>("Date");
        dateSold.setCellValueFactory(new PropertyValueFactory<>("dateSold"));
        dateSold.setMinWidth(135);
        dateSold.setStyle( "-fx-alignment: CENTER;");

        TableColumn<IssueProductNamesDTO, Integer> totalSale = new TableColumn<>("Total");
        totalSale.setCellValueFactory(new PropertyValueFactory<>("totalSale"));
        totalSale.setMinWidth(135);
        totalSale.setStyle( "-fx-alignment: CENTER;");

        TableView<IssueProductNamesDTO> table1 = new TableView<>();
        table1.setItems(sales());
        table1.getColumns().addAll(prodName, prodBrand, userName, custName, custLocation, dateSold, totalSale);

        return table1;
    }

    public ObservableList<IssueProductNamesDTO> sales() {
        return FXCollections.observableArrayList(new Admin(conn, username).getSales());
    }

    public TableView<ProductsSupNameDTO> viewAllProducts() {
        TableColumn<ProductsSupNameDTO, Integer> prodId = new TableColumn<>("Product ID");
        prodId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        prodId.setMinWidth(135);
        prodId.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsSupNameDTO, String> prodName = new TableColumn<>("Product");
        prodName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        prodName.setMinWidth(135);
        prodName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsSupNameDTO, String> prodBrand = new TableColumn<>("Brand");
        prodBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        prodBrand.setMinWidth(135);
        prodBrand.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsSupNameDTO, Double> costPrice = new TableColumn<>("Cost Price");
        costPrice.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        costPrice.setMinWidth(135);
        costPrice.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsSupNameDTO, Double> sellingPrice = new TableColumn<>("Selling Price");
        sellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        sellingPrice.setMinWidth(135);
        sellingPrice.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsSupNameDTO, Integer> stock = new TableColumn<>("Stock");
        stock.setCellValueFactory(new PropertyValueFactory<>("currentStock"));
        stock.setMinWidth(135);
        stock.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsSupNameDTO, String> dateAdded = new TableColumn<>("Date Added");
        dateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        dateAdded.setMinWidth(135);
        dateAdded.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsSupNameDTO, String> sName = new TableColumn<>("Supplier");
        sName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        sName.setMinWidth(135);
        sName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsSupNameDTO, String> sLocation = new TableColumn<>("Location");
        sLocation.setCellValueFactory(new PropertyValueFactory<>("supplierLocation"));
        sLocation.setMinWidth(135);
        sLocation.setStyle( "-fx-alignment: CENTER;");

        TableView<ProductsSupNameDTO> table2 = new TableView<>();
        table2.setItems(allProducts());
        table2.getColumns().addAll(prodId, prodName, prodBrand, costPrice, sellingPrice, stock, dateAdded, sName, sLocation);

        return table2;
    }

    public ObservableList<ProductsSupNameDTO> allProducts() {
        return FXCollections.observableArrayList(new Admin(conn, username).getAllProducts());
    }

    public TableView<ProductsDTO> maxProductsSold() {
        TableColumn<ProductsDTO, String> prodName = new TableColumn<>("Product");
        prodName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        prodName.setMinWidth(230);
        prodName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsDTO, String> prodBrand = new TableColumn<>("Brand");
        prodBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        prodBrand.setMinWidth(230);
        prodBrand.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsDTO, Integer> totalSales = new TableColumn<>("Sales");
        totalSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        totalSales.setMinWidth(240);
        totalSales.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsDTO, String> prodId = new TableColumn<>("Product ID");
        prodId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        prodId.setMinWidth(240);
        prodId.setStyle( "-fx-alignment: CENTER;");

        TableColumn<ProductsDTO, String> supName = new TableColumn<>("Supplier");
        supName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supName.setMinWidth(240);
        supName.setStyle( "-fx-alignment: CENTER;");

        TableView<ProductsDTO> table2 = new TableView<>();
        table2.setItems(maxSoldProduct());
        table2.getColumns().addAll(prodId, prodName, prodBrand, supName, totalSales);

        return table2;
    }

    public ObservableList<ProductsDTO> maxSoldProduct() {
        return FXCollections.observableArrayList(new Admin(conn, username).maxProductSold());
    }

    public TableView<SuppliersDTO> viewAllSuppliers() {
        TableColumn<SuppliersDTO, Integer> supplierID = new TableColumn<>("Supplier ID");
        supplierID.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        supplierID.setMinWidth(235);
        supplierID.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SuppliersDTO, String> supplierName = new TableColumn<>("Name");
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplierName.setMinWidth(235);
        supplierName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SuppliersDTO, String> supplierPhone = new TableColumn<>("Contact No");
        supplierPhone.setCellValueFactory(new PropertyValueFactory<>("supplierPhone"));
        supplierPhone.setMinWidth(235);
        supplierPhone.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SuppliersDTO, String> supplierLocation = new TableColumn<>("Location");
        supplierLocation.setCellValueFactory(new PropertyValueFactory<>("supplierLocation"));
        supplierLocation.setMinWidth(235);
        supplierLocation.setStyle( "-fx-alignment: CENTER;");

        TableView<SuppliersDTO> table2 = new TableView<>();
        table2.setItems(allSuppliers());
        table2.getColumns().addAll(supplierID, supplierName, supplierPhone, supplierLocation);

        return table2;
    }

    public ObservableList<SuppliersDTO> allSuppliers() {
        return FXCollections.observableArrayList(new Admin(conn, username).getAllSuppliers());
    }

    public TableView<CustomerDTO> viewAllCustomers() {
        TableColumn<CustomerDTO, Integer> customerID = new TableColumn<>("Customer ID");
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerID.setMinWidth(235);
        customerID.setStyle( "-fx-alignment: CENTER;");

        TableColumn<CustomerDTO, String> customerName = new TableColumn<>("Name");
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerName.setMinWidth(235);
        customerName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<CustomerDTO, String> customerPhone = new TableColumn<>("Contact No");
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerPhone.setMinWidth(235);
        customerPhone.setStyle( "-fx-alignment: CENTER;");

        TableColumn<CustomerDTO, String> customerLocation = new TableColumn<>("Location");
        customerLocation.setCellValueFactory(new PropertyValueFactory<>("customerLocation"));
        customerLocation.setMinWidth(235);
        customerLocation.setStyle( "-fx-alignment: CENTER;");

        TableView<CustomerDTO> table2 = new TableView<>();
        table2.setItems(allCustomers());
        table2.getColumns().addAll(customerID, customerName, customerPhone, customerLocation);

        return table2;
    }

    public ObservableList<CustomerDTO> allCustomers() {
        return FXCollections.observableArrayList(new Admin(conn, username).getAllCustomers());
    }

    public TableView<CustomerDTO> maxSaleCustomer() {
        TableColumn<CustomerDTO, String> customerName = new TableColumn<>("Name");
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerName.setMinWidth(190);
        customerName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<CustomerDTO, String> customerPhone = new TableColumn<>("Contact No");
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerPhone.setMinWidth(190);
        customerPhone.setStyle( "-fx-alignment: CENTER;");

        TableColumn<CustomerDTO, String> customerLocation = new TableColumn<>("Location");
        customerLocation.setCellValueFactory(new PropertyValueFactory<>("customerLocation"));
        customerLocation.setMinWidth(190);
        customerLocation.setStyle( "-fx-alignment: CENTER;");

        TableColumn<CustomerDTO, Double> sales = new TableColumn<>("Sales");
        sales.setCellValueFactory(new PropertyValueFactory<>("sales"));
        sales.setMinWidth(190);
        sales.setStyle( "-fx-alignment: CENTER;");

        TableColumn<CustomerDTO, Double> customerId = new TableColumn<>("Customer ID");
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerId.setMinWidth(190);
        customerId.setStyle( "-fx-alignment: CENTER;");


        TableView<CustomerDTO> table2 = new TableView<>();
        table2.setItems(maxCustomers());
        table2.getColumns().addAll(customerId, customerName, customerPhone, customerLocation, sales);

        return table2;
    }

    public ObservableList<CustomerDTO> maxCustomers() {
        return FXCollections.observableArrayList(new Admin(conn, username).maxCustomerSold());
    }

    public TableView<UsersDTO> viewAllUsers() {
        TableColumn<UsersDTO, Integer> userId = new TableColumn<>("Employee ID");
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userId.setMinWidth(157);
        userId.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, String> username = new TableColumn<>("username");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        username.setMinWidth(157);
        username.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, String> password = new TableColumn<>("password");
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        password.setMinWidth(157);
        password.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, String> userName = new TableColumn<>("Name");
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userName.setMinWidth(157);
        userName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, String> userPhone = new TableColumn<>("Contact No");
        userPhone.setCellValueFactory(new PropertyValueFactory<>("userPhone"));
        userPhone.setMinWidth(157);
        userPhone.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, String> userLocation = new TableColumn<>("Location");
        userLocation.setCellValueFactory(new PropertyValueFactory<>("userLocation"));
        userLocation.setMinWidth(157);
        userLocation.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, String> category = new TableColumn<>("Category");
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        category.setMinWidth(157);
        category.setStyle( "-fx-alignment: CENTER;");

        TableView<UsersDTO> table1 = new TableView<>();
        table1.setItems(allUsers());
        table1.getColumns().addAll(userId, category, username, password, userName, userPhone, userLocation);

        return table1;
    }

    public ObservableList<UsersDTO> allUsers() {
        return FXCollections.observableArrayList(new Admin(conn, username).getAllUsers());
    }

    public TableView<UsersDTO> maxSaleUser() {
        TableColumn<UsersDTO, String> userName = new TableColumn<>("Name");
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userName.setMinWidth(190);
        userName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, String> userPhone = new TableColumn<>("Contact No");
        userPhone.setCellValueFactory(new PropertyValueFactory<>("userPhone"));
        userPhone.setMinWidth(190);
        userPhone.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, String> userLocation = new TableColumn<>("Location");
        userLocation.setCellValueFactory(new PropertyValueFactory<>("userLocation"));
        userLocation.setMinWidth(190);
        userLocation.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, Double> sales = new TableColumn<>("Sales");
        sales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        sales.setMinWidth(190);
        sales.setStyle( "-fx-alignment: CENTER;");

        TableColumn<UsersDTO, Integer> userId = new TableColumn<>("User ID");
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userId.setMinWidth(190);
        userId.setStyle( "-fx-alignment: CENTER;");


        TableView<UsersDTO> table2 = new TableView<>();
        table2.setItems(maxUsers());
        table2.getColumns().addAll(userId, userName, userPhone, userLocation, sales);

        return table2;
    }

    public ObservableList<UsersDTO> maxUsers() {
        return FXCollections.observableArrayList(new Admin(conn, username).maxUserSold());
    }
}
