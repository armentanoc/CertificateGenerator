package br.ucsal.certificateGenerator.presentation;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import br.ucsal.certificateGenerator.application.CertificateGeneratorService;
import br.ucsal.certificateGenerator.application.EmailService;
import br.ucsal.certificateGenerator.application.ParticipanteService;
import br.ucsal.certificateGenerator.domain.Participante;

public class Main extends Application {

    private final FileChooser fileChooser = new FileChooser();
    private ProgressBar progressBar = new ProgressBar(0);
    private final int HEIGHT = 600;
    private final int WIDTH = 400;
    private final int PROGRESS_BAR_WIDTH = 300;
    
    @Override
    public void start(Stage primaryStage) {
        TextField nomeEventoField = new TextField("Nome do Evento");
        TextField cargaHorariaField = new TextField("Carga Horaria");
        Button submitButton = new Button("Anexar Planilha e Enviar E-mails");
        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        setupFileChooser();

        progressBar.setPrefWidth(PROGRESS_BAR_WIDTH);

        submitButton.setOnAction(e -> {
        	
        	String message = "\nProcessamento iniciado... Aguarde";
            outputTextArea.appendText(message);
            
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile == null) {
                return;
            }

            String nomeEvento = nomeEventoField.getText();
            String cargaHoraria = cargaHorariaField.getText();

            if (nomeEvento.isEmpty() || nomeEvento.equals("Nome do Evento") || cargaHoraria.isEmpty()) {
                outputTextArea.appendText("\nPor favor, preencha todos os campos corretamente.");
                return;
            }

            int cargaHorariaEvento = Integer.parseInt(cargaHoraria);

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ParticipanteService participanteService = new ParticipanteService();
                    List<Participante> listaParticipantes = participanteService.lerPlanilhaDeParticipantes(selectedFile,
                            nomeEvento, cargaHorariaEvento);
                    updateProgress(1, 3);

                    CertificateGeneratorService certificateGeneratorService = new CertificateGeneratorService(listaParticipantes);
                    listaParticipantes = certificateGeneratorService.gerarCertificados();
                    updateProgress(2, 3);

                    EmailService emailService = new EmailService();
                    String result = emailService.enviarEmails(listaParticipantes);
                    updateProgress(3, 3);

                    String message = "\nRELATÓRIO DE ENVIO DE CERTIFICADOS \nNome do Evento: " + nomeEvento + ", Carga Horária: " + cargaHoraria + "\n"
                            + result;
                    outputTextArea.appendText(message);
                    return null;
                }
            };

            progressBar.progressProperty().bind(task.progressProperty());

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

        VBox root = new VBox(10, nomeEventoField, cargaHorariaField, submitButton, outputTextArea, progressBar);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, HEIGHT, WIDTH);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Certificate Generator");
        primaryStage.show();
    }

    private void setupFileChooser() {
        fileChooser.setTitle("Anexar Planilha");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Planilhas", "*.xls", "*.xlsx"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

