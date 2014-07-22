package com.loggeranalyzer;

import java.util.List;

public class SearchData
{
	private String mSearchRoot;
	private int mTotalOccurences;
	private List<SearchResult> mSearchResultsList;
	private String mSearchedText;
	
	public SearchData(String searchRoot, String searchedText, int totalOccurences, List<SearchResult> searchResultsList)
	{
		mSearchRoot = searchRoot;
		mSearchedText = searchedText;
		mTotalOccurences = totalOccurences;
		mSearchResultsList = searchResultsList;
	}
	
	public List<SearchResult> getSearchResults()
	{
		return mSearchResultsList;
	}
	
}

