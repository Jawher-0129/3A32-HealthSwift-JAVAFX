package com.example.noubez.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.noubez.Model.Chambre;

public class ExcelExporter {

    public static void exportChambresToExcel(List<Chambre> chambres, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Chambres");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Numero");
            headerRow.createCell(1).setCellValue("Personnel");
            headerRow.createCell(2).setCellValue("Disponibilite");
            headerRow.createCell(3).setCellValue("Nombre Lits Total");
            headerRow.createCell(4).setCellValue("Nombre Lits Disponible");

            // Fill data
            int rowNum = 1;
            for (Chambre chambre : chambres) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(chambre.getNumero());
                row.createCell(1).setCellValue(chambre.getPersonnel());
                row.createCell(2).setCellValue(chambre.getDisponibilite());
                row.createCell(3).setCellValue(chambre.getNombre_lits_total());
                row.createCell(4).setCellValue(chambre.getNmbr_lits_disponible());
            }

            // Save the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }
}
