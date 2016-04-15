package kpur.views;

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
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;

public class WeeklyCreateCenterController implements Initializable{
	/*********************
	 * MENU ITEMS
	 *********************/
	@FXML
	private MenuBar menu;



	@FXML
	private TextField centerName;
	@FXML
	private TextField fs;

	@FXML
	private Button createBtn;

	/*********************
	 * SYSTEM
	 *********************/
	@FXML
	private void back(ActionEvent event)throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyReportHomeActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}
	@FXML
	private void close(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e->{
			// Alert;
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
	private void createCenter(ActionEvent event){
		// Check Form Validation;
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Please input carefully");
				alert.setHeaderText(null);

				if(centerName.getText().equals("")){
					alert.setContentText("Input your name.");
					alert.showAndWait();
				}
				else if(fs.getText().equals("")){
					alert.setContentText("Input your Father's/Husband Name.");
					alert.showAndWait();
				}else{
					// Storing user data to DATABASE;
					storeCenter();
					int idMsg = getId();
					// Display Dialogue;
					alert.setAlertType(AlertType.INFORMATION);;
					alert.setContentText("Center Created Successfully!\nCenter ID is: " + idMsg);
					alert.showAndWait();
				}
	}

	private void storeCenter(){
		DatabaseConnection db = new DatabaseConnection();

		try {
			db.puts("INSERT INTO centers (center_name, fs) VALUES ('"+ centerName.getText().toString() +"','"+ fs.getText().toString()+"');");
			db.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getId(){
		int userid = 0;
		DatabaseConnection db = new DatabaseConnection();
		db.connect();
		try {
			db.setQuery(db.connect().createStatement());
			String sql = "select center_id from centers order by center_id desc limit 1;";
			ResultSet rs = db.getQuery().executeQuery(sql);
			while(rs.next()){
				userid = Integer.parseInt(rs.getString("center_id"));
			}
			rs.close();
			db.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userid;
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
