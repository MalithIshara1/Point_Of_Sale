import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FXMLDocumentController {
    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    public void loginAdmin(){
        String sql = "select * from admin where username = ? and password = ?";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1,username.getText());
            prepare.setString(2,password.getText());

            result = prepare.executeQuery();
            Alert alert;

            if (username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Massage");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the all blank fields");
                alert.showAndWait();
            }else {
                if (result.next()){

                    getData.username = username.getText();


                        alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login");
                        alert.showAndWait();

                    Parent parent = FXMLLoader.load(this.getClass().getResource("dashboard.fxml"));
                    Scene scene = new Scene(parent);


                   Stage primaryStage = (Stage) main_form.getScene().getWindow();

                 parent.setOnMousePressed((MouseEvent event) ->{
                     x = event.getSceneX();
                     y = event.getSceneY();
                 });

                 parent.setOnMouseDragged((MouseEvent event) ->{
                     primaryStage.setX(event.getScreenX() -x);
                     primaryStage.setY(event.getScreenY() -y);
                 });



                   primaryStage.setScene(scene);
                   primaryStage.centerOnScreen();
                   primaryStage.setTitle("Dashboard");
                }else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Massage");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password");
                    alert.showAndWait();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        System.exit(0);
    }
}
