package kpur.views;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import kpur.model.GlobalFunctions;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by Nusrat on 5/20/2016.
 */
public class WeeklyEditAccountCtrl implements Initializable{

    @FXML
    private MenuBar menu;

    @FXML
    TextField tfName;
    @FXML
    TextField tfHusband;

    @FXML
    ComboBox<String> CbCenterList;

    /*********************
     * VARIABLES
     *********************/
    private ObservableList<String> centerObList = FXCollections.observableArrayList();
    private int theID, theCenter;

    /*********************
     * METHODS
     *********************/
    @FXML
    private void back()throws Exception{
        Stage stage = (Stage) menu.getScene().getWindow();
        Scene scene = menu.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WeeklyReportDataActivity.fxml"));
        scene.setRoot(loader.load());
        stage.setScene(scene);
        WeeklyReportDataCtrl ob = loader.getController();
        ob.setTableAndInfo(theID,theCenter);
        stage.setTitle("Weekly User Report");
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

    public void setDefaults(int id, int center) throws Exception{
        theID = id;
        theCenter = center;
        DatabaseConnection db = new DatabaseConnection();
        db.setQuery(db.connect().createStatement());
        ResultSet rs = db.getQuery().executeQuery("SELECT * FROM weekly_user WHERE user_id = '"+theID+"'");
        if(rs.next()){
            tfName.setText(rs.getString("name"));
            tfHusband.setText(rs.getString("husband"));
        }
        rs.close();
        db.connect().close();
    }

    @FXML
    private void updateAccount() throws Exception{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Weekly user information");
        alert.setContentText("Data updated successfully!");
        alert.setHeaderText(null);

        if(CbCenterList.getValue() == null || tfName.getText().isEmpty() || tfHusband.getText().isEmpty()){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please input properly.");
            alert.showAndWait();
        }else{
            // Storing user data to DATABASE;
            DatabaseConnection db = new DatabaseConnection();
            db.puts("UPDATE weekly_user SET name = '"+tfName.getText()+"', husband = '"+tfHusband.getText()+"', center_id = '"+GlobalFunctions.getCenterCode(CbCenterList.getValue())+"' WHERE user_id = '"+theID+"';");
            db.connect().close();

            // Display Dialogue;
            alert.showAndWait();
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            centerObList = GlobalFunctions.getCenters();
            CbCenterList.getItems().addAll(centerObList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}
