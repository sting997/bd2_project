package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {

	@FXML private TabPane tabPane ;
	// Inject tab content.
	@FXML private Tab eventTab;
	// Inject controller
	@FXML private EventTabController eventTabController;
	
	
//	private void initialize() {
//		eventTabController.initialize();
//	}
}
