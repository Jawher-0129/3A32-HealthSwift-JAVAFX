package com.example.noubez.util;

import com.example.noubez.Model.Personnel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



        public class PDFGenerator {
            private static final int CELL_MARGIN = 5;
            public static void generatePDF(List<Personnel> personnelList, String filePath) throws IOException {
                final int LOGO_X = 50;
                final int LOGO_Y = 700;
                int TEXT_X = 100;
                int TEXT_Y = 680;


                // Position horizontale du tableau
                final int TABLE_X = 100;
                // Position verticale du tableau
                final int TABLE_Y = 600;

                // Marge interne des cellules du tableau

                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);

                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        PDImageXObject logo = PDImageXObject.createFromFile("C:\\Users\\Admin\\Desktop\\3A32HealthSwift\\public\\uploads\\logo_pi.jpg", document);
                        contentStream.drawImage(logo, LOGO_X, LOGO_Y, logo.getWidth() / 4, logo.getHeight() / 4);
                    }

                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(100, 700);
                        contentStream.showText("Liste du Personnel");
                        contentStream.endText();

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date date = new Date();
                        String currentDate = dateFormat.format(date);

                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(300, 750);
                        contentStream.showText("Date de téléchargement : " + currentDate);
                        contentStream.endText();

                        String[] headers = {"Nom", "Prénom", "Expérience", "Rôle", "Évaluation", "Disponibilité"};
                        drawTableRow(contentStream, TABLE_X, TABLE_Y, headers, true);

                        // Draw data rows
                        int y = TABLE_Y;
                        for (Personnel personnel : personnelList) {
                            String[] rowData = {
                                    personnel.getNom(),
                                    personnel.getPrenom_personnel(),
                                    String.valueOf(personnel.getExperience()), // Convert int to String
                                    personnel.getRole(),
                                    String.valueOf(personnel.getRating()), // Convert int to String
                                    String.valueOf(personnel.getDisponibilite()) // Convert int to String
                            };
                            drawTableRow(contentStream, TABLE_X, y -= CELL_MARGIN * 2, rowData, false);
                        }

                    }
                    document.save("C:\\Users\\Admin\\Downloads\\ListePersonnel.pdf");
                }
            }
            private static void drawTableRow(PDPageContentStream contentStream, int startX, int y, String[] rowData, boolean isHeader) throws IOException {
                contentStream.setFont(PDType1Font.HELVETICA, isHeader ? 12 : 10);
                final int[] COLUMN_WIDTHS = {80, 80, 80, 80, 80, 80}; // Largeur des colonnes
                int x = startX;



                for (int i = 0; i < rowData.length; i++) {
                    contentStream.setFont(PDType1Font.HELVETICA, isHeader ? 12 : 10);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(x + CELL_MARGIN, y - CELL_MARGIN);
                    contentStream.showText(rowData[i]);
                    contentStream.endText();
                    x += COLUMN_WIDTHS[i];


                }


            }

        }




