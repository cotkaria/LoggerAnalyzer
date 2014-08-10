package com.loggeranalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application
{
//	class ApplySettingsCallback implements Callback<String, Void>
//	{
//		private MainWindow mMainWindow;
//		public ApplySettingsCallback(MainWindow mainWindow)
//		{
//			mMainWindow = mainWindow;
//		}
//		
//		public Void call(String fileExtension)
//		{
//			mMainWindow.applySettings(fileExtension);
//			return null;
//		}
//	}
//	
	//private static final String RESOURCES_PATH = "../../layouts/";
	private static final String RESOURCES_PATH = "";
	private static final String CONFIG_DIALOG_PATH = RESOURCES_PATH + "ConfigDialog.fxml";
	private static final String SETTINGS_DIALOG_PATH = RESOURCES_PATH + "SettingsDialog.fxml";
	private static final String ABOUT_DIALOG_PATH = RESOURCES_PATH + "AboutDialog.fxml";
	private static final String SAVED_SEARCH_PARAMS_PATH = "save/SavedSearchParamsPath.cfg";
	private static Stage mStage;
	private ConfigDialogController mConfigDialogController;
	
	public static void main(String[] args)
	{
		System.out.println("main()");
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception 
	{
		
		System.out.println("start()");

		mStage = stage;
		mStage.setTitle("Logs analyzer");
		mStage.setResizable(false);
		
		showConfigDialog();
	}
	@Override
	public void stop()
	{	
		if((mConfigDialogController!= null) && mConfigDialogController.shouldSaveConfiguration())
		{
			saveConfigParameters(mConfigDialogController.getDialogConfiguration());
			
		}
		
	}
	
	private void showConfigDialog()
	{
		mConfigDialogController = (ConfigDialogController)loadScene(CONFIG_DIALOG_PATH, mStage);
		
		if(mConfigDialogController != null)
		{
			mConfigDialogController.setInitialParameters(loadSavedConfigParameters());
			mConfigDialogController.setWindowOwner(mStage);
			mConfigDialogController.setOnSearch(searchParameters -> 
			{	
				return LogAnalyzer.search(searchParameters);
			});
			mConfigDialogController.setOnShowSettings(noParam ->
			{
				showSettingsDialog();
				return null;
			});
			mConfigDialogController.setOnAbout(noParam -> 
			{
				showAboutDialog();
				return null;
			});
		}
		else
		{
			System.out.println("Could not initialize the Configuration Dialog.");
		}
	}
	
	private void showSettingsDialog()
	{
		final Stage settingsStage = new Stage();
		settingsStage.setTitle("Settings");
		settingsStage.setResizable(false);
		SettingsDialogController settingsController = (SettingsDialogController)loadScene(SETTINGS_DIALOG_PATH, settingsStage);
		if(settingsController != null)
		{
			settingsController.setFileToUploadExtension(mConfigDialogController.getFileToUploadExtension());
			settingsController.setOnApplySettings(fileToUploadExtension -> 
			{
				mConfigDialogController.setFileToUploadExtension(fileToUploadExtension);
				return null;
			});
		}
	}
	
	private void showAboutDialog()
	{
		final Stage aboutStage = new Stage();
		aboutStage.setTitle("About");
		aboutStage.setResizable(false);
		loadScene(ABOUT_DIALOG_PATH, aboutStage);
		
		
	}
//	private void applySettings(String fileToUploadExtension)
//	{
//		mDialogController.setFileToUploadExtension(fileToUploadExtension);
//	}
	
	private Object loadScene(String scenePath, Stage stage)
	{
		Parent root=null;
		FXMLLoader loader;
		try 
		{
			 loader = new FXMLLoader();
			 loader.setBuilderFactory(new JavaFXBuilderFactory());
			 loader.setLocation(getClass().getResource(scenePath));
			 InputStream inputStream = getClass().getResourceAsStream(scenePath);
			 
			 if(inputStream != null)
			 {
				 root = (Parent)loader.load(inputStream);
				 
				 Scene scene = new Scene(root);
				 stage.setScene(scene); 
				 stage.show();
				 
			 	return loader.getController();
			 }
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private DialogConfiguration loadSavedConfigParameters()  
	{
		DialogConfiguration dialogConfiguration = null;
		try 
		{
			File savedFile = new File(getSaveFilePath());
			if (savedFile.exists())
			{
				FileInputStream inputStream = new FileInputStream(savedFile);
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				dialogConfiguration = (DialogConfiguration)objectInputStream.readObject();
				
				objectInputStream.close();
			}
			else
			{
				dialogConfiguration = new DialogConfiguration();
			}
		}
		catch (Exception e) 
		{
		}
		return dialogConfiguration;
		
	}
	private String getSaveFilePath()
	{
		String saveFilePath = null;
		try 
		{
			URI codePath = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
			saveFilePath = codePath.resolve(SAVED_SEARCH_PARAMS_PATH).getPath();
		} 
		catch (URISyntaxException e) 
		{
			// TODO: handle exception
		}
		
		return saveFilePath;
	}
	private void saveConfigParameters(DialogConfiguration dialogConfiguration)
	{
		if (dialogConfiguration.shouldSaveConfiguration())
		{
			try 
			{
				FileOutputStream fileOutputStream = new FileOutputStream(getSaveFile());
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(dialogConfiguration);
				objectOutputStream.close();
			}
			catch (IOException e) 
			{
				// TODO: handle exception
			}
		}
	}
	private File getSaveFile()
	{
		File saveFile = new File(getSaveFilePath());
		if (saveFile.exists() == false)
		{
			if (saveFile.getParentFile().exists() == false)
			{
				saveFile.getParentFile().mkdirs();
			}

			try 
			{
				saveFile.createNewFile();
					
			}
			catch (IOException e) 
			{
				// TODO: handle exception
			}
		}
		return saveFile;
	}
}
