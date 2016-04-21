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
   
   public SearchTask(String drive, String postfix){
	   _drive = drive;
	   _postfix = postfix;
   }
   
   //see http://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
   //right now use: ExecutorService => shutdownNow();
   public void stop(){
	   //_quit = true;
   }
   
   @Override
   public void run(){
	   System.out.println("Running " +  Thread.currentThread().getName());
	   //search(new File("D:\\").getAbsolutePath(), _postfix);
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
                //System.out.println("isInterrupted " + Thread.currentThread().isInterrupted());
                if(Thread.currentThread().isInterrupted()){
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