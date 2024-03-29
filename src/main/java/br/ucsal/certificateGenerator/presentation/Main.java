package br.ucsal.certificateGenerator.presentation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import br.ucsal.certificateGenerator.application.ParticipanteService;
import br.ucsal.certificateGenerator.domain.Participante;

public class Main {

    public static void main(String[] args) {
    	try {
    		String filePath = "src/main/java/br/ucsal/certificateGenerator/resources/participantes.xls";
        	ParticipanteService participanteService = new ParticipanteService();
        	FileInputStream fis = new FileInputStream(filePath);
            List<Participante> listaParticipantes = participanteService.lerPlanilhaDeParticipantes(fis);
		} catch (Exception e) {
			System.out.println("Erro: "+ e.getMessage());
		}
    }
}