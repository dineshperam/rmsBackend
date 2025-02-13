package com.rms.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="transactions")
public class Transactions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="transaction_id")
	private int transactionId;
	
	@Column(name="receiver")
	private int receiver;
	
	@Column(name="sender")
	private int sender;
	
	@Column(name="royalty_id")
	private int royaltyId;
	
	@Column(name="transaction_date")
	private Date transactionDate;
	
	@Column(name="transaction_amount")
	private double transactionAmount;
	
	@Column(name="manager_id")
	private int managerId;

	@Column(name="transaction_type")
	private String transactionType;
	
	public Transactions(int receiver, int sender, int royaltyId, Date transactionDate,
		            double transactionAmount, Integer managerId, String transactionType) {
		this.receiver = receiver;
		this.sender = sender;
		this.royaltyId = royaltyId;
		this.transactionDate = transactionDate;
		this.transactionAmount = transactionAmount;
		this.managerId = managerId;
		this.transactionType = transactionType;
}
}
