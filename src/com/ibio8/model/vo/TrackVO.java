package com.ibio8.model.vo;

public class TrackVO {
	public String title;
	public String url;
	
	public TrackVO(String title, String url){
		this.title = title;
		this.url = url;
	}
	
	public String toString(){
		return this.title + "|" + this.url;
	}
}
