package com.rms.dtos;

public class TopArtistDTO {
    private int artistId;
    private int month;
    private long totalStreams;

    public TopArtistDTO(int artistId, int month, long totalStreams) {
        this.artistId = artistId;
        this.month = month;
        this.totalStreams = totalStreams;
    }

    public int getArtistId() { return artistId; }
    public int getMonth() { return month; }
    public long getTotalStreams() { return totalStreams; }
}