package controller;




import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class MainController {
	

	@FXML private EventTabController eventTabController;
	@FXML private CustomerTabController customerTabController;
	@FXML private SeatTabController seatTabController;
	@FXML private MenuItem closeMenuItem;
	@FXML
	private void initialize() {
		closeMenuItem.setOnAction((ActionEvent event) -> {
			handleClose();
		});
	}
	
	private void handleClose() {
		eventTabController.close();
		seatTabController.close();
		customerTabController.close();

		Platform.exit();
	}
}
