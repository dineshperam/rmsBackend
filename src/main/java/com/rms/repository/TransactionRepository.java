package com.rms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rms.model.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {
	
	List<Transactions> findByReceiver(int id);
	
	List<Transactions> findByManagerId(int id);
	
	

}
