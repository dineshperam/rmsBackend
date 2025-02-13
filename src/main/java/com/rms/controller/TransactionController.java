package com.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rms.model.Transactions;
import com.rms.service.TransactionService;

@RestController
@RequestMapping("/trans")
@CrossOrigin(origins="*")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
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


}
