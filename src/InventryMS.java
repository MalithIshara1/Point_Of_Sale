import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class InventryMS extends Application {

    private double x = 0;
    private double y = 0;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(parent);

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

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
