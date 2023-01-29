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

public class SellerPage {

    private Label nameLabel, userName, userUsername, userCategory, userPhone, userLocation, userTotalEarnings, userTotalProfitLabel, userTotalEarningsLabel, userTotalProfit;
    private TreeView<String> tree;
    private String username;
    private Connection conn;
    private TextField productName, brandName;

    public SellerPage(String username, Connection conn) {
        this.username = username;
        this.conn = conn;
    }

    public Scene sellerScene(Stage stage) {
        stage.setTitle("Welcome, "+new Seller(conn, username).getSellerFirstName()); // todo: put seller name
        BorderPane layout = new BorderPane();

        // Top Part
        HBox top = new HBox();
        top.setPadding(new Insets(10, 10, 10, 10));
        top.setSpacing(10);
        nameLabel = new Label();
        nameLabel.setText("Hi, "+new Seller(conn, username).getSellerFirstName()+"!");     // TODO: Get First Name from Username
        nameLabel.setFont(new Font("Calibri", 30));
        top.getChildren().add(nameLabel);
        top.setAlignment(Pos.CENTER);
        top.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        nameLabel.setAlignment(Pos.CENTER);
        layout.setTop(top);

        // Left
        TreeItem<String> root, customers,  users, products, settings, selfStats;

        root = new TreeItem<>();
        root.setExpanded(true);

        makeBranch("Sell", root);

        selfStats = makeBranch("Self Stats", root);
        makeBranch("Sales", selfStats);

        products = makeBranch("Products", root);
        makeBranch("View All Products", products);

        customers = makeBranch("Customers", root);
        makeBranch("View My Customers", customers);

        users = makeBranch("Co-Workers", root);
        makeBranch("Overall Sales", users);

        settings = makeBranch("Settings", root);
        makeBranch("Update Details", settings);
        makeBranch("Update Password", settings);
        makeBranch("Logout", settings);

        tree = new TreeView<>(root);
        tree.setShowRoot(false);
        tree.setMinHeight(775);
        tree.setPadding(new Insets(10, 10, 10, 10));
        tree.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

        VBox left = new VBox(20);
        left.getChildren().add(tree);
        layout.setLeft(left);

        // Right
        Label about = new Label("Personal Details");    // TODO: If possible, Image of User
        about.setPadding(new Insets(10, 10, 10, 10));
        userName = new Label(new Seller(conn, username).getSellerFullName());  // Todo: Get all from table
        userName.setPadding(new Insets(20, 10, 10, 10));
        userName.setAlignment(Pos.CENTER);
        userName.setFont(new Font("Calibri", 20));
        userUsername = new Label(username);
        userUsername.setPadding(new Insets(10, 10, 10, 10));
        userUsername.setAlignment(Pos.CENTER);
        userCategory = new Label("Seller");
        userCategory.setPadding(new Insets(10, 10, 10, 10));
        userCategory.setAlignment(Pos.CENTER);
        userPhone = new Label(new Seller(conn,username).getSellerPhone());
        userPhone.setPadding(new Insets(10, 10, 10, 10));
        userPhone.setAlignment(Pos.CENTER);
        userLocation = new Label(new Seller(conn,username).getSellerLocation());
        userLocation.setPadding(new Insets(10, 10, 10, 10));
        userLocation.setAlignment(Pos.CENTER);

        userTotalEarningsLabel = new Label("Total Sold: ");
        userTotalEarningsLabel.setPadding(new Insets(10, 10, 10, 10));
        userTotalEarningsLabel.setAlignment(Pos.CENTER);
        userTotalEarningsLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

        double totalEarnings = new Seller(conn, username).getTotalEarnings();
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
        right.getChildren().addAll(userName, userCategory, userUsername, userPhone, userLocation, userTotalEarningsLabel,
                userTotalEarnings);
        right.setAlignment(Pos.TOP_CENTER);
        right.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        layout.setRight(right);

        // center
        TableView<SellerOwnSales> table = salesReport();
        layout.setCenter(table);

        tree.getSelectionModel().selectedItemProperty().addListener( (v, oldChoice, newChoice) -> {
            String valueSelected = newChoice.getValue();
            if(valueSelected.equals("Sales")) {
                TableView<SellerOwnSales> table1 = salesReport();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("View All Products")) {
                TableView<ProductsSupNameDTO> table1 = viewAllProducts();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("View My Customers")) {
                TableView<MaxCustomer> table1 = viewMaxCustomers();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("Overall Sales")) {
                TableView<AllSellerSales> table1 = allSellerSales();
                layout.setCenter(table1);
            }
            else if(valueSelected.equals("Sell")) {
                new SellProduct(username, conn, layout).start();
            }
            else if(valueSelected.equalsIgnoreCase("Update Details")) {
                new UpdateSellerDetails(username, conn, layout).start();
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

    public TableView<SellerOwnSales> salesReport() {
        TableColumn<SellerOwnSales, String> prodName = new TableColumn<>("Product");
        prodName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        prodName.setMinWidth(135);
        prodName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SellerOwnSales, String> prodBrand = new TableColumn<>("Brand");
        prodBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        prodBrand.setMinWidth(135);
        prodBrand.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SellerOwnSales, String> custName = new TableColumn<>("Customer");
        custName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custName.setMinWidth(135);
        custName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SellerOwnSales, String> custLocation = new TableColumn<>("Location");
        custLocation.setCellValueFactory(new PropertyValueFactory<>("customerLocation"));
        custLocation.setMinWidth(135);
        custLocation.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SellerOwnSales, String> dateSold = new TableColumn<>("Date");
        dateSold.setCellValueFactory(new PropertyValueFactory<>("dateSold"));
        dateSold.setMinWidth(135);
        dateSold.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SellerOwnSales, Double> totalSale = new TableColumn<>("Total");
        totalSale.setCellValueFactory(new PropertyValueFactory<>("totalBuyingCost"));
        totalSale.setMinWidth(135);
        totalSale.setStyle( "-fx-alignment: CENTER;");

        TableColumn<SellerOwnSales, Integer> productQuantity = new TableColumn<>("Quantity");
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        productQuantity.setMinWidth(135);
        productQuantity.setStyle( "-fx-alignment: CENTER;");

        TableView<SellerOwnSales> table1 = new TableView<>();
        table1.setItems(sales());
        table1.getColumns().addAll(custName, custLocation, prodName, prodBrand, productQuantity, dateSold, totalSale);

        return table1;
    }

    public ObservableList<SellerOwnSales> sales() {
        return FXCollections.observableArrayList(new Seller(conn, username).totalSalesbyCustomerAndProduct());
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

    public TableView<MaxCustomer> viewMaxCustomers() {
        TableColumn<MaxCustomer, Integer> customerID = new TableColumn<>("Customer ID");
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerID.setMinWidth(240);
        customerID.setStyle( "-fx-alignment: CENTER;");

        TableColumn<MaxCustomer, String> customerName = new TableColumn<>("Name");
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerName.setMinWidth(240);
        customerName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<MaxCustomer, String> customerPhone = new TableColumn<>("Contact No");
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerPhone.setMinWidth(240);
        customerPhone.setStyle( "-fx-alignment: CENTER;");

        TableColumn<MaxCustomer, Double> totalBuys = new TableColumn<>("Total Sales");
        totalBuys.setCellValueFactory(new PropertyValueFactory<>("totalBuys"));
        totalBuys.setMinWidth(240);
        totalBuys.setStyle( "-fx-alignment: CENTER;");

        TableView<MaxCustomer> table2 = new TableView<>();
        table2.setItems(allCustomers());
        table2.getColumns().addAll(customerID, customerName, customerPhone, totalBuys);

        return table2;
    }

    public ObservableList<MaxCustomer> allCustomers() {
        return FXCollections.observableArrayList(new Seller(conn, username).getMaxSalesCustomer());
    }

    public TableView<AllSellerSales> allSellerSales() {
        TableColumn<AllSellerSales, Integer> userID = new TableColumn<>("Employee ID");
        userID.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userID.setMinWidth(320);
        userID.setStyle( "-fx-alignment: CENTER;");

        TableColumn<AllSellerSales, String> userName = new TableColumn<>("Name");
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userName.setMinWidth(320);
        userName.setStyle( "-fx-alignment: CENTER;");

        TableColumn<AllSellerSales, Double> totalBuys = new TableColumn<>("Total Sales");
        totalBuys.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        totalBuys.setMinWidth(320);
        totalBuys.setStyle( "-fx-alignment: CENTER;");

        TableView<AllSellerSales> table2 = new TableView<>();
        table2.setItems(allSellerSalesFunction());
        table2.getColumns().addAll(userID, userName, totalBuys);

        return table2;
    }

    public ObservableList<AllSellerSales> allSellerSalesFunction() {
        return FXCollections.observableArrayList(new Seller(conn, username).getAllSellerSales());
    }
}
