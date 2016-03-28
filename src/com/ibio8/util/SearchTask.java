package com.ibio8.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ibio8.controller.Controller;
import com.ibio8.model.vo.TrackVO;

public class SearchTask implements Runnable {
   private String _drive;
   private String _postfix;
   private boolean _quit = false;
   
   public SearchTask(String name, String drive, String postfix){
	   _drive = drive;
	   _postfix = postfix;
	   Thread thread = new Thread(this, name);
	   thread.start();
	   System.out.println("Thread " +  name + " exiting.");
   }
   
   //see http://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
   public void stop(){
	   _quit = true;
   }
   
   public void run(){
	   //System.out.println("Running " +  _threadName);
	   //File p = new File("D:\\");
	   //search(p.getAbsolutePath(), _postfix);
	   search(_drive, _postfix);
   }
   
   private void search(String volume, String postfix){
		File root = new File(volume);
		List<TrackVO> list = new ArrayList<TrackVO>(); 
        //String postfix = "mp3";
        try {
            boolean recursive = true;
            Collection<File> files = FileUtils.listFiles(root, null, recursive);
            for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
                File file = (File) iterator.next();
                if(_quit){
                	break;
                }
                //System.out.println("Searching " + volume + " | " + file.getName());
                if (file.getName().toLowerCase().endsWith(postfix)){
                	String path = file.getAbsolutePath();
                	System.out.println(path);
                	String lrc = path.substring(0, path.lastIndexOf(".mp3")) + ".lrc";
                	list.add(new TrackVO(file.getName(), lrc, path));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO add --> build
        Controller.getInstance().addTracks(list);
	}
}