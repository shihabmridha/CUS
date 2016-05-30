package kpur.views;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import kpur.model.GlobalFunctions;

public class HomeActivityController  implements Initializable{

	@FXML
	private Button weeklyReport;

	@FXML
	private Button shareHolder;

	@FXML
	private MenuBar menu;

	@FXML
	private Menu file;

	@FXML
	private Menu help;

	@FXML
	private MenuItem close;

	@FXML
	private MenuItem about;

	@FXML
	private void close(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e->{
			System.out.println("Are you sure?");
		});
		Platform.exit();
	}

	@FXML
	private void about(ActionEvent event) throws Exception{
		GlobalFunctions ob = new GlobalFunctions();
		ob.about();
	}
	@FXML
	private void weeklyReport() throws Exception{
		Stage stage = (Stage) weeklyReport.getScene().getWindow();
		Scene scene = weeklyReport.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyReportHomeActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void shareHolder(ActionEvent event) throws Exception{
		Stage stage = (Stage) shareHolder.getScene().getWindow();
		Scene scene = shareHolder.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ShareHolderHomeActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}


}
