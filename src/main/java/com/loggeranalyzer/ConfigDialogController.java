package com.loggeranalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.controlsfx.dialog.Dialogs;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private TableView<SearchResult> resultsView;

    @FXML
    private TableColumn<SearchResult, String> occurencesColumn;

    @FXML
    private TableColumn<SearchResult, String> timeColumn;

    @FXML
    private TableColumn<SearchResult, String> pathColumn;
    
    @FXML
    private TableColumn<SearchResult, String> dateColumn;
    
    @FXML
    private TableColumn<SearchResult, Boolean> uploadStatColumn;
    
	@FXML
	private Button clearButton;
	
	@FXML
	private Button uploadButton;
	
	@FXML
	private TextField uploadPathTF;

	private Callback<SearchParameters, SearchData> mOnSearch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchButton.setOnAction(event -> onSearch());
		clearButton.setOnAction(event -> {/*TODO clear()*/});
		browseButton.setOnAction(event -> onBrowse());
		datePicker.setValue(LocalDate.now());
		uploadButton.setOnAction(event -> onUpload());
		
		configureResultsView();
	}

	@FXML
	private void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ENTER)) {
			onSearch();
		}
	}

	public void setOnSearch(Callback<SearchParameters, SearchData> onSearch) {
		mOnSearch = onSearch;	
	}
	
	private void onSearch() {
		if (mOnSearch != null) 
		{
			String folderPath = folderPathTF.getText();
			String findText = findTF.getText();
			if(folderPath == null || folderPath.isEmpty()) 
			{
				
				showErrorPopUp("Please provide a valid path!");
			} 
			else if (findText == null || findText.isEmpty()) 
			{
				
				showErrorPopUp("Please provide text to search!");
			}
			else
			{
				LocalDate date = datePicker.getValue();
				SearchParameters searchParameters = new SearchParameters(
						folderPath, findText, date, saveConfig.isSelected());

				SearchData searchData = mOnSearch.call(searchParameters);
				resultsView.setItems(FXCollections.observableArrayList(searchData.getSearchResults()));
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
	
	private void onUpload()
	{
		System.out.println("onUpload()");
		String uploadPath = uploadPathTF.getText();
		if (uploadPath.isEmpty() == false)
		{
			File uploadDirectory = new File(uploadPath);
			if (uploadDirectory.exists() &&  uploadDirectory.isDirectory())
			{
				uploadSelectedFile(uploadDirectory);
			}
			else
			{
				showErrorPopUp("Upload path '" + uploadPath + "' doesn't point to a directory");
				
			}
		}
		else
		{
			showErrorPopUp("Upload path is empty. \nPlease insert a valid upload destination!");
		} 
	}
	
	private void uploadSelectedFile(File uploadDirectory)
	{
		SearchResult selectedSearchResult= resultsView.getSelectionModel().getSelectedItem();
		if (selectedSearchResult != null)
		{	
			String path = selectedSearchResult.pathProperty().getValue();
			String extension = ".rar";
			File file = new File(path + extension);
			if (file.exists())
			{
				try 
				{
					FileUtils.copyFileToDirectory(file,uploadDirectory);
				} 
				catch (IOException e) 
				{
					showErrorPopUp("Could not copy '" + file.getAbsolutePath() + "' to '" + uploadDirectory.getAbsolutePath() + "'");
				}
			}
		}
	}
	
	private void showErrorPopUp(String message)
	{
		Dialogs.create().owner(MainWindow.getStage()).title("Error").message(message).showError();
	}
	private void configureResultsView()
	{
		occurencesColumn.setCellValueFactory(new PropertyValueFactory("occurences"));
		pathColumn.setCellValueFactory(new PropertyValueFactory("path"));
		dateColumn.setCellValueFactory(new PropertyValueFactory("date"));
		timeColumn.setCellValueFactory(new PropertyValueFactory("time"));
		uploadStatColumn.setCellValueFactory(new PropertyValueFactory("uploaded"));
	}
}
