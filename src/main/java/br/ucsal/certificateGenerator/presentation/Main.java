
package br.ucsal.certificateGenerator.application;

import br.ucsal.certificateGenerator.controllers.MouseDrag;
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
	https://github.com/armentanoc/CertificateGenerator/pull/1/conflict?name=src%252Fmain%252Fjava%252Fbr%252Fucsal%252FcertificateGenerator%252Fpresentation%252FMain.java&base_oid=a44688044440f8dc783f9a4d7de89bef13db1996&head_oid=ee3cdf112bffbd6f442479388dd4110cdcdce337
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