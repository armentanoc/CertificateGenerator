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
    private File planilhaParticipantes;

    @FXML
    private Button MailAddFiles;
    @FXML
    private TextField MailFiles;
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
            stage.setTitle("Certificate Generator");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFiles() {
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            planilhaParticipantes = selectedFile;
            MailFiles.setText(selectedFile.getAbsolutePath());
        }
    }

    public void sendMail() {

    MailMessage.setStyle("-fx-text-fill: black;"); 

    String nomeDoEvento = MailSubject.getText();
    String cargaHorariaText = MailSender.getText();

    if (nomeDoEvento.isEmpty() || cargaHorariaText.isEmpty()) {
        MailMessage.setText("\nPor favor, preencha o nome do evento e a carga horária corretamente");
        return;
    }

    if(planilhaParticipantes == null) {
        MailMessage.setText("\nÉ obrigatório anexar um arquivo do tipo planilha com os participantes do evento (Nome, CPF e email).");
        return;
    }

    int cargaHoraria;
    try {
        cargaHoraria = Integer.parseInt(cargaHorariaText);
    } catch (NumberFormatException e) {
        MailMessage.setText("\nPor favor, insira um número válido para carga horária");
        return;
    }

    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            ParticipanteService participanteService = new ParticipanteService();
            List<Participante> listaParticipantes = participanteService.lerPlanilhaDeParticipantes(planilhaParticipantes,
                    nomeDoEvento, cargaHoraria);
            updateProgress(1, 3);

            CertificateGeneratorService certificateGeneratorService = new CertificateGeneratorService(listaParticipantes);
            listaParticipantes = certificateGeneratorService.gerarCertificados();
            updateProgress(2, 3);

            EmailService emailService = new EmailService();
            String result = emailService.enviarEmails(listaParticipantes);
            updateProgress(3, 3);

            String message = "\nRELATÓRIO DE ENVIO DE CERTIFICADOS \nNome do Evento: " + nomeDoEvento + ", Carga Horária: " + cargaHoraria + "\n"
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

public void setAFile() {
    fileChooser.setTitle("Anexar Planilha");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Planilhas", "*.xls", "*.xlsx")
    );
}

    public void setUsername() {
        String email = listInfo.email;
        if (!email.isEmpty()) {
            int atIndex = email.indexOf('@');
            String username = atIndex != -1 ? email.substring(0, atIndex) : email;
            MailUser.setText(username.toUpperCase());
        }
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
