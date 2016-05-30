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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kpur.model.GlobalFunctions;
import kpur.model.DatabaseConnection;

public class ShareHolderCreateAccountController implements Initializable{
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
	private Button createBtn;
    private GlobalFunctions fn = new GlobalFunctions();

	/*********************
	 * SYSTEM
	 *********************/
	@FXML
	private void back(ActionEvent event)throws Exception{
        fn.changeScene(menu,"ShareHolderHomeActivity","Shareholder Home");
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
		fn.about();
	}

	public void createAccount(ActionEvent event) throws Exception{
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
			alert.setContentText("Input Nominee name.");
			alert.showAndWait();
		}
		else if(relation.getText().equals("")){
			alert.setContentText("Input Relation.");
			alert.showAndWait();
		}
		else{
			// Storing user data to DATABASE;
			storeUser();

			// Display Dialogue;
			alert.setAlertType(AlertType.INFORMATION);;
			alert.setContentText("Account Created Successfully!");
			alert.showAndWait();

			// Change scene;
			Stage stage = (Stage) createBtn.getScene().getWindow();
			Scene scene = createBtn.getScene();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShareHolderDataActivity.fxml"));
            scene.setRoot(loader.load());
            stage.setScene(scene);
            ShareHolderDataController ob = loader.<ShareHolderDataController>getController();
            ob.setTable(Integer.toString(getId()));
            stage.setTitle("Share Holder Account");
            stage.show();
		}
	}

	public void storeUser(){
		DatabaseConnection db = new DatabaseConnection();

		try {
			db.puts("INSERT INTO shareholder (name, guardian, mobile, village, ps, nomini, relation) VALUES ('"+ name.getText().toString() +"','"+ guardian.getText().toString()+"','"+ mobile.getText().toString()+"','"+ village.getText().toString()+"','"+ post.getText().toString()+"','"+ nomini.getText().toString()+"','"+ relation.getText().toString()+"');");
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
			String sql = "select user_id from shareholder order by user_id desc limit 1;";
			ResultSet rs = db.getQuery().executeQuery(sql);
			while(rs.next()){
				userid = Integer.parseInt(rs.getString("user_id"));
			}
			rs.close();
			db.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userid;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
