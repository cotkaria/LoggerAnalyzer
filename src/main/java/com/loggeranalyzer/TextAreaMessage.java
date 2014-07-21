package com.loggeranalyzer;

public class TextAreaMessage {

	private static StringBuffer mMessage = new StringBuffer();
	private static String mTextColor = "-fx-text-fill: black";
	
	public static void setMessage(StringBuffer message) {
		mMessage = message;
	}
	public static StringBuffer getMessage()
	{
		return mMessage;
	}
	public static void setTextColor(String textColor) {
		mTextColor = textColor;
	}
	public static String getTextColor()
	{
		return mTextColor;
	}
}
