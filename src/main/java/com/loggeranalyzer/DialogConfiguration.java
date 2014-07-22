package com.loggeranalyzer;

import java.io.Serializable;

public class DialogConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;
	private SearchParameters mSearchParameters;
	private String mUploadPath;
	private boolean mShouldSaveConfiguration;
	
	public DialogConfiguration(SearchParameters searchParameters, String uploadPath, boolean shouldSaveConfiguration)
	{
		mSearchParameters = searchParameters;
		mUploadPath = uploadPath;
		mShouldSaveConfiguration = shouldSaveConfiguration;
	}
	
	public SearchParameters getSearchParameters()
	{
		return mSearchParameters;
	}
	
	public String getUploadPath()
	{
		return mUploadPath;
	}
	public boolean shouldSaveConfiguration()
	{
		return mShouldSaveConfiguration;
	}
	
}
