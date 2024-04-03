package br.ucsal.certificateGenerator.application;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.ucsal.certificateGenerator.domain.EmailManager;
import br.ucsal.certificateGenerator.domain.Participante;

public class EmailService {

	String host = "smtp.gmail.com";
	String username = "testarmentanoc@gmail.com";
	String password = "zlsu viqz bzfv vthr";
	String result = "";

	public String enviarEmails(List<Participante> participantes) throws InterruptedException {
        StringBuilder result = new StringBuilder();
        int numThreads = Runtime.getRuntime().availableProcessors(); 
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (Participante participante : participantes) {
            Runnable emailTask = () -> {
                EmailManager emailManager = new EmailManager(host, username, password, participante);
                try {
					result.append(emailManager.enviarEmail()).append("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
            };
            executor.submit(emailTask);
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        return result.toString();
    }
}
