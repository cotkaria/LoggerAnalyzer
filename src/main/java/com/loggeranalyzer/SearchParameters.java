package com.loggeranalyzer;

import java.io.Serializable;
import java.time.LocalDate;

public class SearchParameters implements Serializable {
	/**
	 * Configuration
	 */
	private static final long serialVersionUID = 1L;
	private String mFolderPath;
	private String mFindText;
	private LocalDate mDate;
	
	
	public SearchParameters(String folderPath, String findText, LocalDate date)
	{
		mFolderPath = folderPath;
		mFindText = findText;
		mDate = date;	
	}
	
	public String getFolderPath()
	{
		return mFolderPath;
	}
	public String getFindText()
	{
		return mFindText;
	}
	public LocalDate getDate()
	{
		return mDate;
	}
	
}
