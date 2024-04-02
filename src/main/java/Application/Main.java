package Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import br.ucsal.certificateGenerator.application.EmailService;
import br.ucsal.certificateGenerator.application.ParticipanteService;
import br.ucsal.certificateGenerator.domain.Participante;
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
		try {
			String nomeEvento = "Seminário de Engenharia de Software";
			int cargaHorariaEvento = 10;
			String participantesFilePath = "src/main/java/br/ucsal/certificateGenerator/infra/participantes.xls";

			ParticipanteService participanteService = new ParticipanteService();
			FileInputStream fis = new FileInputStream(participantesFilePath);

			List<Participante> listaParticipantes = participanteService.lerPlanilhaDeParticipantes(fis, nomeEvento,
					cargaHorariaEvento);
			
			imprimirParticipantes(listaParticipantes);

			EmailService emailService = new EmailService();
			//emailService.enviarEmails(listaParticipantes);

		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	private static void imprimirParticipantes(List<Participante> listaParticipantes) {
		for (Participante participante : listaParticipantes) {
			System.out.println(participante.toString());
			participante = criarDocumentoVazio(participante);
		}
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
	
    @Override
    public void start(Stage stage) {
    	MouseDrag mousedrag = new MouseDrag();
        SelectForm form = new SelectForm();
        Image icon = null;
        FXMLLoader loader = null;
        try {
            form.selectz("/java/design/maca1.png","/java/design/Login.fxml", "/java/design/base.css");
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