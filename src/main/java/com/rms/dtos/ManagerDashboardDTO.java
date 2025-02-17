package com.rms.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDashboardDTO {
	private Long totalSongs;
    private Long totalStreams;
    private Double managerRevenue;
    private Double totalArtistsRevenue;


}