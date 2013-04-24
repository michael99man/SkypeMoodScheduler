package com.github.michael99man.SkypeMoodScheduler;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public final class ScriptRunner {

	private static final ScriptEngineManager sem = new ScriptEngineManager();
	private static ScriptEngine engine = sem.getEngineByName("AppleScript");
	
	public static void changeMood(String in){
		
		String script = "tell application \"Skype\" to send command \"SET PROFILE MOOD_TEXT " + in + "\" script name \"MoodChanger\"";
		
		try {
			engine.eval(script);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String getMood(){
		String script = "tell application \"Skype\" to return (send command \"GET PROFILE MOOD_TEXT\" script name \"MoodGetter\")";
		
		try {
			engine.put("tempMood",engine.eval(script));
			String tempString = (String) engine.get("tempMood");
			return tempString.replaceAll("PROFILE MOOD_TEXT ","");
		} catch (ScriptException e) {
			e.printStackTrace();
			return "Error";
		}
	}
}
