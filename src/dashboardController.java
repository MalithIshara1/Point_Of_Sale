import c.customerData;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.*;


public class dashboardController {

    @FXML
    private AnchorPane root;
    @FXML
    private Button addProduct_addBtn;

    @FXML
    private TextField addProduct_brand;

    @FXML
    private Button addProduct_btn;

    @FXML
    private TableColumn<productData, String> addProduct_col_brand;

    @FXML
    private TableColumn<productData, String> addProduct_col_price;

    @FXML
    private TableColumn<productData, String> addProduct_col_productId;

    @FXML
    private TableColumn<productData, String> addProduct_col_productName;

    @FXML
    private TableColumn<productData, String> addProduct_col_status;

    @FXML
    private TableColumn<productData, String> addProduct_col_type;

    @FXML
    private Button addProduct_deleteBtn;

    @FXML
    private AnchorPane addProduct_form;

    @FXML
    private ImageView addProduct_imageView;

    @FXML
    private Button addProduct_importBtn;

    @FXML
    private TextField addProduct_price;

    @FXML
    private TextField addProduct_productName;

    @FXML
    private ComboBox<?> addProduct_productType;

    @FXML
    private TextField addProduct_productid;

    @FXML
    private Button addProduct_resetBtn;

    @FXML
    private TextField addProduct_search;

    @FXML
    private ComboBox<?> addProduct_status;

    @FXML
    private TableView<productData> addProduct_tableView;

    @FXML
    private Button addProduct_updateBtn;

    @FXML
    private Button close;

    @FXML
    private Label home_availableProduct;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AreaChart<?, ?> home_incomeChart;

    @FXML
    private Label home_numberOrder;

    @FXML
    private BarChart<?, ?> home_orderChart;

    @FXML
    private Label home_totalIncome;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private TextField order_amount;

    @FXML
    private Label order_blance;

    @FXML
    private ComboBox<?> order_brandName;

    @FXML
    private Button order_btn;

    @FXML
    private TableColumn<customerData, String> order_col_brand;

    @FXML
    private TableColumn<customerData, String> order_col_price;

    @FXML
    private TableColumn<customerData, String> order_col_productName;

    @FXML
    private TableColumn<customerData, String> order_col_quantity;

    @FXML
    private TableColumn<customerData, String> order_col_type;

    @FXML
    private AnchorPane order_form;

    @FXML
    private Button order_payBtn;

    @FXML
    private ComboBox<?> order_productName;

    @FXML
    private ComboBox<?> order_productType;

    @FXML
    private Spinner<Integer> order_quantity;

    @FXML
    private Button order_receiptBtn;

    @FXML
    private Button order_resetBtn;

    @FXML
    private TableView<customerData> order_tableView;

    @FXML
    private Label order_total;

    @FXML
    private Label username;

