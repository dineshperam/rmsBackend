package com.rms.service;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import com.rms.model.Partnership;
import com.rms.model.Transactions;

@Service
public class PdfService {

		public byte[] generateTransactionPdf(List<Transactions> transactions) {
	        try (PDDocument document = new PDDocument();
	             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	
	            PDPage page = new PDPage(PDRectangle.A4);
	            document.addPage(page);
	            PDPageContentStream contentStream = new PDPageContentStream(document, page);
	
	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(200, 750);
	            contentStream.showText("Transaction Report");
	            contentStream.endText();
	
	            contentStream.setFont(PDType1Font.HELVETICA, 12);
	            int yPosition = 700;
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	            for (Transactions transaction : transactions) {
	                if (yPosition < 50) { 
	                    contentStream.close();
	                    page = new PDPage(PDRectangle.A4);
	                    document.addPage(page);
	                    contentStream = new PDPageContentStream(document, page);
	                    contentStream.setFont(PDType1Font.HELVETICA, 12);
	                    yPosition = 700;
	                }
	
	                contentStream.beginText();
	                contentStream.newLineAtOffset(50, yPosition);
	                contentStream.showText(
	                    "ID: " + transaction.getTransactionId() +
	                    " | Sender: " + transaction.getSender() +
	                    " | Receiver: " + transaction.getReceiver() +
	                    " | Amount: $" + transaction.getTransactionAmount() +
	                    " | Date: " + sdf.format(transaction.getTransactionDate()) +
	                    " | Type: " + transaction.getTransactionType()
	                );
	                contentStream.endText();
	                yPosition -= 20;
	            }
	
	            contentStream.close();
	            document.save(outputStream);
	            return outputStream.toByteArray();
	        } catch (IOException e) {
	            throw new RuntimeException("Error generating PDF", e);
	        }
	    }
		
		public byte[] generatePartnershipPdf(Optional<Partnership> partnershipOptional) {
	        if (partnershipOptional.isEmpty()) {
	            throw new RuntimeException("Partnership record not found.");
	        }

	        Partnership partnership = partnershipOptional.get();

	        try (PDDocument document = new PDDocument();
	             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

	            PDPage page = new PDPage(PDRectangle.A4);
	            document.addPage(page);
	            PDPageContentStream contentStream = new PDPageContentStream(document, page);

	            // Title
	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(200, 750);
	            contentStream.showText("Partnership Details Report");
	            contentStream.endText();

	            // Content
	            contentStream.setFont(PDType1Font.HELVETICA, 12);
	            int yPosition = 700;
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("Partnership ID: " + partnership.getPartnershipId());
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("Artist ID: " + partnership.getArtistId());
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("Manager ID: " + partnership.getManagerId());
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("Status: " + partnership.getStatus());
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("Percentage: " + partnership.getPercentage() + "%");
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("Comments: " + (partnership.getComments() != null ? partnership.getComments() : "N/A"));
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("Start Date: " + sdf.format(partnership.getStartDate()));
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("End Date: " + sdf.format(partnership.getEndDate()));
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.beginText();
	            contentStream.newLineAtOffset(50, yPosition);
	            contentStream.showText("Duration: " + partnership.getDurationMonths() + " months");
	            contentStream.endText();
	            yPosition -= 20;

	            contentStream.close();
	            document.save(outputStream);
	            return outputStream.toByteArray();

	        } catch (IOException e) {
	            throw new RuntimeException("Error generating Partnership PDF", e);
	        }
}
}
