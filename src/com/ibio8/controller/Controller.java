package com.ibio8.controller;

import java.util.List;

import com.ibio8.view.Canvas;

import javafx.event.EventHandler;
import javafx.util.Duration;

import com.ibio8.core.AudioPlayer;
import com.ibio8.event.AudioEvent;
import com.ibio8.model.Playlist;
import com.ibio8.model.vo.TrackVO;
import com.ibio8.util.ShowLyrics;
import com.ibio8.util.GetData;
import com.ibio8.util.SearchTask;

public class Controller {
	static private volatile Controller _instance = null;
	private Canvas _canvas;
	private Playlist _playlist;
	private AudioPlayer _player;
	private SearchTask _search;
	private ShowLyrics _showLyrics;
	
	public Controller(){
		_playlist = new Playlist();
		_player = new AudioPlayer();
		_showLyrics = new ShowLyrics();
		_player.onHeadUpedated(new EventHandler<AudioEvent>() {
            @Override
            public void handle(AudioEvent e) {
            	//System.out.println("event" + e.playingTime);
            	if(e.playingTime != null){
            		_showLyrics.run(e.playingTime);
            	}
            	if(e.playingTime != null && e.totalTime != null){
            		updateProgress(e.playingTime, e.totalTime);
            	}
            	if(e.completed){
            		next();
            	}
            	//search for artist
            	if(e.artist != null){
            		//System.out.println(e.artist + "---");
            		_canvas.showContent(GetData.load(e.artist));
            	}
            }
        });
	}
	
	static public Controller getInstance(){
		if(_instance == null){
			synchronized (Controller.class) {
                if (_instance == null) {
                	_instance = new Controller();
                }
            }
		}
		return _instance;
	}
	
	public void search(String drive){
		if(_search != null){
			_search.stop();
		}
		_search = new SearchTask("search-task", drive, "mp3");
	}
	
	public void loadTrack(TrackVO track){
		_player.load(track);
		_canvas.showLyrics(null, -1);
		String input = ShowLyrics.load(track.lrc);
		_showLyrics.start(input);
	}
	
	public void playPause(){
		if(_player.isPlay()){
			_player.pause();
		}else{
			_player.play();
		}
	}
	
	public void seek(float ratio){
		_player.seek(ratio);
	}
	
	public void shutdown(){
		if(_search != null){
			_search.stop();
		}
	}

	//============= change view =============
	public void initView(Canvas canvas){
		_canvas = canvas;
	}
	
	public void refreshList(List<TrackVO> list){
		_canvas.buildTracks(list);
	}
	
	public void changeCurrentTrack(int index, TrackVO track){
		_canvas.changeCurrentItem(index, track);
	}
	
	public void showLyrics(List<String> list, int index){
		_canvas.showLyrics(list, index);
	}
	
	public void updateProgress(Duration playingTime, Duration totalTime){
		_canvas.updateProgress(playingTime, totalTime);
	}
	
	//============= change model =============
	public void addTracks(List<TrackVO> list){
		_playlist.add(list);
	}
	
	public void change(int index){
		_playlist.change(index);
	}
	
	public void previous(){
		_playlist.previous();
	}
	
	public void next(){
		_playlist.next();
	}
}
