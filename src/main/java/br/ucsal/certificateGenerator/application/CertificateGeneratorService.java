package br.ucsal.certificateGenerator.application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

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

    private Participante criarDocumentoVazio(Participante participante) {
        String certificatePath = "src/main/java/br/ucsal/certificateGenerator/infra/";
        String fileName = certificatePath + participante.getNome() + "_" + participante.getNomeEvento() + ".pdf";
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);
                customizeCertificate(document, page, participante);
                document.save(fileName);
                document.close();
                participante.setCertificate(file);
                System.out.println("[CERTIFICATE GENERATOR] Certificado criado para: " + participante.getNome());
            } catch (IOException e) {
                System.err.println("[CERTIFICATE GENERATOR] Erro criando certificado: " + e.getMessage());
            }
        } else {
            System.out.println("[CERTIFICATE GENERATOR] Certificado já existe para: " + participante.getNome());
            participante.setCertificate(file);
        }
        return participante;
    }

    private void customizeCertificate(PDDocument document, PDPage page, Participante participante) throws IOException {
    	
    	float POINTS_PER_INCH = 72;
    	float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
    	PDRectangle pageSize = new PDRectangle(297 * POINTS_PER_MM, 210 * POINTS_PER_MM);
        
    	page.setMediaBox(pageSize);
        page.setRotation(0); 
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        BufferedImage image = null;
        String eventName = participante.getNomeEvento();
        if (eventName.toLowerCase().contains("seminario") || eventName.toLowerCase().contains("seminário"))
            image = ImageIO.read(new File("src/main/resources/seminario_background.jpg"));
        else if (eventName.toLowerCase().contains("workshop")) 
            image = ImageIO.read(new File("src/main/resources/workshop_background.jpg"));
        else 
            image = ImageIO.read(new File("src/main/resources/default_background.jpg"));
        
        float width = page.getMediaBox().getWidth();
        float height = page.getMediaBox().getHeight();

        File tempFile = File.createTempFile("temp", ".jpg");
        ImageIO.write(image, "jpg", tempFile);
        contentStream.drawImage(PDImageXObject.createFromFileByContent(tempFile, document), 0, 0, width, height);
        
        // Palavra Certificado 
        
        float textWidth = 36 * 0.5f * (new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD).getStringWidth("CERTIFICADO") / 1000);
        float textHeight = 150;
        float textX = (width - textWidth) / 2 - (textWidth / 2);
        float textY = height - textHeight;
        
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 42);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.newLineAtOffset(textX, textY);
        contentStream.showText("CERTIFICADO");
        contentStream.endText();
        
        // Texto do Certificado
        
        String paragraph = "Certificamos que "+ participante.getNome()+" participou do evento "+participante.getNomeEvento()+" com carga horária de "+ participante.getCargaHorariaEvento()+"h de participação.";
        float maxParagraphHeight = height - textHeight - 50; 
        float lineHeight = 30; 

        List<String> lines = new ArrayList<>();
        String remainingText = paragraph;
        float averageFontWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA).getAverageFontWidth() / 1000 * 10;
        float maxParagraphWidth = width * 0.35f;

        while (!remainingText.isEmpty() && (lines.size() + 1) * lineHeight <= maxParagraphHeight) {
            int charsThatFit = (int)(maxParagraphWidth / averageFontWidth);
            if (remainingText.length() <= charsThatFit) {
                lines.add(remainingText);
                break;
            }
            int lastSpace = remainingText.substring(0, charsThatFit).lastIndexOf(' ');
            if (lastSpace != -1) {
                lines.add(remainingText.substring(0, lastSpace));
                remainingText = remainingText.substring(lastSpace + 1).trim();
            } else {
                lines.add(remainingText.substring(0, charsThatFit));
                remainingText = remainingText.substring(charsThatFit).trim();
            }
        }
        
        float paragraphHeight = lines.size() * lineHeight;
        float paragraphX = (width / 2) - maxParagraphWidth / 2 - 150;
        float paragraphY = height - textHeight - paragraphHeight - 50; 

        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 25); 
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.newLineAtOffset(paragraphX, paragraphY);

        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -lineHeight);
        }

        contentStream.endText();
        contentStream.close();
    }
}
