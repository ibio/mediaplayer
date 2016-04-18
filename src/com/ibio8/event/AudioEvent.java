package com.ibio8.event;

import javafx.event.ActionEvent;
import javafx.util.Duration;

public class AudioEvent extends ActionEvent {
	public Duration playingTime;
	public Duration totalTime;
	public boolean completed;
	public String artist;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3043693079324450538L;
	
	public AudioEvent(Duration playingTime, Duration totalTime, boolean completed, String artist){
		this.playingTime = playingTime;
		this.totalTime = totalTime;
		this.completed = completed;
		this.artist = artist;
	}

}