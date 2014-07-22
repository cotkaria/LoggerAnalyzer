package com.loggeranalyzer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchResult 
{
	private SimpleStringProperty mPath;
	private SimpleIntegerProperty mOccurences;
	private SimpleStringProperty mDate;
	private SimpleStringProperty mTime;
	private SimpleBooleanProperty mIsUploaded;
	
	public SearchResult(int occurences, String path, String date, String time)
	{
		mPath = new SimpleStringProperty(path);
		mOccurences = new SimpleIntegerProperty(occurences);
		mDate = new SimpleStringProperty(date);
		mTime = new SimpleStringProperty(time);
		mIsUploaded = new SimpleBooleanProperty(false);
	}
	public IntegerProperty occurencesProperty ()
	{
		return mOccurences;
	}
	public StringProperty pathProperty ()
	{
		return mPath;
	}
	public StringProperty dateProperty ()
	{
		return mDate;
	}
	public StringProperty timeProperty ()
	{
		return mTime;
	}
	public BooleanProperty uploadedProperty()
	{
		return mIsUploaded;
	}
	 
}
