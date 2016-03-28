package com.ibio8.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.ibio8.model.vo.TrackVO;

public class GetData {
	
	static public String find(TrackVO track){
		return load(track.title);
	}
	
	static private String load(String title){
		Document doc;
		String keyword = FilenameUtils.removeExtension(title);
		String result = null;
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
			doc = Jsoup.connect("https://en.wikipedia.org/w/index.php?search=" + keyword).get();
			//doc = Jsoup.connect("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + keyword).get();
			//Find the first record.
			Elements item = doc.select("ul.mw-search-results li").eq(0);
			System.out.println("search -->" + keyword);
			System.out.println("result -->" + doc);
			//Find the content of the first record.
			if(!item.isEmpty()){
				String href = item.select(".mw-search-result-heading a").attr("href");
				//System.out.println("href -->" + href);
				doc = Jsoup.connect("https://en.wikipedia.org" + href).get();
				//System.out.println(doc);
				Elements content = doc.select("#mw-content-text");
				Elements intro = content.select("p").eq(0);
				result = intro.html();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	static private String load(String title){
		URL url;
		URLConnection connection;
		BufferedReader input;
		String html = "";
		try {
			url = new URL("http://www.google.com/search?hl=en&q=" + title);
			connection = url.openConnection();
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while (input.readLine() != null){
				//System.out.println(inputLine);
				html += input.readLine();
			}
			input.close();
		}catch(MalformedURLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		//System.out.println(inputLine);
		return html;
	}*/
	
	
}
