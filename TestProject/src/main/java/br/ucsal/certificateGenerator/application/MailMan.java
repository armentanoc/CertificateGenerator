package br.ucsal.certificateGenerator.application;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import br.ucsal.certificateGenerator.controllers.AlertMessage;
import br.ucsal.certificateGenerator.controllers.MouseDrag;
import br.ucsal.certificateGenerator.controllers.listInfo;
import br.ucsal.certificateGenerator.domain.Participante;
import br.ucsal.certificateGenerator.presentation.CertificateGeneratorService;
import br.ucsal.certificateGenerator.presentation.EmailService;
import br.ucsal.certificateGenerator.presentation.ParticipanteService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MailMan implements Initializable {
    private final int PROGRESS_BAR_WIDTH = 640;
    private final FileChooser fileChooser = new FileChooser();
    private final AlertMessage alert = new AlertMessage();
    private final MouseDrag mouseDrag = new MouseDrag();
    private final Stage stage = new Stage();

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
    @FXML
    private ProgressBar ProgressiveBar;

    private void selectPage() {
        try {
            mainPage.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(root);

            mouseDrag.enableDrag(root, stage);
            stage.setResizable(false);
            stage.getIcons().add(new Image("maca1.png"));
            stage.setTitle("Email Sender");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFiles() {
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            MailFiles.setText(selectedFile.getAbsolutePath());
        }
    }

    public void sendMail() {
        String subject = MailSubject.getText();
        String sender = MailSender.getText();

        if (subject.isEmpty() || sender.isEmpty()) {
            MailMessage.setText("\nPor favor, preencha todos os campos corretamente");
            return;
        }

        int cargaHoraria;
        try {
            cargaHoraria = Integer.parseInt(sender);
        } catch (NumberFormatException e) {
            MailMessage.setText("\nPor favor, insira um número válido para carga horária");
            return;
        }

        File delta = fileChooser.showOpenDialog(stage);
        if (delta == null) {
            MailMessage.setText("\nPor favor, adicione um arquivo");
            return;
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ParticipanteService participanteService = new ParticipanteService();
                List<Participante> listaParticipantes = participanteService.lerPlanilhaDeParticipantes(delta,
                        subject, cargaHoraria);
                updateProgress(1, 3);

                CertificateGeneratorService certificateGeneratorService = new CertificateGeneratorService(listaParticipantes);
                listaParticipantes = certificateGeneratorService.gerarCertificados();
                updateProgress(2, 3);

                EmailService emailService = new EmailService();
                String result = emailService.enviarEmails(listaParticipantes);
                updateProgress(3, 3);

                String message = "\nRELATÓRIO DE ENVIO DE CERTIFICADOS \nNome do Evento: " + subject + ", Carga Horária: " + cargaHoraria + "\n"
                        + result;
                MailMessage.appendText(message);
                return null;
            }
        };

        ProgressiveBar.progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void setUsername() {
        String text = listInfo.email;
        if (!text.isEmpty()) {
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        MailUser.setText(text);
    }

    public void setAFile() {
        fileChooser.setTitle("Anexar Planilha");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Planilhas", "*.xls", "*.xlsx"));
    }

    public void leave() {
        alert.information("Leaving");
        selectPage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ProgressiveBar.setPrefWidth(PROGRESS_BAR_WIDTH);
        setUsername();
    }
}
