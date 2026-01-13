package ma.ensate.backend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import ma.ensate.backend.domain.Recrutement;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OfferPdfService {

    private static final Color BRAND_DARK = new Color(11, 46, 89);
    private static final Color BRAND_ACCENT = new Color(224, 167, 27);
    private static final Color TEXT_MUTED = new Color(90, 100, 115);
    private static final Color PANEL_BG = new Color(245, 248, 252);
    private static final String LOGO_PATH = "/assets/ensate_logo.png";

    public byte[] generateOfferPdf(Recrutement recrutement) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 48, 48, 120, 70);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, output);
            document.open();
            drawHeader(writer, recrutement);

            BaseFont base = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
            Font heading = new Font(base, 16, Font.BOLD, BRAND_DARK);
            Font subheading = new Font(base, 12, Font.BOLD, BRAND_DARK);
            Font body = new Font(base, 11, Font.NORMAL, new Color(40, 40, 40));
            Font muted = new Font(base, 10, Font.NORMAL, TEXT_MUTED);

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Offre de recrutement", heading));
            document.add(new Paragraph(nullSafe(recrutement.getPoste()), new Font(base, 15, Font.BOLD, BRAND_DARK)));

            Paragraph meta = new Paragraph();
            meta.setSpacingBefore(6);
            meta.add(new Chunk("Departement: ", muted));
            meta.add(new Chunk(nullSafe(recrutement.getDepartement()), body));
            meta.add(new Chunk("   |   Type: ", muted));
            meta.add(new Chunk(nullSafe(recrutement.getTypeContrat()), body));
            meta.add(new Chunk("   |   Statut: ", muted));
            meta.add(new Chunk(nullSafe(recrutement.getStatut()), body));
            document.add(meta);

            document.add(Chunk.NEWLINE);
            document.add(sectionTitle("Description", base));
            Paragraph description = new Paragraph(nonEmpty(recrutement.getDescription(),
                    "Aucune description fournie pour cette offre."), body);
            description.setSpacingBefore(4);
            description.setSpacingAfter(14);
            document.add(description);

            document.add(sectionTitle("Informations cles", base));
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.4f, 3.0f});
            table.setSpacingBefore(6);
            addRow(table, "Nombre de postes", valueOrDash(recrutement.getNombrePostes()), body);
            addRow(table, "Date d'ouverture", formatDate(recrutement.getDateOuverture()), body);
            addRow(table, "Date de cloture", formatDate(recrutement.getDateCloture()), body);
            addRow(table, "Cree le", formatDateTime(recrutement.getCreatedAt()), body);
            document.add(table);

            document.add(Chunk.NEWLINE);
            document.add(sectionTitle("Contact", base));
            Paragraph contact = new Paragraph(
                    "Service RH - Universite Abdelmalek Essaadi, ENSA Tetouan",
                    new Font(base, 10, Font.NORMAL, TEXT_MUTED));
            contact.setSpacingBefore(4);
            document.add(contact);

            Paragraph footer = new Paragraph(
                    "Generation automatique - Systeme de gestion des ressources",
                    new Font(base, 9, Font.NORMAL, TEXT_MUTED));
            footer.setSpacingBefore(18);
            document.add(footer);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate offer PDF", ex);
        } finally {
            document.close();
        }
        return output.toByteArray();
    }

    private void drawHeader(PdfWriter writer, Recrutement recrutement) throws Exception {
        PdfContentByte canvas = writer.getDirectContent();
        float pageWidth = PageSize.A4.getWidth();
        float pageHeight = PageSize.A4.getHeight();

        canvas.saveState();
        canvas.setColorFill(BRAND_DARK);
        canvas.rectangle(0, pageHeight - 90, pageWidth, 90);
        canvas.fill();
        canvas.restoreState();

        Image logo = loadLogo();
        if (logo != null) {
            logo.scaleToFit(58, 58);
            logo.setAbsolutePosition(50, pageHeight - 76);
            canvas.addImage(logo);
        } else {
            canvas.saveState();
            canvas.setColorFill(BRAND_ACCENT);
            canvas.circle(70, pageHeight - 46, 18);
            canvas.fill();
            canvas.restoreState();
        }

        BaseFont headerFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
        canvas.beginText();
        canvas.setFontAndSize(headerFont, 16);
        canvas.setColorFill(Color.WHITE);
        canvas.showTextAligned(Element.ALIGN_LEFT,
                nonEmpty(recrutement.getPoste(), "Offre de recrutement"),
                120, pageHeight - 48, 0);
        canvas.endText();

        canvas.beginText();
        canvas.setFontAndSize(headerFont, 10);
        canvas.setColorFill(new Color(220, 230, 242));
        canvas.showTextAligned(Element.ALIGN_LEFT,
                "Ecole Nationale des Sciences Appliquees - Tetouan",
                120, pageHeight - 68, 0);
        canvas.endText();
    }

    private void addRow(PdfPTable table, String label, String value, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, new Font(valueFont.getBaseFont(), 10, Font.BOLD, TEXT_MUTED)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(7);
        labelCell.setBackgroundColor(PANEL_BG);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(7);
        table.addCell(valueCell);
    }

    private Paragraph sectionTitle(String title, BaseFont base) {
        Font titleFont = new Font(base, 12, Font.BOLD, BRAND_DARK);
        Paragraph p = new Paragraph(title, titleFont);
        p.setSpacingBefore(2);
        p.setSpacingAfter(4);
        return p;
    }

    private String nullSafe(String value) {
        return value == null || value.isBlank() ? "Non renseigne" : value;
    }

    private String nonEmpty(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private String valueOrDash(Integer value) {
        return value == null ? "—" : value.toString();
    }

    private String formatDate(LocalDate date) {
        if (date == null) return "—";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "—";
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private Image loadLogo() throws IOException, BadElementException {
        try (InputStream input = OfferPdfService.class.getResourceAsStream(LOGO_PATH)) {
            if (input == null) return null;
            byte[] bytes = input.readAllBytes();
            return Image.getInstance(bytes);
        }
    }
}
