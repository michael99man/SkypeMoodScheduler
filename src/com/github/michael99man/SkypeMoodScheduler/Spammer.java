package com.github.michael99man.SkypeMoodScheduler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Spammer implements ToolBuilder {
	@SuppressWarnings("unused")
	private JFrame frame;
	private JPanel parent;
	private int[] windowSize = { 100, 100, 350, 150 };

	private boolean on = false;
	
	private JButton goButton;
	private JTextField textfield;
	private JTextField amountfield;
	private JTextField targetfield;
	
	private JProgressBar progressbar;
	
	private boolean started = false;

	@Override
	public String getName() {
		return "Spammer";
	}

	@Override
	public void build(JPanel pane, JFrame frame) {
		parent = pane;
		this.frame = frame;

		frame.setResizable(false);

		parent.setLayout(new BoxLayout(parent, BoxLayout.PAGE_AXIS));

		goButton = new JButton("Go!");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (!started){
					start();
					goButton.setText("Stop");
				} else {
					on = false;
					goButton.setText("Start");
					System.out.println("STOPPED");
				}
			}
		});

		goButton.setMinimumSize(new Dimension(70, 25));

		textfield = new JTextField();
		addReturnListener(textfield);
		
		textfield.setMaximumSize(new Dimension(700, 44));

		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Spam Message");
		title.setTitleJustification(TitledBorder.LEFT);
		textfield.setBorder(title);

		amountfield = new JTextField();
		
		amountfield.setMaximumSize(new Dimension(70, 44));
		amountfield.setPreferredSize(new Dimension(70,44));
		TitledBorder newTitle = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLUE),
				"Amount");
		newTitle.setTitleJustification(TitledBorder.LEFT);
		amountfield.setBorder(newTitle);

		addReturnListener(amountfield);
		
		targetfield = new JTextField();
		targetfield.setMaximumSize(new Dimension(300,44));
		targetfield.setPreferredSize(new Dimension(300,44));
		
		TitledBorder yayTitle = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK),
				"Target");
		yayTitle.setTitleJustification(TitledBorder.RIGHT);
		targetfield.setBorder(yayTitle);
		
		addReturnListener(targetfield);
		
		Box box = Box.createHorizontalBox();
		
		progressbar = new JProgressBar(0);
		progressbar.setStringPainted(true);
		progressbar.setString("Press Go to Begin");
		
		
		progressbar.setMinimumSize(new Dimension(600,50));
		
		parent.add(textfield);
		box.add(amountfield);
		box.add(Box.createHorizontalGlue());
		box.add(targetfield);
		
		parent.add(box);
		
		
		
		Box box2 = Box.createHorizontalBox();
		
		box2.add(goButton);
		box2.add(progressbar);
		parent.add(Box.createVerticalGlue());
		parent.add(box2);
	}

	private void start() {
		int amount;
		String text;
		
		String temp = amountfield.getText();
		temp.replaceAll(" ", "");
		
		try{
		amount = Integer.parseInt(temp);
		} catch (NumberFormatException e){
			amountfield.setText("");
			progressbar.setString("ERROR");
			return;
		}
		text = textfield.getText();
		
		String target = targetfield.getText();
		
		
		if (!started) {
			Thread thread = new Thread(new SpamThread(text, amount, target));
			thread.start();
			
			started = true;
		}
	}

	@Override
	public void unbuild(JPanel pane) {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getWindowSize() {
		return windowSize;
	}

	public void addReturnListener(JTextField textfield){
		textfield.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					start();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
	}
	
	public class SpamThread implements Runnable {

		private String message;
		private int amount;
		private String target;
		private boolean chatmode = false;
		
		public SpamThread(String message, int amount, String target) {
			this.message = message;
			this.amount = amount;
			this.target = target;
			on = true;
			System.out.println("Thread created");
			progressbar.setMaximum(amount);
			progressbar.setString("0/" + amount);
			
			if (target.startsWith("#")){
				System.out.println("CHATMODE = ON");
				chatmode = true;
			}
		}

		@Override
		public void run() {
			if (ScriptRunner.SkypeOn()){
				for (int i = 0; i < amount; i++) {
					if (!on){
						System.out.println("Thread shutting down");
						progressbar.setString("Press Go to Start");
						progressbar.setValue(0);
						progressbar.setMaximum(0);
						started = false;
						return;
					} else {
						String command = "MESSAGE";
						if (chatmode){
							command = "CHATMESSAGE";
						} 
						ScriptRunner.runScript("tell application \"Skype\" to (send command \"" + command + " " + target + " " + message + "\" script name \"Skype Toolkit\")");
					
						progressbar.setValue(i);
						progressbar.setString(i+"/" + amount);
					}
				}
			} else {
				System.out.println("Skype is off");
			}
			progressbar.setValue(amount);
			progressbar.setString("DONE!");
			
			on = false;
			started = false;
		}

	}
}
