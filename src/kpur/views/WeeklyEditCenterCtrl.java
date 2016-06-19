package kpur.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;

import java.sql.ResultSet;

/**
 * Created by Nusrat on 6/17/2016.
 */
public class WeeklyEditCenterCtrl {

    /************************
     * Menu
     ***********************/
    @FXML
    private MenuBar menu;

    /************************
     * TextField
     ***********************/
    @FXML
    private TextField tfCenterName;
    @FXML
    private TextField tfFS;

    /************************
     * TextField
     ***********************/
    @FXML
    private Button btnUpdate;

    /************************
     * Objects AND Variables
     ***********************/
    private int theCenter;

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

    public void setCenterCodeWithTextField(int centerCode) throws Exception{
        theCenter = centerCode;

        DatabaseConnection db = new DatabaseConnection();
        db.setQuery(db.connect().createStatement());
        ResultSet rs = db.getQuery().executeQuery("SELECT * FROM centers WHERE center_id ='"+theCenter+"'");

        if(rs.next()){
            tfCenterName.setText(rs.getString("center_name"));
            tfFS.setText(rs.getString("fs"));
        }

        rs.close();
        db.connect().close();

    }

    @FXML
    private void updateCenterInfo() throws Exception{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Data updated successfully!");
        alert.setHeaderText(null);
        DatabaseConnection db = new DatabaseConnection();
        if(tfCenterName.getText().isEmpty() || tfFS.getText().isEmpty()){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Fields can not be Empty!");
            alert.showAndWait();
        }else{
            db.puts("UPDATE centers SET center_name = '"+tfCenterName.getText()+"', fs = '"+tfFS.getText()+"'  WHERE center_id = '"+theCenter+"';");
            alert.showAndWait();
        }

        db.connect().close();

    }

}
