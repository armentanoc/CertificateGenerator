package application;

import controllers.MouseDrag;
import controllers.SelectForm;
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
        SelectForm form = new SelectForm();
        Image icon = null;
        FXMLLoader loader = null;
        try {
            form.selectz("/design/maca1.png","/design/Login.fxml", "/design/base.css");
            icon = form.getIcon();
            loader = form.getLoader();
            Parent root = loader.load();
            Scene scene = new Scene(root); 
            mousedrag.enableDrag(root, stage);

            stage.setResizable(false);
            stage.getIcons().add(icon);
            stage.setTitle("Email Sender");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {e.printStackTrace();}
    }
	
	
}