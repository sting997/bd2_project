package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
	@Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/gui.fxml"));
    
        Scene scene = new Scene(root);
    
        stage.setTitle("BD2");
        stage.setScene(scene);
        stage.show();
    }
	public static void main(String[] args) {
		launch(args);
	}
}
