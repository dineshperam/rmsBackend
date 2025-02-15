package com.rms.model;
 
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="streams")
public class Streams {
 
    @Id
    @Column(name ="stream_id")
    private int streamId;
    @Column(name="song_id")
    private int songId;
    @Column(name="stream_date")
    private Date streamDate;
    @Column(name="stream_count")
    private long streamCount;
    @Column(name="user_id")
    private int userId;
    @Column(name="status")
    private String status;
    
	public Streams(int streamId, int songId, Date streamDate, int userId) {
		super();
		this.streamId = streamId;
		this.songId = songId;
		this.streamDate = streamDate;
		this.userId = userId;
	}
}