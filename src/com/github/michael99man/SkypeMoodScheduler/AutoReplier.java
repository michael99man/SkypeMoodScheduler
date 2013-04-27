package com.github.michael99man.SkypeMoodScheduler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AutoReplier implements ToolBuilder {

	private static boolean on = false;
	private static final int[] windowSize = { 100, 100, 300, 300 };
	//private String target = "#universelkl/$e86518bb31167572";
	private static String user = "michael99man";

	// IDEA: KEEP LIST OF MESSAGES PLAYED WHILE IT'S ON
	private int delay = 1;

	private JTextField replyMessage;
	private JButton goButton;
	private JButton stopButton;
	private JPanel parent;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	private static LinkedList<SkypeMessage> messageList = new LinkedList<SkypeMessage>();

	
//	 private ToolBuilder instance = this;

	@Override
	public void build(JPanel pane) {
		parent = pane;
		parent.setLayout(null);

		goButton = new JButton("Go!");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!on) {
					on = true;
					Thread checker = new Thread(new Checker(delay, replyMessage.getText()));
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
					
					String messages = "";
					
					for (SkypeMessage m : messageList){
						messages = messages + m.formatString() + "\n";
					}
					
					
					
					
					textArea.setText(messages);
					
					
				}

			}
		});
		stopButton.setBounds(20, 70, 40, 40);

		replyMessage = new JTextField("Input reply here");
		replyMessage.setBounds(60,10,230,30);

		
		textArea = new JTextArea();
		textArea.setBounds(65, 45, 230, 200);

		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(65, 45, 230, 200);
		scrollPane.createVerticalScrollBar();
		//scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
		
		
		parent.add(textArea);
		parent.add(scrollPane);
		
		
		parent.add(replyMessage);
		parent.add(stopButton);
		parent.add(goButton);
	}

	@Override
	public void unbuild(JPanel pane) {
		parent.remove(replyMessage);
		parent.remove(stopButton);
		parent.remove(goButton);

		
		if (on) {
			on = false;
			System.out.println("Stop");
		}
		
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

		public Checker(int delay, String reply) {
			REPLYTEXT = reply;
			this.delay = delay;

			String script = "tell application \"Skype\" to return (send command \"SEARCH RECENTCHATS\" script name \"SkypeTookit\")";
			String recentChats = ScriptRunner.get(script)
					.replaceAll("CHATS ", "").replaceAll(",", "");
			String[] chatList = recentChats.split(" ");
			
			System.out.println(chatList.length);

			for (String id : chatList) {
				int[] midArray = check(id);
				int origLast = midArray[midArray.length - 1];
				IDMAP.put(id, origLast);

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
			while (true) {
				if (!on){
					break;
				}
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
								
								
								String body = ScriptRunner.get("tell application \"Skype\" to return (send command \"GET CHATMESSAGE " + newEnd + " BODY\" script name \"SkypeToolkit\")");
								int index = body.indexOf("BODY");
								String message = body.substring(index + 5);
								
								
								String sender = ScriptRunner.get("tell application \"Skype\" to return (send command \"GET CHATMESSAGE "
										+ newEnd + " FROM_HANDLE\" script name \"SkypeToolkit\")");
								index = sender.indexOf("FROM_HANDLE");
								sender = sender.substring(index + 12);
							
								messageList.add(SkypeMessage.logNew(message, sender));
								
								

							} else if (validate(newEnd) == 0) {
								System.out.println("You just wrote something.");
								//System.out.println(id + "   " + newEnd);
								
								

								
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
