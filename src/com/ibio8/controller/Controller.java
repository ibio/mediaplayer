package com.ibio8.controller;

import java.util.List;

import com.ibio8.view.Canvas;
import com.ibio8.core.AudioPlayer;
import com.ibio8.model.Playlist;
import com.ibio8.model.vo.TrackVO;
import com.ibio8.util.GetData;
import com.ibio8.util.SearchTask;

public class Controller {
	static private volatile Controller _instance = null;
	private Canvas _canvas;
	private Playlist _playlist;
	private AudioPlayer _player;
	private SearchTask _task;
	
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
		if(_task != null){
			_task.stop();
		}
		_task = new SearchTask("search-task", drive, "mp3");
		_task.start();
	}
	
	public void loadTrack(TrackVO track){
		_player.load(track);
		_canvas.showContent(GetData.find(track));
	}
	
	public void playPause(){
		if(_player.isPlay()){
			_player.pause();
		}else{
			_player.play();
		}
	}

	//============= change view =============
	public void initView(Canvas canvas){
		_canvas = canvas;
	}
	
	public void refreshList(List<TrackVO> list){
		System.out.println(1);
		_canvas.buildTracks(list);
		System.out.println(2);
	}
	
	public void changeCurrentTrack(int index, TrackVO track){
		_canvas.changeCurrentItem(index, track);
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
