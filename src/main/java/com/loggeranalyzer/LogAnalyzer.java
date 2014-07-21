package com.loggeranalyzer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.loggeranalyzer.TextAreaMessage;

public class LogAnalyzer 
{	
	public static void main(String[] args) 
	{	
		SearchParameters searchParameters = new SearchParameters("D:\\Logs", "Linux version 3.0.21+", LocalDate.now(), false);
		search(searchParameters);
	}
	
	public static StringBuffer search(SearchParameters searchParameters)
	{	
		//StringBuffer output = new StringBuffer();
		try 
		{
			String folderPath = searchParameters.getFolderPath();
			File logFilesDirectory = new File(folderPath);
			if (!logFilesDirectory.exists()) 
			{
				//output.append("Cannot find path \""+folderPath+"\"\n");
				TextAreaMessage.getMessage().append("Cannot find path \""+folderPath+"\"\n\n");
				TextAreaMessage.setTextColor("-fx-text-fill: black");
				//System.out.println("Cannot find path \""+folderPath+"\"");
			}
			else 
			{
				LocalDate date = searchParameters.getDate();
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
				String findText = searchParameters.getFindText();
				
				//output.append("Searching for " + findText + " in " + folderPath + ":\n");
				TextAreaMessage.setTextColor("-fx-text-fill: black");
				TextAreaMessage.getMessage().append("Searching for " + findText + " in " + folderPath + ":\n");//.toText().setFill(Color.BLACK);
				
				
				int totalMatches = 0;
				File[] logDirectories = logFilesDirectory.listFiles();
				for (File f:logDirectories) 
				{
					if (f.isDirectory() && f.getName().length() > 7 && f.getName().substring(0,8).equals(dateFormat.format(date))) 
					{
						File NAD_DebugLogFile = new File(f.getAbsolutePath()+"\\NAD_Debug.txt");
						if (NAD_DebugLogFile.exists() && NAD_DebugLogFile.isFile()) {							
							int matches = getMatchesFromFile(NAD_DebugLogFile, findText);
							totalMatches += matches;
							//output.append("Found " + matches + " occurences in " + f.getAbsolutePath()+ "\n");
							TextAreaMessage.getMessage().append("Found " + matches + " occurences in " + f.getAbsolutePath()+ "\n");
							TextAreaMessage.setTextColor("-fx-text-fill: black");
							//System.out.println("Found " + matches + " occurences in " + f.getAbsolutePath());
						}
					}
				}
				//System.out.println("Found " + totalMatches + " total matches in " + folderPath);
				
				
				if(totalMatches > 0)
				{
					//output.append("Found " + totalMatches + " total matches.\n");	
					TextAreaMessage.getMessage().append("Found " + totalMatches + " total matches.\n");
					TextAreaMessage.setTextColor("-fx-text-fill: black");
				}
				else
				{
					//output.append("No matches found.\n");
					TextAreaMessage.getMessage().append("No matches found.\n");
					TextAreaMessage.setTextColor("-fx-text-fill: black");
				}
				//output.append("\n");
				TextAreaMessage.getMessage().append("\n");
			}
		} 
		catch(SecurityException e) 
		{
			e.printStackTrace();
			
			//output.append(e.getStackTrace());
			TextAreaMessage.getMessage().append(e.getStackTrace());
			TextAreaMessage.setTextColor("-fx-text-fill: red");
		}
		//return output.toString();
		//return TextAreaMessage.getMessage().toString();
		return TextAreaMessage.getMessage();
	}
	
	private static int getMatchesFromFile(File fileName, String searchedPattern)
	{	
		BufferedReader br = null;
		int counter = 0;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String currentLine = null;
			while((currentLine = br.readLine())!=null) {
				if (currentLine.contains(searchedPattern)) {
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

