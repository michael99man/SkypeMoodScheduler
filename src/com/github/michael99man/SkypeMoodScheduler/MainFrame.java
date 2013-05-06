package com.github.michael99man.SkypeMoodScheduler;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = -487046159617990173L;

	private JPanel contentPane;

	public static Boolean moodScheduler = false;
	public static Boolean spammer = false;
	public static Boolean dynamicMood = false;
	public static Boolean autoreplier = false;

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
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 302, 216);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton MoodScheduler = new JButton("Mood Scheduler");
		MoodScheduler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!moodScheduler) {

					moodScheduler = true;
				}
			}
		});
		MoodScheduler.setBounds(20, 74, 117, 44);
		contentPane.add(MoodScheduler);

		JButton SpammerTool = new JButton("Spammer Tool");
		SpammerTool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!spammer) {
					addBuilder(new Spammer());
					spammer = true;
				}
			}
		});
		SpammerTool.setBounds(160, 74, 117, 44);
		contentPane.add(SpammerTool);

		JButton DynamicUpdater = new JButton("Mood Updater");
		DynamicUpdater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!dynamicMood) {
					addBuilder(new DynamicMoodChanger());
					dynamicMood = true;
				}
			}
		});
		DynamicUpdater.setBounds(20, 129, 117, 44);
		contentPane.add(DynamicUpdater);

		JButton BOB = new JButton("AutoReplier");
		BOB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!autoreplier) {
					addBuilder(new AutoReplier());
					autoreplier = true;
				} else {
					System.out.println(autoreplier);
				}
			}
		});
		BOB.setBounds(160, 129, 117, 44);
		contentPane.add(BOB);

		JLabel topLogo = new JLabel("Skype Toolkit");
		topLogo.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 28));
		topLogo.setBounds(35, 18, 257, 44);
		contentPane.add(topLogo);

		System.out.println("Window Launch");

		// System.out.println("Old Mood: " + ScriptRunner.getMood());
		// String text =
		// "Hi! This is an attempt to change my mood message using Applescript within Shell within Java! IKR";
		// LinkedBlockingQueue<String> lbqs = new LinkedBlockingQueue<String>();
		// lbqs.add(text);
		// SwingUtilities.invokeLater(new ChangeRunner(lbqs));
		// System.out.println("New Mood: " + ScriptRunner.getMood());

	}

	private void addBuilder(ToolBuilder builder) {
		JFrame newFrame = new frame(builder);
		
		newFrame.setVisible(true);
		int[] windowSize = builder.getWindowSize();

		
		newFrame.setBounds(windowSize[0], windowSize[1], windowSize[2],
				windowSize[3]);
		
		JPanel tempPane = new JPanel();
		newFrame.setContentPane(tempPane);
		builder.build(tempPane, newFrame);
		
	}
	
	public class frame extends JFrame implements WindowListener{

		private static final long serialVersionUID = -8054919442026309983L;
		public ToolBuilder builder;
		
		public frame(ToolBuilder Builder){
			addWindowListener(this);
			builder = Builder;
		}
		
		@Override
		public void windowActivated(WindowEvent e) {
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
			System.out.println("Window Closing");
			
			if (builder.getName().equals("MoodScheduler")){
				moodScheduler = false;
			} else if (builder.getName().equals("DynamicMoodChanger")){
				dynamicMood = false;
			} else if (builder.getName().equals("AutoReplier")){
				autoreplier = false;
			} else if (builder.getName().equals("Spammer")){
				spammer = false;
			} else {
				System.out.println("FAILURE: " + builder.getName());
			}
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			
		}
		
	}
}

