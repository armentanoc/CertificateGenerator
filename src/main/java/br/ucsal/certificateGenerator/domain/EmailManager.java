package br.ucsal.certificateGenerator.domain;

import java.io.IOException;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailManager {

	private Participante participante;
	private EmailContent emailContent;
	private EmailSender emailSender;
	private String host;
	private String username;
	private String password;

	public EmailManager(String host, String username, String password, Participante participante) {
		this.participante = participante;
		this.host = host;
		this.username = username;
		this.password = password;
		this.emailSender = new EmailSender(this.host, this.username, this.password);
		this.emailContent = new EmailContent(this.participante);
	}

	public String enviarEmail() throws IOException {
		String email = participante.getEmail();
		if (EmailValidator.getInstance().isValid(email))
			return this.emailSender.sendEmail(email, this.emailContent);
		else
			return "Erro ao enviar e-mail para: " + participante.getEmail() + " (email invalido)";
	}
}