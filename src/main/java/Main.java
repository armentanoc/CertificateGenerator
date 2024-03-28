import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Main {

    public static void main(String[] args) {
        List<Participante> listaParticipantes = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream("participantes.xls");
            HSSFWorkbook planilha = new HSSFWorkbook(fis);
            HSSFSheet participantes = planilha.getSheetAt(0);
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
                listaParticipantes.add(new Participante(nome, cpf, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Participante participante : listaParticipantes) {
            System.out.println("Nome: " + participante.getNome() + ", CPF: " + participante.getCpf() + ", Email: " + participante.getEmail());
        }
    }
}