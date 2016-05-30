package kpur.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

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
	@FXML
	ComboBox<String> selectMonth;
	@FXML
	ComboBox<Integer> selectYear;

	/*********************
	 * BUTTON ITEMS
	 *********************/
	@FXML
	Button viewBtn;
	@FXML
	Button btnExport;

	/*********************
	 * TEXT ITEMS
	 *********************/
	@FXML
	Text txtCenterCode;
	@FXML
	Text txtDirError;

	/*********************
	 * PROGRESS INDICATOR
	 *********************/
	@FXML
	ProgressIndicator progress;

	/*********************
	 * VARIABLES
	 *********************/
	private int centerCode;
	private ObservableList<String> userList = FXCollections.observableArrayList();
	private ObservableList<String> monthList = FXCollections.observableArrayList();
	private ObservableList<Integer> yearList = FXCollections.observableArrayList();
	private String month, year, centerName, theDirectory, fsName;

	private GlobalFunctions fn = new GlobalFunctions();
	private XWPFDocument doc = new XWPFDocument(OPCPackage.open("tables.docx"));

	public WeeklyReportCenterHomeController() throws IOException, InvalidFormatException {
	}

	/*********************
	 * METHODS
	 *********************/
	@FXML
	private void back(ActionEvent event)throws Exception{
		fn.changeScene(menu,"WeeklyReportHomeActivity","Weekly Report");
	}

	@FXML
	private void close() throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e-> System.out.println("Are you sure?"));
		Platform.exit();
	}

	@FXML
	private void createAccount() throws Exception{
		fn.changeScene(menu,"WeeklyCreateAccount","Create weekly user account");
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
			GlobalFunctions.userNotFound();
		}
		rs.close();
		db.connect().close();
	}
	// Set names of center;
	public void setData(int centerCode) throws Exception{
		this.centerCode = centerCode;
		txtCenterCode.setText(Integer.toString(centerCode));
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

	// Saving data list
	private List<Integer> serialNo = new LinkedList<>();
	private List<String> memberName = new LinkedList<>();
	private List<String> husbandName = new LinkedList<>();
	private List<Integer> bfBalance = new LinkedList<>();
	private List<Integer> weeklyDeposit = new LinkedList<>();
	private List<Integer> rebetList = new LinkedList<>();
	private List<String> rebetDate = new LinkedList<>();
	private List<Integer> monthlyCollection = new LinkedList<>();
	private List<Integer> savingReturn = new LinkedList<>();
	private List<String> savingReturnDate = new LinkedList<>();
	private List<Integer> totalBalance = new LinkedList<>();

	// Invest data lish
	private List<Double> investAmount = new LinkedList<>();

	@FXML
	public void exportData() throws Exception{
		Stage stage = (Stage) btnExport.getScene().getWindow();

		if(!selectYear.getSelectionModel().isEmpty() && !selectMonth.getSelectionModel().isEmpty()){
			if(selectMonth.getSelectionModel().getSelectedIndex()+1 >= 10) {
				month = Integer.toString(selectMonth.getSelectionModel().getSelectedIndex() + 1);
			}else {
				month = "0" + Integer.toString(selectMonth.getSelectionModel().getSelectedIndex() + 1);
			}
			year = Integer.toString(selectYear.getValue());

			progress.setProgress(0.1); // Progress indicator

			// Directory chooser
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("JavaFX Projects");
			File selectedDirectory = chooser.showDialog(stage);
			if(selectedDirectory == null){
				txtDirError.setText("No Directory selected!");
			}else{
				txtDirError.setText(selectedDirectory.getAbsolutePath());
				theDirectory = txtDirError.getText();
				progress.setProgress(0.2); // Progress indicator

				// Database connection
				DatabaseConnection db = new DatabaseConnection();
				db.setQuery(db.connect().createStatement());
				ResultSet rs;

				// Getting center name form database
				rs = db.getQuery().executeQuery("SELECT center_name,fs FROM centers WHERE center_id = '"+centerCode+"';");
				if(rs.next()){
					centerName = rs.getString("center_name");
					fsName = rs.getString("fs");
				}

				txtDirError.setText("Setting center name and F/S."); // Custom message
				progress.setProgress(0.3); // Progress indicator


				// Fetching saving data
				fetchingSavingData(db);

				// Fetching invest data
				fetchingInvestData();
				db.connect().close();

				exportDataToDocx();

				txtDirError.setText("File exported successfully."); // Custom message
				progress.setProgress(1); // Progress indicator
			}
		}else{
			txtDirError.setText("Something went wrong!");
		}


	}


	/********************************
	* Storing saving data to List
	********************************/
	private void fetchingSavingData(DatabaseConnection db) throws Exception{
		// Getting savings data from database
		double bfBal = 0, weeklyDep = 0, rebet = 0 , monthlyCol = 0,savingRet = 0,totalBal = 0;
		String date1 = " ", date2 = " ";
		String sql = "SELECT * FROM weekly_saving WHERE date LIKE '%"+month+"/"+year+"' AND user_id IN (SELECT user_id FROM weekly_user WHERE center_id = '"+centerCode+"') ORDER BY user_id;";
		ResultSet rs = db.getQuery().executeQuery(sql);
		int theID = 0, counter = 1;
		txtDirError.setText("Fetching savings data."); // Custom message
		while (rs.next()){
			if(counter == 1){
				theID = rs.getInt("user_id");
				weeklyDeposit.add(((int) rs.getDouble("weekly_deposit")));
				if(rebet < rs.getDouble("rebet") ) {
					rebet = rs.getDouble("rebet");
					date1 = rs.getString("date").substring(0,2);
				}
				if(savingRet < rs.getDouble("saving_return")){
					savingRet = rs.getDouble("saving_return");
					date2 = rs.getString("date").substring(0,2);
				}
				counter++;
			}else{
				if(rs.getInt("user_id") == theID){
					bfBal = rs.getDouble("bf_balance");
					weeklyDep = rs.getDouble("weekly_deposit");
					weeklyDeposit.add(((int) weeklyDep));
					if(rebet < rs.getDouble("rebet") ){
						rebet = rs.getDouble("rebet");
						date1 = rs.getString("date").substring(0,2);
					}
					monthlyCol = rs.getDouble("monthly_collection");
					if(savingRet < rs.getDouble("saving_return")){
						savingRet = rs.getDouble("saving_return");
						date2 = rs.getString("date").substring(0,2);
					}
					totalBal = rs.getDouble("total_balance");
				}else{
					fetchingAccountInfo(theID);
					theID = rs.getInt("user_id");
					weeklyDep = rs.getDouble("weekly_deposit");
					weeklyDeposit.add(((int) weeklyDep));
					bfBalance.add((int)bfBal);
//					if(rebet < rs.getDouble("rebet") ){
//						rebet = rs.getDouble("rebet");
//						date1 = rs.getString("date").substring(0,2);
//					}
					rebetList.add((int)rebet);
					rebetDate.add(date1);
					monthlyCollection.add((int)monthlyCol);
					savingReturn.add((int)savingRet);
					savingReturnDate.add(date2);
					totalBalance.add((int)totalBal);
					rebet = 0;
					savingRet = 0;
					date1 = " ";
					date2 = " ";
				}
			}
		}
		fetchingAccountInfo(theID);
		bfBalance.add((int)bfBal);
		rebetList.add((int)rebet);
		rebetDate.add(date1);
		monthlyCollection.add((int)monthlyCol);
		savingReturn.add((int)savingRet);
		savingReturnDate.add(date2);
		totalBalance.add((int)totalBal);
		System.out.println(rebetDate);
		System.out.println(rebetList);
		rs.close();
	}

	/********************************
	 * Storing invest data to List
	 ********************************/
	private void fetchingInvestData() throws Exception{

	}

	/*********************************
	 * Storing account info to List
	 ********************************/
	private void fetchingAccountInfo(int id) throws Exception{
		DatabaseConnection db = new DatabaseConnection();
		db.setQuery(db.connect().createStatement());
		ResultSet rs;
		rs = db.getQuery().executeQuery("SELECT serial_no,name,husband FROM weekly_user WHERE user_id = '"+id+"';");
		if(rs.next()){
			serialNo.add(rs.getInt("serial_no"));
			memberName.add(rs.getString("name"));
			husbandName.add(rs.getString("husband"));
		}
		rs.close();
		db.connect().close();
	}

	/*********************************
	 * Writing .docx file
	 ********************************/
	private void exportDataToDocx() throws Exception{
		progress.setProgress(0.4); // Progress indicator

		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);
							if (text.contains("CC")) {
								text = text.replace("CC", Integer.toString(centerCode));
								r.setText(text, 0);
							}
							if (text.contains("CN")) {
								text = text.replace("CN", centerName);
								r.setText(text, 0);
							}
							if (text.contains("MONX")) {
								text = text.replace("MONX", selectMonth.getValue()+"/"+selectYear.getValue());
								r.setText(text, 0);
							}
						}
					}
				}
			}
		}

		progress.setProgress(0.5); // Progress indicator

		int i = 0, j = 0;
		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);

							if (text.contains("SL") && i < serialNo.size()) {
								text = text.replace("SL",serialNo.get(i).toString());
								r.setText(text, 0);
							}

							if (text.contains("NMX") && i < memberName.size()) {
								text = text.replace("NMX",memberName.get(i).toString());
								r.setText(text, 0);
							}
							if (text.contains("HBX") && i < husbandName.size()) {
								text = text.replace("HBX",husbandName.get(i).toString());
								r.setText(text, 0);
							}
							if (text.contains("BFX") && i < bfBalance.size()) {
								text = text.replace("BFX",bfBalance.get(i).toString());
								r.setText(text, 0);
							}

							if (text.contains("D1") && j < weeklyDeposit.size()) {
								text = text.replace("D1",weeklyDeposit.get(j).toString());
								r.setText(text, 0); j++;
							}
							if (text.contains("D2") && j < weeklyDeposit.size()) {
								text = text.replace("D2",weeklyDeposit.get(j).toString());
								r.setText(text, 0); j++;
							}
							if (text.contains("D3") && j < weeklyDeposit.size()) {
								text = text.replace("D3",weeklyDeposit.get(j).toString());
								r.setText(text, 0); j++;
							}
							if (text.contains("D4") && j < weeklyDeposit.size()) {
								text = text.replace("D4",weeklyDeposit.get(j).toString());
								r.setText(text, 0); j++;
							}
							if (text.contains("D5") && j < weeklyDeposit.size()) {
								text = text.replace("D5",weeklyDeposit.get(j).toString());
								r.setText(text, 0); j++;
							}

							if (text.contains("RB") && i < rebetList.size()) {
								text = text.replace("RB",rebetList.get(i).toString());
								r.setText(text, 0);
							}

							if (text.contains("DR") && i < rebetDate.size()) {
								text = text.replace("DR", rebetDate.get(i));
								r.setText(text, 0);
							}

							if (text.contains("MNC") && i < monthlyCollection.size()) {
								text = text.replace("MNC",monthlyCollection.get(i).toString());
								r.setText(text, 0);
							}
							if (text.contains("SR") && i < savingReturn.size()) {
								text = text.replace("SR",savingReturn.get(i).toString());
								r.setText(text, 0);
							}
							if (text.contains("SD") && i < savingReturnDate.size()) {
								text = text.replace("SD", savingReturnDate.get(i));
								r.setText(text, 0);
							}

							if (text.contains("TB") && i < totalBalance.size()) {
								text = text.replace("TB",totalBalance.get(i).toString());
								r.setText(text, 0);
								i++;
							}
						}
					}
				}
			}
		}

		// Clear linked list
		bfBalance.clear(); rebetList.clear();monthlyCollection.clear();savingReturn.clear();totalBalance.clear();
		weeklyDeposit.clear();memberName.clear();husbandName.clear();serialNo.clear();
		doc.write(new FileOutputStream(theDirectory + "/output.docx"));
	}

	@FXML
	public void resetIndicator() {
		progress.setProgress(0);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		monthList.addAll("January","February","March","April","May","June","July","August","September","October","November","December");
		yearList.addAll(2025,2024,2023,2022,2021,2020,2019,2018,2017,2016,2015,2014,2013,2012,2011,2010,2009,2008,2007,2006,2005);
		selectMonth.getItems().addAll(monthList);
		selectYear.getItems().addAll(yearList);
	}

}
