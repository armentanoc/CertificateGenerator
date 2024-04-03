package br.ucsal.certificateGenerator.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import br.ucsal.certificateGenerator.application.EmailService;
import br.ucsal.certificateGenerator.application.ParticipanteService;
import br.ucsal.certificateGenerator.domain.Participante;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		try {
			String nomeEvento = "Seminário de Engenharia de Software";
			int cargaHorariaEvento = 10;
			String participantesFilePath = "src/main/java/br/ucsal/certificateGenerator/infra/participantes.xls";

			ParticipanteService participanteService = new ParticipanteService();
			FileInputStream fis = new FileInputStream(participantesFilePath);

			List<Participante> listaParticipantes = participanteService.lerPlanilhaDeParticipantes(fis, nomeEvento,
					cargaHorariaEvento);
			
			// Set the title of the stage
			primaryStage.setTitle("Certificate Generator");
			
			// Create a label
			Label label = new Label(imprimirParticipantes(listaParticipantes));

			// Create a layout pane
			StackPane root = new StackPane();
			root.getChildren().add(label);

			// Create a scene
			Scene scene = new Scene(root, 800, 800);

			// Set the scene on the primary stage
			primaryStage.setScene(scene);
			// Show the stage
			primaryStage.show();

			EmailService emailService = new EmailService();
			emailService.enviarEmails(listaParticipantes);

		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	private static String imprimirParticipantes(List<Participante> listaParticipantes) {
		String result = "";
		for (Participante participante : listaParticipantes) {
			result += participante.toString()+"\n";
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