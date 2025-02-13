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
@Table(name="royalties")
 
public class Royalty {
	@Id
	@Column(name="royalty_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int royaltyId;
	@Column(name="song_id")
	private int songId;
	@Column(name="calculated_date")
	private Date calculatedDate;
	@Column(name="total_streams")
	private long totalStreams;
	@Column(name="royalty_amount")
	private double royaltyAmount;
	@Column(name="artist_id")
	private int artistId;
	@Column(name="status")
	private String status;
}