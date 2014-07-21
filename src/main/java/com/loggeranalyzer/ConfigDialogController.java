package com.loggeranalyzer;

import java.io.File;
import com.loggeranalyzer.TextAreaMessage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
//import org.fxmisc.richtext.StyleClassedTextArea;
import org.controlsfx.dialog.Dialogs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

public class ConfigDialogController implements Initializable {
	@FXML
	private Button searchButton;

	@FXML
	private TextField findTF;

	@FXML
	private TextField folderPathTF;

	@FXML
	private Button browseButton;

	@FXML
	private DatePicker datePicker;

	@FXML
	private CheckBox saveConfig;

	@FXML
	private TextArea resultsArea;
	//private TextArea resultsArea;

	@FXML
	private Button clearButton;

	private Callback<SearchParameters, StringBuffer> mOnSearch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchButton.setOnAction(event -> onSearch());
		// datePicker.setOnAction(event -> onSearch());
		clearButton.setOnAction(event -> resultsArea.clear());
		browseButton.setOnAction(event -> onBrowse());
		datePicker.setValue(LocalDate.now());
		
	}

	@FXML
	private void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ENTER)) {
			onSearch();
		}
	}

	public void setOnSearch(Callback<SearchParameters, StringBuffer> onSearch) {
		mOnSearch = onSearch;	
	}
	
	private void onSearch() {
		if (mOnSearch != null) 
		{
			String folderPath = folderPathTF.getText();
			String findText = findTF.getText();
			if(folderPath == null || folderPath.isEmpty()) 
			{
				//resultsArea.appendText("Please provide a valid path!\n");		
				showErrorPopUp("Please provide a valid path!");
			} 
			else if (findText == null || findText.isEmpty()) 
			{
				//resultsArea.appendText("Please provide text to search!\n");
				showErrorPopUp("Please provide text to search!");
			}
			else
			{
				LocalDate date = datePicker.getValue();
				SearchParameters searchParameters = new SearchParameters(
						folderPath, findText, date, saveConfig.isSelected());

				//String output = mOnSearch.call(searchParameters);
				TextAreaMessage.setMessage(mOnSearch.call(searchParameters));
				//resultsArea.clear();
				resultsArea.setStyle(TextAreaMessage.getTextColor());
				//resultsArea.appendText(output).;
				resultsArea.appendText(TextAreaMessage.getMessage().toString());
				//TextAreaMessage.setMessage(new StringBuffer()); || below method
				TextAreaMessage.getMessage().delete(0, TextAreaMessage.getMessage().length());
			}	
		}
	}
	private void onBrowse() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Open file");
		
		String currentFolderPath = folderPathTF.getText();
		if (currentFolderPath.isEmpty() == false)
		{
			File currentFolder = new File(currentFolderPath);
			if (currentFolder.exists())
			{ 
				
				directoryChooser.setInitialDirectory(currentFolder);
			}
		}
		
		File selectedDirectory = directoryChooser.showDialog(MainWindow
				.getStage());
		if (selectedDirectory != null) {
			folderPathTF.setText(selectedDirectory.getAbsolutePath());
		}
	}

	public void setInitialParameters(SearchParameters searchParameters) {
		if (searchParameters != null) {
			folderPathTF.setText(searchParameters.getFolderPath());
			findTF.setText(searchParameters.getFindText());
			saveConfig.setSelected(searchParameters.getSaveConfig());
			datePicker.setValue(searchParameters.getDate());
		}
	}
	private void showErrorPopUp(String message)
	{
		Dialogs.create().owner(MainWindow.getStage()).title("Error").message(message).showError();
	}
}
