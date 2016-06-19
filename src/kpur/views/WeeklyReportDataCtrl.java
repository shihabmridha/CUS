package kpur.views;

import java.net.URL;
import java.security.Key;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;
import kpur.model.WeeklyInvestData;
import kpur.model.WeeklySavingDepositData;

public class WeeklyReportDataCtrl implements Initializable{

	/************************
	 * Menu
	 ***********************/
	@FXML
	private MenuBar menu;

	/************************
	 * Labels
	 ***********************/
	@FXML
	private Label lbNo;
	@FXML
	private Label lbName;
	@FXML
	private Label lbHusbandName;
	@FXML
	private Label lbCenterCode;
	@FXML
	private Label lbCenterName;
	@FXML
	private Label lbFS;


	/************************
	 * TextFields
	 ***********************/
	@FXML
	private TextField tfDeposit;
	@FXML
	private TextField tfRebet;
	@FXML
	private TextField tfSavingReturn;

	@FXML
	private TextField tfProjectNo;
	@FXML
	private TextField tfODLastMonth;
	@FXML
	private TextField tfInvestmentCBM;
	@FXML
	private TextField tfLastMonthOS;
	@FXML
	private TextField tfDisbudsCurrentMonth;
	@FXML
	private TextField tfWeeklyInstalment;

	/************************
	 * CheckBox
	 ***********************/
	@FXML
	private CheckBox cbInstalmentAmount;

	/************************
	 * DatePicke
	 ***********************/
	@FXML
	private DatePicker dpSavingDeposit;
	@FXML
	private DatePicker dpInvestment;

	/************************
	 * Button
	 ***********************/
	@FXML
	private Button btnViewData;
	@FXML
	private Button btnDeleteAccount;
	@FXML
	private Button btnSavingAdd;
	@FXML
	private Button btnSavingDelete;
	@FXML
	private Button btnInvestAdd;
	@FXML
	private Button btnInvestDelete;
	@FXML
	private Button btnEndOfMonth;

	/************************
	 * ComboBox
	 ***********************/
	@FXML
	private ComboBox<String> selectMonth;
	@FXML
	private ComboBox<Integer> selectYear;

	/************************
	 * TableView
	 ***********************/
	@FXML
	private TableView<WeeklySavingDepositData> savingTable;
	@FXML
	private TableView<WeeklyInvestData> investmentTable;

	/************************
	 * savingTable Columns
	 ***********************/
	@FXML
	private TableColumn<WeeklySavingDepositData, String> colDate;
	@FXML
	private TableColumn<WeeklySavingDepositData, Double> colBFbalance;
	@FXML
	private TableColumn<WeeklySavingDepositData, Double> colWeeklyDeposit;
	@FXML
	private TableColumn<WeeklySavingDepositData, Double> colRebet;
	@FXML
	private TableColumn<WeeklySavingDepositData, Double> colMonthlyCollection;
	@FXML
	private TableColumn<WeeklySavingDepositData, Double> colSavingReturn;
	@FXML
	private TableColumn<WeeklySavingDepositData, Double> colTotalBalance;
	@FXML
	private TableColumn<WeeklySavingDepositData, Integer> colTransId;

	/************************
	 * investmentTable Columns
	 ***********************/
	@FXML
	private TableColumn<WeeklyInvestData, String> colDateInv;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colDisbudsAmount;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colInstolmentAmount;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colProjectNo;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colODlastMonth;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colInvestmentCBM;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colLastMonthOutStanding;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colDisbudsCurrentMonth;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colTotalBalanceInv;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colWeeklyInstolment;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colTotalCollection;
	@FXML
	private TableColumn<WeeklyInvestData, Double> colTotalOutstanding;
    @FXML
    private TableColumn<WeeklyInvestData, Integer> colInvTransId;


	/************************
	 * Objects AND Variables
	 ***********************/
    private ObservableList<WeeklySavingDepositData> savingTableData = FXCollections.observableArrayList();
    private ObservableList<WeeklyInvestData> investTableData = FXCollections.observableArrayList();
	private ObservableList<String> monthsList = FXCollections.observableArrayList();
	private ObservableList<Integer> yearsList = FXCollections.observableArrayList();

    private String today, month, year;
    private int theID, theCenter;

	/*********************
	 * SYSTEMS
	 *********************/
	@FXML
	private void onBackspace(KeyEvent e) throws Exception{
		if(e.getCode().equals(KeyCode.BACK_SPACE)){
			back();
		}
	}

