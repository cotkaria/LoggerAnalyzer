package com.loggeranalyzer;

import java.io.Serializable;

public class DialogConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;
	private SearchParameters mSearchParameters;
	private String mUploadPath;
	private boolean mShouldSaveConfiguration;
	private String mFileToUploadExtension;

	public DialogConfiguration()
	{
		mSearchParameters = new SearchParameters();
		mUploadPath = "";
		mShouldSaveConfiguration = false;
		mFileToUploadExtension = ".zip";
	}
	
	public DialogConfiguration(SearchParameters searchParameters, String uploadPath, boolean shouldSaveConfiguration, String fileToUploadExtension)
	{
		mSearchParameters = searchParameters;
		mUploadPath = uploadPath;
		mShouldSaveConfiguration = shouldSaveConfiguration;
		mFileToUploadExtension = fileToUploadExtension;
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
	public String getFileToUploadExtension()
	{
		return mFileToUploadExtension;
	}
}
