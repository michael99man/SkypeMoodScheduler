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
	private int[] windowSize = { 400, 100, 500, 500 };

	
	private JTextField textField;
	private Choice choice;
	private JButton goButton;
	private JTextField amountField;
	private JFrame frame;
	
	@Override
	public void build(JPanel pane, JFrame frame) {
		parent = pane;
		this.frame = frame;
		
		textField = new JTextField();
		textField.setText("Mood message here");
		textField.setBounds(20,40,50,60);
		
		choice = new Choice();
		
		choice.add("Letter by Letter");
		choice.add("Word by word");
		choice.add("Sentence by sentence");

		choice.setBounds(30,40,50,60);
		
		amountField = new JTextField();
		amountField.setText("Mood message here");
		amountField.setBounds(20,40,50,60);
		
		goButton = new JButton("Start");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					run(textField.getText(), choice.getSelectedItem(), Integer.parseInt(amountField.getText()));
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		goButton.setBounds(100,200,50,50);
		
		parent.add(textField);
		parent.add(choice);
		parent.add(goButton);
		parent.add(amountField);
	}

	@Override
	public void unbuild(JPanel pane) {
		parent = pane;
		parent.remove(textField);
		parent.remove(choice);
		parent.remove(goButton);
		parent.remove(amountField);
	}

	public int[] getWindowSize() {
		return windowSize;
	}

	private void run(String text, String mode, int amount) {
		System.out.println(text+mode+amount);
	}
}
