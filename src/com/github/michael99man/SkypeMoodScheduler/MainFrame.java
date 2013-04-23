package com.github.michael99man.SkypeMoodScheduler;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -487046159617990173L;

	private JPanel contentPane;

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		System.out.println("Window Launch");
		
		
		changeMood("Hi! This is an attempt to change my mood message using Applescript within Shell within Java! IKR");

				

	}
	
	private static final ScriptEngineManager sem = new ScriptEngineManager();
	private static final ScriptEngine engine = sem.getEngineByName("AppleScript");
	
	public static void changeMood(String in){
		
		String script = "tell application \"Skype\" to send command \"SET PROFILE MOOD_TEXT " + in + "\" script name \"MoodChanger\"";
		
		try {
			engine.eval(script);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
