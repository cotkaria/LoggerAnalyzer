package com.loggeranalyzer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogAnalyzer 
{	
	
	public static SearchResultData search(SearchParameters searchParameters)
	{	
		String folderPath = "";
		int totalOccurences = 0;
		List <SearchResult> searchResults = new ArrayList<SearchResult>();
				
		try 
		{
			folderPath = searchParameters.getFolderPath();
			File logFilesDirectory = new File(folderPath);
			if (!logFilesDirectory.exists()) //should not be necessary to make this validation
			{
				folderPath = "Cannot find path \""+folderPath+"\"";
			}
			else 
			{	
				LocalDate date = searchParameters.getDate();
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
				String formattedDate = dateFormat.format(date);
				String findText = searchParameters.getFindText();
				
				File[] logDirectories = logFilesDirectory.listFiles();
				for (File f:logDirectories) 
				{
					String fileName = f.getName();
					if (f.isDirectory() && fileName.length() > 7 && fileName.substring(0,8).equals(formattedDate)) 
					{
						File NAD_DebugLogFile = new File(f.getAbsolutePath()+"\\NAD_Debug.txt");
						if (NAD_DebugLogFile.exists() && NAD_DebugLogFile.isFile()) 
						{							
							int matches = getMatchesFromFile(NAD_DebugLogFile, findText);
							totalOccurences += matches;
							
							System.out.println("Found " + matches + " occurences in " + f.getAbsolutePath());
							String logHour = fileName.substring(9, 11);
							String logMinute = fileName.substring(11,13);
							String logSecond = fileName.substring(13,15);
							String logTime = logHour + ":" + logMinute +":" + logSecond;
							
							searchResults.add(new SearchResult(matches, f.getAbsolutePath(), date.toString(), logTime));
						}
					}
				}
			}
		} 
		catch(SecurityException e) 
		{
			e.printStackTrace();
		}
		return new SearchResultData(folderPath, searchParameters.getFindText(), 
									totalOccurences, searchParameters.getDate(), searchResults);
	}
	
	private static int getMatchesFromFile(File fileName, String searchedPattern)
	{	
		BufferedReader br = null;
		int counter = 0;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String currentLine = null;
			while((currentLine = br.readLine())!=null) {
				if (currentLine.toLowerCase().contains(searchedPattern.toLowerCase())) 
				{
					counter++;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return counter;
	}
	
}

