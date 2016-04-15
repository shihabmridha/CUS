package kpur.views;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;

public class ShareHolderHomeController implements Initializable{
	/*********************
	 * MENU ITEMS
	 *********************/
	@FXML
	private MenuBar menu;
	@FXML
	private Menu file;
	@FXML
	private MenuItem back;
	@FXML
	private MenuItem create;
	@FXML
	private MenuItem close;
	@FXML
	private Menu help;
	@FXML
	private MenuItem about;

	/*********************
	 * BUTTONS
	 *********************/
	@FXML
	private Button goBtn;

	/*********************
	 * TEXT FIELDS
	 *********************/
	@FXML
	private TextField shareHolderNumber;

	/*********************
	 * VARIABLES
	 *********************/
	private String shareHolderId;

	/*********************
	 * COMMON METHODS
	 *********************/
	public void setId(String id){
		shareHolderId = id;
	}
	public String getId(){
		return shareHolderId;
	}

	/*********************
	 * SYSTEM
	 *********************/
	@FXML
	private void back(ActionEvent event)throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}
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

	public void showData(ActionEvent event){

		DatabaseConnection db = new DatabaseConnection();
		db.connect();

		try {
			db.setQuery(db.connect().createStatement());
			String sql = "SELECT user_id FROM shareholder WHERE user_id='"+shareHolderNumber.getText()+"';";
			ResultSet rs = db.getQuery().executeQuery(sql);
			if(rs.next()){
				Stage stage = (Stage) goBtn.getScene().getWindow();
				Scene scene = goBtn.getScene();
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("ShareHolderDataActivity.fxml"));
					scene.setRoot(loader.load());
					stage.setScene(scene);
					ShareHolderDataController ob = loader.<ShareHolderDataController>getController();
					ob.setTable(shareHolderNumber.getText());
					stage.setTitle("Share Holder Account");
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				GlobalFunctions.userNotFount();
			}
			rs.close();
			db.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}



	}

	public void createAccount(ActionEvent event){
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ShareHolderCreateAccountActivity.fxml"));
			scene.setRoot(loader.load());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
