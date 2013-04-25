package com.github.michael99man.SkypeMoodScheduler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AutoReplier implements ToolBuilder {

	private static boolean on = false;
	private static final int[] windowSize = { 100, 100, 300, 300 };
	private String target = "#universelkl/$e86518bb31167572";
	private static String user = "michael99man";

	// IDEA: KEEP LIST OF MESSAGES PLAYED WHILE IT'S ON
	private int delay = 1;

	private JTextField replyMessage;
	private JTextField durationField;
	private JButton goButton;
	private JButton stopButton;
	private JPanel parent;
	private ToolBuilder instance = this;

	@Override
	public void build(JPanel pane) {
		parent = pane;
		parent.setLayout(null);

		goButton = new JButton("Go!");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!on) {
					on = true;
					Thread checker = new Thread(new Checker(delay, target, replyMessage.getText()));
					checker.start();
					System.out.println("Start");
				}

			}
		});
		goButton.setBounds(20, 20, 40, 40);

		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (on) {
					on = false;
					System.out.println("Stop");
				}

			}
		});
		stopButton.setBounds(20, 100, 40, 40);

		replyMessage = new JTextField("Input reply here");
		replyMessage.setBounds(80,70,200,30);

		parent.add(replyMessage);
		parent.add(stopButton);
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

		// private static String checkCommand;

		private static String REPLYTEXT;
		private int delay;
		// private String target;
		// private int originalEnd;
		private static Map<String, Integer> IDMAP = new HashMap<String, Integer>();

		public Checker(int delay, String target, String reply) {
			REPLYTEXT = reply;
			this.delay = delay;
			// this.target = target;

			// String script =
			// "tell application \"Skype\" to send command \"CHATMESSAGE " +
			// target + " Autoreplier: On\" script name \"SkypeToolkit\"";
			// ScriptRunner.runScript(script);

			String script = "tell application \"Skype\" to return (send command \"SEARCH RECENTCHATS\" script name \"SkypeTookit\")";
			String recentChats = ScriptRunner.get(script)
					.replaceAll("CHATS ", "").replaceAll(",", "");
			String[] chatList = recentChats.split(" ");
			
			System.out.println(chatList.length);

			for (String id : chatList) {
				int[] midArray = check(id);
				int origLast = midArray[midArray.length - 1];
				IDMAP.put(id, origLast);

				// int[] originalValues = check(id);
				// originalEnd = originalValues;
			}

		}

		private String getCommand(String target) {
			String checkCommand = "tell application \"Skype\" to return (send command \"GET CHAT "
					+ target
					+ " RECENTCHATMESSAGES\" script name \"SkypeToolkit\")";
			return checkCommand;

		}

		@Override
		public void run() {
			while (on) {
				try {
					for (String id : IDMAP.keySet()) {

						int[] newValues = check(id);
						int newEnd = newValues[newValues.length - 1];

						if (!(newEnd == IDMAP.get(id))) {
							if (validate(newEnd) == 1) {
								System.out.println("There's something new!");

								String script = "tell application \"Skype\" to send command \"CHATMESSAGE "
										+ id
										+ " " + REPLYTEXT + "\" script name \"SkypeToolkit\"";
								ScriptRunner.runScript(script);

							} else if (validate(newEnd) == 0) {
								System.out.println("You just wrote something.");
							}
							IDMAP.put(id, newEnd);
						}
					}
					Thread.sleep(delay * 1000);
				} catch (Exception e) {
					e.printStackTrace();
					on = false;
				}
			}
		}

		private int[] check(String id) {

			String origMessages = ScriptRunner.get(getCommand(id));
			int parseLength = ("CHAT " + id + " RECENTCHATMESSAGES").length();
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

		private int validate(int end) {

			String script = "tell application \"Skype\" to return (send command \"GET CHATMESSAGE "
					+ end + " FROM_HANDLE\" script name \"SkypeToolkit\")";
			String sender = ScriptRunner.get(script);
			if (sender.contains(user)) {
				if ((ScriptRunner
						.get("tell application \"Skype\" to return (send command \"GET CHATMESSAGE "
								+ end + " BODY\" script name \"SkypeToolkit\")"))
						.contains(REPLYTEXT)) {
					return 2;
				} else {
					return 0;
				}
			} else {
				return 1;
			}
		}
	}
}
