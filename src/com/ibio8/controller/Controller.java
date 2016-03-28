package com.ibio8.controller;

import java.util.List;

import com.ibio8.view.Canvas;

import javafx.util.Duration;

import com.ibio8.core.AudioPlayer;
import com.ibio8.model.Playlist;
import com.ibio8.model.vo.TrackVO;
import com.ibio8.util.GetData;
import com.ibio8.util.ShowLyricsTask;
import com.ibio8.util.SearchTask;

public class Controller {
	static private volatile Controller _instance = null;
	private Canvas _canvas;
	private Playlist _playlist;
	private AudioPlayer _player;
	private SearchTask _search;
	private ShowLyricsTask _showLyrics;
	
	public Controller(){
		_playlist = new Playlist();
		_player = new AudioPlayer();
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
		if(_showLyrics != null){
			_showLyrics.stop();
		}
		_canvas.showLyrics(null, -1);
		String input = ShowLyricsTask.load(track.lrc);
		_showLyrics = new ShowLyricsTask("show-lyrics-task", input);
		GetData.find(track);
	}
	
	public void playPause(){
		if(_player.isPlay()){
			_player.pause();
		}else{
			_player.play();
		}
	}
	
	public Duration getPlayingTime(){
		return _player.getPlayingTime();
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
	
	public void showPlayingTime(Duration value){
		_player.getPlayingTime();
	}
}
