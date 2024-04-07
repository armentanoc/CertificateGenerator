package br.ucsal.certificateGenerator.domain;

import java.io.File;

public class EmailContent {

	private Participante participante;
	private String subject;
	private String body;
	private File certificate;

	public EmailContent(Participante participante) {
		this.participante = participante;
		this.subject = "Certificado do Evento " + this.participante.getNomeEvento() + " ("
				+ this.participante.getCargaHorariaEvento() + "h)";
		this.body = "<h1>Olá, " + this.participante.getNome() + "!</h1>" + "<p>Agradecemos a participação no Evento "
				+ this.participante.getNomeEvento() + ". " + "<br>Segue, em anexo o seu certificado referente a "
				+ this.participante.getCargaHorariaEvento() + " horas.</p>";
		this.certificate = this.participante.getCertificate();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public File getCertificate() {
		return certificate;
	}
}
