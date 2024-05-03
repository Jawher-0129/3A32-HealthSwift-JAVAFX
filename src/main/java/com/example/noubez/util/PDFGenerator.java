package com.example.noubez.util;

import com.example.noubez.Model.Personnel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;




















public class PDFGenerator {
    public static void generatePDF(List<Personnel> personnelList, String filePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Liste du Personnel");
                contentStream.endText();


                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String currentDate;
                currentDate = dateFormat.format(date);

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(400, 750);
                contentStream.showText("Date de téléchargement : " + currentDate);
                contentStream.endText();






                int y = 680;
                for (Personnel personnel : personnelList) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, y -= 20);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.showText("Nom: " + personnel.getNom());
                    contentStream.showText("Prenom: " + personnel.getPrenom_personnel());
                    contentStream.showText("Experience: " + personnel.getExperience());
                    contentStream.showText("Role: " + personnel.getRole());
                    contentStream.showText("Rating: " + personnel.getRating());
                    contentStream.showText("Disponibilite: " + personnel.getDisponibilite());

                    contentStream.newLine();
                    // Ajoutez d'autres informations de personnel ici
                    contentStream.endText();
                }
            }













            document.save("C:\\Users\\Admin\\Desktop\\ListePersonnel.pdf");
        }
    }
}
