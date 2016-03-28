package com.ibio8.model.vo;

public class TrackVO {
	public String title;
	public String lrc;
	public String url;
	
	public TrackVO(String title, String lrc, String url){
		this.title = title;
		this.lrc = lrc;
		this.url = url;
	}
	
	public String toString(){
		return this.title + "|" + this.lrc + "|" + this.url;
	}
}
