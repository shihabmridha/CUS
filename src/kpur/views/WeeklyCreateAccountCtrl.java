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
import javafx.scene.control.*;
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

	/*********************
	 * ComboBox
	 *********************/
	@FXML
	ComboBox<String> CbCenterList;

	/*********************
	 * Button
	 *********************/
	@FXML
	private Button btnCreate;



	/*********************
	 * VARIABLES
	 *********************/
	private ObservableList<String> centerObList = FXCollections.observableArrayList();
	private GlobalFunctions fn = new GlobalFunctions();
	private int theCenter;

	/*********************
	 * METHODS
	 *********************/
	@FXML
	private void back()throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		GlobalFunctions ob = new GlobalFunctions();
		ob.goBackToCenterList(stage,scene,theCenter);
	}
	@FXML
	private void close() throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e->{
			System.out.println("Are you sure?");
		});
		Platform.exit();
	}

	public void setCenter(int center){
		theCenter = center;
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

			DatabaseConnection db = new DatabaseConnection();
			db.puts("INSERT INTO weekly_user (center_id, serial_no, name, husband,bf_balance,investment_amount) VALUES ("
					+ "'"+ GlobalFunctions.getCenterCode(CbCenterList.getValue()) +"',"
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

			Stage stage = (Stage) btnCreate.getScene().getWindow();
			Scene scene = btnCreate.getScene();
			GlobalFunctions ob = new GlobalFunctions();
			ob.goToWeeklyAccount(stage,scene,GlobalFunctions.getCenterCode(CbCenterList.getValue()),tfName.getText());

		}
	}

	public int getUserSerialNo() throws Exception{
		int serial = 0, center = GlobalFunctions.getCenterCode(CbCenterList.getValue());
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
