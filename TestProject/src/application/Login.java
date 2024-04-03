package application;


import java.net.URL;
import java.util.ResourceBundle;

import controllers.AlertMessage;
import controllers.MouseDrag;
import controllers.SelectForm;
import controllers.listInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Login implements Initializable {
	
	private AlertMessage alert = new AlertMessage();
	private MouseDrag mousedrag = new MouseDrag();
    private SelectForm form = new SelectForm();
	private Stage stage = new Stage();
	
    Image icon = null;
    FXMLLoader loader = null;
    
    @FXML
    private AnchorPane mainPage;
    
    @FXML
    private Button LoginButton;

    @FXML
    private Button LoginClear;

    @FXML
    private TextField LoginEmail;

    @FXML
    private PasswordField LoginPassword;
    
    private void loginAction() {
		try {
			mainPage.getScene().getWindow().hide();
            form.selectz("/design/maca1.png","/design/SendMail.fxml", "/design/base.css");
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

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void lineClear() {
    	LoginEmail.setText("");
    	LoginPassword.setText("");
    }
    
    public void setTheUser() {
    	listInfo.email = LoginEmail.getText();
    	listInfo.password = LoginPassword.getText();
    	alert.confirm("Account connected");
    	loginAction();
    }
    
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}
}
