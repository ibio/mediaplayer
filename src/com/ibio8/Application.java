//main
package com.ibio8;


import com.ibio8.controller.Controller;

public class Application {

	public static void main(String[] args) {
		Controller controller = Controller.getInstance();
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	//start from view
            	controller.initView();
            }
        });
    }
}
