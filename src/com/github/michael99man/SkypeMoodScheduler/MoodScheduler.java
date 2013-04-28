package com.github.michael99man.SkypeMoodScheduler;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MoodScheduler implements ToolBuilder {

	private JFrame frame;
	private JPanel parent;
	
	@Override
	public void build(JPanel pane, JFrame frame) {
		this.frame = frame;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unbuild(JPanel pane) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getWindowSize() {
		// TODO Auto-generated method stub
		return null;
	}

}
