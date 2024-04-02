package br.ucsal.certificateGenerator.application;

import java.io.IOException;
import java.util.List;

import br.ucsal.certificateGenerator.domain.EmailManager;
import br.ucsal.certificateGenerator.domain.Participante;
import controllers.listInfo;

public class EmailService {

	String host = "smtp.gmail.com";
	String username = "testarmentanoc@gmail.com";
	String password = "zlsu viqz bzfv vthr";
	//String username = System.getenv("GMAIL_USERNAME");
	//String password = System.getenv("GMAIL_PASSWORD");
	// app password específico para CertificateGenerator

	public void enviarEmails(List<Participante> participantes) throws IOException {
		for (Participante participante : participantes) {
			EmailManager emailManager = new EmailManager(host, listInfo.email, listInfo.password, participante);
			emailManager.enviarEmail();
		}
	}
}
