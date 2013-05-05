package com.github.michael99man.SkypeMoodScheduler;

import javax.swing.JFrame;
import javax.swing.JPanel;

public interface ToolBuilder {
	String getName();
	void build(JPanel pane, JFrame frame);
	void unbuild(JPanel pane);
	int[] getWindowSize();
	
}
