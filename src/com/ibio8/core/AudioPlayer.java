package com.ibio8.core;

import java.io.File;

import com.ibio8.model.vo.TrackVO;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer {
	private MediaPlayer _player;
	private Duration _playingTime;
	
	public void load(TrackVO track){
		if(_player != null){
			_player.stop();
		}
		//see http://stackoverflow.com/questions/11273773/javafx-2-1-toolkit-not-initialized
		//new JFXPanel();
		//
		if(track != null){
			File file = new File(track.url);
			Media hit = new Media(file.toURI().toString());
			//System.out.println(file.toURI().toString());
			_player = new MediaPlayer(hit);
			_player.setAutoPlay(true);
			System.out.println(hit.getMetadata());
			//System.out.println(hit.getDuration());
			//System.out.println(hit.getMarkers());
			//System.out.println(hit.getTracks());
			_player.currentTimeProperty().addListener((observableValue, oldDuration, newDuration) -> {
				_playingTime = observableValue.getValue();
				//System.out.println("current:" + _playingTime);
				//System.out.println("Player:" + observableValue + " | Changed from playing at: " + oldDuration + " to play at " + newDuration);
			});
		}
	}
	
	public Duration getPlayingTime(){
		return _playingTime;
	}
	
	public boolean isPlay(){
		boolean flag = false;
		if(_player != null){
			if(_player.getStatus() == MediaPlayer.Status.PLAYING){
				flag = true;
			}
		}
		return flag;
	}
	
	public void play(){
		if(_player != null){
			_player.play();
		}
	}
	
	public void pause(){
		if(_player != null){
			_player.pause();
		}
	}
}
