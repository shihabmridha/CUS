package kpur.views;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;

public class WeeklyReportHomeController implements Initializable{
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

	@FXML
	private TextField centerCode;

	/*********************
	 * BUTTONS
	 *********************/
	@FXML
	private Button goBtn;

	@FXML
	private ComboBox<String> CbCenterList;
	/*********************
	 * Variables
	 *********************/
	int cntrCode;
	private ObservableList<String> centerList = FXCollections.observableArrayList();
	private GlobalFunctions fn = new GlobalFunctions();
	/*********************
	 * SYSTEM
	 *********************/
	@FXML
	private void onBackspace(KeyEvent e) throws Exception{
		if(e.getCode().equals(KeyCode.BACK_SPACE)){
			back();
		}
	}

	@FXML
	void back()throws Exception{
		fn.changeScene(menu,"HomeActivity","CHASHI UNNOYON SOMITI");
	}

	@FXML
	private void close(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e-> System.out.println("Are you sure?"));
		Platform.exit();
	}

	@FXML
	private void showData() throws Exception{

		if(!centerCode.getText().equals("")){
			cntrCode = Integer.parseInt(centerCode.getText());
		}

		DatabaseConnection db = new DatabaseConnection();
		db.connect();
		db.setQuery(db.connect().createStatement());
		String sql = "SELECT center_id FROM centers WHERE center_id='"+cntrCode+"';";
		ResultSet rs = db.getQuery().executeQuery(sql);
		if(rs.next()){
			Stage stage = (Stage) goBtn.getScene().getWindow();
			Scene scene = goBtn.getScene();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyReportCenterHome.fxml"));
			scene.setRoot(loader.load());
			stage.setScene(scene);
			WeeklyReportCenterHomeController ob = loader.<WeeklyReportCenterHomeController>getController();
			ob.setData(cntrCode);
			stage.setTitle("Weekly Report Data");
			stage.show();

		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText(null);
			alert.setContentText("Center not found. Please check again.");
			alert.showAndWait();
		}
		rs.close();
		db.connect().close();
	}

	@FXML
	private void createCenter() throws Exception{
		fn.changeScene(menu,"WeeklyCreateCenterActivity","Create center");
	}

	@FXML
	private void createAccount() throws Exception{
		fn.changeScene(menu,"WeeklyCreateAccount","Create Account");
	}

	@FXML
	private void getCenterCode() throws Exception{
		DatabaseConnection ob = new DatabaseConnection();

		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("select center_id from centers where center_name = '"+CbCenterList.getValue()+"';");
		if(rs.next()){
			cntrCode = rs.getInt("center_id");
		}
		rs.close();
		ob.connect().close();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			centerList = GlobalFunctions.getCenters();
		} catch (Exception e) {
			e.printStackTrace();
		}

		CbCenterList.getItems().addAll(centerList);

	}
}
