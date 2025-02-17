package com.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rms.model.Transactions;
import com.rms.service.PdfService;
import com.rms.service.TransactionService;

@RestController
@RequestMapping("/trans")
@CrossOrigin(origins="*")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private PdfService pdfService;
	
	@GetMapping("/showTrans")
	public List<Transactions> showTransactions(){
		return transactionService.showTrans();
	}
	
	@GetMapping("/showTransById/{id}")
	public List<Transactions> showByUserTransactions(@PathVariable int id){
		return transactionService.showTransByReceiver(id);
	}
	
	@GetMapping("/showTransByManId/{id}")
	public List<Transactions> showByManTrans(@PathVariable int id){
		return transactionService.showTransByManId(id);
	}
	@PostMapping("/addTransaction")
    public ResponseEntity<String> addTransaction(@RequestBody Transactions transaction) {
        transactionService.addTransaction(transaction);
        return ResponseEntity.ok("Transaction successfully added");
    }
	
	@GetMapping("/export-pdf")
	public ResponseEntity<byte[]> exportAllTransactionsToPDF() {
        List<Transactions> transactions = transactionService.showTrans();
        
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        byte[] pdfBytes = pdfService.generateTransactionPdf(transactions);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "alltransactions.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
	
	@GetMapping("/exportPDF/{userId}")
    public ResponseEntity<byte[]> exportTransactionsToPDF(@PathVariable int userId) {
        List<Transactions> transactions = transactionService.getTransactionsByReceiver(userId);
        
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        byte[] pdfBytes = pdfService.generateTransactionPdf(transactions);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "transactions.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
    
    @GetMapping("/exportPDF/manager/{managerId}")
    public ResponseEntity<byte[]> exportTransactionsByManagerToPDF(@PathVariable int managerId) {
        // ✅ Fetch transactions based on manager ID
        List<Transactions> transactions = transactionService.showTransByManId(managerId);
        
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // ✅ Generate PDF using PdfService
        byte[] pdfBytes = pdfService.generateTransactionPdf(transactions);

        // ✅ Set headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "manager_transactions.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }


}
