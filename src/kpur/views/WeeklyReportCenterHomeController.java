package kpur.views;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;

public class WeeklyReportCenterHomeController implements Initializable{
	/*********************
	 * MENU ITEMS
	 *********************/
	@FXML
	MenuBar menu;
	@FXML
	private MenuItem create;

	/*********************
	 * COMBOBOX ITEMS
	 *********************/
	@FXML
	ComboBox<String> cbName;

	/*********************
	 * BUTTON ITEMS
	 *********************/
	@FXML
	Button viewBtn;

	/*********************
	 * VARIABLES
	 *********************/
	private int centerCode;
	private ObservableList<String> userList = FXCollections.observableArrayList();


	/*********************
	 * METHODS
	 *********************/
	@FXML
	private void back(ActionEvent event)throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.setTitle("CHASHI UNNOYON SOMITI");
		stage.show();
	}

	@FXML
	private void close(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e-> System.out.println("Are you sure?"));
		Platform.exit();
	}

	@FXML
	private void createAccount(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyCreateAccount.fxml"));
			scene.setRoot(loader.load());
			stage.setScene(scene);
			stage.setTitle("Create Account");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@FXML
	public void gotoAccount() throws Exception{
		DatabaseConnection db = new DatabaseConnection();
		db.setQuery(db.connect().createStatement());
		String sql = String.format("SELECT user_id FROM weekly_user WHERE center_id='%d' and name='%s';", centerCode, cbName.getValue());
		ResultSet rs = db.getQuery().executeQuery(sql);
		if(rs.next()){
			Stage stage = (Stage) viewBtn.getScene().getWindow();
			Scene scene = viewBtn.getScene();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyReportDataActivity.fxml"));
			scene.setRoot(loader.load());
			stage.setScene(scene);
			WeeklyReportDataCtrl ob = loader.getController();
			ob.setTableAndInfo(rs.getInt("user_id"),centerCode);
			stage.setTitle("Share Holder Account");
			stage.show();
		}else{
			GlobalFunctions.userNotFount();
		}
		rs.close();
		db.connect().close();
	}

	// Set names of center;
	public void setData(int centerCode) throws Exception{
		this.centerCode = centerCode;
		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("select name from weekly_user where center_id='"+centerCode+"'");
		while(rs.next()){
			userList.add(rs.getString("name"));
		}
		rs.close();
		ob.connect().close();

		cbName.getItems().addAll(userList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {}

}
