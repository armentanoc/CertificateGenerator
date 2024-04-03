package br.ucsal.certificateGenerator.domain;

import java.io.File;

public class Participante {
	
	private String nome;
	private String cpf;
	private String email;
	private String nomeEvento;
	private File certificado;
	private int cargaHorariaEvento;

	public Participante(String nome, String cpf, String email, String nomeEvento, int cargaHorariaEvento) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.nomeEvento = nomeEvento;
		this.cargaHorariaEvento = cargaHorariaEvento;
	}

	public String getNome() {
		return nome;
	}

	public String getCpf() {
		return cpf;
	}

	public String getEmail() {
		return email;
	}

	public String getNomeEvento() {
		return nomeEvento;
	}

	public int getCargaHorariaEvento() {
		return cargaHorariaEvento;
	}
	
	public void setCertificate(File certificate) {
		this.certificado = certificate;
	}
	
	public File getCertificate() {
		return this.certificado;
	}

	@Override
	public String toString() {
		return "Participante [nome=" + nome + ", cpf=" + cpf + ", email=" + email + ", nomeEvento=" + nomeEvento
				+ ", cargaHorariaEvento=" + cargaHorariaEvento + ", certificado=" + certificado.getPath() + "]";
	}
}