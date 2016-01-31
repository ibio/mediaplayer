package com.ibio8.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ibio8.model.vo.TrackVO;

public class Playlist extends JPanel {
	private static final long serialVersionUID = 1L;
	protected Canvas _mediator;
	protected JList<String> _tracks;
	
	public Playlist(Canvas mediator){
		_mediator = mediator;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//
		JPanel buttonPane = new JPanel();
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
        	  DefaultListModel<String> listModel = (DefaultListModel<String>) _tracks.getModel();
        	  listModel.removeAllElements();
          }
        });
		
		DefaultComboBoxModel<String> driveList = new DefaultComboBoxModel<String>();
		driveList.addElement("Select Drive");
		String systemDrive = "/";
		for(File path : getAllDrives()){
	   		//System.out.println("Drive Name: " + path.getAbsolutePath());
	   		//to eliminate a large search of system files
	   		if(System.getenv("SystemDrive") != null){
	   			systemDrive = System.getenv("SystemDrive");
	   		}
			if(path.getAbsolutePath().startsWith(systemDrive)){
	   			driveList.addElement(System.getProperty("user.home"));
	   			//to eliminate a mass search on Mac only 
	   			driveList.addElement(System.getProperty("user.home") + "/Documents");
//	   			System.out.println(System.getProperty("user.home"));
	   		}else{
	   			driveList.addElement(path.getAbsolutePath());
	   		}
	   	}
		JComboBox<String> drives = new JComboBox<String>(driveList);
		//drives.setMaximumSize(new Dimension(150, 28));
		
//		comboBoxPane.setPreferredSize(new Dimension(100, 50));
		//driveList.setSelectedIndex(4);
		drives.addActionListener(new DriveSelectionHandler());
		
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createTitledBorder("Search"));
		//buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//see https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html#filler
		buttonPane.add(drives);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(btnSearch);
//		buttonPane.add(Box.createHorizontalGlue());
//		buttonPane.setPreferredSize(new Dimension(200, 80));
		
		//
		_tracks = new JList<String>(new DefaultListModel<String>()); //data has type Object[]
		_tracks.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		_tracks.setLayoutOrientation(JList.VERTICAL);
        //list.setVisibleRowCount(10);
		_tracks.addListSelectionListener(new PlaylistSelectionHandler());
        JScrollPane listScroller = new JScrollPane(_tracks);
        listScroller.setBorder(BorderFactory.createTitledBorder("Result"));
        
        
        this.add(buttonPane);
        this.add(listScroller);
        //this.setPreferredSize(new Dimension(250, 400));
	}
	
	public void createList(List<TrackVO> list){
		DefaultListModel<String> data = new DefaultListModel<String>();
		for (TrackVO item : list) {
			data.addElement(item.title);
	    }
		DefaultListModel<String> listModel = (DefaultListModel<String>) _tracks.getModel();
		listModel.removeAllElements();
        _tracks.setModel(data);
	}
	
	public void changeIndex(int index){
		_tracks.setSelectedIndex(index);
        _tracks.ensureIndexIsVisible(index);
	}
	
	class DriveSelectionHandler implements ActionListener {
		 
	    @Override
	    public void actionPerformed(ActionEvent event) {
	        @SuppressWarnings("unchecked")
			JComboBox<String> combo = (JComboBox<String>) event.getSource();
	        String name = (String) combo.getSelectedItem();
	        _mediator.searchDrive(name);
	    }
	    
	    /*
	    This is for ItemListener, so be sure it's implements ItemListener
	    //drives.addItemListener(new DriveSelectionHandler());
	    public void itemStateChanged(ItemEvent event) {
	    	System.out.println("event.getStateChange()" + event.getStateChange());
	    	System.out.println("ItemEvent.SELECTED" + ItemEvent.SELECTED);
	        if(event.getStateChange() == ItemEvent.SELECTED) {
	            // code here
	        }
	    }*/
	}
	
	//see https://docs.oracle.com/javase/tutorial/uiswing/events/listselectionlistener.html
	class PlaylistSelectionHandler implements ListSelectionListener {
	    public void valueChanged(ListSelectionEvent e) {
	    	@SuppressWarnings("unchecked")
			int index = ((JList<String>) e.getSource()).getSelectedIndex();
	    	//int firstIndex = e.getFirstIndex();
	        //int lastIndex = e.getLastIndex();
	        //final state only
	        if(!e.getValueIsAdjusting()){
//	        	System.out.println(index);
	        	_mediator.loadTrack(index);
	        }
	    }
	}
	   
	protected File[] getAllDrives(){
		File[] paths;
		//FileSystemView fsv = FileSystemView.getFileSystemView();
		//System.out.println("Description: "+fsv.getSystemTypeDescription(path));
		// returns pathnames for files and directory
		paths = File.listRoots();
		return paths;
	}
}
