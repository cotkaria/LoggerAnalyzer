package com.loggeranalyzer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class SettingsDialogController {
	@FXML
	private TextField fileToUploadExtensionFieldTF;
	
	private Callback<String, Void> mOnApplySettings;
	
	@FXML
	private void onApplySettings(ActionEvent event)
	{
		if(mOnApplySettings != null)
		{
			mOnApplySettings.call(fileToUploadExtensionFieldTF.getText());
		}
	}
	public void setOnApplySettings(Callback<String, Void> onApplySettings)
	{
		mOnApplySettings = onApplySettings;
	}
	
	public String getFileToUploadExtension()
	{
		return fileToUploadExtensionFieldTF.getText();
	}
	
	public void setFileToUploadExtension(String fileToUploadExtension)
	{
		fileToUploadExtensionFieldTF.setText(fileToUploadExtension);
	}
}
