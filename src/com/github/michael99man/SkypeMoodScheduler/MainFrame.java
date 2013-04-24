package com.github.michael99man.SkypeMoodScheduler;

import java.awt.EventQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		setBounds(100, 100, 301, 208);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton MoodScheduler = new JButton("Mood Scheduler");
		MoodScheduler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		MoodScheduler.setBounds(20, 74, 117, 44);
		contentPane.add(MoodScheduler);
		
		JButton SpammerTool = new JButton("Spammer Tool");
		SpammerTool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		SpammerTool.setBounds(160, 74, 117, 44);
		contentPane.add(SpammerTool);
		
		JButton DynamicUpdater = new JButton("Mood Updater");
		DynamicUpdater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addBuilder(new DynamicMoodChanger());
			}
		});
		DynamicUpdater.setBounds(20, 129, 117, 44);
		contentPane.add(DynamicUpdater);
		
		JButton BOB = new JButton("BOB");
		BOB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		BOB.setBounds(160, 129, 117, 44);
		contentPane.add(BOB);
		
		JLabel topLogo = new JLabel("Skype Toolkit");
		topLogo.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 28));
		topLogo.setBounds(35, 18, 257, 44);
		contentPane.add(topLogo);
		
		System.out.println("Window Launch");
		
		System.out.println("Old Mood: " + ScriptRunner.getMood());
		String text = "Hi! This is an attempt to change my mood message using Applescript within Shell within Java! IKR";
		LinkedBlockingQueue<String> lbqs = new LinkedBlockingQueue<String>();
		lbqs.add(text);
		SwingUtilities.invokeLater(new ChangeRunner(lbqs));
		System.out.println("New Mood: " + ScriptRunner.getMood());

	}
	
	
	private void addBuilder(ToolBuilder builder){
		JFrame newFrame = new JFrame();
		newFrame.setVisible(true);
		
		newFrame.setBounds(50, 50, 50, 50);
		JPanel tempPane = new JPanel();
		newFrame.setContentPane(tempPane);
		
		builder.build(tempPane);
	}
}
