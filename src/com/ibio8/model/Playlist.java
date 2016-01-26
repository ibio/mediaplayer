
package com.ibio8.model;

import java.util.ArrayList;
import java.util.List;

import com.ibio8.controller.Controller;
import com.ibio8.model.vo.TrackVO;

public class Playlist {
	protected List<TrackVO> _list = new ArrayList<TrackVO>();
	//NOTICE: set default as -1 in order to fire it first time correctly
	protected int _index = -1;
	
	public void add(List<TrackVO> list){
		_list.addAll(list);
		//System.out.println(_list.toString());
		Controller.getInstance().refreshList(_list);
	}
	
	public void change(int index){
		if(index >= 0 && index < _list.size()){
			dispatchNewIndex(index);
		}
	}
	
	public void previous(){
		if(_index - 1 >= 0){
			dispatchNewIndex(_index - 1);
		}
	}
	
	public void next(){
		if(_index + 1 < _list.size()){
			dispatchNewIndex(_index + 1);
		}
	}
	
	public void update(TrackVO track){
		_list.set(_index, track);
	}
	
	protected void dispatchNewIndex(int index){
		if(index != _index){
			_index = index;
			Controller.getInstance().changeCurrentTrack(_index, _list.get(_index));
			Controller.getInstance().loadTrack(_list.get(_index));
		}
	}
}
