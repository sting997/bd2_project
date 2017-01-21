package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/gui.fxml"));
//		FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(getClass().getResource("gui/gui.fxml"));
//		BorderPane root = loader.load();
		
		Scene scene = new Scene(root);

		stage.setTitle("BD2");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void stop() throws Exception {
		
		super.stop();
	}
}
