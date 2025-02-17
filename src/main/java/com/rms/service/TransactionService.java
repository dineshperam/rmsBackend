package com.rms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.model.Transactions;
import com.rms.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	public List<Transactions> showTrans(){
		return transactionRepository.findAll();
	}
	
	public List<Transactions> showTransByReceiver(int id){
		return transactionRepository.findByReceiver(id);
	}
	
	public List<Transactions> showTransByManId(int id){
		return transactionRepository.findByManagerId(id);
	}
	
	public List<Transactions> getTransactionsByReceiver(int userId) {
        return transactionRepository.findByReceiver(userId);
    }
	
	public void addTransaction(Transactions transaction) {
        transaction.setTransactionDate(new Date());
        transactionRepository.save(transaction);
    }

}
