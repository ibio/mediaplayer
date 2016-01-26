package com.ibio8.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.ibio8.controller.Controller;
import com.ibio8.model.vo.TrackVO;

public class Canvas extends JFrame {
	private static final long serialVersionUID = 1L;
	protected ControlBox _controlBox;
	protected Playlist _playlist;
	protected JEditorPane _showPane;
	
	public Canvas(){
		super("Media Player");
		this.setLayout(new BorderLayout());
		
		//initialize();
		_controlBox = new ControlBox(this);
		_playlist = new Playlist(this);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		
		_showPane = new JEditorPane();
		_showPane.setEditable(false);
		_showPane.setContentType("text/html");
		//editorPane.setBackground(Color.cyan);
		_showPane.setPreferredSize(new Dimension(300, 290));
//		try {
//			editorPane.setPage(new URL("http://ibio.github.io"));			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		infoPanel.add(_controlBox);
		infoPanel.add(_showPane);
        //infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        //infoPanel.add(new JScrollPane(editorPane));
        
        //this.setLayout(new GridLayout(1,2));
        this.add(infoPanel, BorderLayout.LINE_START);
        this.add(_playlist, BorderLayout.LINE_END);

        //
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
	}
	
	public void showContent(String html){
		_showPane.setText(html);
	}
	
	public void buildTracks(List<TrackVO> list){
		_playlist.createList(list);
	}
	
	public void changeCurrentItem(int index, TrackVO track){
		_playlist.changeIndex(index);
		//since it's auto-play
		_controlBox.setPlayPause(true);
	}
	
	public void playPause(){
		Controller.getInstance().playPause();
	}
	
	public void playPrev(){
		Controller.getInstance().previous();
	}
	
	public void playNext(){
		Controller.getInstance().next();
	}
	
	public void loadTrack(int index){
		Controller.getInstance().change(index);
	}
	
	public void searchDrive(String drive){
		Controller.getInstance().search(drive);
	}
}
