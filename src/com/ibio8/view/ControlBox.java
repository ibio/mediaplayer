package com.ibio8.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ControlBox extends JPanel {
	private static final long serialVersionUID = 1L;
	protected Canvas _mediator = null;
	protected JButton _btnPlayPause;
	protected boolean _isPlaying = false;
	
	public ControlBox(Canvas mediator){
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		_mediator = mediator;
		
		//Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		
		JButton btnPrev = new JButton("< Prev");
		JButton btnNext = new JButton("Next >");
		_btnPlayPause = new JButton("Play");
		
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
		buttonPane.setBorder(BorderFactory.createTitledBorder("Control"));
		
		//see https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html#filler
		
		buttonPane.add(btnPrev);
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(_btnPlayPause);
		//buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(btnNext);

        JSlider progress = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        progress.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        progress.setBorder(BorderFactory.createTitledBorder("Progress"));
        //progress.setMajorTickSpacing(10);
        //progress.setMinorTickSpacing(1);
        //progress.setPaintTicks(true);
        //progress.setPaintLabels(true);

        _btnPlayPause.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            // display/center the jdialog when the button is pressed
//            System.out.println("on play-pause click!");
            _mediator.playPause();
            setPlayPause(!_isPlaying);
          }
        });
        btnPrev.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	          _mediator.playPrev();
	        }
	    });
        btnNext.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	          _mediator.playNext();
	        }
	    });
        //
        this.add(buttonPane);
        this.add(progress);
        
        //this.setPreferredSize(new Dimension(300, 100));
	}
	
	public void setPlayPause(boolean flag){
		_isPlaying = flag;
		if(_isPlaying){
			_btnPlayPause.setText("Pause");
		}else{
			_btnPlayPause.setText("Play");
		}
	}
	
}