    @FXML
    private Button order_addBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    public void homeDisplayTotalOrders(){

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "select count(id) from customer where date ='"+sqlDate+"' ";

        connect = database.connectDb();

        int countOrder = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                countOrder = result.getInt("count(id)");
            }
            home_numberOrder.setText(String.valueOf(countOrder));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void homeTotalIncome(){
        String sql = "select sum(total) from customer_receipt";

        connect =database.connectDb();
        double totalIncome = 0;

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                totalIncome =result.getInt("sum(total)");
            }
            home_totalIncome.setText("$" +String.valueOf(totalIncome));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void homeAvailableProducts(){
        String sql = "select count(id) from product where status = 'Available' ";

        connect =database.connectDb();

        int countAP = 0;
        try {
            prepare =connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                countAP = result.getInt("count(id)");
            }
            home_availableProduct.setText(String.valueOf(countAP));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void homeIncomeChart(){
        home_incomeChart.getData().clear();

        String sql = "select date, sum(total) from customer_receipt group by date order by  timestamp(date) asc limit 6";

        connect = database.connectDb();

        try{

            XYChart.Series chart = new XYChart.Series();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                chart.getData().add(new XYChart.Data(result.getString(1),result.getInt(2)));
            }

            home_incomeChart.getData().add(chart);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void homeDataChart(){

        XYChart.Series chart = new XYChart.Series();

        home_orderChart.getData().clear();
        String sql = "select date, count(id) from customer group by date order by timestamp(date) asc limit 5 ";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                chart.getData().add(new XYChart.Data(result.getString(1),result.getInt(2)));
            }

            home_orderChart.getData().add(chart);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addProductAdd(){
        String sql = "insert into product (product_id,type,brand,productName,price,status,image,date)" + "values(?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;

            if (addProduct_productid.getText().isEmpty() || addProduct_productType.getSelectionModel().getSelectedItem() == null || addProduct_brand.getText().isEmpty() || addProduct_productName.getText().isEmpty() || addProduct_price.getText().isEmpty() || addProduct_status.getSelectionModel().getSelectedItem() == null || getData.path == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the all blank fields");
                alert.showAndWait();
            }else {

                String checkData = "select product_id from product where product_id ='"+addProduct_productid.getText()+"' ";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID : " + addProduct_productid.getText() + "was already exist!");
                    alert.showAndWait();
                }else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, addProduct_productid.getText());
                    prepare.setString(2, (String) addProduct_productType.getSelectionModel().getSelectedItem());
                    prepare.setString(3, addProduct_brand.getText());
                    prepare.setString(4, addProduct_productName.getText());
                    prepare.setString(5, addProduct_price.getText());
                    prepare.setString(6, (String) addProduct_status.getSelectionModel().getSelectedItem());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");
                    prepare.setString(7, uri);

                    Date date = new Date();
                   java.util.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(8,String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    addProductsShowListData();

                    addProductReset();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addProductUpdate(){

        String uri = getData.path;
        uri = uri.replace("\\","\\\\");

       Date date =  new Date();
       java.util.Date sqlDate = new java.util.Date(date.getTime());

        String sql = "update product set type = '"
                +addProduct_productType.getSelectionModel().getSelectedItem()+"', brand = '"
                +addProduct_brand.getText()+"', productName = '"+addProduct_productName.getText()
                +"', price ='"+ addProduct_price.getText()
                +"', status = '"+addProduct_status.getSelectionModel().getSelectedItem()
                +"', image = '"+uri+"',  date = '"+sqlDate+"' where product_id = '"+addProduct_productid.getText()+"' " ;

        connect = database.connectDb();
        try{

            Alert alert;

            if (addProduct_productid.getText().isEmpty() || addProduct_productType.getSelectionModel().getSelectedItem() == null || addProduct_brand.getText().isEmpty() || addProduct_productName.getText().isEmpty() || addProduct_price.getText().isEmpty() || addProduct_status.getSelectionModel().getSelectedItem() == null || getData.path == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the all blank fields");
                alert.showAndWait();
            }else {
                alert =new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure want to UPDATE product ID: " +addProduct_productid.getText() + " ?" );
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Infomation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Update ");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductReset();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addProductDelete(){
        String sql = "delete from product where product_id ='"+addProduct_productid.getText()+"' ";

        connect = database.connectDb();

        try {
            Alert alert;

            if (addProduct_productid.getText().isEmpty() || addProduct_productType.getSelectionModel().getSelectedItem() == null || addProduct_brand.getText().isEmpty() || addProduct_productName.getText().isEmpty() || addProduct_price.getText().isEmpty() || addProduct_status.getSelectionModel().getSelectedItem() == null || getData.path == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the all blank fields");
                alert.showAndWait();
            }else {
                alert =new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure want to DELETE product ID: " +addProduct_productid.getText() + " ?" );
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Infomation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted ");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductReset();
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addProductReset(){
        addProduct_productid.setText("");
        addProduct_productType.getSelectionModel().clearSelection();
        addProduct_brand.setText("");
        addProduct_productName.setText("");
        addProduct_price.setText("");
        addProduct_status.getSelectionModel().clearSelection();
        addProduct_imageView.setImage(null);

        getData.path = "";


    }

    public void addImportImage(){
        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*jpg","*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null){

            getData.path =file.getAbsolutePath();

            image = new Image(file.toURI().toString(),115,128 , false,true);
            addProduct_imageView.setImage(image);
        }

    }

    private String[] listType ={"Snakcs","Drinks","Dessert","Gadgets","Personal Product","Others"};

    public void addProductListStyle(){
        List<String> listT = new ArrayList<>();

        for (String data: listType){
            listT.add(data);

        }
        ObservableList listData = FXCollections.observableArrayList(listT);
        addProduct_productType.setItems(listData);
    }

    public void addProducrSearch(){
        FilteredList<productData> filter =new FilteredList<>(addProductList, e -> true);

        addProduct_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateProductData ->{

                if (newValue == null || newValue.isEmpty()) {

                    return true;
                }

                String searchKey = newValue.toLowerCase(Locale.ROOT);

                if (predicateProductData.getProductid().toString().contains(searchKey)){
                    return true;
                }else if(predicateProductData.getType().toLowerCase(Locale.ROOT).contains(searchKey)){
                    return true;
                }else if (predicateProductData.getBrand().toLowerCase(Locale.ROOT).contains(searchKey)){
                    return true;
                }else if(predicateProductData.getProductName().toLowerCase(Locale.ROOT).contains(searchKey)){
                    return true;
                }else if (predicateProductData.getPrice().toString().contains(searchKey)){
                    return true;
                }else if (predicateProductData.getStatus().toLowerCase(Locale.ROOT).contains(searchKey)){
                    return true;
                }else
                return false;
            });
        });

        SortedList<productData> sortedList =new SortedList<>(filter);

        sortedList.comparatorProperty().bind(addProduct_tableView.comparatorProperty());
        addProduct_tableView.setItems(sortedList);
    }

    private String [] listStatus = {"Available" ,"Not Available"};

    public void addProductStatus(){
        List<String> listS = new ArrayList<>();

        for (String data: listStatus){
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        addProduct_status.setItems(listData);
    }

    public ObservableList<productData> addProductsListData(){

        ObservableList<productData> producList = FXCollections.observableArrayList();

        String sql = "select * from product";
        connect = database.connectDb();

        try {
            prepare =connect.prepareStatement(sql);
            result =prepare.executeQuery();
            productData prodD;
            while (result.next()){
                prodD =new productData(result.getInt("product_id"),result.getString("type"),result.getString("brand"),result.getString("productName"),result.getDouble("price"),result.getString("status"),result.getString("image"),result.getDate("date"));

                producList.add(prodD);

            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return producList;
    }
    private ObservableList<productData> addProductList;
    public void addProductsShowListData(){

        addProductList = addProductsListData();

        addProduct_col_productId.setCellValueFactory(new PropertyValueFactory<>("productid"));
        addProduct_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addProduct_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addProduct_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addProduct_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProduct_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        addProduct_tableView.setItems(addProductList);
    }

    public void addProductSelect(){
        productData proD = addProduct_tableView.getSelectionModel().getSelectedItem();
        int num = addProduct_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1){
            return;
        }
        addProduct_productid.setText(String.valueOf(proD.getProductid()));
        addProduct_brand.setText(proD.getBrand());
        addProduct_productName.setText(proD.getProductName());
        addProduct_price.setText(String.valueOf(proD.getPrice()));

        String uri = "file:" + proD.getImage();

        image = new Image(uri,115,128 ,false , true);

        addProduct_imageView.setImage(image);
        getData.path =proD.getImage();

    }



    public void ordersAdd(){
        customerId();
        String sql = "insert into customer (customer_id,type,brand,productName,quantity,price,date)" + "values(?,?,?,?,?,?,?)";

        connect =database.connectDb();

        try {

            String checkData = "select * from product where productName ='"+order_productName.getSelectionModel().getSelectedItem()+"' ";

            double priceData = 0;

            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            if (result.next()){
                priceData = result.getDouble("price");
            }

            double totalPData = (priceData * qty);
                Alert alert;
            if (order_productType.getSelectionModel().getSelectedItem() == null || (String) order_brandName.getSelectionModel().getSelectedItem() == null || (String) order_productName.getSelectionModel().getSelectedItem() == null || totalPData == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose product first");
                alert.showAndWait();
            }else {

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, String.valueOf(customerid));
                prepare.setString(2, (String) order_productType.getSelectionModel().getSelectedItem());
                prepare.setString(3, (String) order_brandName.getSelectionModel().getSelectedItem());
                prepare.setString(4, (String) order_productName.getSelectionModel().getSelectedItem());
                prepare.setString(5, String.valueOf(qty));


                prepare.setString(6, String.valueOf(totalPData));

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(7, String.valueOf(sqlDate));

                prepare.executeUpdate();

                ordersShowListData();
                ordersDisplayTotal();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void orderPay(){
        customerId();
        String sql = "insert into customer_receipt (customer_id,total,amount,balance,date)" + "values (?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;
            if (totalP > 0 || order_amount.getText().isEmpty() || amountP == 0){

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1,String.valueOf(customerid));
                    prepare.setString(2,String.valueOf(totalP));
                    prepare.setString(3,String.valueOf(amountP));
                    prepare.setString(4,String.valueOf(balanceP));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(5,String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();


                    ordersShowListData();

                    totalP = 0;
                    balanceP = 0;
                    amountP = 0;

                    order_blance.setText("$0.0");
                    order_amount.setText("");
                }else return;
            }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid :3");
                alert.showAndWait();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void odersReceipt(){

    }

    public void odersReset(){

        customerId();
        String sql = "delete from customer where customer_id '"+customerid+"' ";
        
        connect = database.connectDb();
        
         try{
            if (!order_tableView.getItems().isEmpty()){

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setContentText("Are you sure want to reset ? ");
                alert.setHeaderText(null);
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals( ButtonType.OK)){

                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    ordersShowListData();

                    totalP = 0;
                    balanceP = 0;
                    amountP = 0;

                    order_blance.setText("$0.0");
                    order_total.setText("S0.0");
                    order_amount.setText("");

                }
            }

         }catch (Exception e){
             e.printStackTrace();
         }
    }

    private double amountP;
    private double balanceP;
    public void ordersAmount() {

        Alert alert;

        if (!order_amount.getText().isEmpty()) {
            amountP = Double.parseDouble(order_amount.getText());

            if (totalP > 0) {
                if (amountP >= totalP) {
                    balanceP = (amountP - totalP);

                    order_blance.setText("$" + String.valueOf(balanceP));
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid :3");
                    alert.showAndWait();

                    order_amount.setText("");

                }

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Invalid :3");
                alert.showAndWait();
            }
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            alert.showAndWait();
        }
    }


    private double totalP;
    public void ordersDisplayTotal(){
        customerId();
        String sql = "select  sum(price) from customer where customer_id = '"+customerid+"'";

        connect = database.connectDb();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                totalP = result.getDouble("sum(price)");
            }
            order_total.setText("$"+String.valueOf(totalP));

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private String[] orderlistType ={"Snakcs","Drinks","Dessert","Gadgets","Personal Product","Others"};

    public void orderListStyle(){
        List<String> listT = new ArrayList<>();

        for (String data: orderlistType){
            listT.add(data);

        }
        ObservableList listData = FXCollections.observableArrayList(listT);
        order_productType.setItems(listData);

        orderListBrand();
    }

    public void orderListBrand(){


     String sql = "select brand from product where type ='"+order_productType.getSelectionModel().getSelectedItem()+"' and status ='Available'";
//
//        String sql ="select * from product where type ='"
//                +order_productType.getSelectionModel().getSelectedItem()
//                +"' and status = 'Available' group by brand";

        connect = database.connectDb();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()){
                listData.add(result.getString("brand"));
            }

            order_brandName.setItems(listData);
            orderListProductName();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void orderListProductName(){

        String sql = "select productName from product where brand ='"+order_brandName.getSelectionModel().getSelectedItem()+"' ";

        connect = database.connectDb();

        try {

            prepare =connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()){
                listData.add(result.getString("productName"));
            }
            order_productName.setItems(listData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private SpinnerValueFactory<Integer> spinner;

    public void ordersSpinner(){

        spinner =new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20,0);

        order_quantity.setValueFactory(spinner);
    }

    private int qty;
    public void ordersShowSpinnerValue(){
         qty =  order_quantity.getValue();
    }

    public ObservableList<customerData> ordersListData(){

        customerId();
        ObservableList<customerData> listData = FXCollections.observableArrayList();
        String sql ="select * from customer where customer_id ='"+customerid+"' ";

        connect = database.connectDb();

        try {
            customerData customerD;
            prepare = connect.prepareStatement(sql);
            result =prepare.executeQuery();

            while (result.next()){
                customerD =new customerData(result.getInt("customer_id"),result.getString("type"),result.getString("brand"),result.getString("productName"),result.getInt("quantity"),result.getDouble("price"),result.getDate("date"));

                listData.add(customerD);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<customerData> ordersList;

    public void ordersShowListData(){
        ordersList = ordersListData();

        order_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        order_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        order_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        order_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        order_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        order_tableView.setItems(ordersList);
        ordersDisplayTotal();

    }

    private int customerid;
    public void customerId(){

        String customerId = "select * from customer";

        connect =database.connectDb();
        try {
            prepare = connect.prepareStatement(customerId);
            result = prepare.executeQuery();

            int checkId = 0;

            while (result.next()){
                customerid = result.getInt("customer_id");
            }

            String checkData = "select * from customer_receipt";

            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            while (result.next()){
                checkId = result.getInt("customer_id");
            }

            if(customerid == 0){
                customerid+=1;
            }else if(checkId == customerid){
                customerid+=1;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void switchform(ActionEvent event){

        if(event.getSource() == home_btn){
            home_form.setVisible(true);
            addProduct_form.setVisible(false);
            order_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right,#25a473,#99892b);");
            addProduct_btn.setStyle("-fx-background-color: transparent");
            order_btn.setStyle("-fx-background-color: transparent");

            homeDisplayTotalOrders();
            homeTotalIncome();
            homeAvailableProducts();

            homeIncomeChart();
            homeDataChart();

        }else if (event.getSource() == addProduct_btn){

            home_form.setVisible(false);
            addProduct_form.setVisible(true);
            order_form.setVisible(false);

            addProduct_btn.setStyle("-fx-background-color: linear-gradient(to bottom right,#25a473,#99892b);");
            home_btn.setStyle("-fx-background-color: transparent");
            order_btn.setStyle("-fx-background-color: transparent");

            addProductsShowListData();
            addProductStatus();
            addProductListStyle();
            addProducrSearch();

        }else if (event.getSource() == order_btn){

            home_form.setVisible(false);
            addProduct_form.setVisible(false);
            order_form.setVisible(true);

            order_btn.setStyle("-fx-background-color: linear-gradient(to bottom right,#25a473,#99892b);");
            addProduct_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");

            orderListStyle();
            ordersShowListData();
            orderListBrand();
            orderListProductName();
            ordersSpinner();

        }
    }

    public void  defaultNav(){
        home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right,#25a473,#99892b);");

    }

    private double x = 0;
    private double y = 0;
    public void logout(){


        try {
            Alert alert = new Alert( Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Confirmation Message");
            alert.setContentText("Are you sure want to log out ?");
            Optional<ButtonType> option =   alert.showAndWait();

            if(option.get().equals(ButtonType.OK)) {
                Parent parent = FXMLLoader.load(this.getClass().getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage = (Stage) main_form.getScene().getWindow();

                parent.setOnMousePressed((MouseEvent event) ->{
                    x = event.getSceneX();
                    y = event.getSceneY();

                });

                parent.setOnMouseDragged((MouseEvent event) ->{
                    primaryStage.setX(event.getScreenX() - x);
                    primaryStage.setY(event.getScreenY() - y) ;

                    primaryStage.setOpacity(.8);

                });

                parent.setOnMouseReleased((MouseEvent event) ->{
                    primaryStage.setOpacity(1);
                });

                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                primaryStage.setTitle("Login Page");
            }else return;

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void displayUsername(){
        username.setText(getData.username);
    }

    public void minimize(){
        Stage primaryStage =(Stage) main_form.getScene().getWindow();
        primaryStage.setIconified(true);
    }

    public void close(){
        System.exit(0);
    }

    public void initialize(){

        displayUsername();

        defaultNav();

        homeDisplayTotalOrders();
        homeTotalIncome();
        homeAvailableProducts();

        homeIncomeChart();
        homeDataChart();

        addProductsShowListData();
        addProductStatus();
        addProductListStyle();

        ordersShowListData();
        orderListStyle();
        orderListBrand();
        orderListProductName();
        ordersSpinner();
        //order_total.setText("");
    }
}
