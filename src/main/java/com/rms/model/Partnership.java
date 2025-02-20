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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partnerships")
public class Partnership {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="partnership_id")
	private int partnershipId;
	@Column(name="artist_id")
	private int artistId;
	@Column(name="manager_id")
	private int managerId;
	@Column(name="status")
	private String status;
	@Column(name="percentage")
	private double percentage;
	@Column(name="comments")
	private String comments;
	@Column(name="start_date")
	private Date startDate;
	@Column(name="end_date")
	private Date endDate;
	@Column(name="duration_months")
	private int durationMonths;
	
	
}
