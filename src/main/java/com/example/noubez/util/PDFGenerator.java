package com.example.noubez.util;

import com.example.noubez.Model.Personnel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFGenerator {

    public void handleDownloadPdfButtonAction(String filePath, List<Personnel> personnelList) {
         final float LOGO_X = 50; // Adjust this value as needed
         final float LOGO_Y = 700;
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                contentStream.setNonStrokingColor(Color.BLUE); // Changer la couleur du texte à Bleu
                contentStream.setStrokingColor(Color.BLACK); // Couleur des lignes du tableau en Noir




                    // Add logo
                    PDImageXObject logo = PDImageXObject.createFromFile("C:\\Users\\Admin\\Desktop\\3A32HealthSwift\\public\\uploads\\logo_pi.jpg", document);
                    contentStream.drawImage(logo, LOGO_X, LOGO_Y, logo.getWidth() / 4, logo.getHeight() / 4);


                    // Add date
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    String currentDate = dateFormat.format(date);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(400, 750);
                    contentStream.showText("Date  : " + currentDate);
                    contentStream.endText();






                    // Dessiner le titre centré "PDF Personnel"
                contentStream.beginText();
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - PDType1Font.HELVETICA_BOLD.getStringWidth("Liste des Personnels") / 1000 * 24) / 2, 750);
                contentStream.showText("Liste des Personnels");
                contentStream.endText();

                float margin = 50;
                float y = 700;
                float tableWidth = 500;

                // Dessiner le fond blanc du tableau
                contentStream.setNonStrokingColor(Color.WHITE); // Changer la couleur du fond à Blanc
                contentStream.fillRect(margin, y, tableWidth, -20 * (personnelList.size() + 1)); // Calculer la hauteur en fonction du nombre de lignes

                // Dessiner le tableau avec des bordures arrondies
                drawRoundedRect(contentStream, margin, y, tableWidth, -20 * (personnelList.size() + 1), 10);

                // En-tête du tableau
                String[] headers = {"ID", "Nom", "Prenom"};
                float[] columnWidths = {50, 200, 250}; // Ajuster la largeur des colonnes
                drawTable(contentStream, y, tableWidth, margin, headers, columnWidths, PDType1Font.HELVETICA_BOLD, 12f);

                // Contenu du tableau
                for (Personnel personnel : personnelList) {
                    y -= 20; // Ajuster la hauteur de chaque ligne
                    String[] rowData = {
                            String.valueOf(personnel.getId_personnel()),
                            personnel.getNom(),
                            personnel.getPrenom_personnel()

                    };
                    drawTable(contentStream, y, tableWidth, margin, rowData, columnWidths, PDType1Font.HELVETICA, 12f);
                }
            }

            document.save("C:\\Users\\Admin\\Downloads\\Personnel.pdf");

            System.out.println("PDF généré avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawTable(PDPageContentStream contentStream, float y, float tableWidth, float margin,
                           String[] content, float[] columnWidths, PDType1Font font, float fontSize) throws IOException {
        final int cols = content.length;
        float rowHeight = fontSize + 2 * 2; // Augmenter le padding pour une ligne plus grande

        // Dessiner les lignes du tableau
        contentStream.setLineWidth(0.5f);
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(Color.BLACK); // Couleur noire pour les lignes et les informations

        float textY = y - fontSize;
        float nextX = margin; // Ajout de cette ligne pour garder la trace de la position actuelle en x
        for (int i = 0; i < content.length; i++) {
            float textWidth = font.getStringWidth(content[i]) * fontSize / 1000f;
            float cellWidth = columnWidths[i];
            float textX = nextX + (cellWidth - textWidth) / 2; // Centrer le texte
            contentStream.beginText();
            contentStream.newLineAtOffset(textX, textY);
            contentStream.showText(content[i]);
            contentStream.endText();
            nextX += columnWidths[i]; // Mise à jour de nextX pour la prochaine colonne

            // Dessiner les lignes verticales du tableau
            contentStream.moveTo(nextX, y); // Utilisation de nextX pour la position de la ligne verticale
            contentStream.lineTo(nextX, y - 20);
            contentStream.stroke();
        }

        // Dessiner la ligne horizontale en bas du tableau
        contentStream.moveTo(margin, y - 20);
        contentStream.lineTo(margin + tableWidth, y - 20); // Ajustement de la position de la ligne horizontale
        contentStream.stroke();
    }

    private void drawRoundedRect(PDPageContentStream contentStream, float x, float y, float width, float height, float radius) throws IOException {
        contentStream.moveTo(x + radius, y);
        contentStream.lineTo(x + width - radius, y);
        contentStream.curveTo(x + width - radius / 2, y, x + width, y + radius / 2, x + width, y + radius);
        contentStream.lineTo(x + width, y + height - radius);
        contentStream.curveTo(x + width, y + height - radius / 2, x + width - radius / 2, y + height, x + width - radius, y + height);
        contentStream.lineTo(x + radius, y + height);
        contentStream.curveTo(x + radius / 2, y + height, x, y + height - radius / 2, x, y + height - radius);
        contentStream.lineTo(x, y + radius);
        contentStream.curveTo(x, y + radius / 2, x + radius / 2, y, x + radius, y);
        contentStream.closePath();
        contentStream.stroke();
    }
}