package foreach.cda.Marmiton.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openpdf.text.Document;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import foreach.cda.Marmiton.entity.Recette;
import foreach.cda.Marmiton.entity.RecetteIngredient;

@Service
public class RecetteExportService {

    public byte[] toPdf(Recette recette) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Recette #" + recette.getId() + " - " + recette.getNomPlat()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Durée préparation: " + nullSafe(recette.getDureePreparation()) + " min"));
        document.add(new Paragraph("Durée cuisson: " + nullSafe(recette.getDureeCuisson()) + " min"));
        document.add(new Paragraph("Calories: " + nullSafe(recette.getNombreCalories())));
        document.add(new Paragraph("Partage: " + (Boolean.TRUE.equals(recette.getPartage()) ? "Oui" : "Non")));

        if (recette.getUser() != null) {
            document.add(new Paragraph("Auteur: " + recette.getUser().getPrenom() + " " + recette.getUser().getNom()));
        }

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Ingrédients"));
        for (RecetteIngredient ri : recette.getRecetteIngredients()) {
            String line = "- " + ri.getIngredient().getLibelle() + " (" + ri.getQuantite() + ")";
            document.add(new Paragraph(line));
        }

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Exporté le " + now()));

        document.close();
        return out.toByteArray();
    }

    public byte[] toXlsx(Recette recette) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Recette");

            int r = 0;
            Row title = sheet.createRow(r++);
            title.createCell(0).setCellValue("Recette #" + recette.getId());
            title.createCell(1).setCellValue(recette.getNomPlat());

            Row meta1 = sheet.createRow(r++);
            meta1.createCell(0).setCellValue("Durée préparation (min)");
            meta1.createCell(1).setCellValue(nullSafe(recette.getDureePreparation()));

            Row meta2 = sheet.createRow(r++);
            meta2.createCell(0).setCellValue("Durée cuisson (min)");
            meta2.createCell(1).setCellValue(nullSafe(recette.getDureeCuisson()));

            Row meta3 = sheet.createRow(r++);
            meta3.createCell(0).setCellValue("Calories");
            meta3.createCell(1).setCellValue(nullSafe(recette.getNombreCalories()));

            Row meta4 = sheet.createRow(r++);
            meta4.createCell(0).setCellValue("Partage");
            meta4.createCell(1).setCellValue(Boolean.TRUE.equals(recette.getPartage()) ? "Oui" : "Non");

            if (recette.getUser() != null) {
                Row meta5 = sheet.createRow(r++);
                meta5.createCell(0).setCellValue("Auteur");
                meta5.createCell(1).setCellValue(recette.getUser().getPrenom() + " " + recette.getUser().getNom());
            }

            r++;

            Row header = sheet.createRow(r++);
            header.createCell(0).setCellValue("Ingrédient");
            header.createCell(1).setCellValue("Quantité");

            for (RecetteIngredient ri : recette.getRecetteIngredients()) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(ri.getIngredient().getLibelle());
                row.createCell(1).setCellValue(ri.getQuantite());
            }

            Row footer = sheet.createRow(r++);
            footer.createCell(0).setCellValue("Exporté le");
            footer.createCell(1).setCellValue(now());

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération XLSX: " + e.getMessage(), e);
        }
    }

    private static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private static int nullSafe(Integer value) {
        return value == null ? 0 : value;
    }
}