	@FXML //Menu Back;
	private void back()throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		GlobalFunctions ob = new GlobalFunctions();
		ob.goBackToCenterList(stage,scene,theCenter);
	}

	@FXML // Menu Close;
	private void close() throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e-> System.out.println("Are you sure?"));
		Platform.exit();
	}
	@FXML // Menu About;
	private void about() throws Exception{
		GlobalFunctions ob = new GlobalFunctions();
		ob.about();
	}


	/*************************
	 * Edit Account Info
	 *************************/
	@FXML
	private void editAccountInfo() throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyEditAccount.fxml"));
		scene.setRoot(loader.load());
		WeeklyEditAccountCtrl ob = loader.getController();
		stage.setScene(scene);
		ob.setDefaults(theID,theCenter);
		stage.setTitle("Weekly Report - Edit User Information");
		stage.show();
	}


	/*************************
     * TABLE DATA LOADER
	 *************************/
    //Saving Variables;
    private double savingCurrentBfBalance = 0, savingCurrentMonthlyCollection = 0, savingCurrentTotalBalance = 0;

    // Investment Variables;
    private double theInvestDisbudsAmount = 0, theTotalCollection = 0;

	@FXML
	public void setTableAndInfo(int id, int centerCode) throws Exception{
		theID = id;
		theCenter = centerCode;
        String[] parts = today.split("/");
		month = parts[1];
		year = parts[2];
		gettingPrimaryDataFromDB();
	}

	/*************************
	 * New Entry to Saving
	 *************************/
	@FXML
	void savingEntry() throws Exception{
		if(savingTable.getItems().size() < 5){
			LocalDate dated = dpSavingDeposit.getValue();
			String theDate = GlobalFunctions.formatDate(dated.toString());

			double deposit = 0, rebet = 0, savingReturn = 0;
			int transID = 0;

			if(!tfDeposit.getText().isEmpty()){
				deposit = Double.parseDouble(tfDeposit.getText());
			}
			if(!tfRebet.getText().isEmpty()){
				rebet = Double.parseDouble(tfRebet.getText());
			}
			if(!tfSavingReturn.getText().isEmpty()){
				savingReturn = Double.parseDouble(tfSavingReturn.getText());
			}

			savingCurrentMonthlyCollection += (deposit+rebet);
			savingCurrentTotalBalance += ((deposit+rebet)-savingReturn);

		/*
		 * Put data into database;
		 */
			DatabaseConnection db = new DatabaseConnection();
			db.puts("INSERT INTO weekly_saving (user_id, date, bf_balance, weekly_deposit, rebet, monthly_collection, saving_return, total_balance) VALUES ("
					+ "'"+theID +"',"
					+ "'"+ theDate +"',"
					+ "'"+ savingCurrentBfBalance +"',"
					+ "'"+ deposit +"',"
					+ "'"+ rebet +"',"
					+ "'"+ savingCurrentMonthlyCollection +"',"
					+ "'"+ savingReturn +"',"
					+ "'"+ savingCurrentTotalBalance +"');");

			db.setQuery(db.connect().createStatement());
			String sql = "select trans_id from weekly_saving order by trans_id desc limit 1;";
			ResultSet rs = db.getQuery().executeQuery(sql);
			while(rs.next()){
				transID = Integer.parseInt(rs.getString("trans_id"));
			}
			rs.close();
			db.connect().close();

			savingTableData.add(new WeeklySavingDepositData(theDate, savingCurrentBfBalance, deposit, rebet, savingCurrentMonthlyCollection, savingReturn, savingCurrentTotalBalance, transID));
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Sorry");
			alert.setHeaderText(null);
			alert.setContentText("You can not input more than 5 transaction in a month.");
			alert.showAndWait();
		}

	}

	/***********************************
	 * Delete Entry from Saving
	 ***********************************/
	@FXML
	void deleteSaving() throws Exception{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete transaction");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure? You will not get this data back again!");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			WeeklySavingDepositData thedata = savingTable.getSelectionModel().getSelectedItem();
			savingTableData.remove(thedata);

			DatabaseConnection db = new DatabaseConnection();
			db.setQuery(db.connect().createStatement());
			db.puts(String.format("DELETE FROM weekly_saving WHERE trans_id='%d'", thedata.getTransId()));

			String sql = String.format("select bf_balance,monthly_collection,total_balance from weekly_saving where user_id = '%d' order by trans_id desc limit 1;", theID);
			ResultSet rs = db.getQuery().executeQuery(sql);
			if(rs.next()){
				savingCurrentBfBalance = rs.getDouble("bf_balance");
				savingCurrentMonthlyCollection = rs.getDouble("monthly_collection");
				savingCurrentTotalBalance = rs.getDouble("total_balance");
			}else{
				rs = db.getQuery().executeQuery(String.format("select bf_balance from weekly_user where user_id = '%d';", theID));
				if(rs.next()){
					savingCurrentBfBalance = rs.getDouble("bf_balance");
					savingCurrentMonthlyCollection = 0;
					savingCurrentTotalBalance = savingCurrentBfBalance;
				}
			}

			rs.close();
			db.connect().close();
		}
	}

	/*************************
	 * New Entry to Invest
	 *************************/
	private  double theInstalmentAmount = 0, theProjectNo = 0, theODLastMonth = 0, theInvestCBM = 0, theLastMonthOutStanding = 0, theDisbudsCurrentMonth = 0, theWeeklyInstalment = 0;

	@FXML
	void investEntry() throws Exception{
		double theTotalOutStanding;

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Fix the following error!");
		alert.setHeaderText(null);
		alert.setContentText("Please insert the date.");
		if(investmentTable.getItems().size() < 5){
			int transID = 0;
			double theTotalBalance;

			if(dpInvestment.getValue() == null){
				alert.showAndWait();
			}
			else
			{
				LocalDate dated = dpInvestment.getValue();
				String theDate = GlobalFunctions.formatDate(dated.toString());

				if(!tfProjectNo.getText().isEmpty()) theProjectNo = Double.parseDouble(tfProjectNo.getText());
				if(!tfODLastMonth.getText().isEmpty()) theODLastMonth = Double.parseDouble(tfODLastMonth.getText());
				if(!tfInvestmentCBM.getText().isEmpty()) theInvestCBM = Double.parseDouble(tfInvestmentCBM.getText());
				if(!tfLastMonthOS.getText().isEmpty()) theLastMonthOutStanding = Double.parseDouble(tfLastMonthOS.getText());
				if(!tfDisbudsCurrentMonth.getText().isEmpty()) theDisbudsCurrentMonth = Double.parseDouble(tfDisbudsCurrentMonth.getText());
				if(!tfWeeklyInstalment.getText().isEmpty()) theWeeklyInstalment = Double.parseDouble(tfWeeklyInstalment.getText());



				theTotalBalance = theLastMonthOutStanding + theDisbudsCurrentMonth;
				theTotalCollection += theWeeklyInstalment;
				theTotalOutStanding = (theTotalBalance - theTotalCollection);

				if(cbInstalmentAmount.isSelected()){
					theInstalmentAmount = (theInvestDisbudsAmount+(theInvestDisbudsAmount * 15)/100)/46;
				}

				/*
				 * Put data into database;
				 */
				DatabaseConnection db = new DatabaseConnection();
				db.puts("INSERT INTO weekly_invest (user_id, date, investment_amount, " +
						"instolment_amount, project_no, od_last_month, investment_cbm, " +
						"last_month_outstanding, disbuds_current_month, total_balance_inv, " +
						"weekly_instolment, total_collection, total_outstanding) VALUES ("
						+ "'"+theID +"',"
						+ "'"+ theDate +"',"
						+ "'"+ theInvestDisbudsAmount +"',"
						+ "'"+ theInstalmentAmount +"',"
						+ "'"+ theProjectNo +"',"
						+ "'"+ theODLastMonth +"',"
						+ "'"+ theInvestCBM +"',"
						+ "'"+ theLastMonthOutStanding +"',"
						+ "'"+ theDisbudsCurrentMonth +"',"
						+ "'"+ theTotalBalance +"',"
						+ "'"+ theWeeklyInstalment +"',"
						+ "'"+ theTotalCollection +"',"
						+ "'"+ theTotalOutStanding +"');");

				db.setQuery(db.connect().createStatement());
				String sql = "select trans_id from weekly_invest order by trans_id desc limit 1;";
				ResultSet rs = db.getQuery().executeQuery(sql);
				while(rs.next()){
					transID = Integer.parseInt(rs.getString("trans_id"));
				}
				rs.close();
				db.connect().close();

				investTableData.add(new WeeklyInvestData(theDate,theInvestDisbudsAmount,theInstalmentAmount,theProjectNo,theODLastMonth,theInvestCBM,theLastMonthOutStanding,theDisbudsCurrentMonth,theTotalBalance,theWeeklyInstalment,theTotalCollection,theTotalOutStanding,transID));

				// Reset Everything;
				tfProjectNo.clear();tfODLastMonth.clear();tfInvestmentCBM.clear();tfLastMonthOS.clear();tfDisbudsCurrentMonth.clear();
				tfWeeklyInstalment.clear();
				theInstalmentAmount = 0; theProjectNo = 0; theODLastMonth = 0; theInvestCBM = 0; theWeeklyInstalment = 0;

			}
		}else{
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Sorry");
			alert.setHeaderText(null);
			alert.setContentText("You can not input more than 5 transaction in a month.");
			alert.showAndWait();
		}


	}


	/**********************************
	* End of the month calculation;
	**********************************/
	@FXML
	private void endOfMonth() throws Exception{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation !!");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure? You can not undo this information !");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			DatabaseConnection db = new DatabaseConnection();
			db.setQuery(db.connect().createStatement());
			double bfbal = 0, outstand = 0;
			String sql = "SELECT total_balance FROM weekly_saving WHERE user_id = '"+theID+"' and date like '%"+month+"/"+year+"';";
			ResultSet rs = db.getQuery().executeQuery(sql);
			while(rs.next()){
				bfbal = rs.getDouble("total_balance");
			}
			sql = "SELECT total_outstanding FROM weekly_invest WHERE user_id = '"+theID+"' and date like '%"+month+"/"+year+"';";
			rs = db.getQuery().executeQuery(sql);
			while(rs.next()){
				outstand = rs.getDouble("total_outstanding");
			}
			rs.close();
			System.out.println("BF: " + bfbal);
			System.out.println("Out: " + outstand);
			db.puts("UPDATE weekly_user SET bf_balance='"+bfbal+"',investment_amount='"+outstand+"' WHERE user_id='"+theID+"';");

			db.connect().close();
		}

	}

	/***********************************
	 * Delete Entry from Invest
	 ***********************************/
	@FXML
	void deleteInvest() throws Exception{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete transaction");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure? You will not get this data back again!");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			WeeklyInvestData theData = investmentTable.getSelectionModel().getSelectedItem();
			investTableData.remove(theData);

			DatabaseConnection db = new DatabaseConnection();
			db.setQuery(db.connect().createStatement());
			db.puts(String.format("DELETE FROM weekly_invest WHERE trans_id='%d'", theData.getTransId()));

			db.connect().close();
			gettingPrimaryDataFromDB();
		}
	}


	/***********************************
	 * Delete User
	 ***********************************/
	@FXML
	void deleteAccount() throws Exception{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Account");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure? You will not get this account back again!");

		Optional<ButtonType> res = alert.showAndWait();
		if (res.get() == ButtonType.OK){

			DatabaseConnection db = new DatabaseConnection();
			db.setQuery(db.connect().createStatement());
			db.puts("DELETE FROM weekly_user WHERE user_id='"+ theID +"'");
			db.puts("DELETE FROM weekly_saving WHERE user_id='"+ theID +"'");
			db.puts("DELETE FROM weekly_invest WHERE user_id='"+ theID +"'");
			db.connect().close();
			back();
		}
	}

	/********************************
	* Setting values from database;
	*********************************/
	@FXML
	private void gettingPrimaryDataFromDB() throws Exception{
		// Reset Table
		investTableData.removeAll(investTableData);
		savingTableData.removeAll(savingTableData);

		if(!selectYear.getSelectionModel().isEmpty() && !selectMonth.getSelectionModel().isEmpty()){
			if(selectMonth.getSelectionModel().getSelectedIndex()+1 >= 10)
				month = Integer.toString(selectMonth.getSelectionModel().getSelectedIndex()+1);
			else
				month = "0" + Integer.toString(selectMonth.getSelectionModel().getSelectedIndex()+1);

			year = Integer.toString(selectYear.getValue());
		}

		DatabaseConnection db = new DatabaseConnection();
		db.setQuery(db.connect().createStatement());

		String sql = String.format("select w.*,c.* from weekly_user w,centers c where w.user_id='%d' and c.center_id='%d' and w.center_id = c.center_id;", theID, theCenter);
		ResultSet rs = db.getQuery().executeQuery(sql);
		savingCurrentMonthlyCollection = 0; savingCurrentBfBalance = 0; savingCurrentTotalBalance = 0;
		theDisbudsCurrentMonth = 0; theLastMonthOutStanding = 0; theTotalCollection = 0; theInvestDisbudsAmount = 0;
		if(rs.next()){
			lbFS.setText(": " + rs.getString("fs"));
			lbCenterName.setText(": " + rs.getString("center_name"));
			lbCenterCode.setText(": " + rs.getString("center_id"));
			lbName.setText(": " + rs.getString("name"));
			lbHusbandName.setText(": " + rs.getString("husband"));
			lbNo.setText(": " + rs.getString("serial_no"));

			// Setting current values
			savingCurrentBfBalance = rs.getDouble("bf_balance");
			savingCurrentTotalBalance = savingCurrentBfBalance;
			theInvestDisbudsAmount = rs.getDouble("investment_amount");
		}

		sql = "select * from weekly_saving where user_id='"+theID+"' and date like '%"+month+"/"+year+"';";
		rs = db.getQuery().executeQuery(sql);
		while(rs.next()){
			savingCurrentBfBalance = rs.getDouble("bf_balance");
			savingCurrentMonthlyCollection = rs.getDouble("monthly_collection");
			savingCurrentTotalBalance = rs.getDouble("total_balance");
			savingTableData.add(new WeeklySavingDepositData(rs.getString("date"), rs.getDouble("bf_balance"), rs.getDouble("weekly_deposit"), rs.getDouble("rebet"), rs.getDouble("monthly_collection"), rs.getDouble("saving_return"), rs.getDouble("total_balance"), rs.getInt("trans_id")));
		}

		sql = "select * from weekly_invest where user_id='"+theID+"' and date like '%"+month+"/"+year+"';";
		rs = db.getQuery().executeQuery(sql);
		while (rs.next()){
			theDisbudsCurrentMonth = rs.getDouble("disbuds_current_month");
			theLastMonthOutStanding = rs.getDouble("last_month_outstanding");
			theTotalCollection = rs.getDouble("total_collection");
			investTableData.add(new WeeklyInvestData(
					rs.getString("date"), rs.getDouble("investment_amount"), rs.getDouble("instolment_amount"),
					rs.getDouble("project_no"),rs.getDouble("od_last_month"),rs.getDouble("investment_cbm"),
					rs.getDouble("last_month_outstanding"),rs.getDouble("disbuds_current_month"),rs.getDouble("total_balance_inv"),
					rs.getDouble("weekly_instolment"),rs.getDouble("total_collection"),rs.getDouble("total_outstanding"), rs.getInt("trans_id")
			));
		}

		rs.close();
		db.connect().close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

        // SavingColumns
		colDate.setCellValueFactory(cellData->new ReadOnlyStringWrapper(cellData.getValue().getDate()));
		colBFbalance.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getBfBalance()));
		colWeeklyDeposit.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getWeeklyDeposit()));
		colRebet.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getRebet()));
		colMonthlyCollection.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getMonthlyCollection()));
		colSavingReturn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getSavingReturn()));
		colTotalBalance.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalBalance()));
		colTransId.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getTransId()));
		savingTable.setItems(savingTableData);

        // InvestmentColumns
        colDateInv.setCellValueFactory(cellData->new ReadOnlyStringWrapper(cellData.getValue().getDate()));
        colDisbudsAmount.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getDisbudsAmount()));
        colInstolmentAmount.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getInstalmentAmount()));
        colProjectNo.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getProjectNo()));
        colODlastMonth.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getODLastMonth()));
        colInvestmentCBM.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getInvestment()));
        colLastMonthOutStanding.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getLastMonthOutStanding()));
        colDisbudsCurrentMonth.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getDisbudsCurrentMonth()));
        colTotalBalanceInv.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalBalance()));
        colWeeklyInstolment.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getWeeklyInstolment()));
        colTotalCollection.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalCollection()));
        colTotalOutstanding.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalOutStanding()));
        colInvTransId.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getTransId()));
        investmentTable.setItems(investTableData);

		monthsList.addAll("January","February","March","April","May","June","July","August","September","October","November","December");
		yearsList.addAll(2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date mydate = new Date();
	    //dpMonth.setPromptText(dateFormat.format(mydate));
		selectMonth.getItems().addAll(monthsList);
		selectYear.getItems().addAll(yearsList);

	    dpInvestment.setPromptText(dateFormat.format(mydate));
	    dpSavingDeposit.setPromptText(dateFormat.format(mydate));
	    today = dateFormat.format(mydate);
	}

}
