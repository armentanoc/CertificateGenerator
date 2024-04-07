
package br.ucsal.certificateGenerator.presentation;

import br.ucsal.certificateGenerator.presentation.controllers.MouseDrag;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
    @Override
    public void start(Stage stage) {
    	MouseDrag mousedrag = new MouseDrag();
        try {
        	Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
			Scene scene = new Scene(root);
            mousedrag.enableDrag(root, stage);

            stage.setResizable(false);
            stage.getIcons().add(new Image("maca1.png"));
            stage.setTitle("Certificate Generator");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {e.printStackTrace();}
    }
}