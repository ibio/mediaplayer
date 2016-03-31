package com.ibio8.core;

import java.io.File;
import java.util.Map;

import com.ibio8.event.AudioEvent;
import com.ibio8.model.vo.TrackVO;

import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer {
	private MediaPlayer _player;
	private EventHandler<AudioEvent> _hAudioEvent;
	
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
			_player.setOnReady(new Runnable(){
		        @Override
		        public void run(){
					//System.out.println(hit.getTracks());
//		            System.out.println("total duration" + _player.getTotalDuration());
		            // display media's metadata
		            for (Map.Entry<String, Object> entry : hit.getMetadata().entrySet()){
		                System.out.println(entry.getKey() + ": " + entry.getValue());
		            }
		            // play if you want
		            //_player.play();
		        }
		    });
			_player.currentTimeProperty().addListener((observableValue, oldDuration, newDuration) -> {
				if(_hAudioEvent != null){
					_hAudioEvent.handle(new AudioEvent(observableValue.getValue(), _player.getTotalDuration(), false));
				}
				//System.out.println("current:" + _playingTime);
				//System.out.println("Player:" + observableValue + " | Changed from playing at: " + oldDuration + " to play at " + newDuration);
			});
			_player.setOnEndOfMedia(new Runnable(){
		        @Override public void run() {
		        	//System.out.println("On Completed");
		        	_hAudioEvent.handle(new AudioEvent(null, null, true));
		        }
		      });
		}
	}
	
	public void onHeadUpedated(EventHandler<AudioEvent> obj){
		_hAudioEvent = obj;
	}
	
	public void seek(float ratio){
		if(_player != null && MediaPlayer.Status.PLAYING.equals(_player.getStatus())){
			//System.out.println("status:" + _player.getStatus());
			Duration time = new Duration(ratio * _player.getTotalDuration().toMillis());
			_player.seek(time);
		}
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
