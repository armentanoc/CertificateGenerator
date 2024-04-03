package br.ucsal.certificateGenerator.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.ucsal.certificateGenerator.domain.Participante;

public class ParticipanteService {

	public List<Participante> lerPlanilhaDeParticipantes(File selectedFile, String nomeEvento, int cargaHorariaEvento) {
		List<Participante> listaParticipantes = new ArrayList<>();
		 try (FileInputStream fis = new FileInputStream(selectedFile); XSSFWorkbook planilha = new XSSFWorkbook(fis)) {
		        XSSFSheet participantes = planilha.getSheetAt(0);
		        Iterator<Row> itLinha = participantes.iterator();
		        // Passando Pelas Linhas
		        while (itLinha.hasNext()) {
		            Row linha = itLinha.next();
		            Iterator<Cell> celIt = linha.cellIterator();
		            String nome = "";
		            String cpf = "";
		            String email = "";
		            // Passando pelas Colunas
		            while (celIt.hasNext()) {
		                Cell celula = celIt.next();
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
		            listaParticipantes.add(new Participante(nome, cpf, email, nomeEvento, cargaHorariaEvento));
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return listaParticipantes;
		}
}
