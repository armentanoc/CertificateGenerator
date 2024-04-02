package Application;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MailManagement implements Initializable {
	private AlertMessage alert = new AlertMessage();
	private MouseDrag mousedrag = new MouseDrag();
	private SelectForm form = new SelectForm();
	private Stage stage = new Stage();
	private EmailContent content = new EmailContent();

	Image icon = null;
	FXMLLoader loader = null;

	@FXML
	private Button MailAddFiles;

	@FXML
	private TextArea MailFiles;

	@FXML
	private Button MailLeave;

	@FXML
	private TextArea MailMessage;

	@FXML
	private Button MailRemoveFiles;

	@FXML
	private TextField MailSender;

	@FXML
	private TextField MailSubject;

	@FXML
	private Label MailUser;

	@FXML
	private AnchorPane mainPage;

	private void selectPage() {
		try {
			mainPage.getScene().getWindow().hide();
			form.selectz("/design/maca1.png", "/design/Login.fxml", "/design/base.css");
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

	public void addFiles() {
		MailFiles.setText(null);
	}

	public void removeFiles() {
		
	}

	public void sendMail() {
		String subject = MailSubject.getText();
		String sender = MailSender.getText();
		String message = MailMessage.getText();
		
		
		
	}

	public void setUsername() {
		try {
			String text = listInfo.username;
			if (!text.isEmpty()) {
				text = text.substring(0, 1).toUpperCase() + text.substring(1);
			}
			MailUser.setText(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void leave() {
		alert.information("Leaving");
		selectPage();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUsername();
	}
	
}
