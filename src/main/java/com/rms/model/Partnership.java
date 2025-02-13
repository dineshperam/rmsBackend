package com.rms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@Column(name="partnership_id")
	private int partnershipid;
	
	@Column(name="artist_id")
	private int artistid;
	
	@Column(name="manager_id")
	private int managerid;
	
	@Column(name="status")
	private String status;
	
	
}
