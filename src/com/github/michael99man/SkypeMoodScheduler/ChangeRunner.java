package com.github.michael99man.SkypeMoodScheduler;

import java.util.Queue;

public class ChangeRunner implements Runnable{

	private Queue<String> moods;
	
	public ChangeRunner(Queue<String> moods){
		this.moods = moods;
	}
	
	@Override
	public void run(){
		MainFrame.changeMood(moods.poll());
	}

}
