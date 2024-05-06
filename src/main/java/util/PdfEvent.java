package util;

import entite.Evenement;

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

public class PdfEvent {

    public void handleDownloadPdfButtonAction(String filePath, List<Evenement> evenementList) {
        final float LOGO_X = 50; // Adjust this value as needed
        final float LOGO_Y = 700;
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                contentStream.setNonStrokingColor(Color.BLUE); // Changer la couleur du texte à Bleu
                contentStream.setStrokingColor(Color.BLACK); // Couleur des lignes du tableau en Noir

                // Add date
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String currentDate = dateFormat.format(date);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(400, 750);
                contentStream.showText("Date  : " + currentDate);
                contentStream.endText();

                // Dessiner le titre centré
                contentStream.beginText();
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - PDType1Font.HELVETICA_BOLD.getStringWidth("Liste des Evenements") / 1000 * 24) / 2, 750);
                contentStream.showText("Liste des Evenements");
                contentStream.endText();

                float margin = 50;
                float y = 700;
                float tableWidth = 500;

                // En-tête du tableau
                String[] headers = {"Titre", "Date", "Durée", "Lieu"}; // Adjusted headers
                float[] columnWidths = {200, 150, 50, 100, 150}; // Adjusted column widths

                // Dessiner le fond blanc du tableau
                contentStream.setNonStrokingColor(Color.WHITE); // Changer la couleur du fond à Blanc
                contentStream.fillRect(margin, y, tableWidth, -20 * (evenementList.size() + 1)); // Calculer la hauteur en fonction du nombre de lignes

                // Dessiner le tableau avec des bordures arrondies
                drawRoundedRect(contentStream, margin, y, tableWidth, -20 * (evenementList.size() + 1), 10);

                // En-tête du tableau
                drawTable(contentStream, y, tableWidth, margin, headers, columnWidths, PDType1Font.HELVETICA_BOLD, 12f);

                // Contenu du tableau
                for (Evenement evenement : evenementList) {
                    y -= 20; // Ajuster la hauteur de chaque ligne
                    String[] rowData = {
                            evenement.getTitre(),
                            String.valueOf(evenement.getDate()),
                            String.valueOf(evenement.getDuree()),
                            evenement.getLieu()
                    };
                    drawTable(contentStream, y, tableWidth, margin, rowData, columnWidths, PDType1Font.HELVETICA, 12f);
                }
            }

            document.save("C:\\Users\\user\\Desktop\\esprit 3éme\\pi\\Evenement.pdf");

            System.out.println("PDF généré avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawTable(PDPageContentStream contentStream, float y, float tableWidth, float margin,
                           String[] content, float[] columnWidths, PDType1Font font, float fontSize) throws IOException {
        final int cols = content.length;
        float rowHeight = fontSize + 2 * 2;

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

            // Wrapping text if it's too long
            if (textWidth > cellWidth) {
                float startX = nextX;
                float startY = textY;
                String[] words = content[i].split(" ");
                StringBuilder line = new StringBuilder(words[0]);
                for (int j = 1; j < words.length; j++) {
                    float wordWidth = font.getStringWidth(line.toString() + " " + words[j]) * fontSize / 1000f;
                    if (wordWidth < cellWidth) {
                        line.append(" ").append(words[j]);
                    } else {
                        // Draw the line
                        contentStream.beginText();
                        contentStream.newLineAtOffset(startX, startY);
                        contentStream.showText(line.toString());
                        contentStream.endText();

                        // Move to the next line
                        startY -= fontSize;
                        line = new StringBuilder(words[j]);
                    }
                }
                // Draw the remaining line
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY);
                contentStream.showText(line.toString());
                contentStream.endText();
            } else {
                // Draw the text if it fits in one line
                contentStream.beginText();
                contentStream.newLineAtOffset(textX, textY);
                contentStream.showText(content[i]);
                contentStream.endText();
            }

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
