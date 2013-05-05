package com.github.michael99man.SkypeMoodScheduler;

import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DynamicMoodChanger implements ToolBuilder {

	private JPanel parent;
	private int[] windowSize = { 200, 100, 500, 300 };

	public static Boolean b;
	
	
	private JTextField textField;
	private Choice choice;
	private JButton goButton;
	private JFrame frame;
	public static boolean started = false;
	private Thread UPDATER;

	
	@Override
	public void build(JPanel pane, JFrame frame) {
		parent = pane;
		this.frame = frame;

		textField = new JTextField();
		textField.setText("Mood message here");
		textField.setBounds(20, 40, 50, 60);

		choice = new Choice();

		choice.add("Letter by Letter");
		choice.add("Word by Word");
		choice.add("Sentence by Sentence");

		choice.setBounds(30, 40, 50, 60);

		goButton = new JButton("Start");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!started) {
					try {
						System.out.println("BUTTON PRESSED: START");

						String modeString = choice.getSelectedItem();
						String text = textField.getText();
						String[] array = null;

						Modes mode = null;
						if (modeString.equals("Letter by Letter")) {
							mode = Modes.Letter;
							array = text.split("");
							String[] newArray = new String[array.length-1];
							System.arraycopy(array, 1, newArray, 0, array.length-1);
							array = newArray;
							
						} else if (modeString.equals("Word by Word")) {
							mode = Modes.Word;
							array = text.split(" ");
						} else if (modeString.equals("Sentence by Sentece")) {
							mode = Modes.Sentence;
							array = text.split(".");
						}

						run(array, mode);
						goButton.setText("Stop");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (started) {
					// STAFF
					started = false;
					UPDATER.interrupt();
					goButton.setText("Start");
					
				}
			}
		});
		goButton.setBounds(100, 200, 50, 50);

		parent.add(textField);
		parent.add(choice);
		parent.add(goButton);
	}

	@Override
	public void unbuild(JPanel pane) {
		parent = pane;
		parent.remove(textField);
		parent.remove(choice);
		parent.remove(goButton);
	}

	public int[] getWindowSize() {
		return windowSize;
	}

	private enum Modes {

		Letter, Word, Sentence
	}

	private void run(String text[], Modes mode) {
		started = true;
		UPDATER = new Thread(new Updater(text, mode));
		UPDATER.start();
		System.out.println("Thread Created: MODE: " + mode.name());
	}
	
	
	public static class Updater implements Runnable {
		private String[] text;
		private int delay = 2;
		private Modes mode;
		
		public Updater(String text[], Modes mode) {
			this.text = text;
			this.mode = mode;
		}

		@Override
		public void run() {
			while (true) {
				if (!started) {
					System.out.println("Thread Quit");
					return;
				}

				
				String mood = "";
				
				for (String s : text){
					mood += s;
					if (mode.equals(Modes.Word)){
						mood += " ";
					} else if (mode.equals(Modes.Sentence)){
						mood += ".";
					}
					System.out.println(mood);
					
					ScriptRunner.changeMood(mood);
					try {
						Thread.sleep(delay * 1000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}
				}
			}
		}

	}


	@Override
	public String getName() {
		return "DynamicMoodChanger";
	}
}
