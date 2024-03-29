package br.ucsal.certificateGenerator.domain;
public class Participante {
	private String nome;
    private String cpf;
    private String email;

    public Participante(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
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
}