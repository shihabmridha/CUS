package kpur;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import kpur.model.DatabaseConnection;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {

		try
		{
			DatabaseConnection db = new DatabaseConnection();
			if(!db.hasTable()){
				db.createTable();
			}
			db.connect().close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/kpur/views/HomeActivity.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("CKS - Shihab");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());