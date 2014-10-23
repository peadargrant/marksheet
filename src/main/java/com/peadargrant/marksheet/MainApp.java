/* 
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package com.peadargrant.marksheet;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        // changed so we can access the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = (Parent) loader.load();
        MarkSheetController controller = loader.getController();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/com/peadargrant/marksheet/mainWindow.fxml");
        
        stage.setTitle("MarkSheet");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


}
