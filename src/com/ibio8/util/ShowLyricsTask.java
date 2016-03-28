package com.ibio8.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibio8.controller.Controller;

import javafx.util.Duration;

public class ShowLyricsTask implements Runnable{
	private boolean _quit = false;
	private String _input;
	private List<String> _lyricsList;
	private List<Duration> _timingList;
	
	public ShowLyricsTask(String name, String input){
		_input = input;
		Thread thread = new Thread(this, name);
		thread.start();
	}
	   
	//see http://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
	public void stop(){
		_quit = true;
	}
	   
	public void run(){
		int lastIndex = -1;
		System.out.println("start parse lyrics ==> ");
		parse(_input);
		while(!_quit){
			Duration time = Controller.getInstance().getPlayingTime();
			//System.out.println("time " + time);
			//Since it uses greaterThan so the (i - 1) is the right current line.
			//That's why here it starts from -1.
			int index = -1;
			if(time != null && _lyricsList != null){
				for (Duration key : _timingList) {
					if(key.greaterThanOrEqualTo(time)){
						if(index != lastIndex){
							//System.out.println(index + " | " + _lyricsList.get(index));
							Controller.getInstance().showLyrics(_lyricsList, index);
							lastIndex = index;
						}
						break;
					}
					index++;
				}
			}
		}
	}

	static public String load(String url){
		String content = null;
		try {
			content = readFile(url, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return content;
	}
	
	static public String readFile(String path, Charset encoding) throws IOException{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	private void parse(String input){
		List<String> lines = new ArrayList<String>();
		Pattern regexLine = Pattern.compile("^\\[[\\d:.]+\\]\\S*(.*)", Pattern.MULTILINE);
		if(input != null){
			Matcher mLine = regexLine.matcher(input);
			while (mLine.find()){
				//System.out.println("Found a: " + m.group(0) + "|" + m.group(1) + "|" + m.group(2));
				lines.add(mLine.group());
			}
			
			_lyricsList = new ArrayList<String>();
			_timingList = new ArrayList<Duration>();
			Pattern regexTime = Pattern.compile("\\d{2}:\\d{2}\\.\\d{2}");
			for(String line : lines){
				Matcher mTime = regexTime.matcher(line);
				//find the first one only
				if(mTime.find()){
					String time = mTime.group();
					//System.out.println("time: " + time); //time: 00:03.13
					int minute = Integer.parseInt(time.substring(0, 2));
					int second = Integer.parseInt(time.substring(3, 5));
					int millis = Integer.parseInt(time.substring(6));
					//System.out.println(minute + "|" + second + "|" + millis);
					//System.out.println((minute * 60 + second) * 1000 + millis);
					_lyricsList.add(line.substring(mTime.end() + 1));
					_timingList.add(new Duration((double) ((minute * 60 + second) * 1000 + millis)));
				}
			}
			
			//String str = "[00:04.87]Naughty Boy - Sam Smith";
			//Matcher m2 = Pattern.compile("\\d{2}:\\d{2}\\.\\d{2}").matcher(str);
			//System.out.println(m2.find());
			//System.out.println(m2.group());
			//System.out.println(str.substring(m2.end() + 1));
		}
	}
}
