package kpur.views;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;
import kpur.model.ShareHolderDataClass;

public class ShareHolderDataController implements Initializable{
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
	private MenuItem edit;
	@FXML
	private MenuItem close;
	@FXML
	private Menu help;
	@FXML
	private MenuItem about;

	/*********************
	 * TABLE ITEMS
	 *********************/
	@FXML
	private TableView<ShareHolderDataClass> table;

	@FXML
	private TableColumn<ShareHolderDataClass, String> dateColumn;

	@FXML
	private TableColumn<ShareHolderDataClass, Integer> shareDepositColumn;

	@FXML
	private TableColumn<ShareHolderDataClass, Integer> profitColumn;

	@FXML
	private TableColumn<ShareHolderDataClass, Integer> shareWithdrolColumn;

	@FXML
	private TableColumn<ShareHolderDataClass, Integer> shareBalanceColumn;

	@FXML
	private TableColumn<ShareHolderDataClass, Integer> shareTransColumn;

	/*********************
	 * TEXT ITEMS
	 *********************/
	@FXML
	private Text shareHolderNumber;
	@FXML
	private Text shareHolderName;
	@FXML
	private Text shareHolderMobile;
	@FXML
	private Text shareHolderVillage;
	@FXML
	private Text shareHolderPost;
	@FXML
	private Text shareHolderGuardian;
	@FXML
	private Text shareHolderNomini;
	@FXML
	private Text shareHolderRelation;


	/************************
	 * FIELD ITEMS;
	 ***********************/
	@FXML
	private TextField deposit;
	@FXML
	private TextField profit;
	@FXML
	private TextField withdraw;
	@FXML
	private DatePicker date;

	@FXML
	private Button add;
	@FXML
	private Button delete;

