package kpur.views;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;

public class ShareHolderEditInfoController {
	/*********************
	 * MENU ITEMS
	 *********************/
	@FXML
	private MenuBar menu;
	@FXML
	private MenuItem back;
	@FXML
	private MenuItem close;
	@FXML
	private MenuItem about;

	/*********************
	 * TEXT FIELDS
	 *********************/
	@FXML
	private TextField name;
	@FXML
	private TextField mobile;
	@FXML
	private TextField guardian;
	@FXML
	private TextField village;
	@FXML
	private TextField post;
	@FXML
	private TextField nomini;
	@FXML
	private TextField relation;

	/*********************
	 * OTHER ITEMS
	 *********************/
	@FXML
	private Button updateBtn;

	/*********************
	 * OTHER ITEMS
	 *********************/
	private String ID;

	/*********************
	 * SYSTEM
	 *********************/

	@FXML
	private void back(ActionEvent event)throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ShareHolderDataActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		ShareHolderDataController ob = loader.<ShareHolderDataController>getController();
		ob.setTable(ID);
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

	public void setDefaults(String id){
		this.ID = id;
		System.out.println("BOom");
		DatabaseConnection db = new DatabaseConnection();
		try {
			db.connect();
			db.setQuery(db.connect().createStatement());
			String sql = "select * from shareholder where user_id='"+ID+"';";
			ResultSet rs = db.getQuery().executeQuery(sql);
			if(rs.next()){
				name.setText(rs.getString("name"));
				guardian.setText(rs.getString("guardian"));
				mobile.setText(rs.getString("mobile"));
				village.setText(rs.getString("village"));
				post.setText(rs.getString("ps"));
				nomini.setText(rs.getString("nomini"));
				relation.setText(rs.getString("relation"));
			}
			rs.close();
			db.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateAccount(ActionEvent event) throws Exception{
		// Check Form Validation;
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Please input carefully");
				alert.setHeaderText(null);

				if(name.getText().equals("")){
					alert.setContentText("Input your name.");
					alert.showAndWait();
				}
				else if(guardian.getText().equals("")){
					alert.setContentText("Input your Father's/Husband Name.");
					alert.showAndWait();
				}
				else if(village.getText().equals("")){
					alert.setContentText("Input your village.");
					alert.showAndWait();
				}
				else if(post.getText().equals("")){
					alert.setContentText("Input your PS.");
					alert.showAndWait();
				}
				else if(mobile.getText().equals("")){
					alert.setContentText("Input your mobile number.");
					alert.showAndWait();
				}
				else if(nomini.getText().equals("")){
					alert.setContentText("Input Nomini name.");
					alert.showAndWait();
				}
				else if(relation.getText().equals("")){
					alert.setContentText("Input Relation.");
					alert.showAndWait();
				}
				else{
					// Storing user data to DATABASE;
					updateUser();
					// Display Dialogue;
					alert.setAlertType(AlertType.INFORMATION);;
					alert.setContentText("Account Updated Successfully!");
					alert.showAndWait();

				}
	}

	public void updateUser(){
		DatabaseConnection db = new DatabaseConnection();

		try {
			db.puts("UPDATE shareholder SET name = '"+ name.getText().toString() +"', guardian = '"+ guardian.getText().toString()+"', mobile = '"+ mobile.getText().toString()+"', village = '"+ village.getText().toString()+"', ps = '"+ post.getText().toString()+"', nomini = '"+ nomini.getText().toString()+"', relation = '"+ relation.getText().toString()+"' WHERE user_id = '"+ID+"'");
			db.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
