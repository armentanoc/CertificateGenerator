
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.text.Document;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import br.ucsal.certificateGenerator.application.EmailService;
import br.ucsal.certificateGenerator.application.ParticipanteService;
import br.ucsal.certificateGenerator.domain.Participante;

public class Main {

	public static void main(String[] args) {
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
			emailService.enviarEmails(listaParticipantes);

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
}