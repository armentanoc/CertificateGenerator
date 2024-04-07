
package br.ucsal.certificateGenerator.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.ucsal.certificateGenerator.domain.Participante;

public class ParticipanteService {

    public List<Participante> lerPlanilhaDeParticipantes(File selectedFile, String nomeEvento, int cargaHorariaEvento) {
        List<Participante> listaParticipantes = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(selectedFile);
             XSSFWorkbook planilha = new XSSFWorkbook(fis)) {
            XSSFSheet participantes = planilha.getSheetAt(0);
            int numThreads = Runtime.getRuntime().availableProcessors(); // Number of available CPU cores
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            // Processando em paralelo com threads
            for (Row linha : participantes) {
                Runnable rowProcessingTask = () -> processRow(linha, listaParticipantes, nomeEvento, cargaHorariaEvento);
                executor.submit(rowProcessingTask);
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return listaParticipantes;
    }

    private void processRow(Row linha, List<Participante> listaParticipantes, String nomeEvento, int cargaHorariaEvento) {
        String nome = "";
        String cpf = "";
        String email = "";
        
        // Iterando sobre células
        for (Cell celula : linha) {
            switch (celula.getColumnIndex()) {
                case 0:
                    nome = celula.getStringCellValue();
                    break;
                case 1:
                    cpf = String.valueOf((long) celula.getNumericCellValue());
                    break;
                case 2:
                    email = celula.getStringCellValue();
                    break;
            }
        }
        System.out.println("[PARTICIPANTE SERVICE] Informacoes lidas com sucesso da planilha para o participante: " + nome);
        synchronized (listaParticipantes) {
            listaParticipantes.add(new Participante(nome, cpf, email, nomeEvento, cargaHorariaEvento));
        }
    }
}
