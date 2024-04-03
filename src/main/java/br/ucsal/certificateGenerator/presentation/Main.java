package br.ucsal.certificateGenerator.presentation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import br.ucsal.certificateGenerator.application.EmailService;
import br.ucsal.certificateGenerator.application.ParticipanteService;
import br.ucsal.certificateGenerator.domain.Participante;

public class Main extends Application {

	private final FileChooser fileChooser = new FileChooser();

	@Override
	public void start(Stage primaryStage) {
		TextField nomeEventoField = new TextField("Nome do Evento");
		TextField cargaHorariaField = new TextField("Carga Horaria");
		Button submitButton = new Button("Anexar Planilha e Enviar E-mails");
		TextArea outputTextArea = new TextArea();
		outputTextArea.setEditable(false);
		setupFileChooser();

		submitButton.setOnAction(e -> {
			try {
				outputTextArea.appendText("\nProcessando... Aguarde");
				String nomeEvento = nomeEventoField.getText();
				String cargaHoraria = cargaHorariaField.getText();
				File selectedFile = fileChooser.showOpenDialog(primaryStage);

				if (nomeEvento.isEmpty() || nomeEvento.equals("Nome do Evento") || cargaHoraria.isEmpty() || selectedFile == null)
					throw new IllegalArgumentException("Por favor, preencha todos os campos corretamente e selecione uma planilha.");

				int cargaHorariaEvento = Integer.parseInt(cargaHoraria);
				ParticipanteService participanteService = new ParticipanteService();
				List<Participante> listaParticipantes = participanteService.lerPlanilhaDeParticipantes(selectedFile,
						nomeEvento, cargaHorariaEvento);
				System.out.println(imprimirParticipantes(listaParticipantes)); //debug
				EmailService emailService = new EmailService();
				String result = emailService.enviarEmails(listaParticipantes);
				String message = "\nRelatório de Envio: \nNome do Evento: " + nomeEvento + ", Carga Horária: " + cargaHoraria + "\n"
						+ result;
				outputTextArea.appendText(message);
			} catch (Exception ex) {
				String message = "\nErro: " + ex.getMessage();
				System.err.println(message);
				outputTextArea.appendText(message);
			}
		});

		VBox root = new VBox(10, nomeEventoField, cargaHorariaField, submitButton, outputTextArea);
		Scene scene = new Scene(root, 600, 400);

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

	private static String imprimirParticipantes(List<Participante> listaParticipantes) {
		String result = "";
		for (Participante participante : listaParticipantes) {
			result += participante.toString() + "\n";
			participante = criarDocumentoVazio(participante);
		}
		return result;
	}

	private static Participante criarDocumentoVazio(Participante participante) {
		String certificatePath = "src/main/java/br/ucsal/certificateGenerator/infra/";
		String fileName = certificatePath + participante.getNome() + "_" + participante.getNomeEvento() + ".pdf";
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				PDDocument document = new PDDocument();
				PDPage page = new PDPage();
				document.addPage(page);
				document.save(fileName);
				document.close();
				participante.setCertificate(file);
				System.out.println("Certificado vazio criado para: " + participante.getNome());
			} catch (IOException e) {
				System.err.println("Erro criando certificado: " + e.getMessage());
			}
		} else {
			System.out.println("Certificado já existe para: " + participante.getNome());
			participante.setCertificate(file);
		}
		return participante;
	}
}