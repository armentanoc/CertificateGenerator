package br.ucsal.certificateGenerator.application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import br.ucsal.certificateGenerator.domain.Participante;

public class CertificateGeneratorService {

	private List<Participante> listaParticipantes;

	public CertificateGeneratorService(List<Participante> listaParticipantes) {
		this.listaParticipantes = listaParticipantes;
	}

	private Participante criarDocumentoVazio(Participante participante) {
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

	public List<Participante> gerarCertificados() {
		for (Participante participante : listaParticipantes) {
			participante = criarDocumentoVazio(participante);
			System.out.println(participante.toString() + "\n");
		}
		return listaParticipantes;
	}
}
