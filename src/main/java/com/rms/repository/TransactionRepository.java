package com.rms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rms.model.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {
	
	List<Transactions> findByReceiver(int id);
	
	List<Transactions> findByManagerId(int id);
	
	@Query("SELECT COALESCE(SUM(t.transactionAmount), 0) FROM Transactions t WHERE t.receiver = :managerId")
	Double getManagerTotalRevenue(@Param("managerId") int managerId);

}