	/************************
	 * Objects AND Variables
	 ***********************/
	private ObservableList<ShareHolderDataClass> tableData = FXCollections.observableArrayList();
	private String id;
	private int currentBalance = 0;
	GlobalFunctions cs = new GlobalFunctions();
	/*********************
	 * SYSTEMS
	 *********************/
	@FXML //Menu Back;
	private void back(ActionEvent event)throws Exception{
		cs.changeScene(menu,"ShareHolderHomeActivity","Shareholder Panel");

	}
	@FXML // Menu Close;
	private void close(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e->	System.out.println("Are you sure?"));
		Platform.exit();
	}

	@FXML // Menu About;
	private void about(ActionEvent event) throws Exception{
		GlobalFunctions ob = new GlobalFunctions();
		ob.about();
	}

	@FXML // Menu Edit Info;
	private void updateInfo(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ShareHolderEditInfoActivity.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		ShareHolderEditInfoController ob = loader.<ShareHolderEditInfoController>getController();
		ob.setDefaults(id);
		stage.show();
	}

	// Constructor;
	public ShareHolderDataController(){

	}


	public void setTable(String id){
		this.id = id;

		DatabaseConnection db = new DatabaseConnection();
		db.connect();
		try {
			db.setQuery(db.connect().createStatement());
			String sql = "select * from shareholder_data where user_id = '"+id+"';";
			ResultSet rs = db.getQuery().executeQuery(sql);
			while(rs.next()){
				if(Integer.parseInt(rs.getString("balance")) > 0){
					currentBalance = Integer.parseInt(rs.getString("balance"));
				}
				tableData.add(new ShareHolderDataClass(rs.getString("date"),Integer.parseInt(rs.getString("deposit")),Integer.parseInt(rs.getString("profit")),Integer.parseInt(rs.getString("withdraw")),Integer.parseInt(rs.getString("balance")),Integer.parseInt(rs.getString("trans_id"))));
			}
			rs.close();

			ResultSet info = db.getQuery().executeQuery("select * from shareholder where user_id='"+id+"'");
			if(info.next()){
				shareHolderNumber.setText(info.getString("user_id"));
				shareHolderName.setText(info.getString("name"));
				shareHolderGuardian.setText(info.getString("guardian"));
				shareHolderMobile.setText(info.getString("mobile"));
				shareHolderVillage.setText(info.getString("village"));
				shareHolderPost.setText(info.getString("ps"));
				shareHolderNomini.setText(info.getString("nomini"));
				shareHolderRelation.setText(info.getString("relation"));
			}

			info.close();
			db.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// POPULATING TABLE DATA;
		dateColumn.setCellValueFactory(cellData->new ReadOnlyStringWrapper(cellData.getValue().getdate()));
		shareBalanceColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getshareDepositValue()));
		shareDepositColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getshareDepositValue()));
		profitColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getprofitValue()));
		shareWithdrolColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getshareWithdrolValue()));
		shareBalanceColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getshareBalancelValue()));
		shareTransColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getshareTransValue()));
		table.setItems(getPersonData());
		System.out.println("Current:" + currentBalance);
	}

	// Add Transaction;
	@FXML
	public void addTrans(ActionEvent event) throws Exception{
		LocalDate dated = date.getValue();
		String theDate = GlobalFunctions.formatDate(dated.toString());

		int theDeposit;
		int theProfit;
		int theWithdraw;
		int balance = currentBalance;

		if(deposit.getText().equals("")){
			theDeposit = 0;
		}else{
			theDeposit = Integer.parseInt(deposit.getText().toString());
		}
		if(profit.getText().equals("")){
			theProfit = 0;
		}else{
			theProfit = Integer.parseInt(profit.getText().toString());
		}
		if(withdraw.getText().equals("")){
			theWithdraw = 0;
		}else{
			theWithdraw = Integer.parseInt(withdraw.getText().toString());
		}

		if(theWithdraw != 0){
			balance = balance-theWithdraw;
			currentBalance = balance;
		}else{
			if(theProfit > 0 ){
				balance = balance+((balance*theProfit)/100);
				currentBalance = balance;
			}else{
				balance = balance+(theDeposit + theProfit);
				currentBalance = balance;
			}
		}

		int transID = 0;

		DatabaseConnection db = new DatabaseConnection();
		db.connect();
		try {
			db.puts("INSERT INTO shareholder_data (date, user_id, deposit, profit, withdraw, balance) VALUES ('"+ theDate +"','"+ id +"','"+ theDeposit +"','"+ theProfit +"','"+ theWithdraw+"','"+balance+"');");
			db.setQuery(db.connect().createStatement());
			String sql = "select trans_id from shareholder_data order by trans_id desc limit 1;";
			ResultSet rs = db.getQuery().executeQuery(sql);
			while(rs.next()){
				transID = Integer.parseInt(rs.getString("trans_id"));
			}
			rs.close();
			db.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		tableData.add(new ShareHolderDataClass(theDate,theDeposit,theProfit,theWithdraw,balance,transID));

	}


	@FXML
	public void deleteData(ActionEvent event) throws Exception{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete transaction");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure? You will not get this data back again!");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			ShareHolderDataClass thedata = table.getSelectionModel().getSelectedItem();
			tableData.remove(thedata);

			DatabaseConnection db = new DatabaseConnection();
			db.connect();
			db.setQuery(db.connect().createStatement());
			try {
				db.puts("DELETE FROM shareholder_data WHERE trans_id='"+thedata.getshareTransValue()+"'");

				String sql = "select * from shareholder_data where user_id = '"+id+"';";
				ResultSet rs = db.getQuery().executeQuery(sql);
				while(rs.next()){
					if(Integer.parseInt(rs.getString("balance")) > 0){
						currentBalance = Integer.parseInt(rs.getString("balance"));
					}
				}
				rs.close();
				db.connect().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Good Choice!");
		}
	}


	public ObservableList<ShareHolderDataClass> getPersonData() {
	    return tableData;
	}


	/* *********************
	 * Get current Date;
	 * ********************/
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date today = new Date();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		date.setPromptText(dateFormat.format(today));
	}

}
