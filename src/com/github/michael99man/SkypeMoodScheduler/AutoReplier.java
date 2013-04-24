package com.github.michael99man.SkypeMoodScheduler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AutoReplier implements ToolBuilder {

	private static final int[] windowSize = { 100, 100, 300, 300 };

	private String target = "#michael99man/$williamrandyandmay;ea596a9e35ad23e1";
	private static String user = "michael99man";

	private int delay = 1;

	private JTextField durationField;
	private JButton goButton;
	private JPanel parent;

	@Override
	public void build(JPanel pane) {
		parent = pane;

		goButton = new JButton("Go!");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread checker = new Thread(new Checker(delay, target));
				checker.start();

			}
		});
		goButton.setBounds(20, 20, 200, 200);

		parent.add(goButton);
	}

	@Override
	public void unbuild(JPanel pane) {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getWindowSize() {
		return windowSize;
	}

	private static class Checker implements Runnable {

		private static String checkCommand;

		private int delay;
		private String target;
		private int originalEnd;

		public Checker(int delay, String target) {
			this.delay = delay;
			this.target = target;

			checkCommand = "tell application \"Skype\" to return (send command \"GET CHAT "
					+ target
					+ " RECENTCHATMESSAGES\" script name \"SkypeToolkit\")";
			int[] originalValues = check();
			originalEnd = originalValues[originalValues.length - 1];
		}

		@Override
		public void run() {
			while (true) {
				try {

					int[] newValues = check();
					int newEnd = newValues[newValues.length - 1];

					if (!(newEnd == originalEnd)) {
						if (validate(newEnd)) {
							System.out.println("There's something new!");
							
							originalEnd = new Integer(newEnd);
						} else {
							System.out.println("You just wrote something");
						}

					}
					Thread.sleep(delay * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private int[] check() {

			String origMessages = ScriptRunner.get(checkCommand);
			int parseLength = ("CHAT " + target + " RECENTCHATMESSAGES")
					.length();
			origMessages = (origMessages.substring(parseLength + 1))
					.replaceAll(",", "");
			String[] origStrings = origMessages.split(" ");

			int[] tempArray = new int[origStrings.length];

			int i = 0;
			for (String s : origStrings) {
				tempArray[i] = Integer.parseInt(s);
				i++;
			}

			return tempArray;
		}

		private boolean validate(int end) {

			String script = "tell application \"Skype\" to return (send command \"GET CHATMESSAGE "
					+ end + " FROM_HANDLE\" script name \"SkypeToolkit\")";
			String sender = ScriptRunner.get(script);
			if (sender.contains(user)) {
				return false;
			} else {
				return true;
			}
		}
	}
}
