package kpur.views;

import java.net.URL;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
	private TextField tfDisbudsAmount;
	@FXML
	private TextField tfInstolmentAmount;
	@FXML
	private TextField tfProjectNo;
	@FXML
	private TextField tfWeeklyInstolment;


	/************************
	 * DatePicke
	 ***********************/
	@FXML
	private DatePicker dpMonth;
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
	private Button btnWeeklyDepositReport;
	@FXML
	private Button btnSavingAdd;
	@FXML
	private Button btnSavingDelete;
	@FXML
	private Button btnInvestAdd;
	@FXML
	private Button btnInvestDelete;

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

    private String today;
    private int theID;

	/*********************
	 * SYSTEMS
	 *********************/
	@FXML //Menu Back;
	private void back()throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyReportHomeActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.setTitle("Weekly Report");
		stage.show();
	}
	@FXML // Menu Close;
	private void close() throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e->{
			System.out.println("Are you sure?");
		});
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

	}


	/*************************
     * TABLE DATA LOADER
	 *************************/
    //Saving Variables;
    private double savingCurrentBfBalance = 0, savingCurrentMonthlyCollection = 0, savingCurrentTotalBalance = 0;

    // Investment Variables;
    private double investCurrentDisbudsAmount = 0, investTotalBalance = 0, investLastMonthOS = 0, investDisbudsCurrentMonth = 0;
    private double investCurrentTotalCollection = 0, investTotalOutstanding = 0;

	public void setTableAndInfo(int id, int centerCode) throws Exception{
		theID = id;
        String[] parts = today.split("/");
        String month = parts[1];
        String year = parts[2];

		DatabaseConnection db = new DatabaseConnection();
		db.setQuery(db.connect().createStatement());

		String sql = String.format("select w.*,c.* from weekly_user w,centers c where w.user_id='%d' and c.center_id='%d' and w.center_id = c.center_id;", theID, centerCode);
		ResultSet rs = db.getQuery().executeQuery(sql);

		if(rs.next()){
			lbFS.setText(": " + rs.getString("fs"));
			lbCenterName.setText(": " + rs.getString("center_name"));
			lbCenterCode.setText(": " + rs.getString("center_id"));
			lbName.setText(": " + rs.getString("name"));
			lbHusbandName.setText(": " + rs.getString("husband"));
			lbNo.setText(": " + rs.getString("serial_no"));
			savingCurrentBfBalance = rs.getDouble("bf_balance");
			savingCurrentTotalBalance = savingCurrentBfBalance;
		}

		sql = "select * from weekly_saving where user_id='"+theID+"' and date like '%"+month+"/"+year+"';";
		rs = db.getQuery().executeQuery(sql);
		while(rs.next()){
			savingCurrentBfBalance = rs.getDouble("bf_balance");
			savingCurrentMonthlyCollection = rs.getDouble("monthly_collection");
			savingCurrentTotalBalance = rs.getDouble("total_balance");
			savingTableData.add(new WeeklySavingDepositData(rs.getString("date"), rs.getDouble("bf_balance"), rs.getDouble("weekly_deposit"), rs.getDouble("rebet"), rs.getDouble("monthly_collection"), rs.getDouble("saving_return"), rs.getDouble("total_balance"), rs.getInt("trans_id")));
		}

        //sql = "select * from weekly_invest where user_id='"+theID+"' and date like '%"+month+"/"+year+"';";
        //rs = db.getQuery().executeQuery(sql);
        investTableData.add(new WeeklyInvestData("10/15/2016",20000,0,0,0,0,20000,10000,25000,500,500,25500,1));
		rs.close();
		db.connect().close();

	}


	/*************************
	 * New Entry to Saving
	 *************************/
	@FXML
	void savingEntry() throws Exception{
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
		savingCurrentTotalBalance += (deposit-savingReturn);

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
				rs = db.getQuery().executeQuery(String.format("select bf_balance form weekly_user where user_id = '%d';", theID));
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


	@FXML
	void investEntry() throws Exception{
		LocalDate dated = dpInvestment.getValue();
		String theDate = GlobalFunctions.formatDate(dated.toString());



	}

	/***********************************
	 * Delete Entry from Invest
	 ***********************************/
	@FXML
	void deleteInvest() throws Exception{}

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


		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date mydate = new Date();
	    dpMonth.setPromptText(dateFormat.format(mydate));
	    dpInvestment.setPromptText(dateFormat.format(mydate));
	    dpSavingDeposit.setPromptText(dateFormat.format(mydate));
	    today = dateFormat.format(mydate);
	}

}
