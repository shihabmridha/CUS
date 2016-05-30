package kpur.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import kpur.views.ShareHolderDataController;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GlobalFunctions {
	public void about() throws Exception{
		System.out.println("About");
	}

	public static String formatDate(String date){

		char oldDate[] = new char[10];
		char newDate[] = new char[10];
		String finaldate = "";

		oldDate= date.toCharArray();

		newDate[0] = oldDate[8];
		finaldate += Character.toString(oldDate[8]);
		newDate[1] = oldDate[9];
		finaldate += Character.toString(oldDate[9]);
		newDate[2] = '/';
		finaldate += "/";
		newDate[3] = oldDate[5];
		finaldate += Character.toString(oldDate[5]);
		newDate[4] = oldDate[6];
		finaldate += Character.toString(oldDate[6]);
		newDate[5] = '/';
		finaldate += "/";
		newDate[6] = oldDate[0];
		finaldate += Character.toString(oldDate[0]);
		newDate[7] = oldDate[1];
		finaldate += Character.toString(oldDate[1]);
		newDate[8] = oldDate[2];
		finaldate += Character.toString(oldDate[2]);
		newDate[9] = oldDate[3];
		finaldate += Character.toString(oldDate[3]);

		return finaldate;
	}

	public void changeScene(MenuBar mn, String name, String title) throws Exception{
		Stage stage = (Stage) mn.getScene().getWindow();
		Scene scene = mn.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/kpur/views/" + name + ".fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.setTitle(title + " - CUS");
		stage.show();
	}

	public static ObservableList<String> getCenters() throws Exception{
		ObservableList<String> list = FXCollections.observableArrayList();
		DatabaseConnection ob = new DatabaseConnection();

		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("select center_name from centers;");
		while(rs.next()){
			list.add(rs.getString("center_name"));
		}
		rs.close();
		ob.connect().close();

		return list;
	}

	public static int getCenterCode(String centerName) throws Exception{
		int code = 0;
		DatabaseConnection db = new DatabaseConnection();
		db.setQuery(db.connect().createStatement());
		String sql = "select center_id from centers where center_name='"+centerName+"';";
		ResultSet rs = db.getQuery().executeQuery(sql);
		if(rs.next()){
			code = Integer.parseInt(rs.getString("center_id"));
		}
		rs.close();
		db.connect().close();

		return code;
	}

	public static void userNotFound(){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error!");
		alert.setHeaderText(null);
		alert.setContentText("User not fount. Please check again.");
		alert.showAndWait();
	}


}
