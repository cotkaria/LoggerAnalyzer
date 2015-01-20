package com.loggeranalyzer;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchResultData
{
	private ObservableList<SearchResult> mSearchResultsList;
	private String mFormattedOuput;
	
	public SearchResultData(String searchRoot, String searchedText, int totalOccurences, 	
							LocalDate localDate, List<SearchResult> searchResultsList)
	{
		mSearchResultsList = FXCollections.observableArrayList(searchResultsList);
		if (totalOccurences > 1)
		{
			mFormattedOuput = "Searched for \"" + searchedText + "\" in \"" + searchRoot + "\" on " + localDate + " -> " + totalOccurences + " crashes";
		}	
		else
		{
			mFormattedOuput = "Searched for \"" + searchedText + "\" in \"" + searchRoot + "\" on " + localDate + " -> " + totalOccurences + " crash";
		}
	}
	
	public ObservableList<SearchResult> getSearchResults()
	{
		return mSearchResultsList;
	}
	@Override
	public String toString()
	{
		return mFormattedOuput;
		
	}
}

