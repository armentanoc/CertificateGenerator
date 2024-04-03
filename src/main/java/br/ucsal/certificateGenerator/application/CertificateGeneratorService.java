package br.ucsal.certificateGenerator.application;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import br.ucsal.certificateGenerator.domain.Participante;

public class CertificateGeneratorService {

	private List<Participante> listaParticipantes;

	public CertificateGeneratorService(List<Participante> listaParticipantes) {
		this.listaParticipantes = listaParticipantes;
	}

	public List<Participante> gerarCertificados() throws InterruptedException {
        int numThreads = Runtime.getRuntime().availableProcessors(); // Number of available CPU cores
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (Participante participante : listaParticipantes) {
            Runnable certificateTask = () -> criarDocumentoVazio(participante);
            executor.submit(certificateTask);
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        return listaParticipantes;
    }

    private void criarDocumentoVazio(Participante participante) {
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
                System.out.println("[CERTIFICATE GENERATOR SERVICE] Certificado vazio criado para: " + participante.getNome());
            } catch (IOException e) {
                System.err.println("Erro criando certificado: " + e.getMessage());
            }
        } else {
            System.out.println("Certificado já existe para: " + participante.getNome());
            participante.setCertificate(file);
        }
    }
}
