package kpur.views;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;

public class WeeklyCreateAccountCtrl implements Initializable{
	@FXML
	private MenuBar menu;

	@FXML
	TextField tfName;
	@FXML
	TextField tfHusband;
	@FXML
	TextField tfbfBalance;
	@FXML
	TextField tfInvestmentAmount;

	@FXML
	ComboBox<String> CbCenterList;

	/*********************
	 * VARIABLES
	 *********************/
	private ObservableList<String> centerObList = FXCollections.observableArrayList();


	/*********************
	 * METHODS
	 *********************/
	@FXML
	private void back()throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyReportHomeActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.setTitle("CUS");
		stage.show();
	}

	@FXML
	private void close() throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e->{
			System.out.println("Are you sure?");
		});
		Platform.exit();
	}


	@FXML
	private void createAccount() throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Please input carefully");
		alert.setHeaderText(null);

		if(tfName.getText().equals("")){
			alert.setContentText("Input your name.");
			alert.showAndWait();
		}
		else if(tfHusband.getText().isEmpty()){
			alert.setContentText("Input your Father's/Husband Name.");
			alert.showAndWait();
		}
		else if(tfInvestmentAmount.getText().isEmpty()){
			alert.setContentText("Input Investment Amount.");
			alert.showAndWait();
		}
		else if(tfbfBalance.getText().isEmpty()){
			alert.setContentText("Input B/F balance.");
			alert.showAndWait();
		}
		else if(CbCenterList.getSelectionModel().isEmpty()){
			alert.setContentText("Select center.");
			alert.showAndWait();
		}
		else{
			// Storing user data to DATABASE;
//			System.out.println(getCenterCode());
//			System.out.println((getUserSerialNo()+1));
//			System.out.println(tfName.getText());
//			System.out.println(tfHusband.getText());
//			System.out.println(tfbfBalance.getText());
//			System.out.println(tfInvestmentAmount.getText());

			DatabaseConnection db = new DatabaseConnection();
			db.puts("INSERT INTO weekly_user (center_id, serial_no, name, husband,bf_balance,investment_amount) VALUES ("
					+ "'"+ getCenterCode() +"',"
					+ "'"+ (getUserSerialNo()+1) +"',"
					+ "'"+ tfName.getText()+"',"
					+ "'"+ tfHusband.getText()+"',"
					+ "'"+ Double.parseDouble(tfbfBalance.getText())+"',"
					+ "'"+ Double.parseDouble(tfInvestmentAmount.getText())+"');");

			db.connect().close();

			// Display Dialogue;
			alert.setAlertType(AlertType.INFORMATION);;
			alert.setContentText("Center Created Successfully!");
			alert.showAndWait();
		}
	}

	public int getUserSerialNo() throws Exception{
		int serial = 0, center = getCenterCode();
		DatabaseConnection db = new DatabaseConnection();
		db.setQuery(db.connect().createStatement());
		String sql = "select serial_no from weekly_user where center_id='"+center+"' order by serial_no desc limit 1;";
		ResultSet rs = db.getQuery().executeQuery(sql);
		if(rs.next()){
			serial = Integer.parseInt(rs.getString("serial_no"));
		}
		rs.close();
		db.connect().close();

		return serial;
	}

	private int getCenterCode() throws Exception{
		int code = 0;
		DatabaseConnection db = new DatabaseConnection();
		db.setQuery(db.connect().createStatement());
		String sql = "select center_id from centers where center_name='"+CbCenterList.getValue()+"';";
		ResultSet rs = db.getQuery().executeQuery(sql);
		if(rs.next()){
			code = Integer.parseInt(rs.getString("center_id"));
		}
		rs.close();
		db.connect().close();

		return code;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			centerObList = GlobalFunctions.getCenters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		CbCenterList.getItems().addAll(centerObList);
	}






}
