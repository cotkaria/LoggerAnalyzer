package com.loggeranalyzer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.io.FileUtils;
import org.controlsfx.dialog.Dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

public class ConfigDialogController implements Initializable {
	@FXML
	private Pane root;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private Label searchHistoryLabel;
	
	@FXML
	private Label destinationPathLabel;
	
	@FXML
	private Label sourcePathLabel;	
	
	@FXML
	private Label dateLabel;	
    
	@FXML
    private Label findTextLabel;
	
	@FXML
	private TextField findTF;

	@FXML
	private TextField folderPathTF;

	@FXML
	private Button browseButton;
	
	@FXML
	private Button selectUploadDestinationButton;
	
	@FXML
	private DatePicker datePicker;

	@FXML
	private CheckBox saveConfig;
	
	@FXML
	private ChoiceBox<SearchResultData> searchHistoryList;
	
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

	private Callback<SearchParameters, SearchResultData> mOnSearch;
	private ObservableList<SearchResultData> mSearchResultDataList;
	
    public ConfigDialogController() 
	{
		mSearchResultDataList = FXCollections.observableArrayList();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchButton.setOnAction(event -> onSearch());
		clearButton.setOnAction(event -> onClear());
		browseButton.setOnAction(event -> onBrowse());
		datePicker.setValue(LocalDate.now());
		uploadButton.setOnAction(event -> onUpload());
		selectUploadDestinationButton.setOnAction(event -> onSelectUploadPath());
		configureSearchHistoryView();
		configureResultsView();
		onSearchHistoryUpdated(true);
		
	}
	private void configureSearchHistoryView()
	{	
		searchHistoryList.setItems(mSearchResultDataList);
		searchHistoryList.getSelectionModel().selectedIndexProperty().
			addListener((observableValue, oldIndex, newIndex) -> 
					{
						int newIndexValue = newIndex.intValue();
						if(newIndexValue >= 0)
						{
							resultsView.setItems(mSearchResultDataList.get(newIndexValue).getSearchResults());
						}
						else 
						{
							resultsView.setItems(null);
						}
					});		
	}
	private void configureResultsView()
	{
		resultsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		occurencesColumn.setCellValueFactory(new PropertyValueFactory("occurences"));
		pathColumn.setCellValueFactory(new PropertyValueFactory("path"));
		dateColumn.setCellValueFactory(new PropertyValueFactory("date"));
		timeColumn.setCellValueFactory(new PropertyValueFactory("time"));
		uploadStatColumn.setCellValueFactory(new PropertyValueFactory("uploaded"));
	}
	private void onSearchHistoryUpdated(boolean isEmpty)
	{
		searchHistoryList.setDisable(isEmpty);
		clearButton.setDisable(isEmpty);
		uploadButton.setDisable(isEmpty);
	}
	
	@FXML
	private void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ENTER)) {
			onSearch();
		}
	}

	public void setOnSearch(Callback<SearchParameters, SearchResultData> onSearch) {
		mOnSearch = onSearch;	
	}
	
	public boolean shouldSaveConfiguration()
	{
		return saveConfig.isSelected();
	}
	
	
	public DialogConfiguration getDialogConfiguration()
	{
		String folderPath = folderPathTF.getText();
		String findText = findTF.getText();
		LocalDate date = datePicker.getValue();
		SearchParameters searchParameters = new SearchParameters(folderPath, findText, date);
		return new DialogConfiguration(searchParameters, uploadPathTF.getText(), 
				saveConfig.isSelected());
		
	}
	
	private void onSearch() {
		if (mOnSearch != null) 
		{
			String folderPath = folderPathTF.getText();
			String findText = findTF.getText();
			if(folderPath == null || folderPath.isEmpty() || !(new File(folderPath).exists())) 
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
				SearchParameters searchParameters = new SearchParameters(folderPath, findText, date);
				
				mSearchResultDataList.add(mOnSearch.call(searchParameters));
				searchHistoryList.getSelectionModel().selectLast();
				onSearchHistoryUpdated(false);
			}	
		}
	}
	private void onClear()
	{
		mSearchResultDataList.clear();
		onSearchHistoryUpdated(true);
	}
	
	private void onBrowse() 
	{
		folderPathTF.setText(selectDirectory(folderPathTF.getText()));
	}
	
	
	private void onSelectUploadPath()
	{
		uploadPathTF.setText(selectDirectory(uploadPathTF.getText()));
	}
	
	private String selectDirectory(String initialPath)
	{
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choose directory");
		
		if (initialPath.isEmpty() == false)
		{
			File currentFolder = new File(initialPath);
			if (currentFolder.exists())
			{ 
				directoryChooser.setInitialDirectory(currentFolder);
			}
		}
		
		File selectedDirectory = directoryChooser.showDialog(MainWindow
				.getStage());
		if (selectedDirectory != null) 
		{
			return selectedDirectory.getAbsolutePath();
		}
		return initialPath;
	}

	public void setInitialParameters(DialogConfiguration dialogConfiguration) {
		if (dialogConfiguration != null) {
			SearchParameters searchParameters = dialogConfiguration.getSearchParameters();
			folderPathTF.setText(searchParameters.getFolderPath());
			findTF.setText(searchParameters.getFindText());
			saveConfig.setSelected(dialogConfiguration.shouldSaveConfiguration());
			datePicker.setValue(searchParameters.getDate());
			uploadPathTF.setText(dialogConfiguration.getUploadPath());
			
		}
	}
	
	private void onUpload()
	{	
		String uploadPath = uploadPathTF.getText();
		if (uploadPath.isEmpty() == false)
		{
			File uploadDirectory = new File(uploadPath);
			if (uploadDirectory.exists() &&  uploadDirectory.isDirectory())
			{
				uploadSelectedFiles(uploadDirectory);
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
	
	private void uploadSelectedFiles(File uploadDirectory)
	{
		ObservableList<SearchResult> selectedSearchResults = resultsView.getSelectionModel().getSelectedItems();
		
		if(selectedSearchResults.isEmpty())
		{
			showErrorPopUp("Select an entry from the result view to upload.");
			return;
		}
		
		for(SearchResult searchResult: selectedSearchResults)
		{
			if (searchResult != null)
			{	
								
				String path = searchResult.pathProperty().getValue();
				String extension = ".zip";
				File file = new File(path + extension);
				if (file.exists())
				{
					try 
					{
						FileUtils.copyFileToDirectory(file,uploadDirectory);
						searchResult.uploadedProperty().set(true);
					} 
					catch (IOException e) 
					{	
						showErrorPopUp("Could not copy '" + file.getAbsolutePath() + "' to '" + uploadDirectory.getAbsolutePath() + "'." +
									   "\n" + e.getMessage());						
					}
				}
				else
				{
					showErrorPopUp("File '" + file.getAbsolutePath() +"' does not exist.");
				}
			}
		}
	}
	
	private void showErrorPopUp(String message)
	{
		Dialogs.create().owner(MainWindow.getStage()).title("Error").message(message).showError();
	}
	
	
}
